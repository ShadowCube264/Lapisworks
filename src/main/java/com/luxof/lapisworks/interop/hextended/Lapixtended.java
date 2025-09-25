package com.luxof.lapisworks.interop.hextended;

import abilliontrillionstars.hextended.LanisHextendedStaves;

import com.luxof.lapisworks.init.Mutables;
import com.luxof.lapisworks.interop.hextended.items.AmelWand;
import com.luxof.lapisworks.interop.hextended.items.PartAmelWand;
import com.luxof.lapisworks.items.shit.FullyAmelInterface;
import com.luxof.lapisworks.items.shit.PartiallyAmelInterface;

import static com.luxof.lapisworks.Lapisworks.LOGGER;
import static com.luxof.lapisworks.init.ModItems.registerItem;

import java.util.HashMap;
import java.util.Map;

import static java.util.Map.entry;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;

/* Lapixtended? Lapistended? Hextended Lapis?
 * maybe Lapix/Lapistended for Hextended Gear stuff added by Lapisworks
 * and Hextended Lapis for Lapisworks stuff added by Hextended Gear
 * that makes sense i think (at least in my head) */
public class Lapixtended {
    public static final Item PARTAMEL_ACACIA_WAND = registerItem("staves/incomplete/acacia_wand", new PartAmelWand());
    public static final Item PARTAMEL_BAMBOO_WAND = registerItem("staves/incomplete/bamboo_wand", new PartAmelWand());
    public static final Item PARTAMEL_BIRCH_WAND = registerItem("staves/incomplete/birch_wand", new PartAmelWand());
    public static final Item PARTAMEL_CHERRY_WAND = registerItem("staves/incomplete/cherry_wand", new PartAmelWand());
    public static final Item PARTAMEL_CRIMSON_WAND = registerItem("staves/incomplete/crimson_wand", new PartAmelWand());
    public static final Item PARTAMEL_DARK_OAK_WAND = registerItem("staves/incomplete/dark_oak_wand", new PartAmelWand());
    public static final Item PARTAMEL_EDIFIED_WAND = registerItem("staves/incomplete/edified_wand", new PartAmelWand());
    public static final Item PARTAMEL_JUNGLE_WAND = registerItem("staves/incomplete/jungle_wand", new PartAmelWand());
    public static final Item PARTAMEL_MANGROVE_WAND = registerItem("staves/incomplete/mangrove_wand", new PartAmelWand());
    public static final Item PARTAMEL_MINDSPLICE_WAND = registerItem("staves/incomplete/mindsplice_wand", new PartAmelWand());
    public static final Item PARTAMEL_OAK_WAND = registerItem("staves/incomplete/oak_wand", new PartAmelWand());
    public static final Item PARTAMEL_SPRUCE_WAND = registerItem("staves/incomplete/spruce_wand", new PartAmelWand());
    public static final Item PARTAMEL_WARPED_WAND = registerItem("staves/incomplete/warped_wand", new PartAmelWand());
    public static final Item PARTAMEL_WAND = registerItem("staves/incomplete/generic_wand", new PartAmelWand());
    public static final Item AMEL_WAND = registerItem("staves/amel_wand", new AmelWand());
    private static boolean recipesInitialized = false;

    public static void initHextendedInterop() {
        Mutables.scheduleRegisterAtServerStart(() -> {
            if (recipesInitialized) return;
            // a saying i know goes:
            // "the law's hands are long."
            recipesInitialized = true;
            Map<String, Item> WAND_TO_PARTAMEL = new HashMap<String, Item>(Map.ofEntries(
                // source for ids: hextended
                entry("acacia", PARTAMEL_ACACIA_WAND),
                entry("bamboo", PARTAMEL_BAMBOO_WAND),
                entry("birch", PARTAMEL_BIRCH_WAND),
                entry("cherry", PARTAMEL_CHERRY_WAND),
                entry("crimson", PARTAMEL_CRIMSON_WAND),
                entry("dark_oak", PARTAMEL_DARK_OAK_WAND),
                entry("edified", PARTAMEL_EDIFIED_WAND),
                entry("jungle", PARTAMEL_JUNGLE_WAND),
                entry("mangrove", PARTAMEL_MANGROVE_WAND),
                entry("mindsplice", PARTAMEL_MINDSPLICE_WAND),
                entry("oak", PARTAMEL_OAK_WAND),
                entry("spruce", PARTAMEL_SPRUCE_WAND)
            ));
            for (String id : WAND_TO_PARTAMEL.keySet()) {
                LOGGER.info("Is " + id + " null? " + Registries.ITEM.get(
                    LanisHextendedStaves.id("staff/long" + id)
                ));
                Mutables.registerInfusionRecipe(
                    Registries.ITEM.get(LanisHextendedStaves.id("staff/long/" + id)),
                    (PartiallyAmelInterface)WAND_TO_PARTAMEL.get(id),
                    (FullyAmelInterface)AMEL_WAND
                );
            }
        });
    }
}
