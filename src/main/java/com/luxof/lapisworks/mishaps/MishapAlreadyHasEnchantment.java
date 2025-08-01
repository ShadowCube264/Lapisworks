package com.luxof.lapisworks.mishaps;

import java.util.List;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.mishaps.Mishap;
import at.petrak.hexcasting.api.pigment.FrozenPigment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;

public class MishapAlreadyHasEnchantment extends Mishap {
    private final LivingEntity whatEntity;
    private final Text enchantment;
    private final int whatEnchant;
    private final int whatLevel;

    public MishapAlreadyHasEnchantment(LivingEntity whatEntity, Text enchantment, int whatEnchant, int whatLevel) {
        this.whatEntity = whatEntity;
        this.enchantment = enchantment;
        this.whatEnchant = whatEnchant;
        this.whatLevel = whatLevel;
    }

    @Override
    public FrozenPigment accentColor(CastingEnvironment ctx, Context errorCtx) {
        if (whatEnchant == 0) {
            return dyeColor(DyeColor.RED); // fireyfists
        } else if (whatEnchant == 1) {
            return dyeColor(DyeColor.WHITE); // lightningbending
        } else {
            return dyeColor(DyeColor.BLACK); // custom enchants
        }
    }

    @Override
    public void execute(CastingEnvironment env, Context errorCtx, List<Iota> stack) {
        env.getMishapEnvironment().dropHeldItems();
    }

    @Override
    public Text errorMessage(CastingEnvironment ctx, Context errorCtx) {
        return Text.translatable(
            "mishaps.lapisworks.already_enchanted",
            this.whatEntity.getName(),
            this.enchantment,
            this.enchantment,
            whatLevel
        );
    }
}
