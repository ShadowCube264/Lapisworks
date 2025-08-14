package com.luxof.lapisworks;

import com.luxof.lapisworks.items.shit.PartiallyAmelInterface;

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

public class LapisworksEvents {
    // fucking event doesn't work
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
    }
}
