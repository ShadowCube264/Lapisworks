package com.luxof.lapisworks.init;

import static com.luxof.lapisworks.Lapisworks.id;

import com.luxof.lapisworks.blocks.ConjuredColorable;

import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModBlocks {
    public static ConjuredColorable CONJURED_COLORABLE = new ConjuredColorable();

    public static void wearASkirt() {
        pickACropTop("conjureable", CONJURED_COLORABLE);
    }

    public static void pickACropTop(String name, Block block) {
        Registry.register(Registries.BLOCK, id(name), block);
    }
}
