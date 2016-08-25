package com.SkyIsland.Armory.api;

import java.util.EnumMap;
import java.util.Map;
import java.util.TreeMap;

import com.SkyIsland.Armory.mechanics.DamageType;

import net.minecraft.item.ItemArmor;

/**
 * Allows external mods to register their armor with defined
 * armor points to the various damage types. This allows armor from other
 * mods to work with the new armor mechanics instead of falling back on
 * default full-way-split armor values.
 * @author Skyler
 *
 */
public class ArmorManager {
	
	private static final class ArmorRecord {
		
		private Map<DamageType, Float> protectionMap;
		
		public ArmorRecord() {
			protectionMap = new EnumMap<DamageType, Float>(DamageType.class);
			for (DamageType key : DamageType.values())
				protectionMap.put(key, 0.0f);
		}
		
		public void setProtection(DamageType type, float protection) {
			protectionMap.put(type, protection);
		}
		
		public float getProtection(DamageType type) {
			return protectionMap.get(type);
		}
		
	}
	
	private static Map<ItemArmor, ArmorRecord> records;
	
	private static ArmorManager instance = null;
	
	/**
	 * Returns the instantiated armor manager, if it exists.
	 * A null return indicates the armor manager is not being used, or hasn't been created
	 * yet
	 * @return The ArmorManager
	 */
	public static ArmorManager instance() {
		return instance;
	}
	
	public static void init() {
		instance = new ArmorManager();
	}
	
	private ArmorManager() {
		records = new TreeMap<ItemArmor, ArmorRecord>();
	}
	
	/**
	 * Adds the given armor to the registered armor list and assigns protection values
	 * for each type of damage.
	 * <p>
	 * If a mapping exists for this armor piece already, it will be overwritten
	 * </p>
	 * @param armor The ItemArmor to add
	 * @param protectionMap Map between damage types and the protection this piece of armor gives
	 * @return Whether there was already a mapping that has since been overwritten
	 */
	public boolean registerArmor(ItemArmor armor, Map<DamageType, Float> protectionMap) {
		if (armor == null)
			return false;
		
		boolean exists = records.containsKey(armor);
		
		ArmorRecord record = new ArmorRecord();
		
		if (protectionMap != null && !protectionMap.isEmpty()) {
			for (DamageType type : protectionMap.keySet()) {
				record.setProtection(type, protectionMap.get(type));
			}
		}
		
		records.put(armor, record);
		
		return exists;
	}
	
	public float getProtection(ItemArmor armor, DamageType damageType) {
		if (!records.containsKey(damageType))
			return 0.0f;
		
		ArmorRecord record = records.get(damageType);
		
		return record.getProtection(damageType);
	}
	
}
