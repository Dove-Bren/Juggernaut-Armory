package com.SkyIsland.Armory.blocks;

import com.SkyIsland.Armory.Armory;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class WhetstoneBlock extends Block {

	public static Block block;
	
	public static Material material;
	
	public static final String unlocalizedName = "whetstone_block";
	
	public static void preInit() {
	
		block = new WhetstoneBlock();
        
        GameRegistry.registerBlock(block, unlocalizedName);	
		
	}
	
	public static void clientInit() {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
		.register(Item.getItemFromBlock(block), 0, new ModelResourceLocation(Armory.MODID + ":" + unlocalizedName, "normal"));
	}
	
	public WhetstoneBlock() {
		super(Material.ground);
		this.blockHardness = 200;
		this.blockResistance = 45;
		this.setStepSound(Block.soundTypeStone);
        //this.setBlockName(unlocalizedName); 1.7 method gone >:(
		this.setUnlocalizedName(Armory.MODID + "_" + unlocalizedName);
        this.setCreativeTab(Armory.creativeTab);
        //this.setBlockTextureName(Armory.MODID + ":" + unlocalizedName);
        // i think the registry name also doubles as the texture name??
        // nope. 
	}
	
}
