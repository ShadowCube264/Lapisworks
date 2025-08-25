package com.luxof.lapisworks.client;

import at.petrak.hexcasting.common.lib.HexSounds;

import com.luxof.lapisworks.init.ModItems;
import com.luxof.lapisworks.mixinsupport.EnchSentInterface;

import static com.luxof.lapisworks.Lapisworks.LOGGER;
import static com.luxof.lapisworks.Lapisworks.trinketEquipped;
import static com.luxof.lapisworks.Lapisworks.id;
import static com.luxof.lapisworks.Lapisworks.nullConfigFlags;
import static com.luxof.lapisworks.Lapisworks.pickConfigFlags;
import static com.luxof.lapisworks.LapisworksNetworking.OPEN_CASTING_GRID;
import static com.luxof.lapisworks.LapisworksNetworking.SEND_RNG_SEED;
import static com.luxof.lapisworks.LapisworksNetworking.SEND_SENT;
import static com.luxof.lapisworks.init.ModItems.IRON_SWORD;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.Vec3d;

import org.joml.Random;
import org.lwjgl.glfw.GLFW;

public class LapisworksClient implements ClientModInitializer {
    public static KeyBinding useCastingRing;
    public Vec3d bufferSentinelPos = null;
    public Double bufferSentinelAmbit = null;
    public boolean playerHasJoined = false;
    // no checks for null enforced on this; if this is not sent the error is deserved.
    public Random rng = null;

    public static void registerMPPs() {
        ModelPredicateProviderRegistry.register(
            IRON_SWORD,
            id("blocking"), // first person doesn't work but WHATEVER
            (stack, world, entity, seed) -> {
                return entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0F : 0.0F;
            }    
        );
    }

    public static void overlayWorld(MatrixStack ms, float tickDelta) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null) {
            Vec3d sentinel = ((EnchSentInterface)player).getEnchantedSentinel();
            if (sentinel != null) { THEGRANDROTATER.renderEnchantedSentinel(sentinel, ms, tickDelta); }
        }
    }

    @Override
    public void onInitializeClient() {
        // the eternal fucking grammar battle with this simple Markiplier ass log will drive me insane
        // thankful i won't have to edit this file anymore
        // ^^^^ what was that, chief?
        LOGGER.info("Hello everybody my name is LapisworksClient and today we are going to do is: register a keybind, networking, Model Predicate Providers, spin 4D hypercubes for the FUNNY, and client-side rendering!");
        LOGGER.info("Does NONE of that sound fun? Well, that's because it ain't.");

        WorldRenderEvents.AFTER_TRANSLUCENT.register((ctx) -> {
            overlayWorld(ctx.matrixStack(), ctx.tickDelta());
        });

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
                    client.player.playSound(HexSounds.STAFF_RESET, 1f, 1f);
                    buf.writeBoolean(true);
                } else {
                    buf.writeBoolean(false);
                }
                ClientPlayNetworking.send(OPEN_CASTING_GRID, buf);
            }
        );

        
        ClientPlayNetworking.registerGlobalReceiver(
            SEND_SENT,
            (
                client,
                handler,
                buf,
                responseSender
            ) -> {
                boolean banishSentinel = buf.readBoolean();
                if (banishSentinel) {
                    if (!this.playerHasJoined) {
                        this.bufferSentinelPos = null;
                        this.bufferSentinelAmbit = null;
                    } else {
                        ((EnchSentInterface)client.player).setEnchantedSentinel(null, null);
                    }
                    return;
                }
                Vec3d newPos = new Vec3d(buf.readVector3f());
                Double newAmbit = buf.readDouble();
                if (!this.playerHasJoined) {
                    this.bufferSentinelPos = newPos;
                    this.bufferSentinelAmbit = newAmbit;
                } else {
                    ((EnchSentInterface)client.player).setEnchantedSentinel(newPos, newAmbit);
                }
            }
        );
        ClientPlayNetworking.registerGlobalReceiver(
            SEND_RNG_SEED,
            (
                client,
                handler,
                buf,
                responseSender
            ) -> {
                this.rng = new Random(buf.readLong());
                pickConfigFlags(this.rng);
            }
        );

        ClientPlayConnectionEvents.JOIN.register((
            handler,
            sender,
            client
        ) -> {
            if (client.player == null) {
                LOGGER.error("Genuinely how the fuck is client.player null on JOIN??");
                return;
            }
            this.playerHasJoined = true;
            ((EnchSentInterface)client.player).setEnchantedSentinel(
                this.bufferSentinelPos,
                this.bufferSentinelAmbit
            );
        });

        // i could just use the server_stopping event and send a packet then but i already wrote this so
        // whatever
        ClientPlayConnectionEvents.DISCONNECT.register((
            handler,
            client
        ) -> {
            this.playerHasJoined = false;
            this.bufferSentinelPos = null;
            this.bufferSentinelAmbit = null;
            nullConfigFlags();
            if (client.player != null) {
                // i don't know, okay? just in case or something
                ((EnchSentInterface)client.player).setEnchantedSentinel(null, null);
            }
        });
    }
}
