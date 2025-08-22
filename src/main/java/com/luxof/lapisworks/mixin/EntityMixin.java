package com.luxof.lapisworks.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.luxof.lapisworks.mixinsupport.EnchSentInterface;
import com.luxof.lapisworks.mixinsupport.LapisworksInterface;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.util.math.Vec3d;

@Mixin(Entity.class)
public class EntityMixin {
	@Inject(at = @At("HEAD"), method = "isInvulnerableTo", cancellable = true)
	public void isInvulnerableTo(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        if ((Object)this instanceof LivingEntity) {
            if (damageSource.isIn(DamageTypeTags.IS_FIRE) &&
                ((LapisworksInterface)this).checkFireResist() >= 1) {
                cir.setReturnValue(true);
            }
        }
	}

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
        if ((Object)this instanceof PlayerEntity) {
            if (nbt.getBoolean("LAPISWORKS_EnchSent_exists")) {
                // ServerPlayNetwork or whatever doesn't exist this early in the booting
                ((EnchSentInterface)this).setEnchantedSentinelNoSync(
                    new Vec3d(
                        nbt.getDouble("LAPISWORKS_EnchSent_posX"),
                        nbt.getDouble("LAPISWORKS_EnchSent_posY"),
                        nbt.getDouble("LAPISWORKS_EnchSent_posZ")
                    ),
                    nbt.getDouble("LAPISWORKS_EnchSent_Ambit")
                );
            }
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
        if ((Object)this instanceof PlayerEntity) {
            Vec3d sentPos = ((EnchSentInterface)this).getEnchantedSentinel();
            Double ambit = ((EnchSentInterface)this).getEnchantedSentinelAmbit();
            nbt.putBoolean("LAPISWORKS_EnchSent_exists", sentPos != null);
            Vec3d usePos = sentPos != null ? sentPos : new Vec3d(0.0, 0.0, 0.0);
            double useAmbit = ambit != null ? ambit : 0.0;
            nbt.putDouble("LAPISWORKS_EnchSent_posX", usePos.x);
            nbt.putDouble("LAPISWORKS_EnchSent_posY", usePos.y);
            nbt.putDouble("LAPISWORKS_EnchSent_posZ", usePos.z);
            nbt.putDouble("LAPISWORKS_EnchSent_Ambit", useAmbit);
        }
	}
}
