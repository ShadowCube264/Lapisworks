package com.luxof.lapisworks.init;

import at.petrak.hexcasting.common.lib.HexItems;

import com.luxof.lapisworks.items.shit.FullyAmelInterface;
import com.luxof.lapisworks.items.shit.PartiallyAmelInterface;

import static com.luxof.lapisworks.init.ModItems.AMEL_ITEM;
import static com.luxof.lapisworks.init.ModItems.AMEL_RING;
import static com.luxof.lapisworks.init.ModItems.AMEL_RING2;
import static com.luxof.lapisworks.init.ModItems.AMEL_STAFF;
import static com.luxof.lapisworks.init.ModItems.CASTING_RING;
import static com.luxof.lapisworks.init.ModItems.PARTAMEL_ACACIA_STAFF;
import static com.luxof.lapisworks.init.ModItems.PARTAMEL_BAMBOO_STAFF;
import static com.luxof.lapisworks.init.ModItems.PARTAMEL_BIRCH_STAFF;
import static com.luxof.lapisworks.init.ModItems.PARTAMEL_CHERRY_STAFF;
import static com.luxof.lapisworks.init.ModItems.PARTAMEL_CRIMSON_STAFF;
import static com.luxof.lapisworks.init.ModItems.PARTAMEL_DARK_OAK_STAFF;
import static com.luxof.lapisworks.init.ModItems.PARTAMEL_EDIFIED_STAFF;
import static com.luxof.lapisworks.init.ModItems.PARTAMEL_JUNGLE_STAFF;
import static com.luxof.lapisworks.init.ModItems.PARTAMEL_MANGROVE_STAFF;
import static com.luxof.lapisworks.init.ModItems.PARTAMEL_MINDSPLICE_STAFF;
import static com.luxof.lapisworks.init.ModItems.PARTAMEL_OAK_STAFF;
import static com.luxof.lapisworks.init.ModItems.PARTAMEL_SPRUCE_STAFF;
import static com.luxof.lapisworks.init.ModItems.PARTAMEL_WARPED_STAFF;
import static com.luxof.lapisworks.init.ModItems.AMEL2_ITEM;
import static com.luxof.lapisworks.init.ModItems.AMEL3_ITEM;
import static com.luxof.lapisworks.init.ModItems.AMEL4_ITEM;
import static com.luxof.lapisworks.LapisworksIDs.AMEL_TAG;
import static com.luxof.lapisworks.LapisworksIDs.ENCHSENT_ADVANCEMENT;
import static com.luxof.lapisworks.LapisworksIDs.FLAY_ARTMIND_ADVANCEMENT;
import static com.luxof.lapisworks.LapisworksIDs.JUKEBOX_INTO_LIVE_JUKEBOX;
import static com.luxof.lapisworks.LapisworksIDs.SIMPLE_MIND_INTO_AMETHYST;

import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.logging.log4j.util.TriConsumer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/** all of the stuff here is stuff that's looked at when the player is in the world
 * so no need to rush registering all this before Lapisworks or something */
public class Mutables {
    public static List<Identifier> wizardDiariesGainableAdvancements = new ArrayList<>();
    private static List<Item> FROM_ITEMS = new ArrayList<Item>();
    private static List<PartiallyAmelInterface> TO_PARTAMEL = new ArrayList<>();
    private static List<FullyAmelInterface> TO_FULLAMEL = new ArrayList<>();
    private static Map<Item, Item> moldAmelRecipes = new HashMap<>();
    // like what the fuck are these types below me scoob
    private static Map<Identifier, BiPredicate<BlockPos, World>> imbueMindRecipeFilters = new HashMap<>();
    private static Map<Identifier, TriConsumer<BlockPos, World, ServerPlayerEntity>> imbueMindRecipeDoers = new HashMap<>();


    public static boolean isAmel(ItemStack stack) { return stack.isIn(TagKey.of(RegistryKeys.ITEM, AMEL_TAG)); }
    public static boolean isAmel(Item item) { return isAmel(new ItemStack(item)); }


