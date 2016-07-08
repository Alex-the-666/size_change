package com.github.alexthe666.sizechange.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.player.EntityPlayer;

import com.github.alexthe666.sizechange.SizeChangeUtils;

public class EntityAIHuntSmallCreatures extends EntityAINearestAttackableTarget {

	public EntityAIHuntSmallCreatures(EntityCreature creature) {
		super(creature, EntityLivingBase.class, false);
	}

	@Override
	public boolean shouldExecute() {
		if(super.shouldExecute()){
			if(this.targetEntity != null){
				return this.targetEntity.width < this.taskOwner.width && SizeChangeUtils.getScale(this.targetEntity) < 1 && (this.targetEntity instanceof EntityPlayer || this.targetEntity instanceof EntityCreature);
			}
		}
		return false;
	}

}
