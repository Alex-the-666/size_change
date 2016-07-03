package com.github.alexthe666.sizechange.event;

import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

import org.lwjgl.util.vector.Vector2f;

import com.github.alexthe666.sizechange.SizeChangeUtils;
import com.google.common.collect.Maps;

public class CommonEvents {
	private Map<Entity, Vector2f> sizeCache = Maps.newHashMap();

	@SubscribeEvent
	public void onEntityUpdate(LivingUpdateEvent event) {
		SizeChangeUtils.setScale(event.getEntityLiving(), 0.5F);
		if (!(event.getEntity() instanceof EntityPlayer)) {
			float scale = SizeChangeUtils.getScale(event.getEntity());
			if (sizeCache.containsKey(event.getEntity())) {
				Vector2f size = sizeCache.get(event.getEntity());
				SizeChangeUtils.setSize(event.getEntity(), size.x * scale, size.y * scale);
			} else {
				sizeCache.put(event.getEntity(), new Vector2f(event.getEntity().width, event.getEntity().height));
				SizeChangeUtils.setSize(event.getEntity(), event.getEntity().width * scale, event.getEntity().height * scale);

			}
		}
	}

	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent event) {
		float scale = SizeChangeUtils.getScale(event.player);
		event.player.eyeHeight = (scale - 1) * 1.62f + event.player.getDefaultEyeHeight() * scale - event.player.getDefaultEyeHeight() * (scale - 1);
		if(event.phase == TickEvent.Phase.END){
		if (sizeCache.containsKey(event.player)) {
			Vector2f size = sizeCache.get(event.player);
			SizeChangeUtils.setSize(event.player, size.x * scale, size.y * scale);
		} else {
			sizeCache.put(event.player, new Vector2f(event.player.width, event.player.height));
			SizeChangeUtils.setSize(event.player, event.player.width * scale, event.player.height * scale);
		}
		}
	}
}
