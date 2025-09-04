package cn.stars.halo;

import cn.stars.halo.entity.HaloEntity;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class Halo implements ModInitializer {
    public static final RegistryKey<EntityType<?>> HALO_ENTITY_KEY =
            RegistryKey.of(Registries.ENTITY_TYPE.getKey(), Identifier.of("halo", "halo")); // 游戏内实体id: halo:halo
    public static final EntityType<HaloEntity> HALO_ENTITY = EntityType.Builder.create(HaloEntity::new, SpawnGroup.MISC).dimensions(0.5f, 0.5f).build(HALO_ENTITY_KEY);

    @Override
    public void onInitialize() {
        // 注册Registry
        Registry.register(Registries.ENTITY_TYPE, HALO_ENTITY_KEY, HALO_ENTITY);
        // 注册实体属性
        FabricDefaultAttributeRegistry.register(HALO_ENTITY, HaloEntity.createAttributes());
        // 完成加载
        HaloCore.logger.info("==========[HALO]==========");
        HaloCore.logger.info("Halo Mod {} 已成功载入!", HaloCore.version);
        HaloCore.logger.info("该模组由Stars制作, 模型文件来自SpicyWolf.");
        HaloCore.logger.info("==========================");
    }
}
