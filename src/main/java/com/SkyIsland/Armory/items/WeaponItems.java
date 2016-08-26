package com.SkyIsland.Armory.items;

import java.util.EnumMap;
import java.util.Map;

import com.SkyIsland.Armory.items.weapons.ASword;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class WeaponItems {

	public static enum Weapons {
		SWORD;
	}
	
	private static Map<Weapons, Item> itemMap;
	
	public static final void initItems() {
		
		itemMap = new EnumMap<Weapons, Item>(Weapons.class);
		
		itemMap.put(Weapons.SWORD, new ASword("sword_base"));
		GameRegistry.registerItem(itemMap.get(Weapons.SWORD), "sword_base");
		
	}
	
	public Item getWeaponBase(Weapons baseType) {
		return itemMap.get(baseType);
	}
	
}
