package com.SkyIsland.Armory.mechanics;

import java.util.Map;

import com.SkyIsland.Armory.api.ArmorManager;
import com.SkyIsland.Armory.items.armor.Armor;

import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public final class ArmorUtils {

	/**
	 * Returns a map with all protection values from the given piece of
	 * armor. This handles both mod armor from this mod as well as undefined ones.
	 * @param armor
	 * @return A map with protection values, or null if the passed item is not
	 * a piece of armor (ItemArmor)
	 */
	public static Map<DamageType, Float> getValues(ItemStack armor) {
		if (armor == null || !(armor.getItem() instanceof ItemArmor)) 
			return null;
		
		//check to see if it's some of our custom armors
		if (armor.getItem() instanceof Armor)
			return getCustomValues(armor);
		else if (ArmorManager.instance().hasArmorRegistered((ItemArmor) armor.getItem()) )
			return getDefinedValues(armor);
		else
			return getVanillaValues(armor);
	}
	
	/**
	 * Calculates armor values for a piece of armor based on the values
	 * registered in the Armor Manager
	 * @param armor
	 * @return
	 * @see {@code ArmorManager}
	 */
	private static Map<DamageType, Float> getDefinedValues(ItemStack armor) {
		
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
	private static Map<DamageType, Float> getVanillaValues(ItemStack armor) {
		ItemArmor item = (ItemArmor) armor.getItem();
		float rawPoints = (float) item.damageReduceAmount;
	}
	
	/**
	 * Calculates armor values for a piece of our custom armor. This method, like
	 * the above, assumes the proper check has been made
	 * @param armor
	 * @return
	 */
	private static Map<DamageType, Float> getCustomValues(ItemStack armor) {
		
	}
}
