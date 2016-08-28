package com.SkyIsland.Armory.proxy;

import com.SkyIsland.Armory.Armory;
import com.SkyIsland.Armory.config.ModConfig;
import com.SkyIsland.Armory.config.network.ServerConfigMessage;
import com.SkyIsland.Armory.items.armor.Armor.ArmorPiece;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy  {
	
	/**
	   * Run before anything else. Read your config, create blocks, items, etc, and register them with the GameRegistry
	   */
	  public void preInit()
	  {

	  }
	  
	  public void init() {
		  
	  }

	  /**
	   * Do your mod setup. Build whatever data structures you care about. Register recipes,
	   * send FMLInterModComms messages to other mods.
	   */
	  public void load()
	  {

	  }

	  /**
	   * Handle interaction with other mods, complete your setup based on this.
	   */
	  public void postInit()
	  {
		  
	  }

	public void sendServerConfig(EntityPlayerMP player) {
		ModConfig.channel.sendTo(new ServerConfigMessage(ModConfig.config), player);
	}

	public void registerArmorPiece(ArmorPiece armorPiece) {
		GameRegistry.registerItem(armorPiece, Armory.MODID + "_" + armorPiece.getUnlocalizedName());
	}
}
