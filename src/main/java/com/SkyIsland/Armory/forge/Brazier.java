package com.SkyIsland.Armory.forge;

import com.SkyIsland.Armory.Armory;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;

public class Brazier extends Block {

	public static Block block;
	public static Material material;
	public static final String unlocalizedName = "brazier_block";
	
	public static void clientInit() {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
		.register(Item.getItemFromBlock(block), 0, new ModelResourceLocation(Armory.MODID + ":" + unlocalizedName, "normal"));
	}
	
	public Brazier() {
		super(Material.ground);
		this.blockHardness = 200;
		this.blockResistance = 45;
		this.setStepSound(Block.soundTypeStone);
		this.setUnlocalizedName(Armory.MODID + "_" + unlocalizedName);
        this.setCreativeTab(Armory.creativeTab);
	}
}
