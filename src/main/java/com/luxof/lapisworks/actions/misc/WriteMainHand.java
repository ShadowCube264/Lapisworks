package com.luxof.lapisworks.actions.misc;

import at.petrak.hexcasting.api.addldata.ADIotaHolder;
import at.petrak.hexcasting.api.casting.RenderedSpell;
import at.petrak.hexcasting.api.casting.castables.SpellAction;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.eval.OperationResult;
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage;
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster;
import at.petrak.hexcasting.xplat.IXplatAbstractions;

import com.luxof.lapisworks.MishapThrowerJava;
import com.luxof.lapisworks.mishaps.MishapBadMainhandItem;

import java.util.List;
import java.util.Optional;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;

public class WriteMainHand implements SpellAction {
    @Override
    public Result execute(List<? extends Iota> args, CastingEnvironment ctx) {
        Iota iota = args.get(0);

        Optional<LivingEntity> casterOp = Optional.of(ctx.getCastingEntity());
        if (casterOp.isEmpty()) { MishapThrowerJava.throwMishap(new MishapBadCaster()); }
        LivingEntity caster = casterOp.get();
        ADIotaHolder iotaHolder = IXplatAbstractions.INSTANCE.findDataHolder(caster.getMainHandStack());
        // "let's make the error message more helpful"
        // my dumbass' honest reaction to that information:
        if (iotaHolder == null || iotaHolder.writeIota(iota, true)) {
            MishapThrowerJava.throwMishap(new MishapBadMainhandItem(
                caster.getMainHandStack(),
                Text.translatable("mishaps.lapisworks.bad_item.mainhand.writeable")
            ));
        }
        return new SpellAction.Result(
            new Spell(iota, iotaHolder),
            0,
            List.of(),
            1
        );
    }

    public class Spell implements RenderedSpell {
        public final Iota iota;
        public final ADIotaHolder iotaHolder;
        public Spell(Iota iota, ADIotaHolder iotaHolder) {
            this.iota = iota;
            this.iotaHolder = iotaHolder;
        }

        @Override
        public void cast(CastingEnvironment ctx) {
            iotaHolder.writeIota(iota, false);
        }

        @Override
        public CastingImage cast(CastingEnvironment arg0, CastingImage arg1) {
            return RenderedSpell.DefaultImpls.cast(this, arg0, arg1);
        }
    }

    @Override
    public int getArgc() {
        return 0;
    }

    @Override
    public OperationResult operate(CastingEnvironment arg0, CastingImage arg1, SpellContinuation arg2) {
        return SpellAction.DefaultImpls.operate(this, arg0, arg1, arg2);
    }

    @Override
    public boolean awardsCastingStat(CastingEnvironment arg0) {
        return SpellAction.DefaultImpls.awardsCastingStat(this, arg0);
    }

    @Override
    public Result executeWithUserdata(List<? extends Iota> arg0, CastingEnvironment arg1, NbtCompound arg2) {
        return SpellAction.DefaultImpls.executeWithUserdata(this, arg0, arg1, arg2);
    }

    @Override
    public boolean hasCastingSound(CastingEnvironment arg0) { return true; }
}
