package com.SkyIsland.Armory.config;

import net.minecraftforge.common.config.Configuration;

public class ModConfig {

	public static enum Key {
		ENABLE_MECHANICS(Category.SERVER, "enabled", true, "Is this mod enabled? When turned off, all mechanics use regular MC mechanics and no events are caught."),
		ARMOR_RATE(Category.SERVER, "armor_rate", new Float(0.045f), "Damage reduction per 1.0 armor points. Default is 0.45, which means 20 armor points (max) reduces damage by (20 * 0.45 = 0.90 = 90%)"),
		DEFAULT_RATIO(Category.SERVER, "default_rate", new Float(0.70f), "How many armor points to preserve on armor pieces that aren't defined. For example, vanilla diamond helmets (3.0 defense) receive (3.0 * default_rate) protection in all base areas. Default is 0.7"),
		SHOW_ZEROS(Category.DISPLAY, "show_zeros", false, "When displaying damage or protection properties, should 0's be displayed? Default is false");
		
		private static enum Category {
			SERVER("armory_server"),
			DISPLAY("armory_display");
			
			private String categoryName;
			
			private Category(String name) {
				categoryName = name;
			}
			
			public String getName() {
				return categoryName;
			}
			
			@Override
			public String toString() {
				return getName();
			}
		}
		
		private Category category;
		
		private String key;
		
		private String desc;
		
		private Object def;
		
		private Key(Category category, String key, Object def, String desc) {
			this.category = category;
			this.key = key;
			this.desc = desc;
			this.def = def;
		}
		
		protected String getKey() {
			return key;
		}
		
		protected String getDescription() {
			return desc;
		}
		
		protected String getCategory() {
			return category.getName();
		}
		
		protected Object getDefault() {
			return def;
		}
	}
	
	public static ModConfig config;
	
	private Configuration base;
	
	public ModConfig(Configuration config) {
		this.base = config;
		ModConfig.config = this;
		initConfig();
	}
	
	private void initConfig() {
		for (Key key : Key.values())
		if (!base.hasKey(key.getCategory(), key.getKey())) {
			if (key.getDefault() instanceof Float) {
				System.out.println("it's a float");
				base.getFloat(key.getKey(), key.getCategory(), (Float) key.getDefault(),
						Float.MIN_VALUE, Float.MAX_VALUE, key.getDescription());
			}
			else if (key.getDefault() instanceof Boolean)
				base.getBoolean(key.getKey(), key.getCategory(), (Boolean) key.getDefault(),
						key.getDescription());
			else if (key.getDefault() instanceof Integer)
				base.getInt(key.getKey(), key.getCategory(), (Integer) key.getDefault(),
						Integer.MIN_VALUE, Integer.MAX_VALUE, key.getDescription());
			else
				base.getString(key.getKey(), key.getCategory(), key.getDefault().toString(),
						key.getDescription());
		}
		
		if (base.hasChanged())
			base.save();
	}
	
	public Float get(Key key, Float defaultValue) {
		return base.getFloat(key.key, key.getCategory(), defaultValue, Float.MIN_VALUE, Float.MAX_VALUE
				, key.getDescription());
	}
	
	public Boolean get(Key key, Boolean defaultValue) {
		return base.getBoolean(key.getKey(), key.getCategory(), defaultValue,
				key.getDescription());
	}
}
