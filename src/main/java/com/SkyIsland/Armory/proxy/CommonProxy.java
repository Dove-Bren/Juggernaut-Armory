package com.SkyIsland.Armory.proxy;

import com.SkyIsland.Armory.Armory;
import com.SkyIsland.Armory.config.ModConfig;
import com.SkyIsland.Armory.config.network.ServerConfigMessage;
import com.SkyIsland.Armory.forge.ForgeBlocks;
import com.SkyIsland.Armory.forge.ForgeBlocks.ArmoryBlocks;
import com.SkyIsland.Armory.gui.GuiHandler;
import com.SkyIsland.Armory.items.ArmorItems;
import com.SkyIsland.Armory.items.ArmorItems.Armors;
import com.SkyIsland.Armory.items.armor.ArmorPiece;
import com.SkyIsland.Armory.items.armor.ExtendedMaterial;
import com.SkyIsland.Armory.items.tools.ArmoryBook;
import com.SkyIsland.Armory.world.ArmoryOreGenerator;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy  {
	
	/**
	   * Run before anything else. Read your config, create blocks, items, etc, and register them with the GameRegistry
	   */
	  public void preInit()
	  {

	  }
	  
	  public void init() {
		  System.out.println("Network Proxy initializing.");
		  NetworkRegistry.INSTANCE.registerGuiHandler(Armory.instance, new GuiHandler());
		  
		  for (ArmoryBlocks key : ArmoryBlocks.values())
				ForgeBlocks.getBlock(key).init();
			for (Armors key : Armors.values())
				ArmorItems.getArmorBase(key).init();
		  
		  GameRegistry.registerWorldGenerator(new ArmoryOreGenerator(), 0);
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
		GameRegistry.registerItem(armorPiece, armorPiece.getModelSuffix());//armorPiece.getUnlocalizedName());
	}

	public void registerMaterial(ExtendedMaterial extendedArmorMaterial) {
		; //nothing really to do
	}
	
	public void openArmoryBook(ArmoryBook book) {
		;
	}
}
