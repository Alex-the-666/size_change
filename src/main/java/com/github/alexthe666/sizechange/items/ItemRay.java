package com.github.alexthe666.sizechange.items;

import com.github.alexthe666.sizechange.SizeChange;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemRay extends Item {

	private boolean shrink;
	
	public ItemRay(boolean shrink){
		super();
		this.shrink = shrink;
		this.setUnlocalizedName(shrink ? "sizechange.shrink_ray" : "sizechange.growth_ray");
		this.setMaxStackSize(1);
		this.setMaxDamage(200);
		this.setCreativeTab(SizeChange.tab);
		GameRegistry.register(this, new ResourceLocation((shrink ? "sizechange:shrink_ray" : "sizechange:growth_ray")));
	}
}
