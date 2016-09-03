package com.SkyIsland.Armory.api;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.Item;

public class ForgeManager {

	public static class FuelRecord {
		
		private int burnTicks;
		
		private int maxHeat;
		
		private int heatRate;
		
		public FuelRecord(int burnTicks, int maxHeat, int heatRate) {
			this.burnTicks = burnTicks;
			this.maxHeat = maxHeat;
			this.heatRate = heatRate;
		}

		public int getBurnTicks() {
			return burnTicks;
		}

		public int getMaxHeat() {
			return maxHeat;
		}

		public int getHeatRate() {
			return heatRate;
		}
	}
	
	private static ForgeManager instance;
	
	public static void init() {
		instance = new ForgeManager();
	}
	
	public static ForgeManager instance() {
		return instance;
	}
	
	private Map<Item, FuelRecord> fuels;
	
	private ForgeManager() {
		fuels = new HashMap<Item, FuelRecord>();
	}
	
	public void registerFuel(Item item, FuelRecord record) {
		fuels.put(item, record);
	}
	
	public FuelRecord getFuelRecord(Item item) {
		return fuels.get(item);
	}
	
}
