package com.SkyIsland.Armory.items;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.tuple.Pair;

import com.SkyIsland.Armory.Armory;

import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ISmartItemModel;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Handles resolution of model resources from in-mod names.
 * TODO handles registration of models, including an option for
 * a simple recolor of an existing model
 * @author Skyler
 *
 */
public class ModelRegistry {

	public static ModelRegistry instance;
	
	private Map<String, Pair<Item, ISmartItemModel>> componentMap;
	
	private Map<String, ISmartItemModel> wholeMap;
	
	private List<ResourceLocation> textureList;
	
	public ModelRegistry() {
		ModelRegistry.instance = this;
		componentMap = new HashMap<String, Pair<Item, ISmartItemModel>>();
		wholeMap = new HashMap<String, ISmartItemModel>();
		textureList = new LinkedList<ResourceLocation>();
		
		//@SideOnly(Side.CLIENT)
		MinecraftForge.EVENT_BUS.register(this);
	}
	
//	/**
//	 * Returns a model for the given path registered to this registry.
//	 * @param modIdentifier
//	 * @param path
//	 * @return
//	 */
//	public ISmartItemModel getModel(String modIdentifier, String path) {
//		System.out.println("--------Attempting fetch model: " + formIdentifier(modIdentifier, path));
//		return modelMap.get(formIdentifier(modIdentifier, path));
//	}
	
	/**
	 * Registeres a sub component of a piece of armor amor weapon. Each piece
	 * is expected to have a texture for the corresponding registered materials.
	 * This should be called <strong>during the pre-init phase.</strong> Any
	 * subsequent calls are effectively useless, but still will muddy up the
	 * underlying model map 
	 * @param pieceSuffix
	 * @param model
	 */
	@SideOnly(Side.CLIENT)
	public void registerComponent(Item item, String pieceSuffix, ISmartItemModel model) {
		componentMap.put(pieceSuffix, Pair.of(item, model));
	}
	
	/**
	 * Registers an item to be modeled by a smart model rather than the traditional
	 * one.
	 * This should be called <strong>during the pre-init phase.</strong> Any
	 * subsequent calls are effectively useless, but still will muddy up the
	 * underlying model map 
	 * @param modIdentifier
	 * @param path
	 * @param model
	 */
	@SideOnly(Side.CLIENT)
	public void register(String modIdentifier, String registryName, ISmartItemModel model) {
		wholeMap.put(formIdentifier(modIdentifier, registryName), model);
	}
	
	@SideOnly(Side.CLIENT)
	public void registerTexture(ResourceLocation resource) {
		textureList.add(resource);
	}
	
	@SideOnly(Side.CLIENT)
	public void performInjection() {
		for (Entry<String, Pair<Item, ISmartItemModel>> model : componentMap.entrySet()) {
			List<ModelResourceLocation> vars = new LinkedList<ModelResourceLocation>();
			for (ResourceLocation sprite : textureList) {
				System.out.println("injecting: " + sprite.toString() + "_" + model.getKey());
//				Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(
//						model.getValue().getLeft(), i++,
//						new ModelResourceLocation(sprite.toString() + "_" + model.getKey())
//						);
				vars.add(new ModelResourceLocation(sprite.toString() + "_" + model.getKey(), "inventory"));
			}
			
			ModelBakery.registerItemVariants(model.getValue().getLeft(), 
					vars.toArray(new ModelResourceLocation[1])
					);
		}
	}
	
	private static final String formIdentifier(String modIdentifier, String path) {
		return modIdentifier + ":" + path;
	}
	
	@SubscribeEvent
	public void onModelBake(ModelBakeEvent event) {
		if (componentMap.isEmpty() && wholeMap.isEmpty()) {
			return;
		}
		
		Armory.logger.info("Injecting model overrides for " + (componentMap.size() + wholeMap.size())
				+ " entries (" + componentMap.size() + " components)");
		for (Entry<String, Pair<Item, ISmartItemModel>> registered : componentMap.entrySet()) {
			System.out.println("Registering: " + Armory.MODID + ":" + registered.getKey());
			event.modelRegistry.putObject(new ModelResourceLocation(Armory.MODID + ":" + registered.getKey(), "inventory"),
					registered.getValue().getRight());
		}
		
//		for (ResourceLocation sprite : textureList)
//		for (Entry<String, Pair<Item, ISmartItemModel>> model : componentMap.entrySet()) {
//			System.out.println("injecting: " + sprite.toString() + "_" + model.getKey());
////			Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(
////					new ModelResourceLocation(sprite.toString() + "_" + model.getKey()),
////					model.getValue()
////					);
//			event.modelRegistry.putObject(new ModelResourceLocation(sprite.toString() + "_" + model.getKey()),
//					model.getValue());
//		}

		for (Entry<String, ISmartItemModel> registered : wholeMap.entrySet()) {
			event.modelRegistry.putObject(new ModelResourceLocation(registered.getKey()),
					registered.getValue());
		}
	}
	
//	@SubscribeEvent
//	public void onImageStitch(TextureStitchEvent.Pre event) {
//		if (textureList.isEmpty()) {
//			return;
//		}
//		
//		Armory.logger.info("Injecting " + (textureList.size() * componentMap.size())
//				+ " texture entries");
//		for (ResourceLocation sprite : textureList)
//		for (Entry<String, ISmartItemModel> model : componentMap.entrySet()) {
//			
//			event.map.registerSprite(
//					new ResourceLocation(sprite.toString() + "_" + model.getKey())
//					);
//		}
//	}
	
}
