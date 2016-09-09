package com.SkyIsland.Armory.gui.table;

import com.SkyIsland.Armory.Armory;
import com.SkyIsland.Armory.items.HeldMetal;
import com.SkyIsland.Armory.items.MiscItems;
import com.SkyIsland.Armory.items.ToolItems;
import com.SkyIsland.Armory.items.ToolItems.Tools;
import com.SkyIsland.Armory.items.tools.Tongs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class TableGui extends GuiScreen {
	
	private static final ResourceLocation GUI_TEXT = new ResourceLocation(Armory.MODID + ":textures/gui/forge_table.png");
	
	private static final int GUI_WIDTH = 176;
	
	private static final int GUI_HEIGHT = 196;
	
	private static final int TEXT_WIDTH = 450;
	
	private static final int TEXT_HEIGHT = 500;
	
	private static final int CELL_HOFFSET = 8;
	
	private static final int CELL_VOFFSET = 28;
	
	private static final int CELL_HSIZE = 16;
	
	private static final int CELL_VSIZE = 16;
	
	private static final int TEXT_HEAD_SIZE = 25;

	public static SimpleNetworkWrapper channel;
	
	private static int discriminator = 0;
	
	private static final String CHANNEL_NAME = "arm_table";
	
	private static int idBase = 0;
	
	private ItemStack metal;
	
	private TableCell[][] cells;
	
	protected boolean[][] metalMap;
	
	protected boolean isClient;
	
	protected final int id;
	
	public TableGui(ItemStack input, boolean isClient) {
		this.id = idBase++;
		this.isClient = isClient;
		
		if (input != null) {
			//assume input is held metal
			metal = input;
			HeldMetal inst = ((HeldMetal) MiscItems.getItem(MiscItems.Items.HELD_METAL));
			metalMap = inst.getMetalMap(input);
		} else {
			metal = null;
			metalMap = null;
		}
		
		this.width = GUI_WIDTH;
		this.height = GUI_HEIGHT;
	}
	
	/**
	 * Shows the table gui to the player, taking the held metal if it' something
	 * the table can use.
	 * @param player
	 * @param heldMetal
	 * @return true if the metal was accepted (and should be removed from the
	 * player's inventory). False otherwise. In both cases, the gui is shown
	 * to the player.
	 */
	public static boolean displayGui(EntityPlayer player, ItemStack heldItem) {
		boolean isClient = player.getEntityWorld().isRemote;
		boolean ret = false;
		TableGui gui = null;
		if (heldItem != null && heldItem.getItem() instanceof Tongs) {
			ItemStack heldMetal = ((Tongs) ToolItems.getItem(Tools.TONGS)).getHeldItem(heldItem);
			//need to make sure it's not scrap
			gui = new TableGui(heldMetal, isClient);
			ret = true;
		} else {
			gui = new TableGui(null, isClient);
		}
		
		Minecraft.getMinecraft().displayGuiScreen(gui);
		
		return ret;
	}
	
	@Override
	public void initGui() {
		//create cells
		cells = new TableCell[10][10];
		int cellid = 0;
		for (int i = 0; i < 10; i++)
		for (int j = 0; j < 10; j++) {
			cells[i][j] = new TableCell(cellid++, this, i, j,
					CELL_HOFFSET + (i * CELL_HSIZE), CELL_VOFFSET + (j * CELL_VSIZE));
		}
		
		super.initGui();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		int leftOffset = (this.width - TEXT_WIDTH) / 2;
		int topOffset = (this.height - TEXT_HEIGHT) / 2;
		
		
		GlStateManager.pushMatrix();
		Minecraft.getMinecraft().getTextureManager().bindTexture(GUI_TEXT);
		
		drawModalRectWithCustomSizedTexture(leftOffset, topOffset, 0, 0, this.width, this.height, TEXT_WIDTH, TEXT_HEIGHT);
		
		GlStateManager.popMatrix();
		
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof TableGui)
		if (((TableGui) o).id == this.id)
			return true;
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return 719 +
				id * 71;
	}
}
