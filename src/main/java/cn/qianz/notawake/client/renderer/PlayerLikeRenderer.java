package cn.qianz.notawake.client.renderer;

import cn.qianz.notawake.entity.PlayerLikeEntity;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

import static cn.qianz.notawake.Notawake.MODID;

public class PlayerLikeRenderer extends MobRenderer<PlayerLikeEntity, PlayerModel<PlayerLikeEntity>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "textures/entity/player_like/steve.png");

    public PlayerLikeRenderer(EntityRendererProvider.Context context) {
        super(context, new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER), false), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(PlayerLikeEntity entity) {
        return TEXTURE;
    }
}