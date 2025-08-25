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
    public static void init() {
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
