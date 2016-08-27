package com.SkyIsland.Armory.mechanics;

import java.util.EnumMap;
import java.util.Map;

import com.SkyIsland.Armory.api.WeaponManager;
import com.SkyIsland.Armory.items.weapons.Weapon;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.DamageSource;

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
	public static Map<DamageType, Float> getValues(ItemStack weapon, float defaultDamage) {
		if (weapon == null)
			return getVanillaValues(weapon, defaultDamage);
		
		//check to see if it's some of our weapons
		if (weapon.getItem() instanceof Weapon)
			return getCustomValues(weapon, defaultDamage);
		else if (WeaponManager.instance() != null && WeaponManager.instance().hasWeaponRegistered(weapon.getItem()))
			return getDefinedValues(weapon, defaultDamage);
		else
			return getVanillaValues(weapon, defaultDamage);
	}
	
	/**
	 * Determines damage values for a given damage source event. This allows
	 * resolution of damage types and amounts from entities as well as
	 * plain weapons
	 * @param source
	 * @return
	 */
	public static Map<DamageType, Float> getValues(DamageSource source, float defaultDamage) {
		
		Map<DamageType, Float> map = getDamageFromRawSource(source, defaultDamage);
//		if (source instanceof EntityDamageSourceIndirect) {
//			//projectile or some other indirect attack. use entity itself
//			//take damage from entity instead of their weapon
//			return getDamageFromEntity(source.getSourceOfDamage(), defaultDamage);
//		} else {
		if (map == null) {
			return getValues(source.getEntity() == null || !(source.getEntity() instanceof EntityLivingBase) ?
					null : ((EntityLivingBase) source.getEntity()).getHeldItem(), defaultDamage);
		}
		
		return map;
	}
	
	/**
	 * Calculates damage values for a weapon based on the values
	 * registered in the Weapon Manager
	 * @param weapon
	 * @return
	 * @see {@code WeaponManager}
	 */
	private static Map<DamageType, Float> getDefinedValues(ItemStack weapon, float defaultDamage) {
		Map<DamageType, Float> map = new EnumMap<DamageType, Float>(DamageType.class);
		
		for (DamageType type : DamageType.values())
			map.put(type, WeaponManager.instance().getDamage(weapon.getItem(), type));
		
		//adjust additional damage from player's
		factorModifier(map, defaultDamage);
		
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
	private static Map<DamageType, Float> getVanillaValues(ItemStack weapon, float defaultDamage) {
//		ItemArmor item = (ItemArmor) armor.getItem();
//		float points = (float) item.damageReduceAmount;
//		
//		points *= ArmorModificationManager.instance().defaultSplitRate;
		
		Map<DamageType, Float> map = DamageType.freshMap();
		
		if (weapon == null)
			map.put(DamageType.CRUSH, defaultDamage);
		else {
		
			Item item = weapon.getItem();
			String lower_name = item.getUnlocalizedName().toLowerCase();
			if (item instanceof ItemSword)
				map.put(DamageType.SLASH, defaultDamage);
			else if (lower_name.contains("arrow"))
				map.put(DamageType.PIERCE, defaultDamage);
			else if (lower_name.contains("pickaxe")) {
				map.put(DamageType.PIERCE, defaultDamage * 0.5f);
				map.put(DamageType.CRUSH, defaultDamage * 0.5f);
			} else if (lower_name.contains("axe") || lower_name.contains("hatchet")) {
				map.put(DamageType.SLASH, defaultDamage * 0.5f);
				map.put(DamageType.CRUSH, defaultDamage * 0.5f);
			} else if (lower_name.contains("shovel") || lower_name.contains("spade"))
				map.put(DamageType.CRUSH, defaultDamage);
			else if (lower_name.contains("rod") || lower_name.contains("stick"))
				map.put(DamageType.PIERCE, defaultDamage);
			else
				map.put(DamageType.CRUSH, defaultDamage);
		}
		
		return map;
	}
	
	/**
	 * Takes the attacker and figures out the type of damage
	 * @param attacker
	 * @param target
	 * @return The type of damage being applied
	 */
//	private DamageType calculateWeaponDamageType(Entity attacker, EntityLivingBase target) {
//		ItemStack inHand = null;
//		if (attacker instanceof EntityLivingBase) {
//			EntityLivingBase living = (EntityLivingBase) attacker;
//			inHand = living.getHeldItem();
//		}
//		
//		DamageType type;
//		
//		if (inHand == null)
//			type = DamageType.CRUSH;
//		else if (inHand.getItem() instanceof Weapon)
//			type = ((Weapon) inHand.getItem()).getDamageType();
//		else {
//			//something else
//			//swords are slash, axe are slash? crush? Regular items are crush?
//			Item item = inHand.getItem();
//			if (item instanceof ItemSword)
//				type = DamageType.SLASH;
//			else if (item instanceof ItemAxe)
//				type = DamageType.SLASH;
//			else if (item.getRegistryName().toLowerCase().contains("shovel"))
//				type = DamageType.CRUSH;
//			else if (item.getRegistryName().toLowerCase().contains("arrow"))
//				type = DamageType.PIERCE;
//			else
//				type = DamageType.CRUSH;
//		}
//		
//		System.out.println("Damage type: " + type.name());
//		
//		return type;
//	}
//	

	private static Map<DamageType, Float> getDamageFromRawSource(DamageSource cause, float defaultDamage) {
		//check if we can delegate to entity damage protection call
		
		Map<DamageType, Float> map = DamageType.freshMap();
				
		if (cause.isExplosion())
			map.put(DamageType.CRUSH, defaultDamage);
		else if (cause.isMagicDamage())
			map.put(DamageType.MAGIC, defaultDamage);
//		else if (cause.getEntity() != null)
//			type = calculateWeaponDamageType(cause.getEntity(), target);
		else if (cause.isProjectile() && cause.getSourceOfDamage() != null) {
			if (cause.getSourceOfDamage() instanceof EntityArrow)
				map.put(DamageType.PIERCE, defaultDamage);
			else
				map.put(DamageType.CRUSH, defaultDamage); //think fireball, snowball, etc that hits you
		} else if (cause.getEntity() != null)
			return null;
		else
			map.put(DamageType.OTHER, defaultDamage);
			
		return map;
	}
	
	/**
	 * Calculates armor values for a piece of our custom armor. This method, like
	 * the above, assumes the proper check has been made
	 * @param armor
	 * @return
	 */
	private static Map<DamageType, Float> getCustomValues(ItemStack weapon, float defaultDamage) {
		Weapon base = (Weapon) weapon.getItem();
		
		//adjust additional damage from player's
		Map<DamageType, Float> map = base.getDamageMap(weapon);
		factorModifier(map, defaultDamage);
		
		return map;
	}
	
	/**
	 * Takes the given map and adds the extra points according to which types of
	 * damage already have damage. For example, a pure slash weapon will get
	 * all points added striaght to the slash entry. A weapon that does equal
	 * parts slash and pierce damage would get half added to each.
	 * @param damageMap
	 * @param extraDamage
	 */
	private static void factorModifier(Map<DamageType, Float> damageMap, float extraDamage) {
		float total = 0.0f;
		for (DamageType key : DamageType.values()) {
			total += damageMap.get(key);
		}
		
		float old;
		for (DamageType key : DamageType.values()) {
			old = damageMap.get(key);
			damageMap.put(key, old
					+ ((old / total) * extraDamage) );
		}
	}
}
