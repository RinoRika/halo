package cn.stars.halo.model;

import cn.stars.halo.config.HaloConfig;
import cn.stars.halo.entity.HaloEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

import java.util.Optional;

public class HaloModel
extends GeoModel<HaloEntity> {

    @Override
    public Identifier getModelResource(GeoRenderState geoRenderState) {
        return Identifier.of("halo", HaloConfig.getHaloType());
    }

    @Override
    public Identifier getTextureResource(GeoRenderState renderState) {
        return Identifier.of("halo", "textures/entity/" + HaloConfig.getHaloType() + ".geo.texture.png");
    }

    @Override
    public Identifier getAnimationResource(HaloEntity animatable) {
        return Identifier.of("halo", HaloConfig.getHaloType());
    }
}
