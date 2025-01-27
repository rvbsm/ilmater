package dev.rvbsm.ilmater.mixin.personalrule;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

import carpet.utils.CommandHelper;

import dev.rvbsm.ilmater.IlmaterSettings;
import dev.rvbsm.ilmater.api.PersonalRulesAccess;
import dev.rvbsm.ilmater.player.PersonalRules;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin_personalRules extends LivingEntity implements PersonalRulesAccess {

    @Unique
    protected PersonalRules personalRules = PersonalRules.EMPTY;

    protected PlayerEntityMixin_personalRules(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public @NotNull PersonalRules ilmater$getPersonalRules() {
        if ((PlayerEntity) (Object) this instanceof ServerPlayerEntity player) {
            if (CommandHelper.canUseCommand(player.getCommandSource(), IlmaterSettings.commandPersonalRule)) {
                return this.personalRules;
            }
        }

        return PersonalRules.EMPTY;
    }
}
