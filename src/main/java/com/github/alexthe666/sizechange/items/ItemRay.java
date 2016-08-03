package com.github.alexthe666.sizechange.items;

import com.github.alexthe666.sizechange.SizeChange;

import com.github.alexthe666.sizechange.entity.EntityGrowCharge;
import com.github.alexthe666.sizechange.entity.EntityShrinkCharge;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemRay extends Item {

	private boolean shrink;

	public ItemRay(boolean shrink) {
		super();
		this.shrink = shrink;
		this.setUnlocalizedName(shrink ? "sizechange.shrink_ray" : "sizechange.growth_ray");
		this.setMaxStackSize(1);
		this.setMaxDamage(200);
		this.setCreativeTab(SizeChange.tab);
		GameRegistry.register(this, new ResourceLocation((shrink ? "sizechange:shrink_ray" : "sizechange:growth_ray")));
	}

	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
		if (!playerIn.capabilities.isCreativeMode) {
			itemStackIn.damageItem(1, playerIn);
		}
		worldIn.playSound((EntityPlayer) null, playerIn.posX, playerIn.posY, playerIn.posZ, SizeChange.ray, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

		if (!worldIn.isRemote) {
			EntityThrowable charge = null;
			if (shrink) {
				charge = new EntityShrinkCharge(worldIn, playerIn);
			} else {
				charge = new EntityGrowCharge(worldIn, playerIn);
			}
			charge.setHeadingFromThrower(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 1.0F);
			worldIn.spawnEntityInWorld(charge);
		}

		playerIn.addStat(StatList.getObjectUseStats(this));
		return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
	}
}
