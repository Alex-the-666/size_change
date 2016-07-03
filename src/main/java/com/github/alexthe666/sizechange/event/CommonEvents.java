package com.github.alexthe666.sizechange.event;

import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

import org.lwjgl.util.vector.Vector2f;

import com.github.alexthe666.sizechange.SizeChangeUtils;
import com.google.common.collect.Maps;

public class CommonEvents {
	private Map<Entity, Vector2f> sizeCache = Maps.newHashMap();

	@SubscribeEvent
	public void onEntityUpdate(LivingUpdateEvent event) {
		float scale = SizeChangeUtils.getScale(event.getEntity());
		//System.out.println(event.getEntity().worldObj.isRemote);
		SizeChangeUtils.setSize(event.getEntity(), 0.5F, 0.5F);
		/*
		 * if(event.getEntity() instanceof EntityPlayer){
		 * SizeChangeUtils.setScale(event.getEntity(), 0.1F); }
		 * 
		 * if (sizeCache.containsKey(event.getEntity())) { Vector2f size =
		 * sizeCache.get(event.getEntity()); try {
		 * SizeChangeUtils.setSize(event.getEntity(), size.x * scale, size.y *
		 * scale); } catch (ReflectiveOperationException e) {
		 * e.printStackTrace(); } } else { sizeCache.put(event.getEntity(), new
		 * Vector2f(event.getEntity().width, event.getEntity().height)); try {
		 * SizeChangeUtils.setSize(event.getEntity(), event.getEntity().width *
		 * scale, event.getEntity().height * scale); } catch
		 * (ReflectiveOperationException e) { e.printStackTrace(); } }
		 */
	}

	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent event) {
		float scale = SizeChangeUtils.getScale(event.player);
		event.player.eyeHeight = (scale - 1) * 1.62f
				+ event.player.getDefaultEyeHeight() * scale
				- event.player.getDefaultEyeHeight() * (scale - 1);
	}
}
