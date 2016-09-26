package com.github.alexthe666.sizechange.asm;

import java.lang.reflect.InvocationTargetException;

import com.github.alexthe666.sizechange.SizeChangeUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class SizeChangeHooks {

	public static float getReachDistanceCreative() {
		float scale = SizeChangeUtils.getScale(Minecraft.getMinecraft().thePlayer);
		return Math.max(scale * 5F, 3F);
	}

	public static float getReachDistanceSurvival() {
		float scale = SizeChangeUtils.getScale(Minecraft.getMinecraft().thePlayer);
		return Math.max(scale * 4.5F, 2.5F);
	}

	public static double getFightingReach() {
		float scale = SizeChangeUtils.getScale(Minecraft.getMinecraft().thePlayer);
		return Math.max(scale * 3D, 0);
	}
}
