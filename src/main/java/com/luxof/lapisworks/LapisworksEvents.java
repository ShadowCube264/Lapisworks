package com.luxof.lapisworks;

import com.luxof.lapisworks.items.PartiallyAmelInterface;
import static com.luxof.lapisworks.Lapisworks.LOGGER;

import net.fabricmc.fabric.api.event.player.UseItemCallback;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class LapisworksEvents {
    // fucking event doesn't work
    // TODO: fix this (somehow) and remove mixins or remove this
    public static void init() {
        UseItemCallback.EVENT.register(
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
        );
    }
}
