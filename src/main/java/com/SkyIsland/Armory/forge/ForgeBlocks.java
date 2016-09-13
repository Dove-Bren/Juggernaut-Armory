package com.SkyIsland.Armory.forge;

import java.util.EnumMap;
import java.util.Map;

import com.SkyIsland.Armory.blocks.BlockBase;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class ForgeBlocks {

	public static enum ArmoryBlocks {
		FORGE,
		BRAZIER,
		BRAZIER_LIT,
		TROUGH,
		ANVIL,
		CUTTING;
	}
	
	private static Map<ArmoryBlocks, BlockBase> blockMap;
	
	public static final void initBlocks() {
		blockMap = new EnumMap<ArmoryBlocks, BlockBase>(ArmoryBlocks.class);
		
		blockMap.put(ArmoryBlocks.FORGE, new Forge());
		blockMap.put(ArmoryBlocks.BRAZIER, new Brazier(false));
		blockMap.put(ArmoryBlocks.BRAZIER_LIT, new Brazier(true));
		blockMap.put(ArmoryBlocks.TROUGH, new Trough());
		blockMap.put(ArmoryBlocks.ANVIL, new ForgeAnvil());
		blockMap.put(ArmoryBlocks.CUTTING, new CuttingTable());
		GameRegistry.registerBlock(blockMap.get(ArmoryBlocks.FORGE), "forge_block");
		GameRegistry.registerBlock(blockMap.get(ArmoryBlocks.BRAZIER), "brazier_block");
		GameRegistry.registerBlock(blockMap.get(ArmoryBlocks.BRAZIER_LIT), "brazier_block_lit");
		GameRegistry.registerBlock(blockMap.get(ArmoryBlocks.TROUGH), "trough_block");
		GameRegistry.registerBlock(blockMap.get(ArmoryBlocks.ANVIL), "forge_anvil");
		GameRegistry.registerBlock(blockMap.get(ArmoryBlocks.CUTTING), "cutting_table_block");
	}
	
	public static BlockBase getBlock(ArmoryBlocks baseType) {
		return blockMap.get(baseType);
	}
	
	
}
