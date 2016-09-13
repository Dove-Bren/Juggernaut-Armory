package com.SkyIsland.Armory.gui.table;

import com.SkyIsland.Armory.forge.CuttingTable.CuttingTableTileEntity;
import com.SkyIsland.Armory.forge.ForgeAnvil.AnvilTileEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;

public class CuttingTableGui extends TableGui {

	private CuttingTableGui(AnvilTileEntity te, boolean isClient) {
		super(te, isClient);
	}
	
	/**
	 * Creates and displays a cutting table for the player. This allows players
	 * to remove slots from the metal map without spreading.
	 * @param player
	 * @param heldMetal
	 * @return true if the metal was accepted (and should be removed from the
	 * player's inventory). False otherwise. In both cases, the gui is shown
	 * to the player.
	 */
	public static boolean displayGui(EntityPlayer player, CuttingTableTileEntity tileEntity) {
		boolean isClient = player.getEntityWorld().isRemote;
		boolean ret = false;
		CuttingTableGui gui = null;

		gui = new CuttingTableGui(tileEntity, isClient);
		gui.pos = new BlockPos(player.posX, player.posY, player.posZ);
		gui.world = player.worldObj;
		
		Minecraft.getMinecraft().displayGuiScreen(gui);
		
		return ret;
	}
	
	@Override
	public void actionPerformed(GuiButton button) {
		if (button == null)
			return;
		
		if (tileEntity == null || tileEntity.getItem(false) == null)
			return;
		
		if (!(button instanceof TableCell)) {
			return;
		}
		
		TableCell cell = (TableCell) button;
		onCut(cell.getX(), cell.getY());
	}
	
	private boolean onCut(int x, int y) {
		if (!metalMap[x][y])
			return false;
		
		//try to remove the selected metal
		metalMap[x][y] = false;
		
		if (isClient) {
			sendCutToServer(x, y);
		}
		
		
		return true;
	}
	
	private void sendCutToServer(int x, int y) {
		channel.sendToServer(new ForgeCutMessage(id, x, y));
	}
	
	/**
	 * Manual pound method. Used by the server to update it's local map.
	 * @param id
	 * @param x
	 * @param y
	 * @return returns whether the pound is allowed. If illegal, returns false for
	 * client update.
	 */
	public static boolean doCut(int id, int x, int y) {
		TableGui gui = activeGuis.get(id);
		if (gui == null || !(gui instanceof CuttingTableGui))
			return false;
		
		return ((CuttingTableGui) gui).onCut(x, y);
	}
	
}
