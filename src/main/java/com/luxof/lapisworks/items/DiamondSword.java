package com.luxof.lapisworks.items;

import at.petrak.hexcasting.api.casting.ParticleSpray;
import at.petrak.hexcasting.api.pigment.FrozenPigment;
import at.petrak.hexcasting.common.casting.actions.raycast.OpEntityRaycast;
import at.petrak.hexcasting.common.lib.HexItems;

import com.luxof.lapisworks.items.shit.AmelSword;

import static com.luxof.lapisworks.Lapisworks.LOGGER;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterials;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.Util;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

// enhancement: slightly more attack damage
// unique ability: media beam
public class DiamondSword extends AmelSword {
    public DiamondSword() {
        // mojang try not to have magic numbers challenge
        super(ToolMaterials.DIAMOND, 4, -2.4F, new Item.Settings(), Items.DIAMOND);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        player.getItemCooldownManager().set(this, 60);
        ItemStack stack = player.getStackInHand(hand);
        stack.damage(10, player, (LivingEntity entity) -> {});
        TypedActionResult<ItemStack> ret = TypedActionResult.consume(stack);

        LOGGER.info("Raycasting!");
        Vec3d startPos = player.getEyePos();
        Vec3d endPos = startPos.add(player.getRotationVector().normalize().multiply(10));

        EntityHitResult hitEntityResult = OpEntityRaycast.INSTANCE.getEntityHitResult(
            player,
            world,
            startPos,
            endPos,
            new Box(startPos, endPos),
            (Entity entity) -> { return true; },
            DEFAULT_MAX_COUNT
        );

        if (hitEntityResult == null) { return ret; }
        LOGGER.info("Damaging entity!");
        Entity hitEntity = hitEntityResult.getEntity();

        hitEntity.damage(
            world.getDamageSources().magic(),
            8f
        );

        try {
            ParticleSpray.burst(hitEntity.getPos(), 3, 25)
                .sprayParticles(
                    world.getServer().getWorld(world.getRegistryKey()),
                    new FrozenPigment(
                        // this will bite me in the ass later wont it
                        new ItemStack(HexItems.DYE_PIGMENTS.get(DyeColor.BLUE)),
                        Util.NIL_UUID
                    )
                );
        } catch (Exception e) {
            LOGGER.error("It DID bite me in the ass. If anyone's reading this, it's eithew Wuxof's fauww ow Hex Casting's fauww ow somethiwng OwO awd thwis is why youw swowd wasn't making pawtickwes! OwO UwU");
            e.printStackTrace();
        }

        return ret;
    }
    @Override
    public UseAction getUseAction(ItemStack stack) { return UseAction.NONE; }
}
