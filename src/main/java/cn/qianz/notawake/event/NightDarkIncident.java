package cn.qianz.notawake.event;

import cn.qianz.notawake.util.BgmPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;

import static cn.qianz.notawake.register.ModSoundEvents.BGM2;

/**
 *
 */
public class NightDarkIncident {
    public static final long NIGHT_COME_TIME = 17990;
    public static final int EFFECT_TIME = 800;

    public static void nightDark(TickEvent.LevelTickEvent event) {
        Level level = event.level;
        if (!(event.level instanceof ServerLevel world)) {
            return;
        }
        if ((level.getDayTime() % 24000) == NIGHT_COME_TIME) {
            BgmPlayer.playSoundLocallyStopBgm(BGM2, 1.0F, 1.0F, false, true, EFFECT_TIME);
            world.players().forEach(player -> {
                player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, EFFECT_TIME, 0, false, false, false));
            });
        }
    }

}
