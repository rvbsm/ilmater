package dev.rvbsm.ilmater.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.authlib.GameProfile;
import com.mojang.serialization.Dynamic;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Uuids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import dev.rvbsm.ilmater.IlmaterSettings;
import dev.rvbsm.ilmater.api.DroppedItemsAccess;
import dev.rvbsm.ilmater.api.StaticItemAccess;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin_recoveryItems extends PlayerEntity implements DroppedItemsAccess {

    @Unique
    private static final String RECOVERY_ITEMS_KEY = "ilmater:recovery_items";
    @Shadow
    @Final
    private static Logger LOGGER;
    @Shadow
    public ServerPlayNetworkHandler networkHandler;
    @Unique
    private Set<UUID> droppedItems = new ObjectLinkedOpenHashSet<>();

    public ServerPlayerEntityMixin_recoveryItems(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Shadow
    public abstract int getViewDistance();

    @Shadow
    public abstract ServerWorld getServerWorld();

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void readRecoveryItems(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains(RECOVERY_ITEMS_KEY, NbtElement.LIST_TYPE)) {
            Uuids.LINKED_SET_CODEC.parse(new Dynamic<>(NbtOps.INSTANCE, nbt.get(RECOVERY_ITEMS_KEY)))
                .resultOrPartial(LOGGER::error)
                .ifPresent(recoveryItems -> {
                    this.droppedItems.clear();
                    this.droppedItems.addAll(recoveryItems);
                });
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void writeRecoveryItems(NbtCompound nbt, CallbackInfo ci) {
        Uuids.LINKED_SET_CODEC.encodeStart(NbtOps.INSTANCE, this.droppedItems)
            .resultOrPartial(LOGGER::error)
            .ifPresent(encoded -> nbt.put(RECOVERY_ITEMS_KEY, encoded));
    }

    @Inject(method = "playerTick", at = @At("TAIL"))
    private void compassTick(CallbackInfo ci) {
        if (!IlmaterSettings.recoveryItems || !this.isAlive() || this.age % 20 != 0) {
            return;
        }

        final Optional<BlockPos> lastDeathPos = this.getLastDeathPos()
            .map(GlobalPos::pos)
            .filter(pos -> pos.isWithinDistance(this.getBlockPos(), this.getViewDistance()));

        if (lastDeathPos.isEmpty() || this.droppedItems.isEmpty()) {
            return;
        }


        final boolean isHoldingRecoveryCompass = Stream.of(this.getMainHandStack(), this.getOffHandStack())
            .anyMatch(stack -> stack.isOf(Items.RECOVERY_COMPASS));

        for (final UUID droppedItemUUID : this.droppedItems) {
            if (!(this.getServerWorld().getEntity(droppedItemUUID) instanceof ItemEntity droppedItem)) {
                continue;
            }

            ((StaticItemAccess) droppedItem).ilmater$setStatic(isHoldingRecoveryCompass);

            if (isHoldingRecoveryCompass) {
                final var droppedItemData = new DataTracker.Entry<>(FLAGS, droppedItem.getDataTracker().get(FLAGS));
                droppedItemData.set((byte) (droppedItemData.get() | 1 << GLOWING_FLAG_INDEX));

                this.networkHandler.sendPacket(new EntityTrackerUpdateS2CPacket(
                    droppedItem.getId(),
                    List.of(droppedItemData.toSerialized())));
            } else {
                droppedItem.getDataTracker().set(FLAGS, droppedItem.getDataTracker().get(FLAGS), true);
            }
        }
    }

    @Inject(method = "copyFrom", at = @At("TAIL"))
    private void copyDroppedItems(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {
        this.droppedItems = ((DroppedItemsAccess) oldPlayer).ilmater$getDroppedItems();
    }

    @Inject(method = "dropItem", at = @At("TAIL"))
    private void keepItemIds(CallbackInfoReturnable<ItemEntity> cir, @Local ItemEntity entity) {
        if (!this.isAlive()) {
            this.droppedItems.add(entity.getUuid());
        }
    }

    @Override
    public Set<UUID> ilmater$getDroppedItems() {
        return this.droppedItems;
    }
}
