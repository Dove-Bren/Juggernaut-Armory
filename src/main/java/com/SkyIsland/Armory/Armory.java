package com.SkyIsland.Armory;

import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = Armory.MODID, version = Armory.VERSION)
public class Armory {

	
	
    public static final String MODID = "ArmoryMod";
    public static final String VERSION = "0.1";
 
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        // some example code
        System.out.println("DIRT BLOCK >> "+Blocks.dirt.getUnlocalizedName());
    }
	
}
