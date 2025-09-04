package cn.stars.halo.renderer;

import cn.stars.halo.config.HaloConfig;
import cn.stars.halo.entity.HaloEntity;
import cn.stars.halo.model.HaloModel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class HaloRenderer<R extends LivingEntityRenderState & GeoRenderState> extends GeoEntityRenderer<HaloEntity, R> {
    public HaloRenderer(EntityRendererFactory.Context context) {
        super(context, new HaloModel());
        addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }

    @Override
    public @Nullable RenderLayer getRenderType(R renderState, Identifier texture) {
        return RenderLayer.getEntityTranslucent(texture);
    }

    @Override
    public void render(R renderState, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        ClientPlayerEntity player = minecraft.player;

        if (player != null) {
            // 光环跟随玩家视角旋转
            float playerYaw = player.getYaw();
            poseStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-playerYaw));
            poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(HaloConfig.getAngleOffset()));
        }

        super.render(renderState, poseStack, bufferSource, packedLight);
    }

    @Override
    public boolean shouldRender(HaloEntity entity, Frustum frustum, double x, double y, double z) {
        PlayerEntity player = entity.trackedPlayer;
        return !player.isInvisible() && !player.isDead() && !player.isSpectator() && !player.isSleeping();
    }
}