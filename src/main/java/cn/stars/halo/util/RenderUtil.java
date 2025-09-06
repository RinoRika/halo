package cn.stars.halo.util;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

public class RenderUtil {
    /**
     * 由于模型碰撞箱并不位于玩家头部,旋转会产生偏移,手动计算偏移位置并纠正
     * @author Stars
     */
    public static void doXRotationWithCompensation(MatrixStack poseStack, float yOffset, float zOffset, float degree) {
        float compensationZ = (float) Math.sin(Math.toRadians(degree));
        float compensationY = (float) (1 - Math.cos(Math.toRadians(degree)));
        poseStack.translate(0, compensationY * yOffset, -compensationZ * zOffset);

        poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(degree));
    }
}
