package com.luxof.lapisworks;

import static com.luxof.lapisworks.Lapisworks.LOGGER;
import static com.luxof.lapisworks.Lapisworks.trinketEquipped;
import static com.luxof.lapisworks.Lapisworks.id;
import static com.luxof.lapisworks.LapisworksNetworking.OPEN_CASTING_GRID;
import static com.luxof.lapisworks.init.ModItems.IRON_SWORD;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.Item;
import net.minecraft.network.PacketByteBuf;

import org.lwjgl.glfw.GLFW;

import com.luxof.lapisworks.init.ModItems;

import at.petrak.hexcasting.common.lib.HexSounds;

public class LapisworksClient implements ClientModInitializer {
    public static KeyBinding useCastingRing;

    public void registerMPPs() {
        ModelPredicateProviderRegistry.register(
            IRON_SWORD,
            id("blocking"), // first person doesn't work but WHATEVER
            (stack, world, entity, seed) -> {
                return entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0F : 0.0F;
            }    
        );
    }

    @Override
    public void onInitializeClient() {
        // the eternal fucking grammar battle with this simple Markiplier ass log will drive me insane
        // thankful i won't have to edit this file anymore
        LOGGER.info("Hello everybody my name is LapisworksClient and today we are going to do is: register a keybind, networking, and Model Predicate Providers!");
        useCastingRing = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "keys.lapisworks.use_casting_ring",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_G, // sorry hexical that keybind is mine now
            "key_category.lapisworks.lapisworks"
        ));
        ClientTickEvents.END_CLIENT_TICK.register(
            (MinecraftClient client) -> {
                if (!useCastingRing.wasPressed()) { return; }
                else if (client.player == null) { return; }
                else if (
                    !(trinketEquipped(client.player, ModItems.CASTING_RING) ||
                    trinketEquipped(client.player, (Item)ModItems.AMEL_RING) ||
                    trinketEquipped(client.player, (Item)ModItems.AMEL_RING2))
                ) { return; }

                PacketByteBuf buf = PacketByteBufs.create();
                if (client.player.isSneaking()) {
                    // clear cast grid
                    client.player.playSound(HexSounds.CAST_FAILURE, 1f, 1f);
                    buf.writeBoolean(true);
                } else {
                    buf.writeBoolean(false);
                }
                ClientPlayNetworking.send(OPEN_CASTING_GRID, buf);
            }
        );
    }
}
