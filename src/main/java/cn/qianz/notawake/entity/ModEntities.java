package cn.qianz.notawake.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;

import static cn.qianz.notawake.Notawake.ENTITIES;
import static cn.qianz.notawake.Notawake.MODID;

public class ModEntities {
    public static RegistryObject<EntityType<PlayerLikeEntity>> PLAYER_LIKE;

    public static void register() {
        PLAYER_LIKE = ENTITIES.register("player_like",
                () -> EntityType.Builder.of(PlayerLikeEntity::new, MobCategory.CREATURE)
                        .sized(0.6F, 1.8F)
                        .build(MODID + ":player_like")
        );
    }
}