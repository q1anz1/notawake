package cn.qianz.notawake.register;

import cn.qianz.notawake.Notawake;
import cn.qianz.notawake.entity.PlayerLikeEntity;
import cn.qianz.notawake.event.RandomSoundEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Notawake.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerEvents {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        PlayerLikeEntity.doSummonPlayerLike(event);
        RandomSoundEvent.doRandomSound(event);
    }


}