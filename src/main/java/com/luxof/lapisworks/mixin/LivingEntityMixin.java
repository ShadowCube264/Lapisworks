package com.luxof.lapisworks.mixin;

import com.luxof.lapisworks.mixinsupport.LapisworksInterface;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements LapisworksInterface {

	public AttributeContainer juicedUpVals = new AttributeContainer(
		DefaultAttributeContainer.builder()
			.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 0) // fists
			.add(EntityAttributes.GENERIC_ATTACK_SPEED, 0) // dexterity
			.add(EntityAttributes.GENERIC_MAX_HEALTH, 0) // skin
			.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0) // feet
			.build()
	);
	public boolean fireyFists = false;
	public int lightningBending = 0;

	@Inject(at = @At("HEAD"), method = "onDeath", cancellable = true)
	public void onDeath(DamageSource damageSource, CallbackInfo ci) {
		this.setAllJuicedUpAttrsToZero();
	}

	@Inject(at = @At("HEAD"), method = "onAttacking", cancellable = true)
	public void onAttacking(Entity target, CallbackInfo ci) {
		if (target instanceof LivingEntity) {
			if (this.checkFireyFists()) {
				((LivingEntity)target).setOnFireFor(3);
			}
			int lightningbendingLevel = this.checkLightningBending();
			World world = target.getWorld();
			Vec3d targetPos = target.getPos();

			if ((lightningbendingLevel == 1 && world.isThundering()) ||
				(lightningbendingLevel == 2 && (world.isRaining() || world.isRaining())) ||
				lightningbendingLevel == 3) {
				LightningEntity lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
				lightning.setPos(targetPos.x, targetPos.y, targetPos.z);
				// why?
				world.getServer().getWorld(world.getRegistryKey()).tryLoadEntity(lightning);
			}
		}
	}

	@Override
	public double getAmountOfAttrJuicedUpByAmel(EntityAttribute attribute) {
		return this.juicedUpVals.getCustomInstance(attribute).getBaseValue();
	}

	@Override
	public void setAmountOfAttrJuicedUpByAmel(EntityAttribute attribute, double value) {
		this.juicedUpVals.getCustomInstance(attribute).setBaseValue(value);
	}

	@Override
	public void setAllJuicedUpAttrsToZero() {
		this.juicedUpVals.getAttributesToSend().forEach(
			(EntityAttributeInstance inst) -> {
				inst.setBaseValue(0);
			}
		);
	}

	@Override
	public boolean checkFireyFists() {
		return this.fireyFists;
	}

	@Override
	public void setFireyFists(boolean value) {
		this.fireyFists = value;
	}

	@Override
	public int checkLightningBending() {
		return this.lightningBending;
	}

	@Override
	public void setLightningBending(int level) {
		this.lightningBending = level;
	}

	@Override
	public AttributeContainer getLapisworksAttributes() {
		return this.juicedUpVals;
	}

	@Override
	public void setLapisworksAttributes(AttributeContainer attributes) {
		this.juicedUpVals = attributes;
	}
}
