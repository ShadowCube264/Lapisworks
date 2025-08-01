package com.luxof.lapisworks.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;

import com.luxof.lapisworks.mixinsupport.LapisworksInterface;

import at.petrak.hexcasting.api.casting.eval.env.PlayerBasedCastEnv;

@Mixin(value = PlayerBasedCastEnv.class, remap = false)
public abstract class PlayerBasedCastEnvMixin {
	@Shadow
	public abstract LivingEntity getCastingEntity();

	@Inject(at = @At("HEAD"), method = "canOvercast", cancellable = true)
	protected void canOvercast(CallbackInfoReturnable<Boolean> cir) {
		double amountOfMaxHPJuicedUp = ((LapisworksInterface)getCastingEntity())
			.getAmountOfAttrJuicedUpByAmel(EntityAttributes.GENERIC_MAX_HEALTH);
		if (amountOfMaxHPJuicedUp > 0) {
			cir.setReturnValue(false);
		}
	}
}