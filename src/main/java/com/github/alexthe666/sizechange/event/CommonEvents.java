package com.github.alexthe666.sizechange.event;

import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
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
		float initialScale = SizeChangeUtils.getScale(event.getEntity());
		SizeChangeUtils.setScale(event.getEntityLiving(), 0.125F);
		float scale = SizeChangeUtils.getScale(event.getEntity());
		if (scale != initialScale) {
			if (event.getEntityLiving() instanceof EntityLiving) {
				((EntityLiving) event.getEntityLiving()).getNavigator().setSpeed(scale < 1 ? scale * 3 : scale);
			}
			if (event.getEntityLiving() instanceof EntityPlayer) {
			}
			event.getEntityLiving().getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(scale * 0.10000000149011612D);

		}
		event.getEntityLiving().stepHeight = (float) (0.5D * scale);
		if (!(event.getEntity() instanceof EntityPlayer)) {
			if (sizeCache.containsKey(event.getEntity())) {
				Vector2f size = sizeCache.get(event.getEntity());
				SizeChangeUtils.setSize(event.getEntity(), size.x * scale, size.y * scale);
			} else {
				sizeCache.put(event.getEntity(), new Vector2f(event.getEntity().width, event.getEntity().height));
				SizeChangeUtils.setSize(event.getEntity(), event.getEntity().width * scale, event.getEntity().height * scale);
			}
		}
		// event.getEntityLiving().setAIMoveSpeed(scale);

	}

	@SubscribeEvent
	public void onEntityJump(LivingJumpEvent event) {
		float scale = SizeChangeUtils.getScale(event.getEntity());
		if(scale < 1 && (event.getEntityLiving().isSneaking() || event.getEntityLiving().isSprinting())){
			event.getEntityLiving().motionY = 0.4;
		}else{
			event.getEntityLiving().motionY = 0.3;	
		}
	}
	@SubscribeEvent
	public void onEntitySoundEvent(PlaySoundAtEntityEvent event) {
		// event.setPitch(event.getDefaultPitch() * (0.125F));
	}

	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent event) {
		float scale = SizeChangeUtils.getScale(event.player);
		event.player.eyeHeight = (scale - 1) * 1.62f + event.player.getDefaultEyeHeight() * scale - event.player.getDefaultEyeHeight() * (scale - 1);
		if (event.phase == TickEvent.Phase.END) {
			if (sizeCache.containsKey(event.player)) {
				Vector2f size = sizeCache.get(event.player);
				SizeChangeUtils.setSize(event.player, size.x * scale, size.y * scale);
			} else {
				sizeCache.put(event.player, new Vector2f(event.player.width, event.player.height));
				SizeChangeUtils.setSize(event.player, event.player.width * scale, event.player.height * scale);
			}
			if (event.player.isElytraFlying() && event.player.rotationPitch < -45 && scale < 1) {
				event.player.motionY += 0.1F;
			}
		}
	}
}
