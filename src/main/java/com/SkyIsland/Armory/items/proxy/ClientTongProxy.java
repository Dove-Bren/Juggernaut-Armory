package com.SkyIsland.Armory.items.proxy;

import com.SkyIsland.Armory.items.tools.Tongs;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ClientTongProxy extends CommonTongProxy {

	@Override
	public void updateTongs(Tongs inst, ItemStack tongs, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		super.updateTongs(inst, tongs, worldIn, entityIn, itemSlot, isSelected);
	}
	
}
