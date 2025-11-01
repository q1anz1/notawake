package cn.qianz.notawake.event;

import cn.qianz.notawake.entity.ModEntities;
import cn.qianz.notawake.entity.PlayerLikeEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;

/**
 *
 */
public class PlayerLikeEntitySummonIncident {
    public static final double GENERATE_DISTANCE = 4;
    public static double spawnPlayerLikeChancePerTick;

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

        if (player.getY() - 50 > level.getSeaLevel()) {
            return;
        }

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
                    break;
                }
            }
        }
    }
}
