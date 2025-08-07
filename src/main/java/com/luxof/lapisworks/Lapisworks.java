package com.luxof.lapisworks;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.luxof.lapisworks.init.Patterns;

public class Lapisworks implements ModInitializer {
	public static final String MOD_ID = "lapisworks";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		Patterns.init();
		ModItems.init_shit();

        LOGGER.info("Luxof's pet Lapisworks is getting a bit hyperactive.");
		LOGGER.info("\"Lapisworks! Lapis Lapis!\"");
		LOGGER.info("Feed it redstone.");
	}

	public static Identifier id(String string) {
		return new Identifier(MOD_ID, string);
	}
}
