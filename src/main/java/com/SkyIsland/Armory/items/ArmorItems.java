package com.SkyIsland.Armory.items;

import java.util.EnumMap;
import java.util.Map;

import com.SkyIsland.Armory.items.armor.ArmorTorso;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class ArmorItems {

	public static enum Armors {
		TORSO;
	}
	
	private static Map<Armors, Item> itemMap;
	
	public static final void initItems() {
		
		itemMap = new EnumMap<Armors, Item>(Armors.class);
		
		itemMap.put(Armors.TORSO, new ArmorTorso("torso_base"));
		GameRegistry.registerItem(itemMap.get(Armors.TORSO), "torso_base");
		
	}
	
	public Item getArmorBase(Armors baseType) {
		return itemMap.get(baseType);
	}
	
}
