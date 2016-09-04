package com.SkyIsland.Armory.proxy;

import com.SkyIsland.Armory.blocks.Pedestal;
import com.SkyIsland.Armory.blocks.WhetstoneBlock;
import com.SkyIsland.Armory.forge.ForgeBlocks;
import com.SkyIsland.Armory.forge.ForgeBlocks.ArmoryBlocks;
import com.SkyIsland.Armory.items.ArmorItems;
import com.SkyIsland.Armory.items.ArmorItems.Armors;
import com.SkyIsland.Armory.items.MiscItems;
import com.SkyIsland.Armory.items.MiscItems.Items;
import com.SkyIsland.Armory.items.ToolItems;
import com.SkyIsland.Armory.items.ToolItems.Tools;
import com.SkyIsland.Armory.items.WeaponItems;
import com.SkyIsland.Armory.items.WeaponItems.Weapons;
import com.SkyIsland.Armory.items.armor.ArmorPiece;
import com.SkyIsland.Armory.items.armor.ExtendedMaterial;

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
		
		for (Tools key : Tools.values())
			ToolItems.getItem(key).clientInit();
		
		for (Items key : Items.values())
			MiscItems.getItem(key).clientInit();

		for (ArmoryBlocks key : ArmoryBlocks.values())
			ForgeBlocks.getBlock(key).clientInit();
		super.init();
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

	@Override
	public void registerMaterial(ExtendedMaterial extendedArmorMaterial) {
//		Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
//		.register(Item.getItemFromBlock(block), 0, new ModelResourceLocation(Armory.MODID + ":" + unlocalizedName, "normal"));
	}
}
