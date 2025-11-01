package cn.qianz.notawake.event;

import cn.qianz.notawake.util.BgmPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;

import static cn.qianz.notawake.register.ModSoundEvents.NIGHT_BELL;

/**
 *
 */
public class NightBellIncident {
    private static final long BELL_TIME = 13010;
    private static final int BELL_DURATION = 180;

    public static void playBell(TickEvent.LevelTickEvent event) {
        if (!(event.level instanceof ServerLevel)) {
            return;
        }
        Level level = event.level;
        if ((level.getDayTime() % 24000) == BELL_TIME) {
            BgmPlayer.playSoundLocallyStopBgm(NIGHT_BELL, 1.0F, 1.0F, false, true, BELL_DURATION);
        }
    }
}
