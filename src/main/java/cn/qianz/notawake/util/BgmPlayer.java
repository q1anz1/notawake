package cn.qianz.notawake.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.LogicalSide;

public class BgmPlayer {
    private static int ticker = 0;

    public static void playSoundLocally(SoundEvent soundEvent, boolean stopMusic) {
        playSound(soundEvent, 1, 1, false, stopMusic);
    }

    private static void playSound(SoundEvent soundEvent, float pitch, float volume, boolean looping, boolean stopMusic) {
        Minecraft minecraft = Minecraft.getInstance();
        SoundManager soundManager = minecraft.getSoundManager();
        if (stopMusic) {
            soundManager.stop(null, SoundSource.MUSIC);
        }
        SimpleSoundInstance instance = new SimpleSoundInstance(
                soundEvent.getLocation(), // 声音资源的定位
                SoundSource.AMBIENT,        // 音源分类（如音乐、玩家、环境等）
                volume,                     // 音量 (volume)
                pitch,                    // 音高 (pitch)
                minecraft.level.random,   // 随机源
                looping,                    // 是否循环播放 (looping)
                0,                        // 延迟 (delay)
                SimpleSoundInstance.Attenuation.NONE, // 衰减类型
                0.0D,                     // 相对X坐标
                0.0D,                     // 相对Y坐标
                0.0D,                     // 相对Z坐标
                false                     // 相对监听位置
        );
        soundManager.play(
                instance
        );
    }

    public static void playSoundLocallyStopBgm(SoundEvent soundEvent, float pitch, float volume, boolean looping, boolean stopMusic, int stopBgmTick) {
        playSound(soundEvent, pitch, volume, looping, stopMusic);
        if (stopMusic) {
            setTicker(stopBgmTick);
        }
    }

    private static void setTicker(int ticks) {
        // 为了解决播放时把他妈自己拦截
        ticker = -ticks;
    }

    public static void reduceTicker(TickEvent.PlayerTickEvent event) {
        if (event.side == LogicalSide.SERVER) return;
        if (ticker > 0) {
            ticker --;
        }
    }

    public static void interceptBgm(PlaySoundEvent event) {
        if (ticker == 0) return;
        if (ticker < 0) {
            // 为了解决播放时把他妈自己拦截
            ticker = -ticker;
            return;
        }
        SoundInstance sound = event.getSound();
        if (sound == null) return;
        if (sound.getSource() == SoundSource.MUSIC) {
            event.setSound(null);
        }
    }
}