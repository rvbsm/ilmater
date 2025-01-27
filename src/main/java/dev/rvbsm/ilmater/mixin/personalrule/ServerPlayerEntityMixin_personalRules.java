package dev.rvbsm.ilmater.mixin.personalrule;

import com.mojang.serialization.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

import dev.rvbsm.ilmater.api.PersonalRulesAccess;
import dev.rvbsm.ilmater.player.PersonalRules;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin_personalRules extends PlayerEntityMixin_personalRules {

    @Unique
    private static final String PERSONAL_RULES_KEY = "ilmater:PersonalRules";

    protected ServerPlayerEntityMixin_personalRules(EntityType<? extends PlayerEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void readPersonalRules(NbtCompound nbt, CallbackInfo ci) {
        final Dynamic<NbtElement> nbtDynamic = new Dynamic<>(NbtOps.INSTANCE, nbt.get(PERSONAL_RULES_KEY));
        this.personalRules = new PersonalRules(
            (ServerPlayerEntity) (Object) this,
            this.getWorld().getEnabledFeatures(),
            nbtDynamic);
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void writePersonalRules(NbtCompound nbt, CallbackInfo ci) {
        nbt.put(PERSONAL_RULES_KEY, this.personalRules.toNbt());
    }

    @Inject(method = "copyFrom", at = @At("HEAD"))
    private void copyPersonalRules(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {
        this.personalRules = ((PersonalRulesAccess) oldPlayer).ilmater$getPersonalRules();
    }
}
