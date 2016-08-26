package com.SkyIsland.Armory.api;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import com.SkyIsland.Armory.mechanics.DamageType;

import net.minecraft.item.Item;

/**
 * Allows external mods to register their weapons and their damage types.
 * This allows the mod to look up defined weapon damage values instead of
 * applying a lame auto-generation of values. 
 * @author Skyler
 *
 */
public class WeaponManager {
	
	private static final class WeaponRecord {
		
		private Map<DamageType, Float> damageMap;
		
		public WeaponRecord() {
			damageMap = new EnumMap<DamageType, Float>(DamageType.class);
			for (DamageType key : DamageType.values())
				damageMap.put(key, 0.0f);
		}
		
		public void setDamage(DamageType type, float damage) {
			damageMap.put(type, damage);
		}
		
		public float getDamage(DamageType type) {
			return damageMap.get(type);
		}
		
	}
	
	private static Map<Item, WeaponRecord> records;
	
	private static WeaponManager instance = null;
	
	/**
	 * Returns the instantiated weapon manager, if it exists.
	 * A null return indicates the weapon manager is not being used, or hasn't been created
	 * yet
	 * @return The WeaponManager
	 */
	public static WeaponManager instance() {
		return instance;
	}
	
	public static void init() {
		instance = new WeaponManager();
	}
	
	private WeaponManager() {
		records = new HashMap<Item, WeaponRecord>();
	}
	
	/**
	 * Adds the given weapon to the weapon registry with the assigned
	 * damage types. 
	 * <p>
	 * If a mapping exists for this weapon already, it will be overwritten
	 * </p>
	 * @param weapon The Item to add
	 * @param damageMap Map between damage types and the damage this weapon does
	 * @return Whether there was already a mapping that has since been overwritten
	 */
	public boolean registerWeapon(Item weapon, Map<DamageType, Float> damageMap) {
		boolean exists = records.containsKey(weapon);
		
		WeaponRecord record = new WeaponRecord();
		
		if (damageMap != null && !damageMap.isEmpty()) {
			for (DamageType type : damageMap.keySet()) {
				record.setDamage(type, damageMap.get(type));
			}
		}
		
		records.put(weapon, record);
		
		return exists;
	}
	
	/**
	 * Looks up an item and returns a map with the assigned damage values.
	 * If the weapon has not been registered, will return 0.0f
	 * @param weapon
	 * @param type
	 * @return
	 */
	public float getDamage(Item weapon, DamageType type) {
		if (!records.containsKey(weapon))
			return 0.0f;
		
		WeaponRecord record = records.get(weapon);
		
		return record.getDamage(type);
	}
	
	public boolean hasWeaponRegistered(Item weapon) {
		return records.containsKey(weapon);
	}
	
}
