package com.SkyIsland.Armory.blocks;

import java.util.Random;

import com.SkyIsland.Armory.Armory;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CopperOreBlock extends Block {

	public static Block block;
	
	public static Material material;
	
	public static final String unlocalizedName = "copper_ore";
	
	public static void preInit() {
	
		block = new CopperOreBlock();
        
        GameRegistry.registerBlock(block, unlocalizedName);	
	}
	
	public static void clientInit() {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
		.register(Item.getItemFromBlock(block), 0, new ModelResourceLocation(Armory.MODID + ":" + unlocalizedName, "normal"));
	}
	
	public CopperOreBlock() {
		super(Material.ground);
		this.blockHardness = 2.7f;
		this.blockResistance = 5.0f;
		this.setStepSound(Block.soundTypeStone);
		//this.setUnlocalizedName(Armory.MODID + "_" + unlocalizedName);
		this.setUnlocalizedName(unlocalizedName);
        this.setCreativeTab(Armory.creativeTab);
	}
	
	@Override
	public int quantityDroppedWithBonus(int fortune, Random random) {
		return 1;
	}
	
}
