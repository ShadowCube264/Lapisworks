package com.luxof.lapisworks.actions;

import java.util.List;
import java.util.Optional;

import at.petrak.hexcasting.api.casting.ParticleSpray;
import at.petrak.hexcasting.api.casting.RenderedSpell;
import at.petrak.hexcasting.api.casting.castables.SpellAction;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.eval.OperationResult;
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage;
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster;
import at.petrak.hexcasting.api.casting.mishaps.MishapBadOffhandItem;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Hand;

import com.luxof.lapisworks.MishapThrowerJava;
import com.luxof.lapisworks.ModItems;

public class SwapAmel implements SpellAction {
    public int getArgc() {
        return 0;
    }

    @Override
    public SpellAction.Result execute(List<? extends Iota> args, CastingEnvironment ctx) {
        Optional<LivingEntity> casterOption = Optional.of(ctx.getCastingEntity());
        if (casterOption.isEmpty()) {
            MishapThrowerJava.throwMishap(new MishapBadCaster());
        }
        LivingEntity caster = casterOption.get();
        ItemStack offHandItems = caster.getOffHandStack();
        if (offHandItems.isEmpty()) {
            MishapThrowerJava.throwMishap(MishapBadOffhandItem.of(offHandItems, "amel"));
        }

        int idx = ModItems.AMEL_MODELS.indexOf(offHandItems.getItem());
        if (idx == -1) {
            MishapThrowerJava.throwMishap(MishapBadOffhandItem.of(offHandItems, "amel"));
        }
        Item swapWith = ModItems.AMEL_MODELS.get(idx + 1);
        
        int count = offHandItems.getCount();

        return new SpellAction.Result(
            new Spell(caster, swapWith, count),
            0,
            List.of(ParticleSpray.burst(caster.getPos(), 1, 10 + offHandItems.getCount())),
            1
        );
    }

    public class Spell implements RenderedSpell {
        public final LivingEntity caster;
        public final Item item;
        public final int count;

        public Spell(LivingEntity caster, Item item, int count) {
            this.caster = caster;
            this.item = item;
            this.count = count;
        }

		@Override
		public void cast(CastingEnvironment ctx) {
            this.caster.setStackInHand(Hand.OFF_HAND, new ItemStack(this.item, this.count));
		}

        @Override
        public CastingImage cast(CastingEnvironment arg0, CastingImage arg1) {
            return RenderedSpell.DefaultImpls.cast(this, arg0, arg1);
        }
    }

    @Override
    public boolean awardsCastingStat(CastingEnvironment ctx) {
        return SpellAction.DefaultImpls.awardsCastingStat(this, ctx);
    }

    @Override
    public Result executeWithUserdata(List<? extends Iota> args, CastingEnvironment env, NbtCompound userData) {
        return SpellAction.DefaultImpls.executeWithUserdata(this, args, env, userData);
    }

    @Override
    public boolean hasCastingSound(CastingEnvironment ctx) {
        return SpellAction.DefaultImpls.hasCastingSound(this, ctx);
    }

    @Override
    public OperationResult operate(CastingEnvironment arg0, CastingImage arg1, SpellContinuation arg2) {
        return SpellAction.DefaultImpls.operate(this, arg0, arg1, arg2);
    }
}