    public static void registerInfusionRecipe(@NotNull Item normal, PartiallyAmelInterface partInfusion, FullyAmelInterface fullInfusion) { FROM_ITEMS.add(normal); TO_PARTAMEL.add(partInfusion); TO_FULLAMEL.add(fullInfusion); }
    /** Returns a partAmel even if normal is a partAmel or a fullAmel. */
    @Nullable public static PartiallyAmelInterface getPartAmelProduct(Item normal) {
        if (normal instanceof PartiallyAmelInterface) { return (PartiallyAmelInterface)normal; }
        else if (normal instanceof FullyAmelInterface) { return getPartAmelRequiredFor((FullyAmelInterface)normal); }
        return getFirstValue(normal, FROM_ITEMS, TO_PARTAMEL);
    }
    /** Returns a fullAmel even if normal is a partAmel or a fullAmel. */
    @Nullable public static FullyAmelInterface getFullyAmelProduct(Item normal) {
        if (normal instanceof PartiallyAmelInterface) { return getFullyAmelProduct((PartiallyAmelInterface)normal); }
        else if (normal instanceof FullyAmelInterface) { return (FullyAmelInterface)normal; }
        return getFirstValue(normal, FROM_ITEMS, TO_FULLAMEL);
    }
    @Nullable public static FullyAmelInterface getFullyAmelProduct(PartiallyAmelInterface partAmel) { return getFirstValue(partAmel, TO_PARTAMEL, TO_FULLAMEL); }

    @Nullable public static Item getItemRequiredFor(PartiallyAmelInterface partAmel) { return getFirstValue(partAmel, TO_PARTAMEL, FROM_ITEMS); }
    @Nullable public static Item getItemRequiredFor(FullyAmelInterface fullAmel) { return getFirstValue(fullAmel, TO_FULLAMEL, FROM_ITEMS); }
    @Nullable public static PartiallyAmelInterface getPartAmelRequiredFor(FullyAmelInterface fullAmel) { return getFirstValue(fullAmel, TO_FULLAMEL, TO_PARTAMEL); }

    // justification for protection being taken off: if you're using these you should know what you're doin,
    // and it's not that big of a deal while convenience matters in the singular ones.
    // this does not apply to condoms :wilted_rose:
    public static List<PartiallyAmelInterface> getPartAmelProducts(Item normal) { return getAllValues(normal, FROM_ITEMS, TO_PARTAMEL); }
    public static List<FullyAmelInterface> getFullAmelProducts(Item normal) { return getAllValues(normal, FROM_ITEMS, TO_FULLAMEL); }
    public static List<FullyAmelInterface> getFullAmelProducts(PartiallyAmelInterface partAmel) { return getAllValues(partAmel, TO_PARTAMEL, TO_FULLAMEL); }

    public static List<Item> getItemsRequiredFor(PartiallyAmelInterface partAmel) { return getAllValues(partAmel, TO_PARTAMEL, FROM_ITEMS); }
    public static List<Item> getItemsRequiredFor(FullyAmelInterface fullAmel) { return getAllValues(fullAmel, TO_FULLAMEL, FROM_ITEMS); }
    public static List<PartiallyAmelInterface> getPartAmelsRequiredFor(FullyAmelInterface fullAmel) { return getAllValues(fullAmel, TO_FULLAMEL, TO_PARTAMEL); }


    public static void registerMoldAmelRecipe(@NotNull Item any, @NotNull Item any2) { moldAmelRecipes.put(any, any2); }
    @Nullable public static Item getMoldAmelProduct(@NotNull Item any) { return moldAmelRecipes.get(any); }


    /** Note: any of the params passed to your doer can be null. */
    public static void registerImbueMindRecipe(
        @NotNull BiPredicate<BlockPos, World> filter,
        @NotNull Identifier id,
        // i'm sure log4j wouldn't mind lol
        @NotNull TriConsumer<BlockPos, World, ServerPlayerEntity> doer
    ) {
        imbueMindRecipeFilters.put(id, filter);
        imbueMindRecipeDoers.put(id, doer);
    }
    public static Map<Identifier, TriConsumer<BlockPos, World, ServerPlayerEntity>> checkImbueMindRecipes(
        BlockPos bp, World world
    ) {
        // sure i could make the List<Pair<>> a Map<>, but this has silly uses, a'ight?
        Map<Identifier, TriConsumer<BlockPos, World, ServerPlayerEntity>> ret = new HashMap<>();
        for (Identifier key : imbueMindRecipeFilters.keySet()) {
            if (imbueMindRecipeFilters.get(key).test(bp, world)) {
                ret.put(key, imbueMindRecipeDoers.get(key));
            }
        }
        return ret;
    }


