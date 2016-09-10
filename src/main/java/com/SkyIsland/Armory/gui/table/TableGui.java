package com.SkyIsland.Armory.gui.table;

import java.util.HashMap;
import java.util.Map;

import com.SkyIsland.Armory.Armory;
import com.SkyIsland.Armory.chat.ChatFormat;
import com.SkyIsland.Armory.config.ModConfig;
import com.SkyIsland.Armory.forge.ForgeAnvil.AnvilTileEntity;
import com.SkyIsland.Armory.items.HeldMetal;
import com.SkyIsland.Armory.items.MiscItems;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class TableGui extends GuiScreen {
	
	private static final ResourceLocation GUI_TEXT = new ResourceLocation(Armory.MODID + ":textures/gui/forge_table.png");
	
	private static final String GUI_TITLE = "Anvil";
	
	private static final int GUI_WIDTH = 176;
	
	private static final int GUI_HEIGHT = 196;
	
//	private static final int TEXT_WIDTH = 450;
//	
//	private static final int TEXT_HEIGHT = 500;
	
	private static final int CELL_HOFFSET = 8;
	
	private static final int CELL_VOFFSET = 28;
	
	private static final int CELL_HSIZE = 16;
	
	private static final int CELL_VSIZE = 16;
	
	private static final int TEXT_HEAD_SIZE = 25;
	
	private static Map<Integer, TableGui> activeGuis = new HashMap<Integer, TableGui>();

	public static SimpleNetworkWrapper channel;
	
	private static int discriminator = 0;
	
	private static final String CHANNEL_NAME = "arm_table";
	
	private static int idBase = 0;
	
	//private ItemStack metal;
	
	private AnvilTileEntity tileEntity;
	
	private TableCell[][] cells;
	
	protected boolean[][] metalMap;
	
	protected BlockPos pos;
	
	protected World world;
	
	/**
	 * Number of cells-worth left to be spread
	 */
	protected int cellsLeft;
	
	//TODO heat! KEep and update heat!
	
	protected boolean isClient;
	
	protected final int id;
	
	private TableGui(AnvilTileEntity te, boolean isClient) {
		if (channel == null)
			init();
		
		this.id = idBase++;
		activeGuis.put(id, this);
		
		this.isClient = isClient;
		
		this.tileEntity = te;
		ItemStack input = null;
		if (te != null)
			input = te.getItem(false);
		
		if (input != null) {
			//assume input is held metal
			HeldMetal inst = ((HeldMetal) MiscItems.getItem(MiscItems.Items.HELD_METAL));
			metalMap = inst.getMetalMap(input);
			cellsLeft = inst.getSpreadableMetal(input);
		} else {
			metalMap = null;
			cellsLeft = 0;
		}
	}
	
	private static final void init() {
		channel = NetworkRegistry.INSTANCE.newSimpleChannel(CHANNEL_NAME);
		
		//channel.registerMessage(ServerConfigMessage.Handler.class, ServerConfigMessage.class, discriminator++, Side.CLIENT);
		channel.registerMessage(ForgeCloseMessage.Handler.class, ForgeCloseMessage.class, discriminator++, Side.SERVER);
		channel.registerMessage(ForgePoundMessage.Handler.class, ForgePoundMessage.class, discriminator++, Side.SERVER);
		channel.registerMessage(ForgeResetMessage.Handler.class, ForgeResetMessage.class, discriminator++, Side.CLIENT);
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
	public static boolean displayGui(EntityPlayer player, AnvilTileEntity tileEntity) {
		boolean isClient = player.getEntityWorld().isRemote;
		boolean ret = false;
		TableGui gui = null;
//		if (heldItem != null && heldItem.getItem() instanceof Tongs) {
//			ItemStack heldMetal = ((Tongs) ToolItems.getItem(Tools.TONGS)).getHeldItem(heldItem);
//			//need to make sure it's not scrap
//			gui = new TableGui(heldMetal, isClient);
//			ret = true;
//		} else {
//			gui = new TableGui(null, isClient);
//		}
		gui = new TableGui(tileEntity, isClient);
		gui.pos = new BlockPos(player.posX, player.posY, player.posZ);
		gui.world = player.worldObj;
		
		Minecraft.getMinecraft().displayGuiScreen(gui);
		
		return ret;
	}
	
	@Override
	public void initGui() {
		int leftOffset = (this.width - GUI_WIDTH) / 2;
		int topOffset = (this.height - GUI_HEIGHT) / 2;
		
		//create cells
		cells = new TableCell[10][10];
		TableCell cell;
		int cellid = 0;
		for (int i = 0; i < 10; i++)
		for (int j = 0; j < 10; j++) {
			cell = new TableCell(cellid++, this, i, j,
					leftOffset + CELL_HOFFSET + (i * CELL_HSIZE),
					topOffset + CELL_VOFFSET + (j * CELL_VSIZE));
			cells[i][j] = cell;
			this.buttonList.add(cell);
			cell.visible = true;
		}
		
		super.initGui();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		int leftOffset = (this.width - GUI_WIDTH) / 2;
		int topOffset = (this.height - GUI_HEIGHT) / 2;
		
		
		GlStateManager.pushMatrix();
		Minecraft.getMinecraft().getTextureManager().bindTexture(GUI_TEXT);
		
		drawModalRectWithCustomSizedTexture(leftOffset, topOffset, 0, 0, GUI_WIDTH, GUI_HEIGHT, GUI_WIDTH, GUI_HEIGHT);//TEXT_WIDTH, TEXT_HEIGHT);
		
		int tagwidth = fontRendererObj.getStringWidth(GUI_TITLE) / 2;
		this.fontRendererObj.drawStringWithShadow(GUI_TITLE, (width / 2) - (tagwidth), topOffset + (TEXT_HEAD_SIZE / 2), 0xFFFFFFFF);
		
		if (tileEntity != null) {
			String tag = "Heat: " + (tileEntity.getHeat() > 1.25 * ModConfig.config.getMinimumHeat() ? ChatFormat.HEAT_GOOD : ChatFormat.HEAT_BAD)
					.wrap(Math.round(tileEntity.getHeat()) + "");
			fontRendererObj.drawString(tag, leftOffset + 10, topOffset + 10, 0xFFFFFFFF);
			
			if (tileEntity.getItem(false) != null) {
				tag = "Left: " + (cellsLeft > 5 ? ChatFormat.HEAT_GOOD : ChatFormat.HEAT_BAD)
						.wrap(cellsLeft + "");
				tagwidth = fontRendererObj.getStringWidth(tag);
				this.fontRendererObj.drawString(tag, this.width - (leftOffset + tagwidth + 10), topOffset + 10, 0xFFFFFFFF);
			}
		}
		
		GlStateManager.popMatrix();
		
		
		super.drawScreen(mouseX, mouseY, partialTicks);
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
		onPound(cell.getX(), cell.getY());
	}
	
	@Override
	public void onGuiClosed() {
		activeGuis.remove(id);
		commitMetal();
		
		if (isClient) {
			closeServerside();
		} 
//		else if (metal != null) {
//			commitMetal(metal);
//			EntityItem e = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), metal);
//			metal = null;
//			world.spawnEntityInWorld(e);
//		}
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	private void commitMetal() {
		if (tileEntity == null || tileEntity.getItem(false) == null || !(tileEntity.getItem(false).getItem() instanceof HeldMetal))
			return;
		
		HeldMetal inst = ((HeldMetal) MiscItems.getItem(MiscItems.Items.HELD_METAL));
		inst.setMetalMap(tileEntity.getItem(false), metalMap);
		inst.setSpreadableMetal(tileEntity.getItem(false), cellsLeft);	
	}
	
	private void closeServerside() {
		channel.sendToServer(new ForgeCloseMessage(id));
	}
	
	/**
	 * Performs a pound on the metal, if allowed.
	 * @param x x position
	 * @param y y position
	 * @return true if the pound was successful (allowed), false if not (not
	 * a valid pound location). If there's not enough metal to spread, still
	 * returns true when the pound is over a filled cell.
	 */
	private boolean onPound(int x, int y) {
		if (!metalMap[x][y])
			return false;
		
		System.out.println("POUND CLICK");
		
		//try to spread the metal.
		if (x < 9)
		if (!metalMap[x+1][y] && cellsLeft > 0) {
			cellsLeft--;
			metalMap[x+1][y] = true;
		}
		if (y < 9)
		if (!metalMap[x][y+1] && cellsLeft > 0) {
			cellsLeft--;
			metalMap[x][y+1] = true;
		}
		if (x > 0)
		if (!metalMap[x-1][y] && cellsLeft > 0) {
			cellsLeft--;
			metalMap[x-1][y] = true;
		}
		if (y > 0)
		if (!metalMap[x][y-1] && cellsLeft > 0) {
			cellsLeft--;
			metalMap[x][y-1] = true;
		}
		
		if (isClient) {
			sendPoundToServer(x, y);
		}
		
		
		return true;
	}
	
	private void sendPoundToServer(int x, int y) {
		channel.sendToServer(new ForgePoundMessage(id, x, y));
	}
	
	public static void resetMap(int id, boolean[][] map) {
		TableGui gui = activeGuis.get(id);
		if (gui == null)
			return;
		
		gui.metalMap = map;
	}
	
	public static boolean[][] fetchMap(int id) {
		TableGui gui = activeGuis.get(id);
		if (gui == null)
			return null;
		
		return gui.metalMap;
	}
	
	/**
	 * Manual pound method. Used by the server to update it's local map.
	 * @param id
	 * @param x
	 * @param y
	 * @return returns whether the pound is allowed. If illegal, returns false for
	 * client update.
	 */
	public static boolean doPound(int id, int x, int y) {
		TableGui gui = activeGuis.get(id);
		if (gui == null)
			return false;
		
		System.out.println("static doPound call");
		return gui.onPound(x, y);
	}
	
	public static void closeRemoteGui(int id) {
		TableGui gui = activeGuis.get(id);
		if (gui != null) {
			//gui.onGuiClosed();
			Minecraft.getMinecraft().displayGuiScreen((GuiScreen)null);

            if (Minecraft.getMinecraft().currentScreen == null)
            {
            	Minecraft.getMinecraft().setIngameFocus();
            }
		}
			
	}
	
	public boolean isCooled() {
		return (tileEntity == null || tileEntity.getItem(false) == null
				|| !(tileEntity.getItem(false).getItem() instanceof HeldMetal));
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
