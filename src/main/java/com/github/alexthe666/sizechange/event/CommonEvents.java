package com.github.alexthe666.sizechange.event;

import java.lang.reflect.Field;
import java.util.Map;

import com.github.alexthe666.sizechange.entity.SizeChangeEntityProperties;
import net.ilexiconn.llibrary.server.entity.EntityProperties;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
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
		SizeChangeEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties((event.getEntity()), SizeChangeEntityProperties.class);
		if(properties != null) {
			if (properties.scale == 0) {
				properties.scale = 1;
			}
			if (properties.target_scale == 0) {
				properties.target_scale = 1;
			}
			if (properties.scale < properties.target_scale) {
				float max = properties.target_scale - properties.scale;
				float sub = max / 40;
				properties.scale += sub;
				if (properties.scale >= properties.target_scale) {
					properties.scale = properties.target_scale;
				}
			}
			if (properties.scale > properties.target_scale) {
				float max = properties.scale - properties.target_scale;
				float sub = max / 40;
				properties.scale -= sub;
				if (properties.scale <= properties.target_scale) {
					properties.scale = properties.target_scale;
				}
			}
			double round = (double)Math.round(properties.scale * 1000) / 1000;
			if (round == properties.target_scale){
				properties.scale = properties.target_scale;
			}
			SizeChangeUtils.setScale(event.getEntityLiving(), properties.scale);
			float scale = SizeChangeUtils.getScale(event.getEntity());
				if (properties.base_speed > 0 && !event.getEntityLiving().worldObj.isRemote) {
					float scale_0 = scale == 1 ? 1 : scale > 1 ? scale / 3 : scale * 3;
					event.getEntityLiving().getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(scale_0 * properties.base_speed);
				}
			event.getEntityLiving().stepHeight = scale < 0.5F ? scale : (float) (0.5D * scale);
			if (!(event.getEntity() instanceof EntityPlayer)) {
				if (sizeCache.containsKey(event.getEntity())) {
					Vector2f size = sizeCache.get(event.getEntity());
					SizeChangeUtils.setSize(event.getEntity(), size.x * properties.scale, size.y * properties.scale);
				} else {
					sizeCache.put(event.getEntity(), new Vector2f(event.getEntity().width, event.getEntity().height));
					SizeChangeUtils.setSize(event.getEntity(), event.getEntity().width * scale, event.getEntity().height * scale);
				}
			}
		}
		// event.getEntityLiving().setAIMoveSpeed(scale);

	}

	@SubscribeEvent
	public void onEntityJump(LivingJumpEvent event) {
		float scale = SizeChangeUtils.getScale(event.getEntity());
		if (scale < 0.5F && (event.getEntityLiving().isSneaking() || event.getEntityLiving().isSprinting())) {
			event.getEntityLiving().motionY = 0.4;
		}else if(scale < 0.5F){
			event.getEntityLiving().motionY = 0.2;
		}else if(scale > 1F){
			event.getEntityLiving().motionY *= scale;
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
		if (event.getEntity() instanceof EntityLivingBase) {
			SizeChangeEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties((event.getEntity()), SizeChangeEntityProperties.class);
			properties.base_speed = ((EntityLivingBase)event.getEntity()).getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue();
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
		if(!event.player.getEntityWorld().isRemote){
			((EntityPlayerMP)event.player).interactionManager.setBlockReachDistance(Math.max(scale * 5F, 3F));
		}
	}

	@SubscribeEvent
	public void onEntityHurt(LivingHurtEvent event) {
		SizeChangeEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties((event.getEntity()), SizeChangeEntityProperties.class);
		float scale = properties.scale;
		event.setAmount(event.getAmount() * 1/scale);
		if(event.getSource().getEntity() != null && event.getSource().getEntity() instanceof EntityLivingBase){
			SizeChangeEntityProperties properties1 = EntityPropertiesHandler.INSTANCE.getProperties((event.getSource().getEntity()), SizeChangeEntityProperties.class);
			float scale1 = properties1.scale;
			event.setAmount(event.getAmount() * scale1);
		}
	}
}
