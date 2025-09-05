package cn.stars.halo.config;
import cn.stars.halo.HaloCore;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;

import java.awt.*;
import java.time.Duration;

public class HaloConfigScreen extends Screen {
    private final Screen parent;
    int buttonWidth = 200;
    int buttonHeight = 20;

    public HaloConfigScreen(Screen parent) {
        super(Text.translatable("screen.config.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        int x = (this.width - buttonWidth) / 2;
        int y = (this.height - buttonHeight) / 2;

        ButtonWidget toggleButton = ButtonWidget.builder(Text.translatable("screen.config.type").append(": " + HaloConfig.getHaloType()), button -> {
            String currentType = HaloConfig.getHaloType();
            int index = HaloConfig.HALOS.indexOf(currentType);
            if (index == HaloConfig.HALOS.size() - 1) {
                index = 0;
            } else {
                index++;
            }
            String newType = HaloConfig.HALOS.get(index);
            HaloConfig.setHaloType(newType);
            button.setMessage(Text.translatable("screen.config.type").append(": " + HaloConfig.getHaloType()));
        }).dimensions(x, y - 60, buttonWidth, buttonHeight).tooltip(Tooltip.of(Text.translatable("screen.config.typeTooltip"))).build();

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

        Text credit = Text.of("§b§lHalo §f" + HaloCore.version + " | Made with ❤ by Stars | Model by SpicyWolf");
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
        super.render(context, mouseX, mouseY, deltaTicks);
    }


    @Override
    public void close() {
        if (this.client != null) {
            this.client.setScreen(this.parent);
        }
    }
}