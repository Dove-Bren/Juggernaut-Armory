package com.SkyIsland.Armory.items.weapons.components;

/**
 * General, abstract clss for a rig. Rigs require metal attachments to form
 * complete weapons.
 * @author Skyler
 *
 */
public abstract class HandleRig extends WeaponComponent {

	public abstract boolean canAccept(Blade blade);
	
}
