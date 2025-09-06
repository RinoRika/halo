package cn.stars.halo.entity;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

public class HaloEntity extends PathAwareEntity implements GeoAnimatable {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public PlayerEntity trackedPlayer;

    public HaloEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
        this.noClip = true;
        this.setInvulnerable(true);
        this.setNoGravity(true);
        trackedPlayer = MinecraftClient.getInstance().player;
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return PathAwareEntity.createMobAttributes();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(DefaultAnimations.genericIdleController());
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public void setTrackedPlayer(PlayerEntity player) {
        this.trackedPlayer = player;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.getWorld().isClient) {
            if (this.trackedPlayer == null || this.trackedPlayer.isRemoved()) {
                this.discard();
                return;
            }
            this.setPos(this.trackedPlayer.getX(), this.trackedPlayer.getEyeY() - 1.5f, this.trackedPlayer.getZ());
            this.setAngles(this.trackedPlayer.getYaw(), this.trackedPlayer.getPitch());
            this.setVelocity(this.trackedPlayer.getVelocity());
        }
    }

    @Override
    public double getTick(@Nullable Object o) {
        return 1;
    }

    @Override
    public boolean isInvisible() {
        return false;
    }

    // 重写这些东西,防止生物阻挡玩家交互
    @Override
    public Box getBoundingBox(EntityPose pose) {
        return new Box(getX(), getY(), getZ(), getX(), getY(), getZ());
    }

    @Override
    public boolean isCollidable(Entity entity) {
        return false;
    }

    @Override
    public boolean collides(Vec3d oldPos, Vec3d newPos, List<Box> boxes) {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }
}