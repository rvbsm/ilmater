package dev.rvbsm.ilmater.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.authlib.GameProfile;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

import dev.rvbsm.ilmater.IlmaterSettings;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin_dimensionDisplay extends PlayerEntity {

    @Unique
    private static final Text DIMENSION_DELIMITER = Text.literal(" in ").formatted(Formatting.GRAY);

    protected ServerPlayerEntityMixin_dimensionDisplay(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @ModifyReturnValue(method = "getPlayerListName", at = @At("RETURN"))
    private Text appendDimension(@Nullable Text original) {
        if (IlmaterSettings.dimensionDisplay) {
            final MutableText displayName = Text.empty();
            if (original != null) {
                displayName.append(original);
            } else {
                displayName.append(this.getDisplayName());
            }

            final DimensionType dimension = this.getWorld().getDimension();
            final Identifier dimensionId = dimension.effects();
            final MutableText dimensionName = Text.literal(dimensionId.toShortTranslationKey());

            if (dimension.natural() && !dimension.hasCeiling()) {
                dimensionName.formatted(Formatting.DARK_GREEN);
            } else if (dimension.natural()) {
                dimensionName.formatted(Formatting.DARK_GRAY);
            } else if (dimension.ultrawarm()) {
                dimensionName.formatted(Formatting.DARK_RED);
            } else {
                dimensionName.formatted(Formatting.DARK_PURPLE);
            }

            displayName.append(DIMENSION_DELIMITER).append(dimensionName);

            return displayName;
        }

        return original;
    }
}
