package com.SkyIsland.Armory.proxy;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface ClientInitializable {

	@SideOnly(Side.CLIENT)
	public void clientInit();
	
}
