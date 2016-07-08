package com.github.alexthe666.sizechange.event;

import java.lang.reflect.Field;
import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import org.lwjgl.util.vector.Vector2f;

import com.github.alexthe666.sizechange.SizeChangeUtils;
import com.github.alexthe666.sizechange.entity.ai.EntityAIHuntSmallCreatures;
import com.github.alexthe666.sizechange.entity.ai.EntityAINewOcelotFear;
import com.google.common.collect.Maps;

public class CommonEvents {
	private Map<Entity, Vector2f> sizeCache = Maps.newHashMap();

	@SubscribeEvent
	public void onEntityUpdate(LivingUpdateEvent event) {
		float initialScale = SizeChangeUtils.getScale(event.getEntity());
		SizeChangeUtils.setScale(event.getEntityLiving(), event.getEntity() instanceof EntityOcelot ? 1 : 0.125F);
		float scale = SizeChangeUtils.getScale(event.getEntity());
		if (scale != initialScale) {
			if (event.getEntityLiving() instanceof EntityLiving) {
				((EntityLiving) event.getEntityLiving()).getNavigator().setSpeed(scale < 1 ? scale * 3 : scale);
			}
			if (event.getEntityLiving() instanceof EntityPlayer) {
				event.getEntityLiving().getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(scale * 0.10000000149011612D);
				((EntityPlayer) event.getEntityLiving()).capabilities.setFlySpeed((float) (scale * 0.10000000149011612D));
			}

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
		if (scale < 1 && (event.getEntityLiving().isSneaking() || event.getEntityLiving().isSprinting())) {
			event.getEntityLiving().motionY = 0.4;
		} else {
			event.getEntityLiving().motionY = 0.3;
		}
	}

	@SubscribeEvent
	public void onEntitySoundEvent(PlaySoundAtEntityEvent event) {
		// event.setPitch(event.getDefaultPitch() * (0.125F));
	}

	@SubscribeEvent
	public void onEntityConstructEvent(EntityJoinWorldEvent event) {
		if (event.getEntity() instanceof EntityOcelot) {
			Field avoidAiField = ReflectionHelper.findField(EntityOcelot.class, new String[] { "avoidEntity", "field_175545_bm" });
			try {
				((EntityOcelot) event.getEntity()).tasks.removeTask((EntityAIBase) avoidAiField.get(((EntityOcelot) event.getEntity())));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			((EntityOcelot) event.getEntity()).tasks.addTask(4, new EntityAINewOcelotFear(((EntityOcelot) event.getEntity()), EntityPlayer.class, 16.0F, 0.8D, 1.33D));
			((EntityOcelot) event.getEntity()).targetTasks.addTask(2, new EntityAIHuntSmallCreatures(((EntityOcelot) event.getEntity())));
		}
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
