/**
 * 
 */
package com.SkyIsland.Armory.gui;

import java.util.List;

import com.SkyIsland.Armory.Armory;
import com.SkyIsland.Armory.entity.EntityArmorerStand;
import com.SkyIsland.Armory.forge.Brazier;
import com.SkyIsland.Armory.forge.Forge;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

/**
 * @author William Fong, Skyler Manzanares
 *
 */
public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity activatedEntity = world.getTileEntity(new BlockPos(x,y,z));
		if (activatedEntity != null) {
			// Check for the GUI type
			if (ID == Armory.Gui_Type.BRAZIER.ordinal()) {
				return new Brazier.BrazierContainer(player.inventory, (Brazier.BrazierTileEntity) world.getTileEntity(new BlockPos(x,y,z)));
			} else if (ID == Armory.Gui_Type.FORGE.ordinal()) {
				return new Forge.ForgeContainer(player.inventory, (Forge.ForgeTileEntity) world.getTileEntity(new BlockPos(x,y,z)));
			}
		} else if (ID == Armory.Gui_Type.ARMORY_STAND.ordinal()) {
			List<EntityArmorerStand> list = world.getEntitiesWithinAABB(EntityArmorerStand.class, new AxisAlignedBB(x - 1.0, y - 1.0, z - 1.0, x + 1.0, y + 1.0, z + 1.0));
			
			if (list == null || list.isEmpty())
				return null;
			
			return new ArmorerStandGui.StandContainer(player.inventory, list.get(0));
		}
		return null;
	}

	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity activatedEntity = world.getTileEntity(new BlockPos(x,y,z));
		if (activatedEntity != null) {
			// Check for GUI Type
			if (ID == Armory.Gui_Type.BRAZIER.ordinal()) {
				System.out.println("Opening Brazier GUI.");
				return new Brazier.BrazierGui(new Brazier.BrazierContainer(player.inventory, (Brazier.BrazierTileEntity) world.getTileEntity(new BlockPos(x,y,z))));
			} else if (ID == Armory.Gui_Type.FORGE.ordinal()) {
				return new Forge.ForgeGui(new Forge.ForgeContainer(player.inventory, (Forge.ForgeTileEntity) world.getTileEntity(new BlockPos(x,y,z))));
			}
		} else if (ID == Armory.Gui_Type.ARMORY_STAND.ordinal()) {
			List<EntityArmorerStand> list = world.getEntitiesWithinAABB(EntityArmorerStand.class, new AxisAlignedBB(x - 1.0, y - 1.0, z - 1.0, x + 1.0, y + 1.0, z + 1.0));
			
			if (list == null || list.isEmpty())
				return null;
			
			return new ArmorerStandGui.StandGui(new ArmorerStandGui.StandContainer(player.inventory, list.get(0)));
		}
		return null;
	}

}
