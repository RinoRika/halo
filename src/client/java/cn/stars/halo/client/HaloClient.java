package cn.stars.halo.client;

import cn.stars.halo.Halo;
import cn.stars.halo.HaloCore;
import cn.stars.halo.config.HaloConfig;
import cn.stars.halo.config.HaloConfigScreen;
import cn.stars.halo.entity.HaloEntity;
import cn.stars.halo.renderer.HaloRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.world.ClientWorld;
import org.lwjgl.glfw.GLFW;

public class HaloClient implements ClientModInitializer {
    private static KeyBinding openConfigKey;
    private HaloEntity clientHalo;
    private String lastHalo;

    @Override
    public void onInitializeClient() {
        // 注册客户端的渲染器
        EntityRendererRegistry.register(Halo.HALO_ENTITY, HaloRenderer::new);

        // 加载配置
        HaloConfig.load();
        lastHalo = HaloConfig.getHaloType();

        openConfigKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.halo.open_config", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_H, "category.halo.config"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // 打开配置菜单
            if (openConfigKey.wasPressed()) {
                HaloCore.logger.info("Opening config screen");
                client.setScreen(new HaloConfigScreen(client.currentScreen));
            }

            if (HaloConfig.checkIfNone()) {
                lastHalo = HaloConfig.getHaloType();
                this.removeHalo();
                return;
            }

            ClientPlayerEntity player = client.player;
            if (player == null) {
                // 当玩家不存在时删除光环实体
                this.removeHalo();
                return;
            }

            ClientWorld clientWorld = player.clientWorld;

            if (clientHalo == null || !clientHalo.isAlive() || clientHalo.isRemoved()) {
                clientHalo = new HaloEntity(Halo.HALO_ENTITY, clientWorld);
                clientHalo.setTrackedPlayer(player);
                clientWorld.addEntity(clientHalo);
            } else {
                if (!lastHalo.equals(HaloConfig.getHaloType())) {
                    HaloCore.logger.info("检测到光环改变,将重新生成光环实体");
                    HaloCore.showMessageTranslated("message.halo.animReloadNotice");
                    lastHalo = HaloConfig.getHaloType();
                    this.removeHalo();
                    return;
                }
                // 更新光环实体位置
                // 踩坑: 不要用updatePositionAndAngles(),他会重置实体的碰撞箱,导致玩家无法交互
                clientHalo.setPos(player.getX(), player.getEyeY() - 0.2f, player.getZ());
                clientHalo.setAngles(player.getYaw(), player.getPitch());
                clientHalo.setVelocity(player.getVelocity());
            }
        });
    }

    public void removeHalo() {
        if (clientHalo != null) {
            clientHalo.discard();
            clientHalo = null;
        }
    }
}
