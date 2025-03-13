package dev.rvbsm.ilmater.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;

import dev.rvbsm.ilmater.api.StaticItemAccess;

import net.minecraft.entity.ItemEntity;

import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Debug(export = true)
@Mixin(ItemEntity.class)
public abstract class ItemStackEntityMixin_recoveryItems implements StaticItemAccess {

    @Unique
    private boolean isStatic;

    @WrapWithCondition(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/ItemEntity;itemAge:I", ordinal = 2))
    private boolean stopAging(ItemEntity instance, int value) {
        return !isStatic;
    }

    @Override
    public void ilmater$setStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }
}
