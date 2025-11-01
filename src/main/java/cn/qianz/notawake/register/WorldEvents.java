package cn.qianz.notawake.register;

import cn.qianz.notawake.Notawake;
import cn.qianz.notawake.event.NightBellIncident;
import cn.qianz.notawake.event.NightDarkIncident;
import cn.qianz.notawake.event.PreventSecondDayIncident;
import cn.qianz.notawake.event.TalkerIncident;
import cn.qianz.notawake.util.BgmPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 *
 */
@Mod.EventBusSubscriber(modid = Notawake.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WorldEvents {
    @SubscribeEvent
    public static void onWorldTick(TickEvent.LevelTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            TalkerIncident.talkAllGuideWords(event);
            PreventSecondDayIncident.preventSecondDay(event);
            NightDarkIncident.nightDark(event);
            NightBellIncident.playBell(event);
        } else if (event.phase == TickEvent.Phase.START) {

        }
    }
}
