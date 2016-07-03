package com.github.alexthe666.sizechange.asm;

import java.lang.reflect.InvocationTargetException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class SizeChangeHooks {

	public static void gluPerspective() {
		float farPlaneDistance = (float) (Minecraft.getMinecraft().gameSettings.renderDistanceChunks * 16);
		// Project.gluPerspective(getFOVModifier(LLibrary.PROXY.getPartialTicks(),
		// true), (float)Minecraft.getMinecraft().displayWidth /
		// (float)Minecraft.getMinecraft().displayHeight, 0.05F,
		// farPlaneDistance * MathHelper.SQRT_2);
	}

	private static float getFOVModifier(float partialTicks, boolean b) {
		float f = 0;
		try {
			f = (Float) ReflectionHelper.findMethod(EntityRenderer.class, Minecraft.getMinecraft().entityRenderer, new String[] { "getFOVModifier", "func_175156_o" }, float.class, boolean.class).invoke(Minecraft.getMinecraft().entityRenderer, partialTicks, b);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		System.out.println(f);
		return f;

	}
}
