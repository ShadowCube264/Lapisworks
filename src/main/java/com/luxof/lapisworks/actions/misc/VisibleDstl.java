package com.luxof.lapisworks.actions.misc;

import at.petrak.hexcasting.api.casting.OperatorUtils;
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.eval.OperationResult;
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage;
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation;
import at.petrak.hexcasting.api.casting.iota.BooleanIota;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster;
import at.petrak.hexcasting.api.casting.mishaps.MishapBadLocation;
import at.petrak.hexcasting.api.misc.MediaConstants;

import com.luxof.lapisworks.MishapThrowerJava;

import java.util.List;
import java.util.Optional;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;

public class VisibleDstl implements ConstMediaAction {
    @Override
    public List<Iota> execute(List<? extends Iota> args, CastingEnvironment ctx) {
        Optional<LivingEntity> casterOp = Optional.of(ctx.getCastingEntity());
        if (casterOp.isEmpty()) { MishapThrowerJava.throwMishap(new MishapBadCaster()); }
        Vec3d start = OperatorUtils.getVec3(args, 1, getArgc());
        Vec3d endPoint = OperatorUtils.getVec3(args, 0, getArgc());
        try {
            ctx.assertVecInRange(start);
            ctx.assertVecInRange(endPoint);
        } catch (MishapBadLocation e) {
            MishapThrowerJava.throwMishap(e);
        }
        BlockHitResult hitResult = ctx.getWorld().raycast(
            new RaycastContext(start, endPoint, ShapeType.OUTLINE, FluidHandling.NONE, casterOp.get())
        );
        return List.of(new BooleanIota(hitResult.getType() == HitResult.Type.MISS));
    }

    @Override
    public CostMediaActionResult executeWithOpCount(List<? extends Iota> arg0, CastingEnvironment arg1) {
        return ConstMediaAction.DefaultImpls.executeWithOpCount(this, arg0, arg1);
    }

    @Override
    public int getArgc() {
        return 2;
    }

    @Override
    public long getMediaCost() {
        return (long)(MediaConstants.DUST_UNIT * 0.01);
    }

    @Override
    public OperationResult operate(CastingEnvironment arg0, CastingImage arg1, SpellContinuation arg2) {
        return ConstMediaAction.DefaultImpls.operate(this, arg0, arg1, arg2);
    }
}
