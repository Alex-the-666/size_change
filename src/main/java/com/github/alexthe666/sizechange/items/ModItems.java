package com.github.alexthe666.sizechange.items;

import net.minecraft.item.Item;

public class ModItems {
	public static Item shrink_core;
	public static Item growth_core;
	public static Item shrink_ray;
	public static Item growth_ray;
	
	public static void init(){
		shrink_core = new ItemCore(true);
		growth_core = new ItemCore(false);
		shrink_ray = new ItemRay(true);
		growth_ray = new ItemRay(false);
	}
}
