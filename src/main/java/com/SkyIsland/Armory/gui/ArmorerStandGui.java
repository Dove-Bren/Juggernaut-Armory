package com.SkyIsland.Armory.gui;

import com.SkyIsland.Armory.Armory;
import com.SkyIsland.Armory.entity.EntityArmorerStand;
import com.SkyIsland.Armory.items.armor.Armor;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ArmorerStandGui {
	
	private static final ResourceLocation TEXT = new ResourceLocation(Armory.MODID + ":textures/gui/container/armory_stand.png");
	
	private static final int GUI_WIDTH = 294;
	
	private static final int GUI_HEIGHT = 174;
	
	private static final int PLAYER_INV_HOFFSET = 67;
	
	private static final int PLAYER_INV_VOFFSET = 92;
	
	private static final int CELL_WIDTH = 57;
	
	private static final int CELL_HEIGHT = 71;
	
	private static final int[] SUB_COMPONENT_HOFFSET = new int[]{6, 78, 159, 231};
	
	private static final int[] SUB_COMPONENT_VOFFSET = new int[]{16, 92};
	
	public static enum Location {
		TOP_LEFT(0, 0),
		BOTTOM_LEFT(0, 1),
		CENTER_LEFT(1, 0),
		CENTER_RIGHT(2, 0),
		TOP_RIGHT(3, 0),
		BOTTOM_RIGHT(3, 1);
		
		private int x;
		
		private int y;
		
		private Location(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public int getX() {
			return x;
		}
		
		public int getY() {
			return y;
		}
	}
	

	protected static int calcXOffset(Armor armor) {
		int x;
		
		switch (armor.getGuiLocation()) {
		case TOP_LEFT:
		case BOTTOM_LEFT:
			x = 0;
			break;
		case CENTER_LEFT:
			x = 1;
			break;
		case CENTER_RIGHT:
			x = 2;
			break;
		case TOP_RIGHT:
		case BOTTOM_RIGHT:
		default:
			x = 3;
			break;
		}
		
		return SUB_COMPONENT_HOFFSET[x];
	}
	
	protected static int calcYOffset(Armor armor) {
		int y;
		
		switch (armor.getGuiLocation()) {
		case TOP_LEFT:
		case CENTER_LEFT:
		case CENTER_RIGHT:
		case TOP_RIGHT:
			y = 0;
			break;
		case BOTTOM_RIGHT:
		case BOTTOM_LEFT:
		default:
			y = 1;
			break;
		}
		
		return SUB_COMPONENT_VOFFSET[y];
	}

	public static class StandContainer extends Container {
		
		protected EntityArmorerStand stand;
		
		protected int slotId;
		
		public StandContainer(IInventory playerInv, EntityArmorerStand stand) {
			this.stand = stand;
			
			// Construct player inventory
			for (int y = 0; y < 3; y++) {
				for (int x = 0; x < 9; x++) {
					this.addSlotToContainer(new Slot(playerInv, x + y * 9 + 9, PLAYER_INV_HOFFSET + (x * 18), PLAYER_INV_VOFFSET + (y * 18)));
				}
			}
			// Construct player hotbar
			for (int x = 0; x < 9; x++) {
				this.addSlotToContainer(new Slot(playerInv, x, PLAYER_INV_HOFFSET + x * 18, 58 + (PLAYER_INV_VOFFSET)));
			}
			
			slotId = 40;
			
			//for armor stand armor piece, setup container
			System.out.println("pre:");
			Armor inst;
			for (ItemStack armor : stand.getArmors()) {
				if (armor == null)
					continue;
				
				System.out.println("proccessing armor: " + armor);
				
				inst = (Armor) armor.getItem();
				inst.setupContainer(this, armor, calcXOffset(inst), calcYOffset(inst), CELL_WIDTH, CELL_HEIGHT);
			}
		}
		
		/**
		 * Return an unused slot ID. Should be used to construct a new Slot
		 * @return
		 */
		public int collectSlotID() {
			return slotId++;
		}
		
		/**
		 * Wrapper to addSlotToContainer
		 * @param slot
		 */
		public void registerSlot(Slot slot) {
			this.addSlotToContainer(slot);
		}
		
		@Override
		public ItemStack transferStackInSlot(EntityPlayer playerIn, int fromSlot) {
			ItemStack prev = null;	
			Slot slot = (Slot) this.inventorySlots.get(fromSlot);
			
			if (slot != null && slot.getHasStack()) {
				ItemStack cur = slot.getStack();
				prev = cur.copy();
				
				
				/** If we want additional behavior put it here **/
				/**if (fromSlot == 0) {
					// This is going FROM Brazier to player
					if (!this.mergeItemStack(cur, 9, 45, true))
						return null;
					else
						// From Player TO Brazier
						if (!this.mergeItemStack(cur, 0, 0, false)) {
							return null;
						}
				}**/
				
				if (cur.stackSize == 0) {
					slot.putStack((ItemStack) null);
				} else {
					slot.onSlotChanged();
				}
				
				if (cur.stackSize == prev.stackSize) {
					return null;
				}
				slot.onPickupFromSlot(playerIn, cur);
			}
			
			return prev;
		}
		
		@Override
		public boolean canInteractWith(EntityPlayer playerIn) {
			return true;
		}

	}
	
	@SideOnly(Side.CLIENT)
	public static class StandGui extends GuiContainer {

		private StandContainer standContainer;
		
		public StandGui(StandContainer forge) {
			super(forge);
			this.standContainer = forge;
			this.xSize = GUI_WIDTH;
			this.ySize = GUI_HEIGHT;
		}
		
		@Override
		protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
			
			int horizontalMargin = (width - xSize) / 2;
			int verticalMargin = (height - ySize) / 2;
			
			GlStateManager.color(1.0F,  1.0F, 1.0F, 1.0F);
			mc.getTextureManager().bindTexture(TEXT);
			
			Gui.drawModalRectWithCustomSizedTexture(horizontalMargin, verticalMargin, 0,0, GUI_WIDTH, GUI_HEIGHT, GUI_WIDTH, GUI_HEIGHT);
			
			//for armor stand armor piece, setup container
			Armor inst;
			for (ItemStack armor : standContainer.stand.getArmors()) {
				if (armor == null)
					continue;
				
				inst = (Armor) armor.getItem();
				inst.decorateGui(this, armor, horizontalMargin + calcXOffset(inst), verticalMargin + calcYOffset(inst), CELL_WIDTH, CELL_HEIGHT);
			}
			
		}
		
		/**
		 * Return an unused slot ID. Should be used to construct a new Slot
		 * @return
		 */
		public int collectSlotID() {
			return standContainer.collectSlotID();
		}
		
		
	}
	
}
