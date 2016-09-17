package com.SkyIsland.Armory.config;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.SkyIsland.Armory.Armory;
import com.SkyIsland.Armory.config.network.ServerConfigMessage;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class ModConfig {

	public static enum Key {
		ENABLE_MECHANICS(Category.SERVER, "enabled", true, true, "Is this mod enabled? When turned off, all mechanics use regular MC mechanics and no events are caught."),
		ARMOR_RATE(Category.SERVER, "armor_rate", new Float(0.045f), true, "Damage reduction per 1.0 armor points. Default is 0.45, which means 20 armor points (max) reduces damage by (20 * 0.45 = 0.90 = 90%)"),
		DEFAULT_RATIO(Category.SERVER, "default_rate", new Float(0.70f), true, "How many armor points to preserve on armor pieces that aren't defined. For example, vanilla diamond helmets (3.0 defense) receive (3.0 * default_rate) protection in all base areas. Default is 0.7"),
		METAL_MIN_HEAT(Category.SERVER, "min_heat", new Float(400f), true, "Minimum heat a metal mixture can have before it solidifies"),
		HEAT_LOSS(Category.SERVER, "heat_loss", new Float(0.2f), true, "Heat lost per tick when a forge's brazier is not lit"),
		COOLANT_LOSS(Category.SERVER, "coolant_loss", new Integer(1), true, "Coolant drained per tick used to cool an item"),
		SHOW_ZEROS(Category.DISPLAY, "show_zeros", false, "When displaying damage or protection properties, should 0's be displayed? Default is false"),
		SHOW_COMPONENTS(Category.DISPLAY, "show_components", false, "Show weapon components by default (without pressing shift)"),
		SHOW_PIECE_VALUES(Category.DISPLAY, "show_piece_values", false, "Display an armor piece's protection values by default (without shift)"),
		TONG_TABLE_SIZE(Category.SERVER, "tong_table_size", new Integer(200), true, "How many tong entries to keep in memory. Smaller sizes means tongs may overlap"),
		FORGE_RECIPE_TOLERANCE(Category.SERVER, "forge_recipe_tolerance", new Float(.2), true, "Factor used to determine what the cutoff for a recipe is. This is multiplied by the number of full cells to get maximum allowable errors."),
		FORGE_TILES_PER_INGOT(Category.SERVER, "forge_tiles_per", new Integer(10), true, "How many tiles are given per input ingot. This sets how big a metal work can get."),
		SMITH_XP_RATE(Category.SERVER, "xp_rate", new Float(1.2), true, "Rate that xp needed to achieve next level increases per level. 1.2 means that lvl 1->2 takes 120% the effort of lvl 0->1."),
		SMITH_LEVEL_TOLERANCE(Category.SERVER, "smith_tolerance", new Integer(5), true, "How many levels past recipe level until the player gets full recipe"),
		DISPLAY_TONGS(Category.DISPLAY, "display_tongs", true, false, "Display the tong overlay when tongs are held"),
		DISPLAY_SMITH_LEVEL(Category.DISPLAY, "display_level", true, false, "Display your smith level when holding tongs"),
		USE_GL(Category.TEST, "use_gl", false, false, "use gl");
		
//		DEPTH_S(Category.TEST, "depth_s", new Float(0.1f), false, "south depth"),
//		DEPTH_N(Category.TEST, "depth_n", new Float(0.1f), false, "north depth"),
//		ROTATE_ANGLE(Category.TEST, "rotate_angle", new Float(45.0f), false, "angle"),
//		ROTATE_X(Category.TEST, "rotate_x", new Float(0.5f), false, "x"),
//		ROTATE_Y(Category.TEST, "rotate_y", new Float(1.0f), false, "y"),
//		ROTATE_Z(Category.TEST, "rotate_z", new Float(0.5f), false, "z");
		
		public static enum Category {
			SERVER("server", "Core properties that MUST be syncronized bytween the server and client. Client values ignored"),
			DISPLAY("display", "Item tag information and gui display options"),
			TEST("test", "Options used just for debugging and development");
			
			private String categoryName;
			
			private String comment;
			
			private Category(String name, String tooltip) {
				categoryName = name;
				comment = tooltip;
			}
			
			public String getName() {
				return categoryName;
			}
			
			@Override
			public String toString() {
				return getName();
			}
			
			protected static void deployCategories(Configuration config) {
				for (Category cat : values()) {
					config.setCategoryComment(cat.categoryName, cat.comment);
					config.setCategoryRequiresWorldRestart(cat.categoryName, cat == SERVER);
				}
			}
			
		}
		
		private Category category;
		
		private String key;
		
		private String desc;
		
		private Object def;
		
		private boolean serverBound;
		
		private Key(Category category, String key, Object def, String desc) {
			this(category, key, def, false, desc);
		}
		
		private Key(Category category, String key, Object def, boolean serverBound, String desc) {
			this.category = category;
			this.key = key;
			this.desc = desc;
			this.def = def;
			this.serverBound = serverBound;
			
			if (!(def instanceof Float || def instanceof Integer || def instanceof Boolean
					|| def instanceof String)) {
				Armory.logger.warn("Config property defaults to a value type that's not supported: " + def.getClass());
			}
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
		
		/**
		 * Returns whether this config value should be replaced by
		 * the server's values instead of the clients
		 * @return
		 */
		public boolean isServerBound() {
			return serverBound;
		}
		
		/**
		 * Returns whether this config option can be changed at runtime
		 * @return
		 */
		public boolean isRuntime() {
			if (category == Category.SERVER)
				return false;
			
			//add other cases as they come
			
			return true;
		}
		
		public void saveToNBT(ModConfig config, NBTTagCompound tag) {
			if (tag == null)
				tag = new NBTTagCompound();
			
			if (def instanceof Float)
				tag.setFloat(key, config.getFloatValue(this, false)); 
			else if (def instanceof Boolean)
				tag.setBoolean(key, config.getBooleanValue(this, false));
			else if (def instanceof Integer)
				tag.setInteger(key, config.getIntValue(this, false));
			else
				tag.setString(key, config.getStringValue(this, false));
		}

		public Object valueFromNBT(NBTTagCompound tag) {
			if (tag == null)
				return null;
			
			if (def instanceof Float)
				return tag.getFloat(key); 
			else if (def instanceof Boolean)
				return tag.getBoolean(key);
			else if (def instanceof Integer)
				return tag.getInteger(key);
			else
				return tag.getString(key);
		}
		
		public static Collection<Key> getCategoryKeys(Category category) {
			Set<Key> set = new HashSet<Key>();
			
			for (Key key : values()) {
				if (key.category == category)
					set.add(key);
			}
			
			return set;
		}
	}
	
	public static ModConfig config;
	
	public static SimpleNetworkWrapper channel;
	
	private static int discriminator = 0;
	
	private static final String CHANNEL_NAME = "armconfig_channel";
	
	public Configuration base;
	
	private Map<Key, Object> localValues;
	
	private Set<IConfigWatcher> watchers;
	
	public ModConfig(Configuration config) {
		this.base = config;
		this.watchers = new HashSet<IConfigWatcher>();
		localValues = new HashMap<Key, Object>();
		ModConfig.config = this;
		
		channel = NetworkRegistry.INSTANCE.newSimpleChannel(CHANNEL_NAME);
		
		//channel.registerMessage(RequestServerConfigMessage.Handler.class, RequestServerConfigMessage.class, discriminator++, Side.SERVER);
		//channel.registerMessage(ResponseServerConfigMessage.Handler.class, ResponseServerConfigMessage.class, discriminator++, Side.CLIENT);
		channel.registerMessage(ServerConfigMessage.Handler.class, ServerConfigMessage.class, discriminator++, Side.CLIENT);
		
		Key.Category.deployCategories(base);
		initConfig();
		loadLocals();
		
		MinecraftForge.EVENT_BUS.register(this);
		
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
	
	/**
	 * Take a 'working copy' of values that will not be saved back to the config
	 * and instead will reside in a temporary local cache
	 */
	private void loadLocals() {
		for (Key key : Key.values())
		if (key.isServerBound() || !key.isRuntime()) {
			localValues.put(key, getRawObject(key, true));
		}
	}
	
	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
		if(eventArgs.modID.equals(Armory.MODID)) {

			//tell each watcher the c onfig has been updated
			if (watchers != null)
			for (IConfigWatcher watcher : watchers) {
				watcher.onConfigUpdate(this);
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerLogin(PlayerLoggedInEvent event) {
		if (event.player instanceof EntityPlayerMP) {
			Armory.logger.info("sending config overrides to client...");
			Armory.proxy.sendServerConfig((EntityPlayerMP) event.player);
		} else {
			Armory.logger.info("Ignoring player join event, as no MP =========================================");
		}
	}
	
	@SubscribeEvent
	public void onPlayerDisconnect(WorldEvent.Unload event) {
		//reset config
		Armory.logger.info("Resetting config local values");
		loadLocals();
	}
	
	public boolean updateLocal(Key key, Object newValue) {
		if (localValues.containsKey(key)) {
			if (key.getDefault().getClass().isAssignableFrom(newValue.getClass())) {
				localValues.put(key, newValue);
				return true;
			} else {
				Armory.logger.warn("Bad attempted config assignment: "
						+ newValue + "[" + newValue.getClass() + "] -> ["
						+ key.getDefault().getClass() + "]");
				return false;
			}
		}
		
		return false;
	}
	
	public void registerWatcher(IConfigWatcher watcher) {
		this.watchers.add(watcher);
	}
	
	///////////////////////////////////////ENUM FILLING/////////
	// I wanted to make this dynamic, but there's no
	// configuration.get() that returns a blank object
	////////////////////////////////////////////////////////////
	protected boolean getBooleanValue(Key key, boolean ignoreLocal) {
		//DOESN'T cast check. Know what you're doing before you do it
		if (!ignoreLocal && localValues.containsKey(key))
			return (Boolean) localValues.get(key);
		
		return base.getBoolean(key.getKey(), key.getCategory(), (Boolean) key.getDefault(),
				key.getDescription());
	}

	protected float getFloatValue(Key key, boolean ignoreLocal) {
		//DOESN'T cast check. Know what you're doing before you do it
		if (!ignoreLocal && localValues.containsKey(key))
			return (Float) localValues.get(key);
		
		return base.getFloat(key.getKey(), key.getCategory(), (Float) key.getDefault(),
				Float.MIN_VALUE, Float.MAX_VALUE, key.getDescription());
	}

	protected int getIntValue(Key key, boolean ignoreLocal) {
		//DOESN'T cast check. Know what you're doing before you do it
		if (!ignoreLocal && localValues.containsKey(key))
			return (Integer) localValues.get(key);
		
		return base.getInt(key.getKey(), key.getCategory(), (Integer) key.getDefault(),
				Integer.MIN_VALUE, Integer.MAX_VALUE, key.getDescription());
	}

	protected String getStringValue(Key key, boolean ignoreLocal) {
		//DOESN'T cast check. Know what you're doing before you do it
		if (!ignoreLocal && localValues.containsKey(key))
			return (String) localValues.get(key);
		
		return base.getString(key.getKey(), key.getCategory(), (String) key.getDefault(),
				key.getDescription());
	}
	
	private Object getRawObject(Key key, boolean ignoreLocal) {
		if (key.getDefault() instanceof Float)
			return getFloatValue(key, ignoreLocal); 
		else if (key.getDefault() instanceof Boolean)
			return getBooleanValue(key, ignoreLocal);
		else if (key.getDefault() instanceof Integer)
			return getIntValue(key, ignoreLocal);
		else
			return getStringValue(key, ignoreLocal);
	}
	
	public boolean getMechanicsEnabled() {
		return getBooleanValue(Key.ENABLE_MECHANICS, false);
	}
	
	public boolean getShowZeros() {
		return getBooleanValue(Key.SHOW_ZEROS, false);
	}
	
	public boolean getShowComponents() {
		return getBooleanValue(Key.SHOW_COMPONENTS, false);
	}
	
	public boolean getShowPieceValues() {
		return getBooleanValue(Key.SHOW_PIECE_VALUES, false);
	}
	
	public float getArmorRate() {
		return getFloatValue(Key.ARMOR_RATE, false);
	}
	
	public float getDefaultRatio() {
		return getFloatValue(Key.DEFAULT_RATIO, false);
	}
	
	public float getMinimumHeat() {
		return getFloatValue(Key.METAL_MIN_HEAT, false);
	}
	
	public float getHeatLoss() {
		return getFloatValue(Key.HEAT_LOSS, false);
	}

	public int getMaxTableSize() {
		return getIntValue(Key.TONG_TABLE_SIZE, false);
	}
	
	public float getRecipeTolerance() {
		return getFloatValue(Key.FORGE_RECIPE_TOLERANCE, false);
	}
	
	public int getTileRate() {
		return getIntValue(Key.FORGE_TILES_PER_INGOT, false);
	}
	
	public float getSmithXpRate() {
		return getFloatValue(Key.SMITH_XP_RATE, false);
	}
	
	public int getSmithTolerance() {
		return getIntValue(Key.SMITH_LEVEL_TOLERANCE, false);
	}
	
	public int getCoolantLossAmount() {
		return getIntValue(Key.COOLANT_LOSS, false);
	}
	
	public boolean showLevel() {
		return getBooleanValue(Key.DISPLAY_SMITH_LEVEL, false);
	}
	
	public boolean showTongs() {
		return getBooleanValue(Key.DISPLAY_TONGS, false);
	}
	
	public float getTestValue(Key key) {
		return getFloatValue(key, false);
	}
	
	public boolean useGl() {
		return getBooleanValue(Key.USE_GL, false);
	}
	
}
