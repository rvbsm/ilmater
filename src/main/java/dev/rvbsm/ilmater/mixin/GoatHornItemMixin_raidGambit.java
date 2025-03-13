package dev.rvbsm.ilmater.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.GoatHornItem;
import net.minecraft.item.Instrument;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.village.raid.Raid;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.function.Predicate;

import dev.rvbsm.ilmater.IlmaterSettings;

@Mixin(GoatHornItem.class)
public abstract class GoatHornItemMixin_raidGambit {

    @Unique
    private static final Predicate<MobEntity> RAIDER_PREDICATE = (entity) -> entity.isAlive() &&
        entity.getType().isIn(EntityTypeTags.RAIDERS);

    @Inject(method = "use", at = @At(value = "RETURN", ordinal = 0))
    private void comeAtMe(
        CallbackInfoReturnable<ActionResult> cir,
        @Local(argsOnly = true) World world,
        @Local(argsOnly = true) PlayerEntity user,
        @Local Instrument instrument
    ) {
        if (IlmaterSettings.raidGambit && world instanceof ServerWorld serverWorld) {
            final Collection<? extends MobEntity> raiders;

            final Raid raid = serverWorld.getRaidAt(user.getBlockPos());
            if (raid != null && raid.isActive()) {
                raiders = raid.getAllRaiders();
            } else {
                final Box hornRange = new Box(user.getBlockPos()).expand(Math.min(instrument.range(), 64));
                raiders = serverWorld.getEntitiesByClass(MobEntity.class, hornRange, RAIDER_PREDICATE);
            }

            for (final MobEntity raider : raiders) {
                raider.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 60));
                raider.setTarget(user);
            }
        }
    }
}
