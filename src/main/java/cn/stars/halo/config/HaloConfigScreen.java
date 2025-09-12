package cn.stars.halo.config;

import cn.stars.halo.Halo;
import cn.stars.halo.HaloCore;
import cn.stars.halo.entity.HaloEntity;
import cn.stars.halo.renderer.HaloRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import software.bernie.geckolib.constant.dataticket.DataTicket;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;

import java.time.Duration;
import java.util.Map;

public class HaloConfigScreen extends Screen {
    private final Screen parent;
    int buttonWidth = 200;
    int buttonHeight = 20;
    private HaloEntity haloEntity;
    public static boolean shouldUpdate = true;

    public HaloConfigScreen(Screen parent) {
        super(Text.translatable("screen.config.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        int x = (this.width - buttonWidth) / 2;
        int y = (this.height - buttonHeight) / 2;

        ButtonWidget toggleButton = ButtonWidget.builder(Text.translatable("screen.config.type").append(": ").append(HaloCore.translateCharacterName(HaloConfig.getHaloType())), button -> {
            String currentType = HaloConfig.getHaloType();
            int index = HaloConfig.HALOS.indexOf(currentType);
            if (Screen.hasControlDown() || Screen.hasShiftDown() || Screen.hasAltDown()) {
                if (index == 0) {
                    index = HaloConfig.HALOS.size() - 1;
                } else {
                    index--;
                }
            } else {
                if (index == HaloConfig.HALOS.size() - 1) {
                    index = 0;
                } else {
                    index++;
                }
            }
            String newType = HaloConfig.HALOS.get(index);
            HaloConfig.setHaloType(newType);
            button.setMessage(Text.translatable("screen.config.type").append(": ").append(HaloCore.translateCharacterName(HaloConfig.getHaloType())));
        }).dimensions(x, y - 60, buttonWidth, buttonHeight).tooltip(Tooltip.of(Text.translatable("screen.config.typeTooltip"))).build();
        toggleButton.setTooltipDelay(Duration.ofSeconds(1));

        ButtonWidget firstPersonVisibleButton = ButtonWidget.builder(Text.translatable("screen.config.firstPersonVisibility").append(": " + HaloConfig.isFirstPersonVisible()), button -> {
            HaloConfig.setFirstPersonVisible(!HaloConfig.isFirstPersonVisible());
            button.setMessage(Text.translatable("screen.config.firstPersonVisibility").append(": " + HaloConfig.isFirstPersonVisible()));
        }).dimensions(x, y - 30, buttonWidth, buttonHeight).tooltip(Tooltip.of(Text.translatable("screen.config.firstPersonVisibilityTooltip"))).build();
        firstPersonVisibleButton.setTooltipDelay(Duration.ofSeconds(1));

        SliderWidget angleOffsetSlider = new SliderWidget(x, y, buttonWidth, buttonHeight, Text.translatable("screen.config.angleOffset").append(": " + HaloConfig.getAngleOffset()), (double) (HaloConfig.getAngleOffset() + 90) / 180) {
            @Override
            protected void updateMessage() {
                int actualOffset = (int) (this.value * 180) - 90;
                this.setMessage(Text.translatable("screen.config.angleOffset").append(": " + actualOffset));
            }

            @Override
            protected void applyValue() {
                int actualOffset = (int) (this.value * 180) - 90;
                HaloConfig.setAngleOffset(actualOffset);
            }
        };
        angleOffsetSlider.setTooltip(Tooltip.of(Text.translatable("screen.config.angleOffsetTooltip")));
        angleOffsetSlider.setTooltipDelay(Duration.ofSeconds(1));

        SliderWidget heightOffsetSlider = new SliderWidget(x, y + 30, buttonWidth, buttonHeight, Text.translatable("screen.config.heightOffset").append(": " + String.format("%.2f", HaloConfig.getHeightOffset())), (HaloConfig.getHeightOffset() + 4) / 8) {
            @Override
            protected void updateMessage() {
                double actualOffset = this.value * 8 - 4;
                this.setMessage(Text.translatable("screen.config.heightOffset").append(": " + String.format("%.2f", actualOffset)));
            }

            @Override
            protected void applyValue() {
                double actualOffset = this.value * 8 - 4;
                HaloConfig.setHeightOffset(actualOffset);
            }
        };
        heightOffsetSlider.setTooltip(Tooltip.of(Text.translatable("screen.config.heightOffsetTooltip")));
        heightOffsetSlider.setTooltipDelay(Duration.ofSeconds(1));

        TextWidget subtitleText = new TextWidget(Text.translatable("screen.config.subtitle"), textRenderer);
        subtitleText.setX(width / 2 - textRenderer.getWidth(Text.translatable("screen.config.subtitle")) / 2);
        subtitleText.setY(y - 80);

        Text credit = Text.of("§b§lHalo §f" + HaloCore.version + " | Made with ❤ by Stars");
        TextWidget creditText = new TextWidget(credit, textRenderer);
        creditText.setX(width / 2 - textRenderer.getWidth(credit) / 2);
        creditText.setY(height - 10);

        this.addDrawableChild(toggleButton);
        this.addDrawableChild(firstPersonVisibleButton);
        this.addDrawableChild(angleOffsetSlider);
        this.addDrawableChild(heightOffsetSlider);
        this.addDrawableChild(subtitleText);
        this.addDrawableChild(creditText);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        if (haloEntity != null) {
            shouldUpdate = false;
            try {
                drawEntity(context, width / 2 + 150, height / 2 - 250, width / 2 + 400, height / 2 + 200, 100, 1, mouseX, mouseY, haloEntity);
            } catch (Exception e) {
               HaloCore.logger.error("绘制光环时遇到问题, 请报告开发者!");
            }
        } else {
            haloEntity = new HaloEntity(Halo.HALO_ENTITY, MinecraftClient.getInstance().world);
            haloEntity.setTrackedPlayer(MinecraftClient.getInstance().player);
            haloEntity.shouldUpdate = false;
        }
        super.render(context, mouseX, mouseY, deltaTicks);
    }

    public void drawEntity(DrawContext context, int x1, int y1, int x2, int y2, int size, float scale, float mouseX, float mouseY, LivingEntity entity) {
        float f = (float)width / 2.0F;
        float g = (float)height / 2.0F;
        context.enableScissor(x1, y1, x2, y2);
        float h = (float)Math.atan((f - mouseX) / 40.0F);
        Quaternionf quaternionf = (new Quaternionf()).rotateX((float) Math.PI + 1f);
        Quaternionf quaternionf2 = (new Quaternionf()).rotateY(h * 20.0F * ((float)Math.PI / 180F));
        float o = entity.getScale();
        Vector3f vector3f = new Vector3f(0.0F, entity.getHeight() / 2.0F + scale * o, 0.0F);
        float p = (float)size / o;
        drawEntity(context, x1, y1, x2, y2, p, vector3f, quaternionf, quaternionf2, entity);
        context.disableScissor();
    }

    public void drawEntity(DrawContext drawer, int x1, int y1, int x2, int y2, float scale, Vector3f translation, Quaternionf rotation, @Nullable Quaternionf overrideCameraAngle, LivingEntity entity) {
        EntityRenderDispatcher entityRenderDispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
        EntityRenderer<? super LivingEntity, ?> entityRenderer = entityRenderDispatcher.getRenderer(entity);
        HaloRenderer renderer = (HaloRenderer) entityRenderer;
        LivingEntityRenderState state = (LivingEntityRenderState) renderer.getAndUpdateRenderState(entity, 1f);
        state.entityType = entity.getType();
        drawer.addEntity(state, scale, translation, rotation, overrideCameraAngle, x1, y1, x2, y2);
    }


    @Override
    public void close() {
        shouldUpdate = true;
        if (this.client != null) {
            this.client.setScreen(this.parent);
        }
        haloEntity = null;
    }
}