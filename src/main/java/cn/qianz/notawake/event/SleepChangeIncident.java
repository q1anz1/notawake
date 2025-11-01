package cn.qianz.notawake.event;

import cn.qianz.notawake.register.ModSoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;

import java.util.Objects;

/**
 *
 */
public class SleepChangeIncident {
    public static double teleportChance;

    public static void onPlayerSleep(PlayerSleepInBedEvent event) {
        Player player = event.getEntity();
        Level world = player.level();
        if (world.isClientSide()) {
            return;
        }
        if (world.getRandom().nextFloat() >= teleportChance) {
            return;
        }

        Objects.requireNonNull(world.getServer()).execute(() -> {
            player.stopSleeping();
            teleportPlayerRandomly(player);
            player.playNotifySound(
                    ModSoundEvents.SCP_HORROR_1.get(),
                    SoundSource.HOSTILE,
                    3.0F,
                    1.0F);
        });
    }


    private static void teleportPlayerRandomly(Player player) {
        Level world = player.level();

        double x = player.getX() + world.getRandom().nextInt(20) - 10;
        double z = player.getZ() + world.getRandom().nextInt(20) - 10;
        double y = world.getHeight(Heightmap.Types.WORLD_SURFACE, (int)x, (int)z) + 4;

        player.teleportTo(x, y, z);
        player.setXRot(-90f);
    }
}
