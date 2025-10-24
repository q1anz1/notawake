package cn.qianz.notawake.entity;

import cn.qianz.notawake.Notawake;
import cn.qianz.notawake.event.CustomMusicPlayer;
import cn.qianz.notawake.register.ModSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static cn.qianz.notawake.entity.ModEntities.PLAYER_LIKE;
import static cn.qianz.notawake.register.ModSoundEvents.BGM1;

@Mod.EventBusSubscriber(modid = Notawake.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PlayerLikeEntity extends PathfinderMob {

    private static final int MAX_LIVE_TIME = 2400; // tick
    private static final int DESPAWN_TIME = 7; // tick
    public static final int DESPAWN_DISTANCE_WHEN_LOOK_AT = 30;
    public static final double DESPAWN_WATCH_ANGLE = 0.5; // cos(x)
    public static final float DESPAWN_VOLUME = 4.0F;
    public static final float DESPAWN_PITCH = 1.0F;
    public static final float MAXIMUM_DIXATION_DISTANCE = 30.0F;
    public static final double GENERATE_DISTANCE = 4;
    public static double spawnPlayerLikeChancePerTick;

    private int liveTime = 0;

    private int staredTicks = 0; // 被注视的tick计数
    public PlayerLikeEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
    }

    @SubscribeEvent
    public static void onAttributeCreate(EntityAttributeCreationEvent event) {
        event.put(PLAYER_LIKE.get(), createAttributes().build());
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 2.0D);
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        return false; // 免疫所有伤害
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this)); // 防止溺水
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, MAXIMUM_DIXATION_DISTANCE));
    }

    @Override
    public void tick() {
        super.tick();

        updateLiveTime();
        checkIsBeingStaredAt();
    }

    private void updateLiveTime() {
        liveTime++;
        if (liveTime> MAX_LIVE_TIME) {
            this.remove(RemovalReason.DISCARDED); // 移除实体
        }
    }

    private void checkIsBeingStaredAt() {
        // 检测玩家注视
        if (isBeingStaredAt()) {
            if (staredTicks == 1) {
                List<Player> nearbyPlayers = getNearbyPlayer(DESPAWN_DISTANCE_WHEN_LOOK_AT+30);
                Player player = getWhoIsStaring(nearbyPlayers);
                playSoundHorror1ToNearlyWatchedPlayer(player);
                teleportToPlayersFace(player);
                CustomMusicPlayer.playSoundLocally(BGM1, true);
                damagePlayerToNearDeath(player);
            }
            staredTicks++;
            if (staredTicks >= DESPAWN_TIME) {

                discard0();
            }
        } else {
            staredTicks = 0; // 重置计时
        }
    }

    private void teleportToPlayersFace(Player player) {
        if (player == null) return;

        Vec3 lookVec = player.getLookAngle();
        double offsetX = player.getX() + lookVec.x;
        double offsetY = player.getY() ;
        double offsetZ = player.getZ() + lookVec.z;

        this.setPos(offsetX, offsetY, offsetZ);
        lookAtPlayerDirectly(player);
    }

    private void lookAtPlayerDirectly(Player player) {
        if (player == null || !player.isAlive()) return;

        // 计算实体到玩家的方向向量
        double dx = player.getX() - this.getX();
        double dy = player.getY() - this.getY();
        double dz = player.getZ() - this.getZ();

        // 计算水平旋转角（yRot）
        double horizontalDistance = Math.sqrt(dx * dx + dz * dz);
        float yaw = (float) Math.toDegrees(Math.atan2(dz, dx)) - 90.0F;

        // 计算垂直旋转角（xRot）
        float pitch = (float) -Math.toDegrees(Math.atan2(dy, horizontalDistance));

        // 设置实体的旋转角度
        this.setYRot(yaw);
        this.setXRot(pitch);

        // 可选：同步头部旋转（防止身体和头不同步）
        this.yHeadRot = yaw;
        this.yBodyRot = yaw;
    }

    private void damagePlayerToNearDeath(Player player) {
        if (player == null || player.isDeadOrDying()) return;
        player.hurt(player.damageSources().starve(),  0.1F);
        player.setHealth(1.0F);
    }

    private List<Player> getNearbyPlayer(int distance) {
        return level().getEntitiesOfClass(
                Player.class,
                this.getBoundingBox().inflate(distance) // 以实体为中心，扩展检测范围
        );
    }

    private Player getWhoIsStaring(List<Player> players) {
        for (Player player : players) {
            // 检查玩家是否在注视实体
            if (isPlayerStaring(player)) {
                return player;
            }
        }
        return null;
    }

    private void playSoundHorror1ToNearlyWatchedPlayer(Player player) {
        // 播放声音
        if (player != null) {
            player.playNotifySound(
                    ModSoundEvents.SCP_HORROR_1.get(),
                    SoundSource.HOSTILE,
                    DESPAWN_VOLUME,
                    DESPAWN_PITCH);
        }
    }

    private void discard0() {  // 消失
        if (!level().isClientSide) {
            // 服务器端生成消失粒子
            ((ServerLevel)level()).sendParticles(
                    ParticleTypes.SMOKE,
                    getX(), getEyeY(), getZ(),
                    40,  // 粒子数量
                    0.5, 0.5, 0.5,  // 随机偏移
                    0.1  // 速度
            );

        }
        remove(RemovalReason.DISCARDED);
    }

    private boolean isBeingStaredAt() {     // 检测是否有玩家在注视这个实体
        for (Player player : level().players()) {
            if (isPlayerStaring(player)) {
                return true;
            }
        }
        return false;
    }

    private boolean isPlayerStaring(Player player) {
        // 1. 距离检查
        if (distanceTo(player) > DESPAWN_DISTANCE_WHEN_LOOK_AT) {
            return false;
        }

        // 2. 视线角度检查（玩家视角方向与实体方向的夹角）
        Vec3 playerLook = player.getViewVector(1.0F).normalize();
        Vec3 entityDir = new Vec3(
                this.getX() - player.getX(),
                this.getEyeY() - player.getEyeY(),
                this.getZ() - player.getZ()
        ).normalize();

        double dotProduct = playerLook.dot(entityDir);
        if (dotProduct <= DESPAWN_WATCH_ANGLE) {
            return false; // 玩家视角未对准实体
        }

        // 3. 射线检测（检查视线是否被方块阻挡）
        Vec3 playerEyePos = new Vec3(player.getX(), player.getEyeY(), player.getZ());
        Vec3 entityEyePos = new Vec3(this.getX(), this.getEyeY(), this.getZ());

        ClipContext context = new ClipContext(
                playerEyePos,
                entityEyePos,
                ClipContext.Block.COLLIDER, // 检测碰撞方块
                ClipContext.Fluid.NONE,     // 忽略流体
                null
        );

        BlockHitResult hitResult = player.level().clip(context);

        // 如果射线未命中任何方块，或者命中的方块是实体本身的位置，则未被阻挡
        return hitResult.getType() == HitResult.Type.MISS ||
                hitResult.getBlockPos().equals(this.blockPosition());
    }

    public static void doSummonPlayerLike(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player == null ||  event.player.level().isClientSide()) {
            return;
        }
        if (Math.random() > spawnPlayerLikeChancePerTick) {
            return;
        }
        Player player = event.player;
        Level world = player.level();
        ServerLevel level = (ServerLevel) player.level();

        float playerYaw = player.getYRot();
        double distance = GENERATE_DISTANCE;

        for (int i = 0; i < 10; i++) {
            double angle = Math.toRadians(playerYaw - 90 + (Math.random() * 180 - 90));

            double x = player.getX() + Math.cos(angle) * distance;
            double z = player.getZ() + Math.sin(angle) * distance;
            double y = player.getY();

            BlockPos blockPos = new BlockPos((int)x, (int) y, (int)z);
            BlockPos blockPos2 = new BlockPos((int)x, (int) y + 1, (int)z);

            if (world.getBlockState(blockPos).isAir() && world.getBlockState(blockPos2).isAir()) {
                PlayerLikeEntity entity = ModEntities.PLAYER_LIKE.get().create(level);
                if (entity != null) {
                    entity.moveTo((int)x, (int)y, (int)z);
                    level.addFreshEntity(entity);
                    System.out.println("1");
                    break;
                }
            }
        }
    }
}
