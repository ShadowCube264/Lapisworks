package com.luxof.lapisworks.client;

import at.petrak.hexcasting.client.ClientTickCounter;
import at.petrak.hexcasting.common.lib.HexSounds;

import com.luxof.lapisworks.init.ModItems;
import com.luxof.lapisworks.mixinsupport.EnchSentInterface;
import com.mojang.blaze3d.systems.RenderSystem;

import static com.luxof.lapisworks.Lapisworks.LOGGER;
import static com.luxof.lapisworks.Lapisworks.trinketEquipped;
import static com.luxof.lapisworks.Lapisworks.id;
import static com.luxof.lapisworks.LapisworksNetworking.OPEN_CASTING_GRID;
import static com.luxof.lapisworks.LapisworksNetworking.SEND_SENT;
import static com.luxof.lapisworks.init.ModItems.IRON_SWORD;

import java.util.List;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class LapisworksClient implements ClientModInitializer {
    public static KeyBinding useCastingRing;

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
            if (sentinel != null) {
                renderEnchantedSentinel(ms, sentinel, tickDelta);
                LOGGER.info("yay");
            } else {
                //LOGGER.info("NULL SENT");
            }
        }
    }

    public static RotationAxis allAxes = RotationAxis.of(new Vector3f(1, 1, 1));
    public static List<Vector3f> vertices = List.of(
        new Vector3f(-1.0f, 1.0f, 1.0f),
        new Vector3f(1.0f, 1.0f, 1.0f),
        new Vector3f(-1.0f, -1.0f, 1.0f),
        new Vector3f(1.0f, -1.0f, 1.0f),
        new Vector3f(-1.0f, 1.0f, -1.0f),
        new Vector3f(1.0f, 1.0f, -1.0f),
        new Vector3f(-1.0f, -1.0f, -1.0f),
        new Vector3f(1.0f, -1.0f, -1.0f)
    );
    public static List<Integer> drawOrder = List.of(
        0, 1, 3, 2, 0, 2,
        7, 5, 1, 7,
        6, 4, 5, 6,
        2, 0, 4, 2,
        7,
        5, 4, 1
    );
    public static void renderEnchantedSentinel(
        MatrixStack ms,
        Vec3d sentinel,
        float tickDelta
    ) {
        ms.push();

        MinecraftClient mc = MinecraftClient.getInstance();
        Camera cam = mc.gameRenderer.getCamera();
        Vec3d camPos = cam.getPos();
        ms.translate(
            sentinel.x - camPos.x,
            sentinel.y - camPos.y,
            sentinel.z - camPos.z
        ); 

        float time = ClientTickCounter.getTotal() / 2; // why not? it's perfectly useable
        float spinSpeed = 1f / 30f;

        ms.multiply(allAxes.rotation(spinSpeed * time));
        //ms.scale(0.5f, 0.5f, 0.5f);
        Matrix4f matrix = ms.peek().getPositionMatrix();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.disableDepthTest();
        RenderSystem.disableCull();
        //RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.lineWidth(5f);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        buffer.begin(DrawMode.LINE_STRIP, VertexFormats.POSITION_COLOR);

        drawOrder.forEach((Integer idx) -> {
            Vector3f v = vertices.get(idx);
            buffer.vertex(matrix, v.x, v.y, v.z).color(132, 62, 207, 255).next();
        });

        tessellator.draw();

        RenderSystem.enableDepthTest();
        RenderSystem.enableCull();
        ms.pop();
    }

    @Override
    public void onInitializeClient() {
        // the eternal fucking grammar battle with this simple Markiplier ass log will drive me insane
        // thankful i won't have to edit this file anymore
        LOGGER.info("Hello everybody my name is LapisworksClient and today we are going to do is: register a keybind, networking, Model Predicate Providers, and client-side rendering!");
        LOGGER.info("Does NONE of that sound fun? Well, that's because it ain't.");

        WorldRenderEvents.AFTER_TRANSLUCENT.register((WorldRenderContext ctx) -> {
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
                if (client.player == null) {
                    return;
                }
                Vec3d newPos;
                Double newAmbit;
                try {
                    newPos = new Vec3d(buf.readVector3f());
                    newAmbit = buf.readDouble();
                } catch (Exception e) {
                    newPos = null;
                    newAmbit = null;
                }
                ((EnchSentInterface)client.player).setEnchantedSentinel(newPos, newAmbit);
            }
        );
    }
}
