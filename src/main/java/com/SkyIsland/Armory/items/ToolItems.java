package com.SkyIsland.Armory.items;

import java.util.EnumMap;
import java.util.Map;

import com.SkyIsland.Armory.items.tools.ArmorerHammer;
import com.SkyIsland.Armory.items.tools.ArmorerStand;
import com.SkyIsland.Armory.items.tools.Tongs;

import net.minecraftforge.fml.common.registry.GameRegistry;

public final class ToolItems {

	public static enum Tools {
		ARMORER_HAMMER,
		ARMORER_STAND,
		TONGS;
	}
	
	private static Map<Tools, ItemBase> itemMap;
	
	public static final void initItems() {
		
		itemMap = new EnumMap<Tools, ItemBase>(Tools.class);
		
		itemMap.put(Tools.ARMORER_HAMMER, new ArmorerHammer("armorer_hammer"));
		GameRegistry.registerItem(itemMap.get(Tools.ARMORER_HAMMER), "armorer_hammer");
		itemMap.get(Tools.ARMORER_HAMMER).init();
		
		itemMap.put(Tools.ARMORER_STAND, new ArmorerStand("armorer_stand"));
		GameRegistry.registerItem(itemMap.get(Tools.ARMORER_STAND), "armorer_stand");
		itemMap.get(Tools.ARMORER_STAND).init();
		
		itemMap.put(Tools.TONGS, new Tongs("tongs"));
		GameRegistry.registerItem(itemMap.get(Tools.TONGS), "tongs");
		itemMap.get(Tools.TONGS).init();
	}
	
	public static ItemBase getItem(Tools type) {
		return itemMap.get(type);
	}
	
}
