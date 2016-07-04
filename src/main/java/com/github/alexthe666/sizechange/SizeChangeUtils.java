package com.github.alexthe666.sizechange;

import com.google.common.collect.Maps;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.Map;

public class SizeChangeUtils {
    private static Map<Entity, Float> scales = Maps.newHashMap();

    public static void setSize(Entity entity, float x, float y) {
        if (entity instanceof EntityPlayer) {
            x = Math.max(0.25F, x);
            y = Math.max(0.35F, y);
        }
        entity.setEntityBoundingBox(new AxisAlignedBB(entity.posX - x / 2, entity.posY, entity.posZ - x / 2, entity.posX + x / 2, entity.posY + y, entity.posZ + x / 2));
        entity.width = x;
        entity.height = y;
    }

    public static void setScale(Entity entity, float scale) {
        scales.put(entity, scale);
    }

    public static float getScale(Entity entity) {
        return scales.containsKey(entity) ? scales.get(entity) : 1F;
    }
}
