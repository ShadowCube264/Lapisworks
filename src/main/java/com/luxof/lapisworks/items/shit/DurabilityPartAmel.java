package com.luxof.lapisworks.items.shit;

import static com.luxof.lapisworks.LapisworksIDs.INFUSED_AMEL;

import at.petrak.hexcasting.api.utils.NBTHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public interface DurabilityPartAmel extends BasePartAmel {
    int getAmelWorthInDurability();

    default void makeAmelCountAppropriate(ItemStack stack) {
        NBTHelper.putInt(
            stack,
            INFUSED_AMEL,
            // hope intdiv does it :pray:
            (stack.getMaxDamage() - stack.getDamage()) / getAmelWorthInDurability()
        );
    }

    default void damageWRTAmelCount(PlayerEntity user, World world, Hand hand, int dmg) {
        ItemStack stack = user.getStackInHand(hand);
        stack.damage(dmg, user, any -> {});
        makeAmelCountAppropriate(stack);
    }

    @Override
    default void onImbue(ItemStack stack, int addedAmel) { makeAmelCountAppropriate(stack); }
}
