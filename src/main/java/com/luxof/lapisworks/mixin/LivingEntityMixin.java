package com.luxof.lapisworks.mixin;

import com.luxof.lapisworks.mixinsupport.DamageSupportInterface;
import com.luxof.lapisworks.mixinsupport.LapisworksInterface;

import net.minecraft.enchantment.EnchantmentHelper;
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

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements LapisworksInterface, DamageSupportInterface {

	public AttributeContainer juicedUpVals = new AttributeContainer(
		DefaultAttributeContainer.builder()
			.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 0) // fists
			.add(EntityAttributes.GENERIC_ATTACK_SPEED, 0) // dexterity
			.add(EntityAttributes.GENERIC_MAX_HEALTH, 0) // skin
			.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0) // feet
			.build()
	);
	public int fireyFists = 0;
	public int lightningBending = 0;
	public int fallDmgRes = 0;
	public int longBreath = 0;
	public int fireResist = 0;

	@Inject(at = @At("HEAD"), method = "onDeath")
	public void onDeath(DamageSource damageSource, CallbackInfo ci) {
		this.setAllJuicedUpAttrsToZero();
		this.setAllEnchantsToZero();
	}

	@Inject(at = @At("HEAD"), method = "onAttacking")
	public void onAttacking(Entity target, CallbackInfo ci) {
		if (target instanceof LivingEntity) {
			if (this.checkFireyFists() == 1) {
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

	@Inject(at = @At("HEAD"), method = "computeFallDamage", cancellable = true)
	public void computeFallDamage(float fallDistance, float damageMultiplier, CallbackInfoReturnable<Integer> cir) {
		if (fallDistance < 10 * ((LapisworksInterface)this).checkFallDmgRes()) {
			cir.setReturnValue(0);
		}
	}

	@Inject(at = @At("HEAD"), method = "getNextAirUnderwater", cancellable = true)
	public void getNextAirUnderwater(int air, CallbackInfoReturnable<Integer> cir) {
		int i = EnchantmentHelper.getRespiration((LivingEntity)(Object)this); // this looks dangerous to me
		cir.setReturnValue(
			// can't shadow getRandom() for whatever reason
			((LivingEntity)(Object)this).getRandom().nextInt(
				i + (this.checkLongBreath() * 2) + 1
			) > 0 ? air : air - 1
		);
	}

	@Inject(at = @At("HEAD"), method = "getNextAirOnLand")
	public void getNextAirOnLand(int air, CallbackInfoReturnable<Integer> cir) {
		cir.setReturnValue(
			Math.min(
				air + 4 + (2 * this.checkLongBreath()),
				// can't shadow getMaxAir either but it's because it's in Entity not LivingEntity
				((LivingEntity)(Object)this).getMaxAir()
			)
		);
	}

	@Inject(at = @At("HEAD"), method = "damage", cancellable = true)
	public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		if (!this.damageHelper(source, amount, (LivingEntity)(Object)this, this.getEnchantments())) {
			cir.setReturnValue(false);
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

	@Override public int checkFireyFists() { return this.fireyFists; }
	@Override public void setFireyFists(int level) { this.fireyFists = level; }

	@Override public int checkLightningBending() { return this.lightningBending; }
	@Override public void setLightningBending(int level) { this.lightningBending = level; }

	@Override public int checkFallDmgRes() { return this.fallDmgRes; }
	@Override public void setFallDmgRes(int level) { this.fallDmgRes = level; }

	@Override public int checkLongBreath() { return this.longBreath; }
	@Override public void setLongBreath(int level) { this.longBreath = level; }

	@Override public int checkFireResist() { return this.fireResist; }
	@Override public void setFireResist(int level) { this.fireResist = level; }

	@Override
	public AttributeContainer getLapisworksAttributes() { return this.juicedUpVals; }
	@Override
	public void setLapisworksAttributes(AttributeContainer attributes) { this.juicedUpVals = attributes; }

	@Override
	public List<Integer> getEnchantments() {
		return List.of(
			this.checkFireyFists(),
			this.checkLightningBending(),
			this.checkFallDmgRes(),
			this.checkLongBreath(),
			this.checkFireResist()
		);
	}

	@Override
	public void setEnchantments(int[] levels) { // nbt shit
		int size = levels.length;
		this.setFireyFists(size >= 1 ? levels[0] : 0);
		this.setLightningBending(size >= 2 ? levels[1] : 0);
		this.setFallDmgRes(size >= 3 ? levels[2] : 0);
		this.setLongBreath(size >= 4 ? levels[3] : 0);
		this.setFireResist(size >= 5 ? levels[4] : 0);
	}

	@Override
	public void setAllEnchantsToZero() {
		this.setFireyFists(0);
		this.setLightningBending(0);
		this.setFallDmgRes(0);
		this.setLongBreath(0);
		this.setFireResist(0);
	}
}
