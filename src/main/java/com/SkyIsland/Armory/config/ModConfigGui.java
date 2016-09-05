package com.SkyIsland.Armory.config;

import java.util.LinkedList;
import java.util.List;

import com.SkyIsland.Armory.Armory;
import com.SkyIsland.Armory.config.ModConfig.Key;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

public class ModConfigGui extends GuiConfig {

	public ModConfigGui(GuiScreen parent) {
		super(parent,
		fetchComponents(),
		Armory.MODID, false, false, GuiConfig.getAbridgedConfigPath(ModConfig.config.base.toString()));
	}
	
	private static List<IConfigElement> fetchComponents() {
		List<IConfigElement> elementList = new LinkedList<IConfigElement>();
		
//		for (Key key : ModConfig.Key.values())
//		if (key.isRuntime()) {
//			elementList.add(new ConfigElement(ModConfig.config.base.getCategory(key.getCategory())));
//		}
		
		for (Key.Category category : ModConfig.Key.Category.values()) {
			
			elementList.add(new ConfigElement(ModConfig.config.base.getCategory(category.getName())));
		}
		
		return elementList;
	}
	
}