    /** doesn't ever give null in the List. */
    private static <T extends Object, T2 extends Object> List<T2> getAllValues(T key, List<T> keys, List<T2> values) {
        List<T2> ret = new ArrayList<T2>();
        for (int i = 0; i < values.size(); i++) {
            T gotKey = keys.get(i);
            if (gotKey != key) { continue; }
            else if (gotKey == null) { continue; }
            ret.add(values.get(i));
        }
        return ret;
    }
    /** only returns null when there is no non-null value for the given key. */
    @Nullable
    private static <T extends Object, T2 extends Object> T2 getFirstValue(T key, List<T> keys, List<T2> values) {
        for (int i = 0; i < values.size(); i++) {
            T gotKey = keys.get(i);
            if (gotKey != key) { continue; }
            else if (gotKey == null) { continue; }
            return values.get(i);
        }
        return null;
    }



    public static void innitBruv() {
        wizardDiariesGainableAdvancements.add(ENCHSENT_ADVANCEMENT);
        wizardDiariesGainableAdvancements.add(FLAY_ARTMIND_ADVANCEMENT);

        registerInfusionRecipe((Item)HexItems.STAFF_ACACIA, (PartiallyAmelInterface)PARTAMEL_ACACIA_STAFF, AMEL_STAFF);
        registerInfusionRecipe((Item)HexItems.STAFF_BAMBOO, (PartiallyAmelInterface)PARTAMEL_BAMBOO_STAFF, AMEL_STAFF);
        registerInfusionRecipe((Item)HexItems.STAFF_BIRCH, (PartiallyAmelInterface)PARTAMEL_BIRCH_STAFF, AMEL_STAFF);
        registerInfusionRecipe((Item)HexItems.STAFF_CHERRY, (PartiallyAmelInterface)PARTAMEL_CHERRY_STAFF, AMEL_STAFF);
        registerInfusionRecipe((Item)HexItems.STAFF_CRIMSON, (PartiallyAmelInterface)PARTAMEL_CRIMSON_STAFF, AMEL_STAFF);
        registerInfusionRecipe((Item)HexItems.STAFF_DARK_OAK, (PartiallyAmelInterface)PARTAMEL_DARK_OAK_STAFF, AMEL_STAFF);
        registerInfusionRecipe((Item)HexItems.STAFF_EDIFIED, (PartiallyAmelInterface)PARTAMEL_EDIFIED_STAFF, AMEL_STAFF);
        registerInfusionRecipe((Item)HexItems.STAFF_JUNGLE, (PartiallyAmelInterface)PARTAMEL_JUNGLE_STAFF, AMEL_STAFF);
        registerInfusionRecipe((Item)HexItems.STAFF_MANGROVE, (PartiallyAmelInterface)PARTAMEL_MANGROVE_STAFF, AMEL_STAFF);
        registerInfusionRecipe((Item)HexItems.STAFF_MINDSPLICE, (PartiallyAmelInterface)PARTAMEL_MINDSPLICE_STAFF, AMEL_STAFF);
        registerInfusionRecipe((Item)HexItems.STAFF_OAK, (PartiallyAmelInterface)PARTAMEL_OAK_STAFF, AMEL_STAFF);
        registerInfusionRecipe((Item)HexItems.STAFF_SPRUCE, (PartiallyAmelInterface)PARTAMEL_SPRUCE_STAFF, AMEL_STAFF);
        registerInfusionRecipe((Item)HexItems.STAFF_WARPED, (PartiallyAmelInterface)PARTAMEL_WARPED_STAFF, AMEL_STAFF);

        registerInfusionRecipe(CASTING_RING, null, AMEL_RING);
        registerInfusionRecipe(CASTING_RING, null, AMEL_RING2);

        registerMoldAmelRecipe(AMEL_ITEM, AMEL2_ITEM);
        registerMoldAmelRecipe(AMEL2_ITEM, AMEL3_ITEM);
        registerMoldAmelRecipe(AMEL3_ITEM, AMEL4_ITEM);
        registerMoldAmelRecipe(AMEL4_ITEM, AMEL_ITEM);
        registerMoldAmelRecipe((Item)AMEL_RING, (Item)AMEL_RING2);
        registerMoldAmelRecipe((Item)AMEL_RING2, (Item)AMEL_RING);

        registerImbueMindRecipe(
            (bp, world) -> world.getBlockState(bp).getBlock() == Blocks.AMETHYST_BLOCK,
            SIMPLE_MIND_INTO_AMETHYST,
            (bp, world, caster) -> world.setBlockState(bp, Blocks.BUDDING_AMETHYST.getDefaultState())
        );
        registerImbueMindRecipe(
            (bp, world) -> world.getBlockState(bp).getBlock() == Blocks.JUKEBOX,
            JUKEBOX_INTO_LIVE_JUKEBOX,
            (bp, world, caster) -> world.setBlockState(bp, ModBlocks.LIVE_JUKEBOX_BLOCK.getDefaultState())
        );
    }
}
