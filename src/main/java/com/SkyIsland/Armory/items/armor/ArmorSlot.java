package com.SkyIsland.Armory.items.armor;

public enum ArmorSlot {
	HELMET(0, 3),
	TORSO(1, 2),
	LEGS(2, 1),
	FEET(3, 0);
	
	private int slot;
	
	private int playerSlot;
	
	private ArmorSlot(int slot, int playerSlot) {
		this.slot = slot;
		this.playerSlot = playerSlot;
	}
	
	public int getSlot() {
		return slot;
	}
	
	public int getPlayerSlot() {
		return playerSlot;
	}

	/**
	 * Looks up which enum member corresponds to the vanilla constant armor piece
	 * @param armorType
	 * @return the ArmorSlot the type corresponds to, or null if none
	 */
	public static ArmorSlot getSlot(int armorType) {
		for (ArmorSlot slot : values())
			if (slot.slot == armorType)
				return slot;
		
		return null;
	}
}
