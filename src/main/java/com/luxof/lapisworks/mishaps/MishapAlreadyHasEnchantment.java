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
    private final Text enchantmentName;
    private final int whatEnchant;
    private final int whatLevel;

    public MishapAlreadyHasEnchantment(LivingEntity whatEntity, Text enchantmentName, int whatEnchant, int whatLevel) {
        this.whatEntity = whatEntity;
        this.enchantmentName = enchantmentName;
        this.whatEnchant = whatEnchant;
        this.whatLevel = whatLevel;
    }

    @Override
    public FrozenPigment accentColor(CastingEnvironment ctx, Context errorCtx) {
        if (this.whatEnchant == 0) {
            return dyeColor(DyeColor.RED); // fireyfists
        } else if (this.whatEnchant == 1) {
            return dyeColor(DyeColor.WHITE); // lightningbending
        } else {
            // any other stuff (like from an addon that hasn't mixined to this)
            return dyeColor(DyeColor.BLACK);
        }
    }

    @Override
    public void execute(CastingEnvironment env, Context errorCtx, List<Iota> stack) {}

    @Override
    public Text errorMessage(CastingEnvironment ctx, Context errorCtx) {
        return Text.translatable(
            "mishaps.lapisworks.already_enchanted",
            this.whatEntity.getName(),
            this.enchantmentName,
            this.enchantmentName,
            whatLevel
        );
    }
}
