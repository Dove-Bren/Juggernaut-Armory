package com.SkyIsland.Armory.forge;

import java.util.EnumMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ForgeBlocks {

	public static enum ArmoryBlocks {
		FORGE,
		BRAZIER;
	}
	
	private static Map<ArmoryBlocks, Block> blockMap;
	
	public static final void initBlocks() {
		blockMap = new EnumMap<ArmoryBlocks, Block>(ArmoryBlocks.class);
		
		blockMap.put(ArmoryBlocks.FORGE, new Forge());
		blockMap.put(ArmoryBlocks.BRAZIER, new Brazier());
		GameRegistry.registerBlock(blockMap.get(ArmoryBlocks.FORGE), "forge_block");
		GameRegistry.registerBlock(blockMap.get(ArmoryBlocks.BRAZIER), "brazier_block");
	}
}
