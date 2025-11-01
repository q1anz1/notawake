package cn.qianz.notawake.register;

import cn.qianz.notawake.Notawake;
import cn.qianz.notawake.event.*;
import cn.qianz.notawake.util.BgmPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Notawake.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerEvents {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            PlayerLikeEntitySummonIncident.doSummonPlayerLike(event);
            RandomSoundIncident.doRandomSound(event);
            BgmPlayer.reduceTicker(event);
        }
    }

    @SubscribeEvent
    public static void onPlayerSleep(PlayerSleepInBedEvent event) {

        SleepChangeIncident.onPlayerSleep(event);
    }

    @SubscribeEvent
    public static void onPlayerWakeUp(PlayerWakeUpEvent event) {

        DeleteBedIncident.deleteBed(event);
        TalkerIncident.firstWakeUp(event);
    }

    @SubscribeEvent
    public static void onPlaySound(PlaySoundEvent event) {
        BgmPlayer.interceptBgm(event);
    }
}