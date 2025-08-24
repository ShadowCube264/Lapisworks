package com.luxof.lapisworks.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

import com.luxof.lapisworks.mixinsupport.EnchSentInterface;
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

	public boolean isVecInRangeEnvironmentHelper(Vec3d vec) {
		if (this.getCastingEntity() == null) { return false; }
		else if (!(this.getCastingEntity() instanceof PlayerEntity)) { return false; }

		EnchSentInterface player = (EnchSentInterface)this.getCastingEntity();
		Vec3d sentPos = player.getEnchantedSentinel();
		Double sentAmbit = player.getEnchantedSentinelAmbit();

		if (sentPos == null) { return false; }
		if (vec.distanceTo(sentPos) > sentAmbit) { return false; }

		return true;
	}

	@Inject(at = @At("HEAD"), method = "isVecInRangeEnvironment", cancellable = true)
	public void isVecInRangeEnvironment(Vec3d vec, CallbackInfoReturnable<Boolean> cir) {
		boolean res = isVecInRangeEnvironmentHelper(vec);
		if (res) { cir.setReturnValue(res); }
	}
}