package com.github.alexthe666.sizechange;

import com.github.alexthe666.sizechange.entity.EntityGrowCharge;
import com.github.alexthe666.sizechange.entity.EntityShrinkCharge;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;

import com.github.alexthe666.sizechange.event.ClientEvents;
import com.github.alexthe666.sizechange.items.ModItems;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {

	public void render() {
		MinecraftForge.EVENT_BUS.register(new ClientEvents());
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		renderItem.getItemModelMesher().register(ModItems.shrink_core, 0, new ModelResourceLocation("sizechange:shrink_core", "inventory"));
		renderItem.getItemModelMesher().register(ModItems.growth_core, 0, new ModelResourceLocation("sizechange:growth_core", "inventory"));
		renderItem.getItemModelMesher().register(ModItems.shrink_ray, 0, new ModelResourceLocation("sizechange:shrink_ray", "inventory"));
		renderItem.getItemModelMesher().register(ModItems.growth_ray, 0, new ModelResourceLocation("sizechange:growth_ray", "inventory"));
		RenderingRegistry.registerEntityRenderingHandler(EntityShrinkCharge.class, new RenderSnowball<EntityShrinkCharge>(Minecraft.getMinecraft().getRenderManager(), ModItems.shrink_core, renderItem));
		RenderingRegistry.registerEntityRenderingHandler(EntityGrowCharge.class, new RenderSnowball<EntityGrowCharge>(Minecraft.getMinecraft().getRenderManager(), ModItems.growth_core, renderItem));
	}
}
