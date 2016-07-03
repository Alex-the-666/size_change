package com.github.alexthe666.sizechange;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;

import com.google.common.collect.Maps;

public class SizeChangeUtils {
	private static Method setSize;
	private static Map<Entity, Float> scales = Maps.newHashMap();

	static {
		for (Method method : Entity.class.getDeclaredMethods()) {
			for (String name : new String[] { "setSize", "func_177725_a" }) {
				if (method.getName().equals(name)) {
					method.setAccessible(true);
					setSize = method;
					break;
				}
			}
		}
	}

	public static void setSize(Entity entity, float x, float y){
		try {
			setSize.invoke(entity, x, y);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	public static void setScale(Entity entity, float scale) {
		scales.put(entity, scale);
	}

	public static float getScale(Entity entity) {
		return scales.containsKey(entity) ? scales.get(entity) : 1F;
	}
}
