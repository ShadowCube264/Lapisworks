package com.luxof.lapisworks;

import java.util.List;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItems {
    // Short form of "Amethyst Lazuli"
    public static final Item AMEL_ITEM = new Item(new FabricItemSettings().maxCount(64));
    public static final Item AMEL2_ITEM = new Item(new FabricItemSettings().maxCount(64));
    public static final Item AMEL3_ITEM = new Item(new FabricItemSettings().maxCount(64));

    private static final List<String> itemNames = List.of(
        "amel",
        "amel2",
        "amel3"
    );
    private static final List<Item> items = List.of(
        AMEL_ITEM,
        AMEL2_ITEM,
        AMEL3_ITEM
    );

    public static final List<Item> AMEL_MODELS = List.of(AMEL_ITEM, AMEL2_ITEM, AMEL3_ITEM);

    public static final ItemGroup LapisMagicShitGroup = FabricItemGroup.builder()
        .icon(() -> new ItemStack(AMEL_ITEM))
        .displayName(Text.translatable("itemgroup.lapisworks.lapismagicshitgroup"))
        .entries((context, entries) -> {
            for (int i = 0; i < items.size(); i++) {
                entries.add(items.get(i));
            }
        })
        .build();

    public static void init_shit() {
        Registry.register(
            Registries.ITEM_GROUP,
            new Identifier(Lapisworks.MOD_ID, "lapismagicshitgroup"),
            LapisMagicShitGroup
        );
        for (int i = 0; i < items.size(); i++) {
            register(itemNames.get(i), items.get(i));
        }
    }

    public static void register(String name, Item item) {
        Registry.register(Registries.ITEM, new Identifier(Lapisworks.MOD_ID, name), item);
    }
}
