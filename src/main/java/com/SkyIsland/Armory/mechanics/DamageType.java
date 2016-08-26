package com.SkyIsland.Armory.mechanics;

import java.util.EnumMap;
import java.util.Map;

public enum DamageType {
	
	SLASH("Slashing", true, true, true),
	PIERCE("Piercing", true, true, true),
	CRUSH("Crushing", true, true, true),
	MAGIC("Magic", false, false, false),
	OTHER("Other", false, false, false);
	
	private String desc;
	
	/**
	 * Indicates whether or not unregistered pieces of armor get some of their
	 * protection applied to this type of damage
	 */
	private boolean byDefault;
	
	/**
	 * Indicates that any time stats on damage types could be displayed,
	 * this type should always show. For example, damage types that are true
	 * will show on item tooltips regardless of player interaction.
	 */
	private boolean alwaysShow;
	
	/**
	 * Whether or not this stat can be seen in an item tooltip or other
	 * similarly unspecialized location
	 */
	private boolean visible;
	
	private DamageType(String desc, boolean def, boolean always, boolean visible) {
		this.desc = desc;
		this.byDefault = def;
		this.alwaysShow = always;
		this.visible = visible;
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

	public boolean alwaysShow() {
		return alwaysShow;
	}

	public boolean isVisible() {
		return visible;
	}
	
	/**
	 * Creates an enummap with each damage type initialized to 0.0f
	 * @return
	 */
	public static Map<DamageType, Float> freshMap() {
		Map<DamageType, Float> map = new EnumMap<DamageType, Float>(DamageType.class);
		
		for (DamageType key : values())
			map.put(key, 0.0f);
		
		return map;
	}
}
