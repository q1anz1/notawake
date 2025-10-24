package cn.qianz.notawake.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public class CustomMusicPlayer {
    public static void playSoundLocally(SoundEvent soundEvent, boolean stopMusic) {
        Minecraft minecraft = Minecraft.getInstance();
        SoundManager soundManager = minecraft.getSoundManager();
        if (stopMusic) {
            soundManager.stop(null, SoundSource.MUSIC);
        }
        soundManager.play(
                SimpleSoundInstance.forMusic(soundEvent)
        );
    }
}