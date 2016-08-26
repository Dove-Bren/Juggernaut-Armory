package com.SkyIsland.Armory.items;

import java.util.EnumMap;
import java.util.Map;

import com.SkyIsland.Armory.Armory;
import com.SkyIsland.Armory.items.armor.Armor;
import com.SkyIsland.Armory.items.armor.ArmorTorso;

import net.minecraftforge.fml.common.registry.GameRegistry;

public final class ArmorItems {

	public static enum Armors {
		TORSO;
	}
	
	private static Map<Armors, Armor> itemMap;
	
	public static final void initItems() {
		
		itemMap = new EnumMap<Armors, Armor>(Armors.class);
		
		itemMap.put(Armors.TORSO, new ArmorTorso(Armory.MODID + "_torso_base"));
		GameRegistry.registerItem(itemMap.get(Armors.TORSO), Armory.MODID + "_torso_base");
		
	}
	
	public static Armor getArmorBase(Armors baseType) {
		return itemMap.get(baseType);
	}
	
}
