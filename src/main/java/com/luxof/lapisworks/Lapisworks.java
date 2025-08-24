package com.luxof.lapisworks;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.Map;
import java.util.Optional;

import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;

import com.luxof.lapisworks.init.ModItems;
import com.luxof.lapisworks.init.Patterns;
import com.luxof.lapisworks.init.ModBlocks;
import com.luxof.lapisworks.items.PartiallyAmelStaff;
import com.luxof.lapisworks.items.shit.FullyAmelInterface;
import com.luxof.lapisworks.items.shit.PartiallyAmelInterface;
import com.luxof.lapisworks.items.CastingRing;

import at.petrak.hexcasting.api.pigment.FrozenPigment;
import at.petrak.hexcasting.common.items.ItemStaff;
import at.petrak.hexcasting.common.lib.HexItems;

// why is this project actually big?
public class Lapisworks implements ModInitializer {
	public static Map<Item, FullyAmelInterface> swordToAmelMap = Map.of(
		Items.DIAMOND_SWORD, (FullyAmelInterface)ModItems.DIAMOND_SWORD,
		Items.IRON_SWORD, (FullyAmelInterface)ModItems.IRON_SWORD,
		Items.GOLDEN_SWORD, (FullyAmelInterface)ModItems.GOLD_SWORD
	);

	// ahhh who cares let it error if it will
	private static FrozenPigment BLACK_FP = new FrozenPigment(new ItemStack(HexItems.DYE_PIGMENTS.get(DyeColor.BLACK)), Util.NIL_UUID);
	private static FrozenPigment BROWN_FP = new FrozenPigment(new ItemStack(HexItems.DYE_PIGMENTS.get(DyeColor.BROWN)), Util.NIL_UUID);
	private static FrozenPigment BLUE_FP = new FrozenPigment(new ItemStack(HexItems.DYE_PIGMENTS.get(DyeColor.BLUE)), Util.NIL_UUID);
	private static FrozenPigment CYAN_FP = new FrozenPigment(new ItemStack(HexItems.DYE_PIGMENTS.get(DyeColor.CYAN)), Util.NIL_UUID);
	private static FrozenPigment GRAY_FP = new FrozenPigment(new ItemStack(HexItems.DYE_PIGMENTS.get(DyeColor.GRAY)), Util.NIL_UUID);
	private static FrozenPigment GREEN_FP = new FrozenPigment(new ItemStack(HexItems.DYE_PIGMENTS.get(DyeColor.GREEN)), Util.NIL_UUID);
	private static FrozenPigment LIGHT_BLUE_FP = new FrozenPigment(new ItemStack(HexItems.DYE_PIGMENTS.get(DyeColor.LIGHT_BLUE)), Util.NIL_UUID);
	private static FrozenPigment LIGHT_GRAY_FP = new FrozenPigment(new ItemStack(HexItems.DYE_PIGMENTS.get(DyeColor.LIGHT_GRAY)), Util.NIL_UUID);
	private static FrozenPigment LIME_FP = new FrozenPigment(new ItemStack(HexItems.DYE_PIGMENTS.get(DyeColor.LIME)), Util.NIL_UUID);
	private static FrozenPigment MAGENTA_FP = new FrozenPigment(new ItemStack(HexItems.DYE_PIGMENTS.get(DyeColor.MAGENTA)), Util.NIL_UUID);
	private static FrozenPigment ORANGE_FP = new FrozenPigment(new ItemStack(HexItems.DYE_PIGMENTS.get(DyeColor.ORANGE)), Util.NIL_UUID);
	private static FrozenPigment PINK_FP = new FrozenPigment(new ItemStack(HexItems.DYE_PIGMENTS.get(DyeColor.PINK)), Util.NIL_UUID);
	private static FrozenPigment PURPLE_FP = new FrozenPigment(new ItemStack(HexItems.DYE_PIGMENTS.get(DyeColor.PURPLE)), Util.NIL_UUID);
	private static FrozenPigment RED_FP = new FrozenPigment(new ItemStack(HexItems.DYE_PIGMENTS.get(DyeColor.RED)), Util.NIL_UUID);
	private static FrozenPigment WHITE_FP = new FrozenPigment(new ItemStack(HexItems.DYE_PIGMENTS.get(DyeColor.WHITE)), Util.NIL_UUID);
	private static FrozenPigment YELLOW_FP = new FrozenPigment(new ItemStack(HexItems.DYE_PIGMENTS.get(DyeColor.YELLOW)), Util.NIL_UUID);

	public static final String MOD_ID = "lapisworks";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LapisworksEvents.init();
		Patterns.init();
		ModItems.init_shit();
		LapisworksServer.lockIn();
		ModBlocks.wearASkirt();

