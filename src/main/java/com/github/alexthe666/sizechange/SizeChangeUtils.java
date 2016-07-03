package com.github.alexthe666.sizechange;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;

import com.google.common.collect.Maps;

public class SizeChangeUtils {
	private static Map<Entity, Float> scales = Maps.newHashMap();

	public static void setSize(Entity entity, float x, float y) {
		entity.setEntityBoundingBox(new AxisAlignedBB(entity.posX - x/2, entity.posY, entity.posZ - x/2, entity.posX + x/2, entity.posY +y, entity.posZ + x/2));
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
