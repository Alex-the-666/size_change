package com.github.alexthe666.sizechange.entity;

import com.github.alexthe666.sizechange.SizeChangeUtils;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityGrowCharge extends EntityThrowable
{
    public EntityGrowCharge(World worldIn) {
        super(worldIn);
    }

    public EntityGrowCharge(World worldIn, EntityLivingBase throwerIn) {
        super(worldIn, throwerIn);
    }

    public EntityGrowCharge(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    public static void func_189662_a(DataFixer fixer) {
        EntityThrowable.func_189661_a(fixer, "grow_charge");
    }

    protected void onImpact(RayTraceResult result) {
        if (result.entityHit != null && !result.entityHit.equals(this.getThrower())) {
            if (result.entityHit instanceof EntityLivingBase) {
                float initialScale = SizeChangeUtils.getScale(result.entityHit);
                float newScale = Math.min(8, initialScale + initialScale);
                SizeChangeEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties((result.entityHit), SizeChangeEntityProperties.class);
                properties.target_scale = newScale;
                properties.scale = initialScale;
            }
        }
        for (int j = 0; j < 20; ++j) {
            float f2 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
            float f3 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
            float f4 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
            this.worldObj.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX + f2, this.posY + f4, this.posZ + f3, 1, 0, 0, new int[0]);
        }
        if (!this.worldObj.isRemote) {
            this.setDead();
        }
    }
}