package com.luxof.lapisworks.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.luxof.lapisworks.mixinsupport.LapisworksInterface;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.nbt.NbtCompound;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(at = @At("HEAD"), method = "readNbt")
	public void readNbt(NbtCompound nbt, CallbackInfo ci) {
        if ((Object)this instanceof LivingEntity) {
            ((LapisworksInterface)this).setLapisworksAttributes(new AttributeContainer(
                DefaultAttributeContainer.builder()
                    .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, nbt.getDouble("LAPISWORKS_JUICED_FISTS"))
                    .add(EntityAttributes.GENERIC_ATTACK_SPEED, nbt.getDouble("LAPISWORKS_JUICED_DEX"))
                    .add(EntityAttributes.GENERIC_MAX_HEALTH, nbt.getDouble("LAPISWORKS_JUICED_SKIN"))
                    .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, nbt.getDouble("LAPISWORKS_JUICED_FEET"))
                    .build()
            ));
            ((LapisworksInterface)this).setEnchantments(
                nbt.getIntArray("LAPISWORKS_ENCHANTMENTS")
            );
        }
	}

	@Inject(at = @At("HEAD"), method = "writeNbt")
	public void writeNbt(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> cir) {
        if ((Object)this instanceof LivingEntity) {
            AttributeContainer attrs = ((LapisworksInterface)this).getLapisworksAttributes();
            nbt.putDouble("LAPISWORKS_JUICED_FISTS", attrs.getBaseValue(EntityAttributes.GENERIC_ATTACK_DAMAGE));
            nbt.putDouble("LAPISWORKS_JUICED_DEX", attrs.getBaseValue(EntityAttributes.GENERIC_ATTACK_SPEED));
            nbt.putDouble("LAPISWORKS_JUICED_SKIN", attrs.getBaseValue(EntityAttributes.GENERIC_MAX_HEALTH));
            nbt.putDouble("LAPISWORKS_JUICED_FEET", attrs.getBaseValue(EntityAttributes.GENERIC_MOVEMENT_SPEED));
            nbt.putIntArray("LAPISWORKS_ENCHANTMENTS", ((LapisworksInterface)this).getEnchantments());
        }
	}
}
