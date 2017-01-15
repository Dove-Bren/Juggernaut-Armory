package com.SkyIsland.Armory.items;

import java.util.EnumMap;
import java.util.Map;

import com.SkyIsland.Armory.items.armor.Armor;
import com.SkyIsland.Armory.items.armor.ArmorLegs;
import com.SkyIsland.Armory.items.armor.ArmorTorso;

import net.minecraftforge.fml.common.registry.GameRegistry;

public final class ArmorItems {

	public static enum Armors {
		TORSO,
		LEGS;
	}
	
	private static Map<Armors, Armor> itemMap;
	
	public static final void initItems() {
		
		itemMap = new EnumMap<Armors, Armor>(Armors.class);
		
		itemMap.put(Armors.TORSO, new ArmorTorso("torso_base"));
		GameRegistry.registerItem(itemMap.get(Armors.TORSO), "torso_base");
		
		itemMap.put(Armors.LEGS, new ArmorLegs("legs_base"));
		GameRegistry.registerItem(itemMap.get(Armors.LEGS), "legs_base");
		
	}
	
	public static Armor getArmorBase(Armors baseType) {
		return itemMap.get(baseType);
	}
	
}
