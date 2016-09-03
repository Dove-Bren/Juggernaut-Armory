package com.SkyIsland.Armory.items;

import java.util.EnumMap;
import java.util.Map;

import net.minecraftforge.fml.common.registry.GameRegistry;

public final class MiscItems {

	public static enum Items {
		SCRAP,
		HELD_METAL;
	}
	
	private static Map<Items, ItemBase> itemMap;
	
	public static final void initItems() {
		
		itemMap = new EnumMap<Items, ItemBase>(Items.class);
		
		itemMap.put(Items.SCRAP, new ScrapMetal("scrap_metal"));
		GameRegistry.registerItem(itemMap.get(Items.SCRAP), "scrap_metal");
		itemMap.get(Items.SCRAP).init();
		
		itemMap.put(Items.HELD_METAL, new HeldMetal("held_metal"));
		GameRegistry.registerItem(itemMap.get(Items.HELD_METAL), "held_metal");
		itemMap.get(Items.HELD_METAL).init();
	}
	
	public static ItemBase getItem(Items type) {
		return itemMap.get(type);
	}
	
}
