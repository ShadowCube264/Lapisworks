package com.luxof.lapisworks.mishaps;

import java.util.List;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.mishaps.Mishap;
import at.petrak.hexcasting.api.pigment.FrozenPigment;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;

public class MishapNotEnoughOffhandItems extends Mishap {
    private final ItemStack item;
    private final int required;

    public MishapNotEnoughOffhandItems(ItemStack itemsInOffhand, int requiredAmount) {
        this.item = itemsInOffhand;
        this.required = requiredAmount;
    }

    @Override
    public FrozenPigment accentColor(CastingEnvironment ctx, Context errorCtx) {
        return dyeColor(DyeColor.BROWN);
    }

    @Override
    public void execute(CastingEnvironment env, Context errorCtx, List<Iota> stack) {
        env.getMishapEnvironment().dropHeldItems();
    }

    @Override
    public Text errorMessage(CastingEnvironment ctx, Context errorCtx) {
        return Text.translatable(
            "mishaps.lapisworks.not_enough_offhand_items",
            this.required,
            this.item.getName(),
            this.item.getCount(),
            this.item.getName()
        );
    }
}
