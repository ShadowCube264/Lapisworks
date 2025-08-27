package com.luxof.lapisworks.mishaps;

import java.util.List;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.mishaps.Mishap;
import at.petrak.hexcasting.api.pigment.FrozenPigment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;

public class MishapBadMainhandItem extends Mishap {
    public final ItemStack item;
    public final Text itemText;
    public final Text wanted;

    public MishapBadMainhandItem(Text itemText, Text wanted) {
        this.item = null;
        this.itemText = itemText;
        this.wanted = wanted;
    }
    public MishapBadMainhandItem(ItemStack item, Text wanted) {
        this.item = item;
        this.itemText = null;
        this.wanted = wanted;
    }
    public MishapBadMainhandItem(ItemStack item, Item wanted) {
        this.item = item;
        this.itemText = null;
        this.wanted = wanted.getName();
    }

    @Override
    public FrozenPigment accentColor(CastingEnvironment ctx, Context errorCtx) {
        return dyeColor(DyeColor.BROWN);
    }

    @Override
    protected Text errorMessage(CastingEnvironment ctx, Context errorCtx) {
        if (this.item == null) {
            return Text.translatable(
                "mishaps.lapisworks.bad_general_item.mainhand",
                this.wanted,
                this.itemText
            );
        }
        if (this.item.isEmpty()) {
            return Text.translatable(
                "mishaps.lapisworks.no_item.mainhand",
                this.wanted,
                this.item.getName(),
                this.item.getCount()
            );
        } else {
            return Text.translatable(
                "mishaps.lapisworks.bad_item.mainhand",
                this.wanted
            );
        }
    }

    @Override
    public void execute(CastingEnvironment ctx, Context errorCtx, List<Iota> stack) {
        ctx.getMishapEnvironment().dropHeldItems();
    }
}
