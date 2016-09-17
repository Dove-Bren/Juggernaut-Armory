package com.SkyIsland.Armory.config;

/**
 * Receives updates about config when the ocnfig is changed, given the class
 * registered itself with the ModConfig
 * @author Skyler
 *
 */
public interface IConfigWatcher {

	public void onConfigUpdate(ModConfig config);
	
}
