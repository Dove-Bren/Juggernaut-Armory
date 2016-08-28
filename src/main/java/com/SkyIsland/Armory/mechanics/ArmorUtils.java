package com.SkyIsland.Armory.mechanics;

import java.util.EnumMap;
import java.util.Map;

import com.SkyIsland.Armory.api.ArmorManager;
import com.SkyIsland.Armory.config.ModConfig;
import com.SkyIsland.Armory.items.armor.Armor;
import com.SkyIsland.Armory.items.armor.Armor.ArmorPiece;

import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public final class ArmorUtils {

	/**
	 * Returns a map with all protection values from the given piece of
	 * armor. This handles both mod armor from this mod as well as undefined ones.
	 * @param armor
	 * @return A map with protection values, or null if the passed item is null
	 */
	public static Map<DamageType, Float> getValues(ItemStack armor) {
		if (armor == null)// || !(armor.getItem() instanceof ItemArmor)) 
			return null;
		
		if (armor.getItem() instanceof ArmorPiece) {
			return getArmorPieceValues(armor);
		}
		
		//check to see if it's some of our custom armors
		if (armor.getItem() instanceof Armor)
			return getCustomValues(armor);
		else if (ArmorManager.instance() != null && ArmorManager.instance().hasArmorRegistered((ItemArmor) armor.getItem()) )
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
		ItemArmor base = (ItemArmor) armor.getItem();
		
		Map<DamageType, Float> map = new EnumMap<DamageType, Float>(DamageType.class);
		
		for (DamageType type : DamageType.values())
			map.put(type, ArmorManager.instance().getProtection(base, type));
		
		return map;
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
		float points = (float) item.damageReduceAmount;
		
		points *= ModConfig.config.getDefaultRatio();//ArmorModificationManager.instance().defaultSplitRate;
		
		Map<DamageType, Float> map = new EnumMap<DamageType, Float>(DamageType.class);
		
		for (DamageType type : DamageType.values())
		if (type.isByDefault())
			map.put(type, points);
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
	
	private static Map<DamageType, Float> getArmorPieceValues(ItemStack armor) {
		ArmorPiece base = (ArmorPiece) armor.getItem();
		
		Map<DamageType, Float> map = DamageType.freshMap();
		for (DamageType type : DamageType.values())
			map.put(type, base.getProtection(armor, type));
		
		return map;
	}
}
