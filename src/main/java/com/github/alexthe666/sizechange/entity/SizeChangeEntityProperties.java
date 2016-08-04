package com.github.alexthe666.sizechange.entity;

import net.ilexiconn.llibrary.server.entity.EntityProperties;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;

public class SizeChangeEntityProperties extends EntityProperties {

	public float scale;
	public float target_scale;
	public double base_speed;

	@Override
	public void saveNBTData(NBTTagCompound compound) {
		compound.setFloat("Scale", scale);
		compound.setFloat("TargetScale", target_scale);
		compound.setDouble("DefaultSpeed", base_speed);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		this.scale = compound.getFloat("Scale");
		this.target_scale = compound.getFloat("TargetScale");
		this.base_speed = compound.getDouble("DefaultSpeed");
	}

	@Override
	public void init() {
		scale = 1;
		target_scale = 1;
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
