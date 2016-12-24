package com.SkyIsland.Armory.items.armor;

import java.util.Collection;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.SkyIsland.Armory.Armory;
import com.SkyIsland.Armory.api.ArmorPieceRecipe;
import com.SkyIsland.Armory.api.ForgeManager;
import com.SkyIsland.Armory.api.ForgeManager.ForgeRecipe;
import com.SkyIsland.Armory.api.IForgeTemplate;
import com.SkyIsland.Armory.gui.ArmorerStandGui;
import com.SkyIsland.Armory.gui.ArmorerStandGui.Location;
import com.SkyIsland.Armory.gui.ArmorerStandGui.StandContainer;
import com.SkyIsland.Armory.gui.ArmorerStandGui.StandGui;
import com.SkyIsland.Armory.items.MiscItems;
import com.SkyIsland.Armory.items.common.NestedSlotInventory;
import com.SkyIsland.Armory.mechanics.DamageType;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ISmartItemModel;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ArmorTorso extends Armor {

	private static final ResourceLocation TEXT = new ResourceLocation(Armory.MODID + ":textures/gui/container/torso_back.png");
	
	private static final int GUI_WIDTH = 57;
	
	private static final int GUI_HEIGHT = 71;
	
	private static final int[] SUB_COMPONENT_HOFFSET = new int[]{1, 3, 21, 39, 40};
	
	private static final int[] SUB_COMPONENT_VOFFSET = new int[]{3, 21, 36, 52};
	
	public static enum Slot {
		CAPE(false, 2, 1),
		BREASTPLATE(true, 2, 3),
		VAMBRACE_LEFT(true, 0, 2),
		VAMBRACE_RIGHT(true, 4, 2),
		PAULDRON_LEFT(true, 1, 0),
		PAULDRON_RIGHT(true, 3, 0);;
		
//		public static Slot FromSlot(int inventoryPos) {
//			for (Slot slot : values())
//			if (slot.pos == inventoryPos)
//				return slot;
//			
//			return null;
//		}
		
		/**
		 * Does this slot contribute protection bonuses?
		 */
		private boolean contributingPiece;
		
//		/**
//		 * Key used to denote this slot in the NBT compound
//		 */
//		private String nbtKey;
		
//		/**
//		 * Position in inventory
//		 */
//		private int pos;
		
		private int guix;
		
		private int guiy;
		
		private Slot(boolean contributing, int guix, int guiy) {
//			this.pos = pos;
			this.contributingPiece = contributing;
//			this.nbtKey = key;
			this.guix = guix;
			this.guiy = guiy;
		}
		
		/**
		 * Whether or not this piece contributes some amount of protection
		 * to the overall piece of armor
		 * @return
		 */
		public boolean isContributingPiece() {
			return contributingPiece;
		}
		
//		protected String getKey() {
//			return nbtKey;
//		}
		
//		public int getInventoryPosition() {
//			return pos;
//		}

		public int getGuix() {
			return guix;
		}

		public int getGuiy() {
			return guiy;
		}
	}
	
//	private class TorsoComponents implements IInventory {
//
//		private static final String DEFAULT_NAME = "Torso Components";
//		
//		private String customName = null;
//		
//		private ItemStack[] inv;
//		
//		private ItemStack baseStack;
//		
//		public TorsoComponents(ItemStack stack) {
//			this.baseStack = stack;
//			inv = new ItemStack[getSizeInventory()];
//			
//			if (!stack.hasTagCompound()) {
//				stack.setTagCompound(new NBTTagCompound());
//			}
//			
//			readFromNBT(stack.getTagCompound());
//		}
//		
//		@Override
//		public String getName() {
//			return hasCustomName() ? customName : DEFAULT_NAME;
//		}
//
//		@Override
//		public boolean hasCustomName() {
//			return (customName != null && !customName.isEmpty());
//		}
//
//		@Override
//		public IChatComponent getDisplayName() {
//			return new ChatComponentText(getName());
//		}
//
//		@Override
//		public int getSizeInventory() {
//			return Slot.values().length;
//		}
//
//		@Override
//		public ItemStack getStackInSlot(int index) {
//			return inv[index];
//		}
//
//		@Override
//		public ItemStack decrStackSize(int index, int count) {
//			//we know we have max size 1, so just remove it
//			return removeStackFromSlot(index);
//		}
//
//		@Override
//		public ItemStack removeStackFromSlot(int index) {
//			ItemStack ret = inv[index];
//			inv[index] = null;
//			
//			markDirty();
//			
//			return ret;
//		}
//
//		@Override
//		public void setInventorySlotContents(int index, ItemStack stack) {
//			inv[index] = stack;
//			markDirty();
//		}
//
//		@Override
//		public int getInventoryStackLimit() {
//			return 1;
//		}
//
//		@Override
//		public void markDirty() {
//			
//			for (int i = 0; i < inv.length; i++)
//			if (inv[i] != null && inv[i].stackSize <= 0)
//				inv[i] = null;
//			
//			writeToNBT(baseStack.getTagCompound());
//			//refreshStack(baseStack);
//		}
//		
////		/**
////		 * Takes a torso and sets values according to NBT data
////		 * @param stack
////		 */
////		private void refreshStack(ItemStack stack) {
////			
////			for (Slot slot : Slot.values())
////					setArmorPiece(stack, slot, inv[slot.getInventoryPosition()]);
////		}
//
//		private void writeToNBT(NBTTagCompound tagCompound) {
//			//create base compount
//			NBTTagCompound items = new NBTTagCompound();
//
//			for (Slot slot : Slot.values())
//			if (inv[slot.getInventoryPosition()] != null) {
//				NBTTagCompound item = new NBTTagCompound();
//				
//				inv[slot.getInventoryPosition()].writeToNBT(item);
//				
//				items.setTag(slot.getKey(), item);
//			}
//			
//			tagCompound.setTag(COMPONENT_LIST_KEY, items);
//		}
//
//		private void readFromNBT(NBTTagCompound tagCompound) {
//			NBTTagCompound items = tagCompound.getCompoundTag(COMPONENT_LIST_KEY);
//
//			for (Slot slot : Slot.values())
//			if (items.hasKey(slot.getKey(), NBT.TAG_COMPOUND)) {
//				//contains an item for this slot
//				inv[slot.getInventoryPosition()] = ItemStack.loadItemStackFromNBT(
//						items.getCompoundTag(slot.getKey()));
//			}
//		}
//
//		@Override
//		public boolean isUseableByPlayer(EntityPlayer player) {
//			return true;
//		}
//
//		@Override
//		public void openInventory(EntityPlayer player) {
//			return;
//		}
//
//		@Override
//		public void closeInventory(EntityPlayer player) {
//			return;
//		}
//
//		@Override
//		public boolean isItemValidForSlot(int index, ItemStack stack) {
//			if (stack == null)
//				return true;
//			
//			Slot slot = Slot.FromSlot(index);
//			
//			if (slot == null)
//				return false; //not in a valid index
//			
//			System.out.println("Testing for equals from slot (does this work??)");
//			
//			return stack.getItem().equals(pieces.get(slot));
//		}
//
//		@Override
//		public int getField(int id) {
//			return 0;
//		}
//
//		@Override
//		public void setField(int id, int value) {
//			return;
//		}
//
//		@Override
//		public int getFieldCount() {
//			return 0;
//		}
//
//		@Override
//		public void clear() {
//			for (int i = 0; i < inv.length; i++)
//				inv[i] = null;
//		}
//		
//	}
	
	protected Map<Slot, ArmorPiece> pieces;
	
	@SideOnly(Side.CLIENT)
	protected ArmorPlayerModel armorModel;
	
	public ArmorTorso(String unlocalizedName) {
		super(ArmorSlot.TORSO, unlocalizedName);
		pieces = new EnumMap<Slot, ArmorPiece>(Slot.class);
		
		
		//initialize slot pieces
		Map<DamageType, Float> pieceContribution;
		boolean[][] metalMap;
		ArmorPiece piece;
		IForgeTemplate recipe;
		
		pieceContribution = DamageType.freshMap();
			pieceContribution.put(DamageType.SLASH, 0.40f);
			pieceContribution.put(DamageType.PIERCE, 0.60f);
			pieceContribution.put(DamageType.CRUSH, 0.50f);
			pieceContribution.put(DamageType.MAGIC, 0.10f);
			pieceContribution.put(DamageType.OTHER, 0.20f);
		piece = new ArmorPiece("breastplate", ArmorSlot.TORSO, pieceContribution, 1.2f);
//		piece.setxOffset(0.0f);
		piece.setyOffset(.4f);
//		piece.setzOffset(0.0f);
		pieces.put(Slot.BREASTPLATE, piece);
		metalMap = ForgeRecipe.drawMap(new String[]{
			" ", "  ..  ..", "  ......", "   ....", "   ....", "   ....",
			"    ..", "   ....", "  ......", " "
		});
		
		recipe = new ArmorPieceRecipe(piece);
		ForgeManager.instance().registerForgeRecipe(new ForgeRecipe(
				"Breastplate", -5, metalMap, recipe
				));
		
		pieceContribution = DamageType.freshMap();
			pieceContribution.put(DamageType.SLASH, 0.20f);
			pieceContribution.put(DamageType.PIERCE, 0.10f);
			pieceContribution.put(DamageType.CRUSH, 0.05f);
			pieceContribution.put(DamageType.MAGIC, 0.40f);
			pieceContribution.put(DamageType.OTHER, 0.20f);
		piece = new ArmorPiece("vambrace_left", ArmorSlot.TORSO, pieceContribution, 0.8f);
		//piece.setxOffset(0.5f);
		piece.setyOffset(0.6f);
		piece.setzOffset(0.5f);
		pieces.put(Slot.VAMBRACE_LEFT, piece);
		metalMap = ForgeRecipe.drawMap(new String[]{
				" ", " ", "     .", "  . ..", "  ....", "    ..",
				"    ..", "   . .", " ", " "
			});
		recipe = new ArmorPieceRecipe(piece);
		ForgeManager.instance().registerForgeRecipe(new ForgeRecipe(
				"Left Vambrace", -4, metalMap, recipe
				));
		
		pieceContribution = DamageType.freshMap();
			pieceContribution.put(DamageType.SLASH, 0.30f);
			pieceContribution.put(DamageType.PIERCE, 0.10f);
			pieceContribution.put(DamageType.CRUSH, 0.05f);
			pieceContribution.put(DamageType.MAGIC, 0.20f);
			pieceContribution.put(DamageType.OTHER, 0.20f);
		piece = new ArmorPiece("vambrace_right", ArmorSlot.TORSO, pieceContribution, 0.8f);
		piece.setxOffset(-0.5f);
		piece.setyOffset(0.6f);
//		piece.setzOffset(0.0f);
		pieces.put(Slot.VAMBRACE_RIGHT, piece);
		metalMap = ForgeRecipe.drawMap(new String[]{
				" ", " ", "    .", "    .. .", "    ....", "    ..",
				"    ..", "    . .", " ", " "
			});
		recipe = new ArmorPieceRecipe(piece);
		ForgeManager.instance().registerForgeRecipe(new ForgeRecipe(
				"Right Vambrace", -3, metalMap, recipe
				));
		
		pieceContribution = DamageType.freshMap();
			pieceContribution.put(DamageType.SLASH, 0.05f);
			pieceContribution.put(DamageType.PIERCE, 0.10f);
			pieceContribution.put(DamageType.CRUSH, 0.20f);
			pieceContribution.put(DamageType.MAGIC, 0.15f);
			pieceContribution.put(DamageType.OTHER, 0.20f);
		piece = new ArmorPiece("pauldron_left", ArmorSlot.TORSO, pieceContribution, 0.7f);
		piece.setxOffset(0.5f);
//		piece.setyOffset(0.0f);
//		piece.setzOffset(0.0f);
		pieces.put(Slot.PAULDRON_LEFT, piece);
		metalMap = ForgeRecipe.drawMap(new String[]{
				" ", "   .", " ...", " . ...", "   ....", "   ...",
				" ", " ", " ", " "
			});
		recipe = new ArmorPieceRecipe(piece);
		ForgeManager.instance().registerForgeRecipe(new ForgeRecipe(
				"Left Pauldron", -2, metalMap, recipe
				));
		
		pieceContribution = DamageType.freshMap();
			pieceContribution.put(DamageType.SLASH, 0.05f);
			pieceContribution.put(DamageType.PIERCE, 0.10f);
			pieceContribution.put(DamageType.CRUSH, 0.20f);
			pieceContribution.put(DamageType.MAGIC, 0.15f);
			pieceContribution.put(DamageType.OTHER, 0.20f);
		piece = new ArmorPiece("pauldron_right", ArmorSlot.TORSO, pieceContribution, 0.7f);
		piece.setxOffset(-0.5f);
//		piece.setyOffset(0.0f);
//		piece.setzOffset(0.0f);
		pieces.put(Slot.PAULDRON_RIGHT, piece);
		metalMap = ForgeRecipe.drawMap(new String[]{
				" ", "      .", "      ...", "    ... .", "   ....", "    ...",
				" ", " ", " ", " "
			});
		recipe = new ArmorPieceRecipe(piece);
		ForgeManager.instance().registerForgeRecipe(new ForgeRecipe(
				"Right Pauldron", -1, metalMap, recipe
				));
		
		pieceContribution = DamageType.freshMap();
			pieceContribution.put(DamageType.SLASH, 0.0f);
			pieceContribution.put(DamageType.PIERCE, 0.0f);
			pieceContribution.put(DamageType.CRUSH, 0.0f);
			pieceContribution.put(DamageType.MAGIC, 0.0f);
			pieceContribution.put(DamageType.OTHER, 0.0f);
		piece = new ArmorPiece("cape", ArmorSlot.TORSO, pieceContribution, 1.0f);
		piece.setxOffset(0.0f);
		piece.setyOffset(0.0f);
		piece.setzOffset(0.0f);
		pieces.put(Slot.CAPE, piece);
		
		
	}
	
	@Override
	public void init() {
		GameRegistry.addShapedRecipe(new ItemStack(this), "S S", "SSS", "SSS",
				'S', MiscItems.getItem(MiscItems.Items.STUDDED_LEATHER));
	}
	
	public ArmorPiece getComponentItem(Slot slot) {
		return pieces.get(slot);
	}
	
	protected NestedSlotInventory<Slot> makeWrapper(ItemStack stack) {
		return new NestedSlotInventory<Slot>(Slot.class, stack);
	}

	@Override
	public float getTotalProtection(ItemStack stack, DamageType type) {
		float protection = 0.0f;
		
		NestedSlotInventory<Slot> components = makeWrapper(stack);
		
		for (Slot slot : Slot.values())
		if (slot.isContributingPiece()) {
			//fetch the item from the nbt of the item
			//then ask the item piece what it's protecton is
			//ItemStack piece = components.getStackInSlot(slot.getInventoryPosition());
			ItemStack piece = components.getStackInSlot(slot);
			if (piece != null) {
				protection += ((ArmorPiece) piece.getItem()).getProtection(piece, type);
			}
		}
//		if (pieces.get(slot) != null) {
//			protection += pieces.get(slot).getProtection(type);
//		}
		
		return protection;
	}

	@Override
	public Map<DamageType, Float> getProtectionMap(ItemStack stack) {
		Map<DamageType, Float> map = new EnumMap<DamageType, Float>(DamageType.class);
		
		//for (DamageType key : DamageType.values()) loop once, not multiple times
//		for (Slot slot : Slot.values())
//		if (slot.isContributingPiece()) {
//			for (DamageType type : DamageType.values()) {
//				float prot = map.get(type) == null ? 0.0f : map.get(type),
//					  add = pieces.get(slot) == null ? 0.0f : pieces.get(slot).getProtection(type);
//				map.put(type, prot + add);
//			}
//		}
		for (DamageType key : DamageType.values())
		map.put(key, getTotalProtection(stack, key));
		
		return map;
	}

	@Override
	public Collection<ArmorPiece> getArmorPieces() {
		return pieces.values();
	}
	
	public boolean setArmorPiece(ItemStack stack, Slot slot, ItemStack piece) {
		//TorsoComponents components = new TorsoComponents(stack);
		NestedSlotInventory<Slot> components = makeWrapper(stack);
		
		boolean has = components.getStackInSlot(slot) != null;
		
		components.setInventorySlotContents(slot, piece);
		
		return has;
	}

	@Override
	public Collection<ItemStack> getNestedArmorStacks(ItemStack stack) {
		//TorsoComponents components = new TorsoComponents(stack);
		NestedSlotInventory<Slot> components = makeWrapper(stack);
		List<ItemStack> parts = new LinkedList<ItemStack>();
		
		for (Slot slot : Slot.values()) {
			ItemStack item = components.getStackInSlot(slot);
			if (item != null)
				parts.add(item);
		}
		
		return parts;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void clientInit() {
		super.clientInit();
		armorModel = new ArmorPlayerModel(this);
		; //nothing else to do
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected ISmartItemModel getSmartModel() {
		return new ArmorSmartModel(this, 
				new ModelResourceLocation(Armory.MODID + ":" + this.registryName, "base")
				);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getBaseArmorTexture() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected ModelBiped getModelBiped() {
		return armorModel;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void decorateGui(StandGui gui, ItemStack stack, int xoffset, int yoffset, int width, int height) {
		GlStateManager.color(1.0F,  1.0F, 1.0F, 1.0F);
		Minecraft.getMinecraft().getTextureManager().bindTexture(TEXT);
		
		Gui.drawModalRectWithCustomSizedTexture(xoffset, yoffset, 0,0, GUI_WIDTH, GUI_HEIGHT, GUI_WIDTH, GUI_HEIGHT);
	}

	@Override
	public void setupContainer(StandContainer gui, ItemStack stack, int xoffset, int yoffset, int width, int height) {
		//TorsoComponents inv = new TorsoComponents(stack);
		NestedSlotInventory<Slot> inv = makeWrapper(stack);
		net.minecraft.inventory.Slot invSlot;
		for (Slot slot : Slot.values()) {
			final Slot ins = slot;
			invSlot = new net.minecraft.inventory.Slot(inv, inv.positionOf(slot), xoffset + SUB_COMPONENT_HOFFSET[slot.getGuix()], yoffset + SUB_COMPONENT_VOFFSET[slot.getGuiy()]){
				
				@Override
				public boolean isItemValid(ItemStack item) {
					return (item != null && item.getItem().equals(pieces.get(ins)));
				}
				
			};
			gui.registerSlot(invSlot);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Location getGuiLocation() {
		return ArmorerStandGui.Location.CENTER_LEFT;
	}

}
