package com.SkyIsland.Armory.mechanics;

import java.util.Map;

import net.minecraft.item.ItemArmor;

public final class ArmorUtils {

	public static Map<DamageType, Float> getValues(ItemArmor armor) {
		
		//check to see if it's some of our custom armors
		
	}
	
	/**
	 * calculates armor values for a piece of armor that does not have a mapping
	 * in {@code ArmorManager} and isn't {@code Armor}
	 * <p>
	 * Note that this method doesn't perform a check as to whether a defined
	 * mapping exists; it assumes it isn't. It's private, after all. be smart.
	 * </p>
	 * @param armor
	 * @return
	 */
	private static Map<DamageType, Float> getVanillaValues(ItemArmor armor) {
		
	}
	
}
