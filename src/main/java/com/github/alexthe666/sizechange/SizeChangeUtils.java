package com.github.alexthe666.sizechange;

import com.github.alexthe666.sizechange.entity.SizeChangeEntityProperties;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;

public class SizeChangeUtils {

    public static void setSize(Entity entity, float x, float y) {
        if (entity instanceof EntityPlayer) {
            x = MathHelper.clamp(x, 0.25F, 7);
            y = MathHelper.clamp(y, 0.3F, 16);
        }
        entity.setEntityBoundingBox(new AxisAlignedBB(entity.posX - x / 2, entity.posY, entity.posZ - x / 2, entity.posX + x / 2, entity.posY + y, entity.posZ + x / 2));
        entity.width = x;
        entity.height = y;
    }

    public static void setScale(Entity entity, float scale) {
        SizeChangeEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties((entity), SizeChangeEntityProperties.class);
        properties.scale = scale;
    }

    public static float getScale(Entity entity) {
        SizeChangeEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties((entity), SizeChangeEntityProperties.class);
        return properties == null || !(entity instanceof EntityLivingBase) ? 1 : properties.scale;
    }
}
