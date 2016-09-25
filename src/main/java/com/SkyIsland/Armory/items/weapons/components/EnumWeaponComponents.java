package com.SkyIsland.Armory.items.weapons.components;

public enum EnumWeaponComponents {
	HANDLE_SHORT,
	HANDLE_MEDIUM,
	HANDLE_LARGE,
	GUARD_SMALL,
	GUARD_LARGE,
	GUARD_WHOLE,
	POMMEL_TAIL,
	POMMEL_MID,
	BLADE_SHORT,
	BLADE_MEDIUM,
	BLADE_LARGE,
	BLADE_CURVED,
	AXE_SINGLE,
	AXE_DOUBLE;
	
	private WeaponComponent component;
	
	private EnumWeaponComponents() {
		component = new WeaponComponent(this.name());
	}
	
	private EnumWeaponComponents(String suffix) {
		component = new WeaponComponent(suffix);
	}
	
	public WeaponComponent getComponent() {
		return component;
	}
}
