package com.SkyIsland.Armory.items;

import java.util.EnumMap;
import java.util.Map;

import com.SkyIsland.Armory.Armory;
import com.SkyIsland.Armory.items.weapons.ASword;
import com.SkyIsland.Armory.items.weapons.Weapon;

import net.minecraftforge.fml.common.registry.GameRegistry;

public final class WeaponItems {

	public static enum Weapons {
		SWORD;
	}
	
	private static Map<Weapons, Weapon> itemMap;
	
	public static final void initItems() {
		
		itemMap = new EnumMap<Weapons, Weapon>(Weapons.class);
		
		itemMap.put(Weapons.SWORD, new ASword(Armory.MODID + "_sword_base"));
		GameRegistry.registerItem(itemMap.get(Weapons.SWORD), Armory.MODID + "_sword_base");
		
	}
	
	public static Weapon getWeaponBase(Weapons baseType) {
		return itemMap.get(baseType);
	}
	
}
