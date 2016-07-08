package com.github.alexthe666.sizechange;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;

import com.github.alexthe666.sizechange.event.ClientEvents;
import com.github.alexthe666.sizechange.items.ModItems;

public class ClientProxy extends CommonProxy {

	public void render() {
		MinecraftForge.EVENT_BUS.register(new ClientEvents());
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		//ModelBakery.registerItemVariants(ModItems.shrink_ray, new ResourceLocation("sizechange:shrink_ray"));
		renderItem.getItemModelMesher().register(ModItems.shrink_ray, 0, new ModelResourceLocation("sizechange:shrink_ray", "inventory"));
		//ModelBakery.registerItemVariants(ModItems.growth_ray, new ResourceLocation("sizechange:growth_ray"));
		renderItem.getItemModelMesher().register(ModItems.growth_ray, 0, new ModelResourceLocation("sizechange:growth_ray", "inventory"));

	}
}
