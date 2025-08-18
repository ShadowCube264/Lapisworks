package com.luxof.lapisworks.actions;

import java.util.ArrayList;
import java.util.List;

import at.petrak.hexcasting.api.casting.OperatorUtils;
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.eval.OperationResult;
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage;
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.ListIota;
import at.petrak.hexcasting.api.casting.iota.Vec3Iota;
import at.petrak.hexcasting.api.misc.MediaConstants;
import net.minecraft.util.math.Vec3d;

public class CubeExalt implements ConstMediaAction {
    @Override
    public List<Iota> execute(List<? extends Iota> args, CastingEnvironment ctx) {
        List<Iota> cube = new ArrayList<Iota>();
        Vec3d pointA = OperatorUtils.getBlockPos(args, 0, getArgc()).toCenterPos();
        Vec3d pointB = OperatorUtils.getBlockPos(args, 1, getArgc()).toCenterPos();

        boolean hollow = OperatorUtils.getBool(args, 2, getArgc());
        List<Vec3d> points = hollow ? generatePointsInHollowCube(pointA, pointB) : generatePointsInFilledCube(pointA, pointB);;
        points.forEach((Vec3d el) -> { cube.add(new Vec3Iota(el)); });

        return List.of(new ListIota(cube));
    }

    @Override
    public CostMediaActionResult executeWithOpCount(List<? extends Iota> arg0, CastingEnvironment arg1) {
        return ConstMediaAction.DefaultImpls.executeWithOpCount(this, arg0, arg1);
    }

    @Override
    public int getArgc() {
        return 3;
    }

    @Override
    public long getMediaCost() {
        return (long)(MediaConstants.DUST_UNIT * 0.01);
    }

    @Override
    public OperationResult operate(CastingEnvironment arg0, CastingImage arg1, SpellContinuation arg2) {
        return ConstMediaAction.DefaultImpls.operate(this, arg0, arg1, arg2);
    }
    
    public static List<Vec3d> generatePointsInFilledCube(Vec3d pointA, Vec3d pointB) {
        List<Vec3d> points = new ArrayList<Vec3d>();
        // naming things is hard
        boolean a = pointA.x < pointB.x;
        boolean b = pointA.y < pointB.y;
        boolean c = pointA.z < pointB.z;
        Vec3d A = new Vec3d(
            a ? pointA.x : pointB.x,
            b ? pointA.y : pointB.y,
            c ? pointA.z : pointB.z
        );
        Vec3d B = new Vec3d(
            a ? pointB.x : pointA.x,
            b ? pointB.y : pointA.y,
            c ? pointB.z : pointA.z
        );
        
        for (double z = A.z; z < Math.ceil(B.z); z++) {
            for (double y = A.y; y < Math.ceil(B.y); y++) {
                for (double x = A.x; x < Math.ceil(B.x); x++) {
                    points.add(new Vec3d(Math.min(x, B.x), Math.min(y, B.y), Math.min(z, B.z)));
                }
            }
        }

        return points;
    }

    public static List<Vec3d> generatePointsInHollowCube(Vec3d pointA, Vec3d pointB) {
        List<Vec3d> points = new ArrayList<Vec3d>();

        // my brain is too small for the other approach (tried and skill issued)
        generatePointsInFilledCube(pointA, pointB).forEach((Vec3d any) -> {
            if (any.x == pointA.x || any.y == pointA.y || any.z == pointA.z ||
                any.x == pointB.x || any.y == pointB.y || any.z == pointB.z) {
                points.add(any);
            }
        });

        return points;
    }
}
