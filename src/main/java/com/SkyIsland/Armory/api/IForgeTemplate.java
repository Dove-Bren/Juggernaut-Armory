package com.SkyIsland.Armory.api;

import com.SkyIsland.Armory.api.ForgeManager.MetalRecord;

import net.minecraft.item.ItemStack;

/**
 * Template item class which can take in a user's forging performance and produce
 * a custom forge piece from it.
 * @author Skyler
 *
 */
public interface IForgeTemplate {

	/**
	 * Creates an item part based on user performance.
	 * @param metalRecord The metal record of the material that was made into the item
	 * @param performance performance rating, from 0.0f to 1.0f. Any numbers outside
	 * of this range will not result in a call to this method.
	 * @return An itemstack of the item this template produces
	 */
	public ItemStack produce(MetalRecord metalRecord, float performance);
	
}
