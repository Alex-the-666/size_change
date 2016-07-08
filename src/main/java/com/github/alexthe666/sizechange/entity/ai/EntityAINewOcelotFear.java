package com.github.alexthe666.sizechange.entity.ai;

import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;

import com.github.alexthe666.sizechange.SizeChangeUtils;

public class EntityAINewOcelotFear extends EntityAIAvoidEntity<EntityPlayer> {

	protected EntityOcelot ocelot;
	
	public EntityAINewOcelotFear(EntityOcelot ocelot, Class<EntityPlayer> classToAvoidIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn) {
		super(ocelot, classToAvoidIn, avoidDistanceIn, farSpeedIn, nearSpeedIn);
		this.ocelot = ocelot;
	}

	@Override
	public boolean shouldExecute() {
		if (super.shouldExecute()) {
			return !this.ocelot.isTamed() && SizeChangeUtils.getScale(this.closestLivingEntity) >= 1;
		}
		return false;
	}

}
