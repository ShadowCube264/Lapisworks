package com.luxof.lapisworks.mixin;

import static com.luxof.lapisworks.Lapisworks.LOGGER;
import static com.luxof.lapisworks.LapisworksNetworking.SEND_SENT;

import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.luxof.lapisworks.mixinsupport.EnchSentInterface;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

// "you know you can just use Cardinal Components for this-"
// Metal Gear Rising: Revengeance OST It Has To Be This Way Extended
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements EnchSentInterface {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) { super(entityType, world); }

    public Vec3d enchSentPos = null;
    public Double sentRange = null;

    @Override @Nullable public Vec3d getEnchantedSentinel() { return this.enchSentPos; }
    @Override @Nullable public Double getEnchantedSentinelAmbit() { return this.sentRange; }
    @Override
    public void setEnchantedSentinel(Vec3d pos, Double ambit) {
        this.enchSentPos = pos;
        this.sentRange = ambit;
        LOGGER.info("i scream i scream i scream sentRange is " + sentRange);
        
        // can you tell i don't know what i'm doing?
        if (!((Object)this instanceof ClientPlayerEntity)) {
            PacketByteBuf buf = PacketByteBufs.create();
            if (this.enchSentPos != null) {
                Vec3d temp = this.enchSentPos;
                buf.writeVector3f(new Vector3f((float)temp.x, (float)temp.y, (float)temp.z));
                buf.writeDouble(this.sentRange);
            }
            ServerPlayNetworking.send((ServerPlayerEntity)(Object)this, SEND_SENT, buf);
        }
    }

    @Override
    public void setEnchantedSentinelNoSync(Vec3d pos, Double ambit) {
        this.enchSentPos = pos;
        this.sentRange = ambit;
    }
    @Override
    public boolean shouldBreakSent() {
        return this.getEnchantedSentinel() == null ?
            false :
            this.getPos().distanceTo(this.getEnchantedSentinel()) > 32.0;
    }
    @Override
    public void breakSent() {
        this.setEnchantedSentinel(null, null);
    }

    @Inject(at = @At("HEAD"), method = "tick")
    public void tick(CallbackInfo ci) {
        if (this.shouldBreakSent()) { this.breakSent(); }
    }
}
