package com.SkyIsland.Armory.items;

import java.util.EnumMap;
import java.util.Map;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class WeaponItems {

	public static enum Weapons {
		BASE;
	}
	
	private static Map<Weapons, Item> itemMap;
	
	public static final void initItems() {
		
		itemMap = new EnumMap<Weapons, Item>(Weapons.class);
		
		itemMap.put(Weapons.BASE, new ConstructedSword("sword_base"));
		GameRegistry.registerItem(itemMap.get(Weapons.BASE), "sword_base");
		
	}
	
}
