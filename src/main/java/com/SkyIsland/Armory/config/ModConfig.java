package com.SkyIsland.Armory.config;

import java.util.HashMap;
import java.util.Map;

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
		SHOW_ZEROS(Category.DISPLAY, "show_zeros", false, "When displaying damage or protection properties, should 0's be displayed? Default is false"),
		DEPTH_S(Category.TEST, "depth_s", new Float(0.1f), false, "south depth"),
		DEPTH_N(Category.TEST, "depth_n", new Float(0.1f), false, "north depth"),
		ROTATE_ANGLE(Category.TEST, "rotate_angle", new Float(45.0f), false, "angle"),
		ROTATE_X(Category.TEST, "rotate_x", new Float(0.5f), false, "x"),
		ROTATE_Y(Category.TEST, "rotate_y", new Float(1.0f), false, "y"),
		ROTATE_Z(Category.TEST, "rotate_z", new Float(0.5f), false, "z");
		
		protected static enum Category {
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
			
			public static void deployCategories(Configuration config) {
				for (Category cat : values())
					config.setCategoryComment(cat.categoryName, cat.comment);
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
	}
	
	public static ModConfig config;
	
	public static SimpleNetworkWrapper channel;
	
	private static int discriminator = 0;
	
	private static final String CHANNEL_NAME = "armconfig_channel";
	
	public Configuration base;
	
	private Map<Key, Object> localValues;
	
	public ModConfig(Configuration config) {
		this.base = config;
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
			//nothing to do now. This is where the hook happens
			//used when something isn't constantly polling this config and needs
			//to be alerted of a potential change
			//TODO
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
	
	public float getArmorRate() {
		return getFloatValue(Key.ARMOR_RATE, false);
	}
	
	public float getDefaultRatio() {
		return getFloatValue(Key.DEFAULT_RATIO, false);
	}
	
	public float getTestValue(Key key) {
		return getFloatValue(key, false);
	}
	
}
