package dev.rvbsm.ilmater.api;

import org.jetbrains.annotations.NotNull;

import net.minecraft.entity.player.PlayerEntity;

import java.util.Map;
import java.util.UUID;
import java.util.stream.LongStream;

public interface RewardsCooldownAccess {

    @NotNull Map<UUID, Long> ilmater$getCooldownsMap();

    @NotNull LongStream ilmater$getCooldowns();

    void ilmater$putCooldowns(@NotNull LongStream stream);

    void ilmater$updateCooldowns(long time);

    long ilmater$remainingCooldown(@NotNull PlayerEntity player);
}
