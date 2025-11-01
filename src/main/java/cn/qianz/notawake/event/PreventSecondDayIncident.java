package cn.qianz.notawake.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.TickEvent;


public class PreventSecondDayIncident {
    public static void preventSecondDay(TickEvent.LevelTickEvent event) {
        if (event.level instanceof ServerLevel level) {
            long time = level.getDayTime();
            long currentTimeOfDay = time % 24000;

            if (currentTimeOfDay > 18000) {
                long newTime = (time / 24000) * 24000 + 18000;
                level.setDayTime(newTime);
            }
        }
    }
}
