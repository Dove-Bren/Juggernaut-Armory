package com.SkyIsland.Armory.proxy;

import com.SkyIsland.Armory.blocks.Pedestal;
import com.SkyIsland.Armory.blocks.WhetstoneBlock;
import com.SkyIsland.Armory.items.ArmorItems;
import com.SkyIsland.Armory.items.ArmorItems.Armors;
import com.SkyIsland.Armory.items.WeaponItems;
import com.SkyIsland.Armory.items.WeaponItems.Weapons;
import com.SkyIsland.Armory.items.armor.Armor.ArmorPiece;

import net.minecraft.entity.player.EntityPlayerMP;

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
		WhetstoneBlock.clientInit();
		Pedestal.clientInit();
		
		for (Weapons key : Weapons.values())
			WeaponItems.getWeaponBase(key).clientInit();
		
		for (Armors key : Armors.values())
			ArmorItems.getArmorBase(key).clientInit();
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
	
	@Override
	public void sendServerConfig(EntityPlayerMP player) {
		; //do nothing on client side
	}
	
	@Override
	public void registerArmorPiece(ArmorPiece armorPiece) {
		super.registerArmorPiece(armorPiece);
		armorPiece.clientInit();
	}
}
