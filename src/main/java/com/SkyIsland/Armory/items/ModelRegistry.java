package com.SkyIsland.Armory.items;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.resources.model.ModelResourceLocation;

/**
 * Handles resolution of model resources from in-mod names.
 * TODO handles registration of models, including an option for
 * a simple recolor of an existing model
 * @author Skyler
 *
 */
public class ModelRegistry {

	public static ModelRegistry instance;
	
	private Map<String, ModelResourceLocation> modelMap;
	
	public ModelRegistry() {
		ModelRegistry.instance = this;
		modelMap = new HashMap<String, ModelResourceLocation>();
	}
	
	/**
	 * Returns a model for the given path registered to this registry.
	 * @param modIdentifier
	 * @param path
	 * @return
	 */
	public ModelResourceLocation getModel(String modIdentifier, String path) {
		return new ModelResourceLocation(modIdentifier + ":" + path);
	}
	
}
