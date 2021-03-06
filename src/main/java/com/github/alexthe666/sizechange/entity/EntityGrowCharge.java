package com.github.alexthe666.sizechange.entity;

import com.github.alexthe666.sizechange.SizeChangeUtils;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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

    protected void onImpact(RayTraceResult result) {
        if (result.entityHit != null && !result.entityHit.equals(this.getThrower())) {
            if (result.entityHit instanceof EntityLivingBase) {
                float initialScale = getNearestSize(SizeChangeUtils.getScale(result.entityHit), new float[]{0.125F, 0.25F, 0.5F, 1F, 2F, 4F, 8F});
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
            this.world.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX + f2, this.posY + f4, this.posZ + f3, 1, 0, 0, new int[0]);
        }
        if (!this.world.isRemote) {
            this.setDead();
        }
    }

    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender(float partialTicks) {
        return 15728880;
    }

    public float getBrightness(float partialTicks) {
        return 1.0F;
    }

    public float getNearestSize(float i, float[] sizes) {
        float distance = Math.abs(sizes[0] - i);
        int idx = 0;
        for(int c = 1; c < sizes.length; c++){
            float cdistance = Math.abs(sizes[c] - i);
            if(cdistance < distance){
                idx = c;
                distance = cdistance;
            }
        }
        return sizes[idx];
    }
}