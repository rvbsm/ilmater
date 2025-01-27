package dev.rvbsm.ilmater.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.world.World;

import dev.rvbsm.ilmater.IlmaterSettings;

@Mixin(TridentEntity.class)
public abstract class TridentEntityMixin_loyalVoid extends PersistentProjectileEntity {


    @Shadow
    @Final
    private static TrackedData<Byte> LOYALTY;
    @Shadow
    private boolean dealtDamage;

    protected TridentEntityMixin_loyalVoid(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void tickInVoid() {
        if (IlmaterSettings.loyalVoid && this.dataTracker.get(LOYALTY) > 0 && this.getOwner() != null) {
            this.dealtDamage = true;
        } else {
            super.tickInVoid();
        }
    }
}
