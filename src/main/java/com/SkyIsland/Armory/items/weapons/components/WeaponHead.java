package com.SkyIsland.Armory.items.weapons.components;

import java.util.List;

import net.minecraft.item.ItemStack;

/**
 * Blade/Head of a weapon. First part that goes into a rig to construct weapons.
 * Heads contain the logic for what is needed to form a complete weapon.
 * @author Skyler
 *
 */
public abstract class WeaponHead extends WeaponComponent {

	protected WeaponHead(String modelSuffix) {
		super(modelSuffix);
	}
	
	/**
	 * Can the passed weaponHead (in assembly) accept the given
	 * item addition. An example would be a sword blade being given a 
	 * generic crossbar. If the blade doesn't already have a crossbar (and
	 * is on the step that requires a crossbar be added), returns true.
	 * Otherwise, returns false. A mace being offered a crossbar
	 * would always say no, as crossbars don't go on maces.
	 * @param weaponHead the itemstack head itself
	 * @param curentStack the current list of items on the head
	 * @param addition the item the player is trying to add
	 * @return whether the addition can be added
	 */
	public abstract boolean accepts(ItemStack weaponHead, 
			List<ItemStack> currentStack, ItemStack addition);
	
	/**
	 * Check and return whether the given stack of items is correct and
	 * makes a complete weapon.
	 * @param weaponHead
	 * @param currentStack
	 * @return
	 */
	public abstract boolean isComplete(ItemStack weaponHead,
			List<ItemStack> currentStack);
	
	/**
	 * Finalize a stack of weapon pieces with the head, creating a weapon.
	 * @param weaponHead The head of the weapon.
	 * @param currentStack The pieces stacked on top of the head. In other
	 * words, currentStack U weaponHead = all pieces in the weapon
	 * @return null if an invalid weapon is being created, or a complete
	 * itemstack that's been constructed appropriately and can now be
	 * used by the player.
	 */
	public abstract ItemStack formWeapon(ItemStack weaponHead,
			List<ItemStack> currentStack);
}
