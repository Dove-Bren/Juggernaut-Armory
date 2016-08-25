package com.SkyIsland.Armory.items;

import net.minecraft.item.Item;

/**
 * General, abstract clss for a rig. Rigs require metal attachments to form
 * complete weapons.
 * @author Skyler
 *
 */
public abstract class HandleRig extends Item {

	public abstract boolean canAccept(Blade blade);
	
}
