package cn.qianz.notawake.event;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 */
public class RandomSoundEvent {
    public static double randomSoundChancePerTick;
    public static final List<SoundEvent> soundEvents = new ArrayList<>();

    static {
        soundEvents.add(SoundEvents.PLAYER_HURT);
        soundEvents.add(SoundEvents.STONE_STEP);
        soundEvents.add(SoundEvents.SAND_STEP);
        soundEvents.add(SoundEvents.GLASS_STEP);
    }

    public static void doRandomSound(TickEvent.PlayerTickEvent event) {
        if (event.side == LogicalSide.CLIENT) return;
        Player player = event.player;
        if (Math.random() < randomSoundChancePerTick) {
            playBadSound(player);
        }
    }

    private static void playBadSound(Player player) {
        Level world = player.level();
        double offsetX = Math.random() * 20 - 10;
        double offsetY = Math.random() * 2 - 1;
        double offsetZ = Math.random() * 20 - 10;

        double x = player.getX() + offsetX;
        double y = player.getY() + offsetY;
        double z = player.getZ() + offsetZ;

        playSound(world, x, y, z, getRandomSound());
    }

    private static SoundEvent getRandomSound() {
        Random random = new Random();
        return soundEvents.get(random.nextInt(soundEvents.size()));
    }

    private static void playSound(Level world, double x, double y, double z,  SoundEvent soundEvent) {
        world.playSound(null, x, y, z,
                soundEvent,
                SoundSource.AMBIENT,
                0.5f, // 音量
                0.8f + world.random.nextFloat() * 0.4f); // 音调（0.8-1.2之间随机）
    }
}
