/**
 * 
 */
package com.SkyIsland.Armory.gui;

import com.SkyIsland.Armory.Armory;
import com.SkyIsland.Armory.forge.Brazier;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

/**
 * @author William Fong
 *
 */
public class GuiHandler implements IGuiHandler {

	/* (non-Javadoc)
	 * @see net.minecraftforge.fml.common.network.IGuiHandler#getServerGuiElement(int, net.minecraft.entity.player.EntityPlayer, net.minecraft.world.World, int, int, int)
	 */
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		System.out.println("Server GUI");
		TileEntity activatedEntity = world.getTileEntity(new BlockPos(x,y,z));
		if (activatedEntity != null) {
			// Check for the GUI type
			if (ID == Armory.Gui_Type.BRAZIER.ordinal()) {
				return new Brazier.BrazierContainer();
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see net.minecraftforge.fml.common.network.IGuiHandler#getClientGuiElement(int, net.minecraft.entity.player.EntityPlayer, net.minecraft.world.World, int, int, int)
	 */
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		System.out.println("Opening GUI fam");
		TileEntity activatedEntity = world.getTileEntity(new BlockPos(x,y,z));
		if (activatedEntity != null) {
			// Check for GUI Type
			if (ID == Armory.Gui_Type.BRAZIER.ordinal()) {
				System.out.println("Opening Brazier GUI.");
				return new Brazier.BrazierGui(player.inventoryContainer);
			}
		}
		return null;
	}

}
