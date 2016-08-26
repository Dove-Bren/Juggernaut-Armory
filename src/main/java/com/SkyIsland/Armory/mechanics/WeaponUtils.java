package com.SkyIsland.Armory.mechanics;

import java.util.EnumMap;
import java.util.Map;

import com.SkyIsland.Armory.api.WeaponManager;
import com.SkyIsland.Armory.items.armor.Armor;
import com.SkyIsland.Armory.items.weapons.Weapon;

import net.minecraft.item.ItemStack;

/**
 * Provides easy ways to break down the statistics of a weapon
 * @author Skyler
 *
 */
public final class WeaponUtils {

	/**
	 * Returns a map with all damage values from the given weapon.
	 * Weapons from this mod, vanilla weapons, and unknown weapons are supported.
	 * @param weapon
	 * @return A map with damage values for each damage type
	 */
	public static Map<DamageType, Float> getValues(ItemStack weapon) {
		
		//check to see if it's some of our weapons
		if (weapon.getItem() instanceof Weapon)
			return getCustomValues(weapon);
		else if (WeaponManager.instance() != null && WeaponManager.instance().hasWeaponRegistered(weapon.getItem()))
			return getDefinedValues(weapon);
		else
			return getVanillaValues(weapon);
	}
	
	/**
	 * Calculates damage values for a weapon based on the values
	 * registered in the Weapon Manager
	 * @param weapon
	 * @return
	 * @see {@code WeaponManager}
	 */
	private static Map<DamageType, Float> getDefinedValues(ItemStack weapon) {
		Map<DamageType, Float> map = new EnumMap<DamageType, Float>(DamageType.class);
		
		for (DamageType type : DamageType.values())
			map.put(type, WeaponManager.instance().getDamage(weapon.getItem(), type));
		
		return map;
	}

	/**
	 * calculates damage values for a weapon that does not have a mapping
	 * in {@code WeaponManager} and isn't a {@code Weapon}
	 * <p>
	 * Note that this method doesn't perform a check as to whether a defined
	 * mapping exists; it assumes it isn't. It's private, after all. be smart.
	 * </p>
	 * @param weapon
	 * @return
	 */
	private static Map<DamageType, Float> getVanillaValues(ItemStack weapon) {
//		ItemArmor item = (ItemArmor) armor.getItem();
//		float points = (float) item.damageReduceAmount;
//		
//		points *= ArmorModificationManager.instance().defaultSplitRate;
		
		Map<DamageType, Float> map = new EnumMap<DamageType, Float>(DamageType.class);
		
		for (DamageType type : DamageType.values())
		if (type.isByDefault())
			map.put(type, 1.0f); //TODO 1? should it be split?
		else
			map.put(type, 0.0f);
		
		return map;
	}
	
	/**
	 * Calculates armor values for a piece of our custom armor. This method, like
	 * the above, assumes the proper check has been made
	 * @param armor
	 * @return
	 */
	private static Map<DamageType, Float> getCustomValues(ItemStack armor) {
		Armor base = (Armor) armor.getItem();
		
		return base.getProtectionMap(armor);
	}
}
