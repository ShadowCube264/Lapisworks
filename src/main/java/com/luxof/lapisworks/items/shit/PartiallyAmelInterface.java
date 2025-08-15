package com.luxof.lapisworks.items.shit;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public interface PartiallyAmelInterface {
    int getMaxDurability();
    int getAmelWorthInDurability();

    default void specialUseBehaviour(PlayerEntity player, World world, Hand hand) {};
    default void specialAttackBehaviour(PlayerEntity player, World world, Hand hand, Entity entity) {};
}
