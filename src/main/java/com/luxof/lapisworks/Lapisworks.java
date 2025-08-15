package com.luxof.lapisworks;

import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.luxof.lapisworks.init.Patterns;
import com.luxof.lapisworks.items.PartiallyAmelStaff;
import com.luxof.lapisworks.items.shit.FullyAmelInterface;
import com.luxof.lapisworks.items.shit.PartiallyAmelInterface;
import com.luxof.lapisworks.items.CastingRing;

import at.petrak.hexcasting.common.items.ItemStaff;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;

// why is this project actually big?
public class Lapisworks implements ModInitializer {
	public static Map<Item, FullyAmelInterface> swordToAmelMap = Map.of(
		Items.DIAMOND_SWORD, (FullyAmelInterface)ModItems.DIAMOND_SWORD,
		Items.IRON_SWORD, (FullyAmelInterface)ModItems.IRON_SWORD,
		Items.GOLDEN_SWORD, (FullyAmelInterface)ModItems.GOLD_SWORD
	);
	public static final String MOD_ID = "lapisworks";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		Patterns.init();
		ModItems.init_shit();
		LapisworksServer.lockIn();

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
}
