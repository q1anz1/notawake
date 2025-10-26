package cn.qianz.notawake.register;

import cn.qianz.notawake.Notawake;
import cn.qianz.notawake.event.PreventSecondDayEvent;
import cn.qianz.notawake.event.TalkerEvent;
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
            TalkerEvent.talkAllGuideWords(event);
            PreventSecondDayEvent.preventSecondDay(event);
        }
    }
}
