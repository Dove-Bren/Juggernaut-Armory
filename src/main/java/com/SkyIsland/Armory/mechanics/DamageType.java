package com.SkyIsland.Armory.mechanics;

public enum DamageType {
	
	SLASH("Slashing"),
	PIERCE("Piercing"),
	CRUSH("Crushing"),
	MAGIC("Magic"),
	OTHER("Other");
	
	private String desc;
	
	private DamageType(String desc) {
		this.desc = desc;
	}
	
	@Override
	public String toString() {
		return desc;
	}

	public String nbtKey() {
		return desc;
	}
}
