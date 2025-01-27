package dev.rvbsm.ilmater.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2LongLinkedOpenHashMap;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.vault.VaultServerData;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.LongStream;

import dev.rvbsm.ilmater.IlmaterSettings;
import dev.rvbsm.ilmater.api.RewardsCooldownAccess;

@Mixin(VaultServerData.class)
public abstract class VaultServerDataMixin_vaultCooldown implements RewardsCooldownAccess {

    @Shadow
    @Final
    private static int MAX_STORED_REWARDED_PLAYERS;
    @Unique
    private final Map<UUID, Long> rewardedCooldowns = new Object2LongLinkedOpenHashMap<>();
    @Shadow
    @Final
    private Set<UUID> rewardedPlayers;

    @ModifyExpressionValue(
        method = "<clinit>", at = @At(
        value = "INVOKE",
        target = "Lcom/mojang/serialization/codecs/RecordCodecBuilder;create(Ljava/util/function/Function;)Lcom/mojang/serialization/Codec;",
        remap = false))
    private static Codec<VaultServerData> appendCooldowns(Codec<VaultServerData> original) {
        return RecordCodecBuilder.create(instance -> instance.group(
            MapCodec.assumeMapUnsafe(original).forGetter(Function.identity()),
            Codec.LONG_STREAM.lenientOptionalFieldOf("ilmater:rewarded_cooldowns", LongStream.of())
                .forGetter(data -> ((RewardsCooldownAccess) data).ilmater$getCooldowns())).apply(
            instance, (data, cooldowns) -> {
                ((RewardsCooldownAccess) data).ilmater$putCooldowns(cooldowns);
                return data;
            }));
    }

    @Shadow
    private void markDirty() {
        throw new AssertionError("replaced by mixin");
    }

    @Shadow
    abstract long getStateUpdatingResumeTime();

    @Inject(method = "markPlayerAsRewarded", at = @At("TAIL"))
    private void markWithCooldown(CallbackInfo ci, @Local(argsOnly = true) PlayerEntity player) {
        final long cooldownTime = player.getWorld().getTime() + IlmaterSettings.vaultCooldownTicks;
        this.rewardedCooldowns.put(player.getUuid(), cooldownTime);

        if (this.rewardedCooldowns.size() > MAX_STORED_REWARDED_PLAYERS) {
            final Iterator<Map.Entry<UUID, Long>> it = this.rewardedCooldowns.entrySet().iterator();
            if (it.hasNext()) {
                it.next();
                it.remove();
            }
        }
    }

    @Inject(method = "copyFrom", at = @At("TAIL"))
    private void copyCooldown(VaultServerData data, CallbackInfo ci) {
        this.rewardedCooldowns.clear();
        this.rewardedCooldowns.putAll(((RewardsCooldownAccess) data).ilmater$getCooldownsMap());
    }

    @Override
    public @NotNull Map<UUID, Long> ilmater$getCooldownsMap() {
        return this.rewardedCooldowns;
    }

    @Override
    public @NotNull LongStream ilmater$getCooldowns() {
        return this.rewardedCooldowns.values().stream().mapToLong(Long::longValue);
    }

    @Override
    public void ilmater$putCooldowns(@NotNull LongStream stream) {
        final List<Long> cooldownList = stream.boxed().toList();

        final Iterator<UUID> rewardedIterator = this.rewardedPlayers.iterator();
        final Iterator<Long> cooldownIterator = cooldownList.iterator();

        int diff = this.rewardedPlayers.size() - cooldownList.size();
        for (; diff > 0; diff--) {
            rewardedIterator.next();
        }

        for (; diff < 0; diff++) {
            cooldownIterator.next();
        }

        while (rewardedIterator.hasNext()) {
            this.rewardedCooldowns.put(rewardedIterator.next(), Long.valueOf(cooldownIterator.next()));
        }
    }

    @Override
    public void ilmater$updateCooldowns(long currentTime) {
        if (currentTime >= this.getStateUpdatingResumeTime()) {
            this.rewardedCooldowns.entrySet().removeIf(entry -> entry.getValue() <= currentTime);

            if (this.rewardedPlayers.size() != this.rewardedCooldowns.size()) {
                this.rewardedPlayers.clear();
                this.rewardedPlayers.addAll(this.rewardedCooldowns.keySet());

                this.markDirty();
            }
        }
    }

    @Override
    public long ilmater$remainingCooldown(@NotNull PlayerEntity player) {
        return this.rewardedCooldowns.getOrDefault(player.getUuid(), 0L) - player.getWorld().getTime();
    }
}
