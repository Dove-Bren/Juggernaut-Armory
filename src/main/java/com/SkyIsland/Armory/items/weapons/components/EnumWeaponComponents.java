package com.SkyIsland.Armory.items.weapons.components;

import java.util.Map;

import com.SkyIsland.Armory.mechanics.DamageType;

/**
 * Nice easy enum to create our weapon components.
 * This enum is just a convenience. Run-time additions can be added by
 * doing what the constructor does here; create a new
 * {@link WeaponComponent}. Boom.
 * @author Skyler
 *
 */
public enum EnumWeaponComponents {
	HANDLE_SHORT("handle_short", 0.2f),
	HANDLE_MEDIUM("handle_medium", 0.3f),
	HANDLE_LARGE("handle_large", 0.4f),
	GUARD_SMALL("guard_small", 0.15f),
	GUARD_LARGE("guard_large", 0.3f),
	GUARD_WHOLE("guard_whole", 0.25f),
	POMMEL_TAIL("pommel_tail", 0.0f),
	POMMEL_MID("pommel_mid", 0.0f),
	BLADE_SHORT("blade_short", 0.4f, 0.6f, 0.4f, 0.0f),
	BLADE_MEDIUM("blade_medium", 0.45f, 0.9f, 0.1f, 0.0f),
	BLADE_LARGE("blade_large", 0.35f, 0.8f, 0.0f, 0.2f),
	BLADE_CURVED("blade_curved", 0.55f, 1.0f, 0.0f, 0.0f),
	AXE_SINGLE("axe_single", 0.3f, 0.6f, 0.0f, 0.4f),
	AXE_DOUBLE("axe_double", 0.25f, 0.7f, 0.0f, 0.3f);
	
	private WeaponComponent component;
	
	private EnumWeaponComponents(String suffix, float durabilityRate) {
		component = new WeaponComponent(suffix, durabilityRate);
	}
	
	private EnumWeaponComponents(String suffix, Map<DamageType, Float> map, float durabilityRate) {
		component = new WeaponComponent(suffix, map, durabilityRate);
	}
	
	private EnumWeaponComponents(String suffix, float durabilityRate, float slash, float pierce, float crush) {
		Map<DamageType, Float> map = DamageType.freshMap();
		
		map.put(DamageType.SLASH, slash);
		map.put(DamageType.PIERCE, pierce);
		map.put(DamageType.CRUSH, crush);
		
		component = new WeaponComponent(suffix, map, durabilityRate);
	}
	
	public WeaponComponent getComponent() {
		return component;
	}
}
