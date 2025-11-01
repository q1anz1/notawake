package cn.qianz.notawake.event;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;

/**
 *
 */
public class DeleteBedIncident {
    public static void deleteBed(PlayerWakeUpEvent event) {
        if (event.getEntity().level().isClientSide) return;

        Player player = event.getEntity();
        Level level = player.level();

        BlockPos bedPos = player.getSleepingPos().orElse(null);
        if (bedPos == null) return;

        BlockState bedState = level.getBlockState(bedPos);
        if (bedState.getBlock() instanceof BedBlock) {
            level.setBlock(bedPos, Blocks.AIR.defaultBlockState(), 3);

            level.playSound(null, bedPos, SoundEvents.ITEM_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);

            level.addParticle(ParticleTypes.POOF,
                    bedPos.getX() + 0.5, bedPos.getY() + 0.5, bedPos.getZ() + 0.5,
                    0, 0.1, 0);


            RandomSource random = level.random;
            int woolCount = 1 + random.nextInt(2);
            Item woolItem = Items.WHITE_WOOL;
            ItemStack woolStack = new ItemStack(woolItem, woolCount);
            dropItemWithDelay(level, bedPos, woolStack);


            int woodCount = 2 + random.nextInt(2);
            Item woodItem = Items.OAK_PLANKS;
            ItemStack woodStack = new ItemStack(woodItem, woodCount);
            dropItemWithDelay(level, bedPos, woodStack);
        }

    }

    private static void dropItemWithDelay(Level level, BlockPos pos, ItemStack stack) {
        RandomSource random = level.random;
        double offsetX = 0.5 + (random.nextDouble() - 0.5) * 0.5;
        double offsetY = 0.5;
        double offsetZ = 0.5 + (random.nextDouble() - 0.5) * 0.5;

        ItemEntity itemEntity = new ItemEntity(
                level,
                pos.getX() + offsetX,
                pos.getY() + offsetY,
                pos.getZ() + offsetZ,
                stack
        );

        itemEntity.setPickUpDelay(20);

        itemEntity.setDeltaMovement(
                (random.nextDouble() - 0.5) * 0.1,
                random.nextDouble() * 0.1 + 0.1,
                (random.nextDouble() - 0.5) * 0.1
        );

        level.addFreshEntity(itemEntity);
    }
}
