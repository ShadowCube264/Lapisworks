package com.luxof.lapisworks.actions;

import at.petrak.hexcasting.api.casting.OperatorUtils;
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
import at.petrak.hexcasting.api.misc.MediaConstants;

import com.luxof.lapisworks.MishapThrowerJava;
import com.luxof.lapisworks.items.shit.FullyAmelInterface;
import com.luxof.lapisworks.items.shit.PartiallyAmelInterface;
import com.luxof.lapisworks.mishaps.MishapBadMainhandItem;
import com.luxof.lapisworks.mishaps.MishapNotEnoughOffhandItems;

import static com.luxof.lapisworks.init.Mutables.getFullyAmelProduct;
import static com.luxof.lapisworks.init.Mutables.getPartAmelProduct;
import static com.luxof.lapisworks.init.Mutables.isAmel;

import java.util.List;
import java.util.Optional;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;

public class ImbueAmel implements SpellAction {
    public int getArgc() {
        return 1;
    }

    @Override
    public SpellAction.Result execute(List<? extends Iota> args, CastingEnvironment ctx) {
        LivingEntity caster = Optional.of(ctx.getCastingEntity()).orElseGet(() -> {
            MishapThrowerJava.throwMishap(new MishapBadCaster()); return null;
        });
        int wantToInfuseAmount = OperatorUtils.getInt(args, 0, getArgc());
        if (wantToInfuseAmount <= 0) {
            // go fuck yourself
            return new SpellAction.Result(
                new DoNothingSpell(),
                0L,
                List.of(),
                1
            );
        }

        ItemStack offHandItems = caster.getOffHandStack();
        ItemStack mainHandItems = caster.getMainHandStack();

        MishapBadOffhandItem needAmel = MishapBadOffhandItem.of(offHandItems, "amel");
        MishapBadMainhandItem needImbueable = new MishapBadMainhandItem(
            mainHandItems,
            Text.translatable("mishaps.lapisworks.bad_item.mainhand.imbueable")
        );

        if (offHandItems.isEmpty()) {
            MishapThrowerJava.throwMishap(needAmel);
        } else if (!isAmel(offHandItems)) {
            MishapThrowerJava.throwMishap(needAmel);
        } else if (mainHandItems.isEmpty()) {
            MishapThrowerJava.throwMishap(needImbueable);
        } else if (mainHandItems.getItem() instanceof FullyAmelInterface) {
            MishapThrowerJava.throwMishap(needImbueable);
        }

        Item mainHandItem = mainHandItems.getItem();
        PartiallyAmelInterface partAmel = getPartAmelProduct(mainHandItem);
        FullyAmelInterface fullAmel = getFullyAmelProduct(mainHandItem);

        // yes i will explain my math (past programmer is the worst)
        int requiredAmelForFullInfusion = mainHandItem instanceof PartiallyAmelInterface ?
            // amel needed to make full amel = damage suffered / 1 amel's worth for healing
            (int)Math.ceil((double)mainHandItems.getDamage() / (double)partAmel.getAmelWorthInDurability()) :
            // or just the amount it takes
            fullAmel.getRequiredAmelToMakeFromBase();
        // use Math.min() so i don't overspend, calculate amount required to make a fullAmel on one side
        int infuseAmount = Math.min(wantToInfuseAmount, requiredAmelForFullInfusion);

        if (offHandItems.getCount() < infuseAmount) {
            MishapThrowerJava.throwMishap(new MishapNotEnoughOffhandItems(offHandItems, infuseAmount));
        }

        ItemStack changeToItemStack;
        if (infuseAmount == requiredAmelForFullInfusion) { changeToItemStack = new ItemStack((Item)fullAmel); }
        else {
            changeToItemStack = new ItemStack((Item)partAmel);
            changeToItemStack.setDamage(
                mainHandItems.getDamage() - infuseAmount * partAmel.getAmelWorthInDurability()
            );
        }

        if (!(mainHandItem instanceof PartiallyAmelInterface)) {
            changeToItemStack.setDamage(
                changeToItemStack.getMaxDamage() - infuseAmount * partAmel.getAmelWorthInDurability()
            );
        }

        return new SpellAction.Result(
            new Spell(caster, changeToItemStack, infuseAmount),
            MediaConstants.SHARD_UNIT * 2 * offHandItems.getCount(),
            List.of(ParticleSpray.burst(caster.getPos(), 1, 10 + offHandItems.getCount())),
            1
        );
    }

    public class Spell implements RenderedSpell {
        public final LivingEntity caster;
        public final ItemStack changeToItem;
        public final int count;

        public Spell(LivingEntity caster, ItemStack changeToItem, int count) {
            this.caster = caster;
            this.changeToItem = changeToItem;
            this.count = count;
        }

		@Override
		public void cast(CastingEnvironment ctx) {
            ItemStack offHandItems = this.caster.getOffHandStack();
            this.caster.setStackInHand(Hand.OFF_HAND, new ItemStack(
                offHandItems.getItem(),
                offHandItems.getCount() - this.count
            ));
            this.caster.setStackInHand(Hand.MAIN_HAND, this.changeToItem);
		}

        @Override
        public CastingImage cast(CastingEnvironment arg0, CastingImage arg1) {
            return RenderedSpell.DefaultImpls.cast(this, arg0, arg1);
        }
    }

    public class DoNothingSpell implements RenderedSpell {
        @Override
        public void cast(CastingEnvironment arg0) {
            return;
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
