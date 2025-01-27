package dev.rvbsm.ilmater.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.entity.VaultBlockEntity;
import net.minecraft.block.vault.VaultServerData;
import net.minecraft.server.world.ServerWorld;

import dev.rvbsm.ilmater.IlmaterSettings;
import dev.rvbsm.ilmater.api.RewardsCooldownAccess;

@Mixin(VaultBlockEntity.class)
public abstract class VaultBlockEntityMixin_vaultCooldown {

    @Debug(export = true)
    @Mixin(VaultBlockEntity.Server.class)
    public static abstract class ServerMixin {

        @Inject(method = "tick", at = @At("HEAD"))
        private static void tickCooldowns(
            CallbackInfo ci,
            @Local(argsOnly = true) ServerWorld world,
            @Local(argsOnly = true) VaultServerData serverData
        ) {
            if (IlmaterSettings.vaultCooldownTicks >= 0) {
                ((RewardsCooldownAccess) serverData).ilmater$updateCooldowns(world.getTime());
            }
        }
    }
}
