package com.luxof.lapisworks.mixin;

import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class RaycastContextMixin {
    @Shadow @Final private Vec3d start;
    @Shadow @Final private Vec3d end;
    @Shadow @Final private ShapeType shapeType;
    @Shadow @Final private FluidHandling fluid;
    @Shadow @Final private ShapeContext entityPosition;

    @Inject(at = @At("HEAD"), method = "<init>(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/world/RaycastContext$ShapeType;Lnet/minecraft/world/RaycastContext$FluidHandling;Lnet/minecraft/entity/Entity;)V", cancellable = true)
    public void constructor(Vec3d start, Vec3d end, ShapeType shapeType, FluidHandling fluidHandling, Entity entity, CallbackInfo ci) {
        this.start = start;
        this.end = end;
        this.shapeType = shapeType;
        this.fluid = fluidHandling;
        this.entityPosition = entity == null ? ShapeContext.absent() : ShapeContext.of(entity);
        ci.cancel();
    }
}
