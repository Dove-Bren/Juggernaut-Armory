package com.SkyIsland.Armory.api;

import java.util.List;

import net.minecraft.item.ItemStack;

/**
 * Template item class which can take in the pieces of a weapon and
 * construct a final weapon
 * @author Skyler
 *
 */
public interface IWeaponTemplate {

	/**
	 * Takes a list of final components and constructs a final weapon. 
	 * @param components
	 * @return
	 */
	public ItemStack construct(List<ItemStack> components);
	
}
