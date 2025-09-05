package cn.stars.halo.renderer;

import cn.stars.halo.config.HaloConfig;
import cn.stars.halo.entity.HaloEntity;
import cn.stars.halo.model.HaloModel;
import cn.stars.halo.util.RenderUtil;
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
        if (HaloConfig.checkIfNone()) return;

        MinecraftClient minecraft = MinecraftClient.getInstance();
        ClientPlayerEntity player = minecraft.player;

        if (player != null) {
            // 光环跟随玩家视角旋转
            float playerYaw = player.getYaw();
            poseStack.translate(0.0D, -24 / 16.0, 0.0D);
            poseStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-playerYaw));

            RenderUtil.doXRotationWithCompensation(poseStack, 1.8f, 1.5f, HaloConfig.getAngleOffset());

            poseStack.translate(0.0D, HaloConfig.getHeightOffset() / 16.0, 0.0D);

            // 梓的光环在立绘里看着像是竖着的,但是实际上是和别的人一样的
        /*    if (HaloConfig.getHaloType().equals("azusa")) {
                RenderUtil.doXRotationWithCompensation(poseStack, 2.5f, 2.25f, -90);
            } */
        }

        super.render(renderState, poseStack, bufferSource, packedLight);
    }

    @Override
    public boolean shouldRender(HaloEntity entity, Frustum frustum, double x, double y, double z) {
        PlayerEntity player = entity.trackedPlayer;
        return !player.isInvisible() && !player.isDead() && !player.isSpectator() && !player.isSleeping() && (HaloConfig.isFirstPersonVisible() || MinecraftClient.getInstance().gameRenderer.getCamera().isThirdPerson());
    }

}