        LOGGER.info("Luxof's pet Lapisworks is getting a bit hyperactive.");
		LOGGER.info("\"Lapisworks! Lapis Lapis!\"");
		LOGGER.info("Feed it redstone.");
	}

	public static Identifier id(String string) {
		return new Identifier(MOD_ID, string);
	}

	@Nullable
	public static <T extends Item> PartiallyAmelInterface getPartAmelFromNorm(T item) {

		if (item instanceof PartiallyAmelInterface) {
			return (PartiallyAmelInterface)item;

		} else if (item instanceof ItemStaff) {
			int idx = ModItems.HEX_STAVES.indexOf(item);
			if (idx == -1) { return ModItems.PARTAMEL_STAFF; }
			else { return ModItems.PARTAMEL_STAVES.get(idx); }

		} else {
			return null;
		}

	}

	@Nullable
	public static Item getNormFromPartAmel(PartiallyAmelInterface givenItem) {

		if (givenItem instanceof PartiallyAmelStaff) {
			PartiallyAmelStaff item = (PartiallyAmelStaff)givenItem;
			int idx = ModItems.PARTAMEL_STAVES.indexOf(item);
			if (idx == -1) { return null; }
			else { return ModItems.HEX_STAVES.get(idx); }

		} else {
			return null;
		}

	}

	@Nullable
	public static Integer getRequiredAmelToComplete(ItemStack items) {
		if (items.getItem() instanceof PartiallyAmelInterface) { return items.getDamage(); }
		else if (items.getItem() instanceof FullyAmelInterface) { return 0; }
		else { 
			FullyAmelInterface fullAmel = getFullAmelFromNorm(items.getItem());
			if (fullAmel == null) { return null; } else { return fullAmel.getRequiredAmelToMakeFromBase(); }
		}
	}

	@Nullable
	public static <T extends Item> FullyAmelInterface getFullAmelFromNorm(T item) {
		if (item instanceof CastingRing) { return ModItems.AMEL_RING; }
		else if (item instanceof ItemStaff) { return ModItems.AMEL_STAFF; }
		// i dunno what would happen if i casted null to FullyAmelInterface
		// so
		else { return swordToAmelMap.get(item); }
	}

	public static boolean trinketEquipped(LivingEntity entity, Item item) {
		Optional<TrinketComponent> trinkCompOp = TrinketsApi.getTrinketComponent(entity);
		return trinkCompOp.isEmpty() ? false : trinkCompOp.get().isEquipped(item);
	}

	public static boolean isAmel(Item item) { return ModItems.AMEL_MODELS.indexOf(item) != -1; }

	@Nullable
	public static FrozenPigment getPigmentFromDye(DyeColor dye) {
		// if I can't have Map to do it I'll get a function to do it
		switch (dye) {
			case BLACK: return BLACK_FP;
			case BROWN: return BROWN_FP;
			case BLUE: return BLUE_FP;
			case CYAN: return CYAN_FP;
			case GRAY: return GRAY_FP;
			case GREEN: return GREEN_FP;
			case LIGHT_BLUE: return LIGHT_BLUE_FP;
			case LIGHT_GRAY: return LIGHT_GRAY_FP;
			case LIME: return LIME_FP;
			case MAGENTA: return MAGENTA_FP;
			case ORANGE: return ORANGE_FP;
			case PINK: return PINK_FP;
			case PURPLE: return PURPLE_FP;
			case RED: return RED_FP;
			case WHITE: return WHITE_FP;
			case YELLOW: return YELLOW_FP;
		}
		return null;
	}

	@Nullable
	public static DyeColor getDyeFromPigment(FrozenPigment pigment) {
		// uncommon, that's my excuse
		if (pigment == BLACK_FP) { return DyeColor.BLACK; }
		else if (pigment == BROWN_FP) { return DyeColor.BROWN; }
		else if (pigment == BLUE_FP) { return DyeColor.BLUE; }
		else if (pigment == CYAN_FP) { return DyeColor.CYAN; }
		else if (pigment == GRAY_FP) { return DyeColor.GRAY; }
		else if (pigment == GREEN_FP) { return DyeColor.GREEN; }
		else if (pigment == LIGHT_BLUE_FP) { return DyeColor.LIGHT_BLUE; }
		else if (pigment == LIGHT_GRAY_FP) { return DyeColor.LIGHT_GRAY; }
		else if (pigment == LIME_FP) { return DyeColor.LIME; }
		else if (pigment == MAGENTA_FP) { return DyeColor.MAGENTA; }
		else if (pigment == ORANGE_FP) { return DyeColor.ORANGE; }
		else if (pigment == PINK_FP) { return DyeColor.PINK; }
		else if (pigment == PURPLE_FP) { return DyeColor.PURPLE; }
		else if (pigment == RED_FP) { return DyeColor.RED; }
		else if (pigment == WHITE_FP) { return DyeColor.WHITE; }
		else if (pigment == YELLOW_FP) { return DyeColor.YELLOW; }
		else { return null; }
	}

	public static double clamp(double num, double min, double max) { return Math.min(Math.max(num, min), max); }
	public static float clamp(float num, float min, float max) { return Math.min(Math.max(num, min), max); }
}
