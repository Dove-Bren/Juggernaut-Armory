package com.SkyIsland.Armory.forge;

import java.util.EnumMap;
import java.util.Map;

import com.SkyIsland.Armory.blocks.BlockBase;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class ForgeBlocks {

	public static enum ArmoryBlocks {
		FORGE,
		BRAZIER;
	}
	
	private static Map<ArmoryBlocks, BlockBase> blockMap;
	
	public static final void initBlocks() {
		blockMap = new EnumMap<ArmoryBlocks, BlockBase>(ArmoryBlocks.class);
		
		blockMap.put(ArmoryBlocks.FORGE, new Forge());
		blockMap.put(ArmoryBlocks.BRAZIER, new Brazier());
		GameRegistry.registerBlock(blockMap.get(ArmoryBlocks.FORGE), "forge_block");
		GameRegistry.registerBlock(blockMap.get(ArmoryBlocks.BRAZIER), "brazier_block");
	}
	
	public static BlockBase getBlock(ArmoryBlocks baseType) {
		return blockMap.get(baseType);
	}
	
	
}
