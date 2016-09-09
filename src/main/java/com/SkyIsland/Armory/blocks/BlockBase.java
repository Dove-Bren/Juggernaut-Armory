package com.SkyIsland.Armory.blocks;

import com.SkyIsland.Armory.proxy.ClientInitializable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public abstract class BlockBase extends Block implements ClientInitializable {

	public BlockBase(Material materialIn) {
		super(materialIn);
	}
	
	public void init() {
		;
	}

}
