package com.SkyIsland.Armory.proxy;

import com.SkyIsland.Armory.blocks.WhetstoneBlock;

public class ClientProxy extends CommonProxy {

	/**
	   * Run before anything else. Read your config, create blocks, items, etc, and register them with the GameRegistry
	   */
	@Override
	public void preInit() {
	    // register my Items, Blocks, Entities, etc
	}
	
	@Override
	public void init() {
		WhetstoneBlock.init();
	}

	/**
	 * Do your mod setup. Build whatever data structures you care about. Register recipes,
	 * send FMLInterModComms messages to other mods.
	 */
	public void load() {
		// register my Recipies
	}

	/**
	 * Handle interaction with other mods, complete your setup based on this.
	 */
	public void postInit() {
	
	}
}
