package com.luxof.lapisworks;

import static com.luxof.lapisworks.Lapisworks.LOGGER;

import com.luxof.lapisworks.init.ThemConfigFlags;
import com.luxof.lapisworks.items.shit.PartiallyAmelInterface;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import vazkii.patchouli.api.PatchouliAPI;

public class LapisworksEvents {
    // fucking event doesn't work
    // ^^^ i realize now, that that was probably because i didn't initialize ts (ts (this) shit)
    public static void init() {
        // kept for the future if i ever need it
        /*UseItemCallback.EVENT.register(
            (PlayerEntity player, World world, Hand hand) -> {
                ItemStack stack = player.getStackInHand(hand);
                Item item = stack.getItem();
                if (!(item instanceof PartiallyAmelInterface)) {
                    return TypedActionResult.pass(stack);
                }
                ((PartiallyAmelInterface)item).specialUseBehaviour(player, world, hand);
                LOGGER.info("someone used the item yessss");
                return TypedActionResult.pass(stack);
            }
        );*/

        AttackEntityCallback.EVENT.register(
            (PlayerEntity player, World world, Hand hand, Entity entity, EntityHitResult hitResult) -> {
                Item item = player.getStackInHand(hand).getItem();
                if (player.isSpectator()) { return ActionResult.PASS; }
                else if (hitResult.getType() == HitResult.Type.MISS) { return ActionResult.PASS; }
                else if (!(entity instanceof LivingEntity)) { return ActionResult.PASS; }
                else if (!(item instanceof PartiallyAmelInterface)) { return ActionResult.PASS; }
                ((PartiallyAmelInterface)item).specialAttackBehaviour(
                    player,
                    world,
                    hand,
                    entity
                );
                return ActionResult.PASS;
            }
        );

        ServerLifecycleEvents.SERVER_STARTED.register((server) -> {
            int picked = Math.abs((int)(server.getOverworld().getSeed() % 6));
            LOGGER.info("Seed was " + server.getOverworld().getSeed());
            LOGGER.info("We picked " + server.getOverworld().getSeed() % 6 + ", so that's " + picked);
            LOGGER.info("Btw just casted to int no absing that's " + (int)(server.getOverworld().getSeed() % 6));
            PatchouliAPI.get().setConfigFlag(ThemConfigFlags.allEnchSentFlags[picked], true);
            ThemConfigFlags.chosenEnchSent = picked;
        });
        ServerLifecycleEvents.SERVER_STOPPING.register((server) -> {
            int picked = Math.abs((int)(server.getOverworld().getSeed() % 6));
            PatchouliAPI.get().setConfigFlag(ThemConfigFlags.allEnchSentFlags[picked], false);
            ThemConfigFlags.chosenEnchSent = null;
        });
    }
}
