package com.github.alexthe666.sizechange.items;

import com.github.alexthe666.sizechange.SizeChange;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.List;

public class ModItems {

	public static List<Item> ITEMS = new ArrayList<>();

	@GameRegistry.ObjectHolder(SizeChange.MODID + ":shrink_core")
	public static Item shrink_core = new ItemCore(true);
	@GameRegistry.ObjectHolder(SizeChange.MODID + ":growth_core")
	public static Item growth_core = new ItemCore(false);
	@GameRegistry.ObjectHolder(SizeChange.MODID + ":shrink_ray")
	public static Item shrink_ray = new ItemRay(true);
	@GameRegistry.ObjectHolder(SizeChange.MODID + ":growth_ray")
	public static Item growth_ray = new ItemRay(false);
}
