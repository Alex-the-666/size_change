package com.github.alexthe666.sizechange.entity;

import net.ilexiconn.llibrary.server.entity.EntityProperties;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;

public class SizeChangeEntityProperties extends EntityProperties {

	public float scale;
	public float scale_progress;

	@Override
	public void saveNBTData(NBTTagCompound compound) {
		compound.setFloat("Scale", scale);
		compound.setFloat("ScaleProg", scale_progress);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		this.scale = compound.getFloat("Scale");
		this.scale_progress = compound.getFloat("ScaleProg");
	}

	@Override
	public void init() {
		scale = 1;
	}

	@Override
	public String getID() {
		return "ScaleChange";
	}

	@Override
	public Class getEntityClass() {
		return EntityLivingBase.class;
	}

}
