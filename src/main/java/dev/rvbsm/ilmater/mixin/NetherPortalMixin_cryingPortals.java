package dev.rvbsm.ilmater.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.dimension.NetherPortal;

import dev.rvbsm.ilmater.IlmaterSettings;

@Mixin(NetherPortal.class)
public abstract class NetherPortalMixin_cryingPortals {

    @ModifyReturnValue(method = "method_30487", at = @At("RETURN"), remap = false)
    private static boolean isCrying(boolean original, @Local(argsOnly = true) BlockState state) {
        return original || (IlmaterSettings.cryingPortals && state.isOf(Blocks.CRYING_OBSIDIAN));
    }
}
