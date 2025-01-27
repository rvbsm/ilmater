package dev.rvbsm.ilmater.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.VaultBlock;
import net.minecraft.block.entity.VaultBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import dev.rvbsm.ilmater.IlmaterSettings;
import dev.rvbsm.ilmater.IlmaterTranslation;
import dev.rvbsm.ilmater.api.RewardsCooldownAccess;
import dev.rvbsm.ilmater.time.TickDuration;

@Mixin(VaultBlock.class)
public abstract class VaultBlockMixin_vaultCooldown {

    @Inject(method = "onUseWithItem", at = @At(value = "RETURN", ordinal = 0))
    private void notifyCooldown(
        CallbackInfoReturnable<ActionResult> cir,
        @Local(argsOnly = true) World world,
        @Local(argsOnly = true) BlockPos pos,
        @Local(argsOnly = true) PlayerEntity player
    ) {
        if (IlmaterSettings.vaultCooldownTicks >= 0 && !world.isClient) {
            if (world.getBlockEntity(pos) instanceof VaultBlockEntity vault) {
                final long ticks = ((RewardsCooldownAccess) vault.getServerData()).ilmater$remainingCooldown(player);

                if (ticks > 0) {
                    player.sendMessage(
                        IlmaterTranslation.translatable("block", "vault.cooldown")
                            .append(TickDuration.fromTicks(ticks)), true);
                }
            }
        }
    }
}
