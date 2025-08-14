package com.luxof.lapisworks.items;

import com.luxof.lapisworks.items.shit.AmelSword;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterials;

public class GoldSword extends AmelSword {
    public GoldSword() {
        super(ToolMaterials.GOLD, 3, -2.4F, new Item.Settings(), Items.GOLD_INGOT);
    }
}
