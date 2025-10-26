package cn.qianz.notawake.register;


import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.RegistryObject;

import static cn.qianz.notawake.Notawake.MODID;
import static cn.qianz.notawake.Notawake.SOUND_EVENTS;

public class ModSoundEvents {

    public static RegistryObject<SoundEvent> SCP_HORROR_1;
    public static SoundEvent BGM1;
    public static RegistryObject<SoundEvent> ATTACK1;

    public static void register() {
        SCP_HORROR_1 = SOUND_EVENTS.register("scp_horror_1",
                () -> SoundEvent.createVariableRangeEvent(
                        new ResourceLocation(MODID, "scp_horror_1")
                ));
        ATTACK1 = SOUND_EVENTS.register("attack1",
                () -> SoundEvent.createVariableRangeEvent(
                        new ResourceLocation(MODID, "attack1")
                ));
        BGM1 = SoundEvent.createVariableRangeEvent(
                new ResourceLocation(MODID, "bgm1")
        );

    }
}