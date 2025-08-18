package com.luxof.lapisworks.actions;

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

import static com.luxof.lapisworks.Lapisworks.LOGGER;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public class SphereDst implements ConstMediaAction {
    @Override
    public List<Iota> execute(List<? extends Iota> args, CastingEnvironment ctx) {
        Vec3d offset = OperatorUtils.getVec3(args, 0, getArgc());
        List<Iota> sphere = new ArrayList<Iota>();

        generatePointsOnHollowSphere(
            OperatorUtils.getIntBetween(args, 1, 1, 64, getArgc())
        ).forEach((Vec3d el) -> { sphere.add(new Vec3Iota(el.add(offset))); });

        return List.of(new ListIota(sphere));
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

    /** Generates all points on the circumference of a pixel-y circle. (spacing = 1) */
    public static List<Vec2f> generatePointsOnHollowCircle(
            int radius,
            boolean semiCircle,
            double circumference
    ) {
        int pointNum = (int)Math.floor(Math.max(circumference, 1) / (semiCircle ? 2 : 1));
        double step = 2 * Math.PI / pointNum;
        List<Vec2f> points = new ArrayList<Vec2f>();
        float phi = 0.0F;
        for (int i = 0; i < pointNum; i++) {
            points.add(new Vec2f((float)Math.cos(phi)*radius, (float)Math.sin(phi)*radius));
            phi += step;
        }
        return points;
    }

    /** Generates all points on the circumference of a pixel-y circle. (spacing = 1) */
    public static List<Vec2f> generatePointsOnHollowCircle(int radius, boolean semiCircle) {
        return generatePointsOnHollowCircle(radius, semiCircle, 2 * Math.PI * radius);
    }
    
    /** Generates all points on the surface of a pixel-y sphere. (spacing = 1) */
    public static List<Vec3d> generatePointsOnHollowSphere(int radius) {
        // the initial semi-circle gives us all we need to create the actual sphere
        List<Vec2f> semiCircle = generatePointsOnHollowCircle(radius, true);

        List<Vec3d> sphere = new ArrayList<Vec3d>();
        for (int z = 0; z < semiCircle.size(); z++) {
            int whar = z; // "final or effectively final based on its usage" my ass.
            // i pray this works
            LOGGER.info(whar + ": " + semiCircle.get(whar).x + " " + semiCircle.get(whar).y);
            int rad = Math.round(semiCircle.get(whar).y);
            generatePointsOnHollowCircle(rad, false).forEach((Vec2f el) -> {
                sphere.add(new Vec3d(el.x, el.y, whar));
            });
        }

        return sphere;
    }
}
