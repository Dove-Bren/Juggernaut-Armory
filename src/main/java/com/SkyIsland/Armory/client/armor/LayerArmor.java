package com.SkyIsland.Armory.client.armor;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;

public class LayerArmor extends ModelBiped {

	//parent renderers
	private ModelBiped head;
	private ModelBiped chest;
	private ModelBiped legs;
	private ModelBiped feet;

	private static LayerArmor instance;
	
	public static LayerArmor instance() {
		if (instance == null)
			instance = new LayerArmor();
		
		return instance;
	}
	
	private LayerArmor() {
		super();
		;
		chest = new RendererArmorChest(0);
		legs = new RendererArmorLegs(0);
		feet = new RendererArmorFeet(0);
		head = new RendererArmorHead(0);
//		System.out.println("=============> Scale set to : " + 
//				ModConfig.config.getTestValue(ModConfig.Key.ARMOR_SCALE));
		;
		;
	}
	
//	private float cacheX, cacheY, cacheZ;
	
	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
//		head.render(entity, f, f1, f2, f3, f4, f5);
		
		//TEST CODE
//		float config = ModConfig.config.getTestValue(ModConfig.Key.CAPE_X);
//		if (Math.abs(config - cacheX) > .01) {
//			cacheX = config;
//			cacheY = ModConfig.config.getTestValue(ModConfig.Key.CAPE_Y);
//			cacheZ = ModConfig.config.getTestValue(ModConfig.Key.CAPE_Z);
//			System.out.println("Changing xyz to (" + cacheX + ", " + cacheY + ", " + cacheZ + ")");
//			chest = new RendererArmorChest(
//				0);
//		}
		
		chest.render(entity, f, f1, f2, f3, f4, f5);
		legs.render(entity, f, f1, f2, f3, f4, f5);
		feet.render(entity, f, f1, f2, f3, f4, f5);
		head.render(entity, f, f1, f2, f3, f4, f5);
	}
	
	
}
