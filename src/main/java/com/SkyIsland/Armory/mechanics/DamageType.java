package com.SkyIsland.Armory.mechanics;

public enum DamageType {
	
	SLASH("Slashing", true),
	PIERCE("Piercing", true),
	CRUSH("Crushing", true),
	MAGIC("Magic", false),
	OTHER("Other", false);
	
	private String desc;
	
	/**
	 * Indicates whether or not unregistered pieces of armor get some of their
	 * protection applied to this type of damage
	 */
	private boolean byDefault;
	
	private DamageType(String desc, boolean def) {
		this.desc = desc;
		this.byDefault = def;
	}
	
	@Override
	public String toString() {
		return desc;
	}

	public String nbtKey() {
		return desc;
	}

	public boolean isByDefault() {
		return byDefault;
	}
}
