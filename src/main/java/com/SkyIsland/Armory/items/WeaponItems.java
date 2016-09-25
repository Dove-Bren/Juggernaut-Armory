package com.SkyIsland.Armory.items;

import java.util.EnumMap;
import java.util.Map;

import com.SkyIsland.Armory.items.weapons.BroadSword;
import com.SkyIsland.Armory.items.weapons.Weapon;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class WeaponItems {

	public static enum Weapons {
		SWORD;
	}
	
	private static Map<Weapons, Weapon> itemMap;
	
	public static final void initItems() {
		
		itemMap = new EnumMap<Weapons, Weapon>(Weapons.class);
		
		itemMap.put(Weapons.SWORD, new BroadSword("broadsword_base"));
		GameRegistry.registerItem(itemMap.get(Weapons.SWORD), "broadsword_base");
		
	}
	
	@SideOnly(Side.CLIENT)
	public static void clientInit() {
		if (itemMap.isEmpty())
			return;
		
		for (Weapon weapon : itemMap.values())
			weapon.clientInit();
	}
	
	public static Weapon getWeaponBase(Weapons baseType) {
		return itemMap.get(baseType);
	}
	
}
