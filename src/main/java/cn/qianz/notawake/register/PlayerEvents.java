package cn.qianz.notawake.register;

import cn.qianz.notawake.Notawake;
import cn.qianz.notawake.entity.PlayerLikeEntity;
import cn.qianz.notawake.event.DeleteBedEvent;
import cn.qianz.notawake.event.RandomSoundEvent;
import cn.qianz.notawake.event.SleepChangeEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Notawake.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerEvents {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        PlayerLikeEntity.doSummonPlayerLike(event);
        RandomSoundEvent.doRandomSound(event);
    }

    @SubscribeEvent
    public static void onPlayerSleep(PlayerSleepInBedEvent event) {
        SleepChangeEvent.onPlayerSleep(event);
    }

    @SubscribeEvent
    public static void onPlayerWakeUp(PlayerWakeUpEvent event) {
        DeleteBedEvent.deleteBed(event);
    }
}