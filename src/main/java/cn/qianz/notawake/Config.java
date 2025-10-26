package cn.qianz.notawake;

import cn.qianz.notawake.entity.PlayerLikeEntity;
import cn.qianz.notawake.event.RandomSoundEvent;
import cn.qianz.notawake.event.SleepChangeEvent;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;


@Mod.EventBusSubscriber(modid = Notawake.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.DoubleValue SPAWN_PLAYER_LIKE_CHANCE_PER_TICK =  BUILDER.comment("PlayerLike entity spawn chance per tick (Default: 0.00005)").defineInRange("spawn_player_like_chance", 0.00005D, 0, 1);
    public static final ForgeConfigSpec.DoubleValue RANDOM_SOUND_CHANCE_PER_TICK =  BUILDER.comment("Play a werid random sound chance per tick (Default: 0.0005)").defineInRange("random_sound_chance", 0.0005D, 0, 1);
    public static final ForgeConfigSpec.DoubleValue PLAYER_RANDOM_TP_WHEN_SLEEP_CHANCE =  BUILDER.comment("Player will be randomly teleport when sleeping (Default: 0.05)").defineInRange("random_sleep_tp", 0.05D, 0, 1);
    static final ForgeConfigSpec SPEC = BUILDER.build();


    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        PlayerLikeEntity.spawnPlayerLikeChancePerTick = SPAWN_PLAYER_LIKE_CHANCE_PER_TICK.get();
        RandomSoundEvent.randomSoundChancePerTick = RANDOM_SOUND_CHANCE_PER_TICK.get();
        SleepChangeEvent.teleportChance = PLAYER_RANDOM_TP_WHEN_SLEEP_CHANCE.get();
    }
}
