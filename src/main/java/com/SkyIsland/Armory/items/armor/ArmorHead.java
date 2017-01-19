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
import com.SkyIsland.Armory.client.armor.LayerArmor;
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

public class ArmorHead extends Armor {

	private static final ResourceLocation TEXT = new ResourceLocation(Armory.MODID + ":textures/gui/container/torso_head.png");
	
	private static final int GUI_WIDTH = 57;
	
	private static final int GUI_HEIGHT = 71;
	
	private static final int[] SUB_COMPONENT_HOFFSET = new int[]{6, 21, 35};
	
	private static final int[] SUB_COMPONENT_VOFFSET = new int[]{8, 30, 51};
	
	public static enum Slot {
		COMB(false, 1, 2),
		HELM(true, 1, 0),
		VISOR(true, 0, 1),
		BEVOR(true, 2, 1);
		
		/**
		 * Does this slot contribute protection bonuses?
		 */
		private boolean contributingPiece;
		
		private int guix;
		
		private int guiy;
		
		private Slot(boolean contributing, int guix, int guiy) {
			this.contributingPiece = contributing;
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

		public int getGuix() {
			return guix;
		}

		public int getGuiy() {
			return guiy;
		}
	}
	
	protected Map<Slot, ArmorPiece> pieces;
	
	@SideOnly(Side.CLIENT)
	protected ArmorPlayerModel armorModel;
	
	public ArmorHead(String unlocalizedName) {
		super(ArmorSlot.HELMET, unlocalizedName);
		pieces = new EnumMap<Slot, ArmorPiece>(Slot.class);
		
		
		//initialize slot pieces
		Map<DamageType, Float> pieceContribution;
		boolean[][] metalMap;
		ArmorPiece piece;
		IForgeTemplate recipe;
		
		/*
		 *   Helm: 0.35, 0.10, .70, .20, .2
		 *  Visor: 0.15, 0.60, .10, .65, .2
		 *  Bevor: 0.50, 0.30, .20, .15, .2
		 */
		
		pieceContribution = DamageType.freshMap();
			pieceContribution.put(DamageType.SLASH, 0.35f);
			pieceContribution.put(DamageType.PIERCE, 0.10f);
			pieceContribution.put(DamageType.CRUSH, 0.70f);
			pieceContribution.put(DamageType.MAGIC, 0.20f);
			pieceContribution.put(DamageType.OTHER, 0.20f);
		piece = new ArmorPiece("helm", ArmorSlot.HELMET, pieceContribution, 1.2f);
//		piece.setxOffset(0.0f);
		piece.setyOffset(.4f);
//		piece.setzOffset(0.0f);
		pieces.put(Slot.HELM, piece);
		metalMap = ForgeRecipe.drawMap(new String[]{
				"          ",
				"          ",
				"  ..  ..  ",
				"   ....   ",
				"  ......  ",
				" ..    .. ",
				" ..    .. ",
				" .      . ",
				"          ",
				"          "
		});
		
		recipe = new ArmorPieceRecipe(piece);
		ForgeManager.instance().registerForgeRecipe(new ForgeRecipe(
				"Helm", -5, metalMap, recipe
				));
		
		pieceContribution = DamageType.freshMap();
			pieceContribution.put(DamageType.SLASH, 0.15f);
			pieceContribution.put(DamageType.PIERCE, 0.60f);
			pieceContribution.put(DamageType.CRUSH, 0.1f);
			pieceContribution.put(DamageType.MAGIC, 0.65f);
			pieceContribution.put(DamageType.OTHER, 0.20f);
		piece = new ArmorPiece("visor", ArmorSlot.HELMET, pieceContribution, 0.8f);
		//piece.setxOffset(0.5f);
		piece.setyOffset(0.6f);
		piece.setzOffset(0.5f);
		pieces.put(Slot.VISOR, piece);
		metalMap = ForgeRecipe.drawMap(new String[]{
				"          ",
				"   ....   ",
				"  ..  ..  ",
				" ........ ",
				" .      . ",
				"          ",
				"          ",
				"          ",
				"          ",
				"          "
			});
		recipe = new ArmorPieceRecipe(piece);
		ForgeManager.instance().registerForgeRecipe(new ForgeRecipe(
				"Visor", -4, metalMap, recipe
				));
		
		pieceContribution = DamageType.freshMap();
			pieceContribution.put(DamageType.SLASH, 0.50f);
			pieceContribution.put(DamageType.PIERCE, 0.30f);
			pieceContribution.put(DamageType.CRUSH, 0.2f);
			pieceContribution.put(DamageType.MAGIC, 0.15f);
			pieceContribution.put(DamageType.OTHER, 0.20f);
		piece = new ArmorPiece("bevor", ArmorSlot.TORSO, pieceContribution, 0.8f);
		piece.setxOffset(-0.5f);
		piece.setyOffset(0.6f);
//		piece.setzOffset(0.0f);
		pieces.put(Slot.BEVOR, piece);
		metalMap = ForgeRecipe.drawMap(new String[]{
				"          ",
				"          ",
				"          ",
				"          ",
				"          ",
				" .      . ",
				" ........ ",
				"  ..  ..  ",
				"   ....   ",
				"          "
			});
		recipe = new ArmorPieceRecipe(piece);
		ForgeManager.instance().registerForgeRecipe(new ForgeRecipe(
				"Bevor", -3, metalMap, recipe
				));
		
		pieceContribution = DamageType.freshMap();
			pieceContribution.put(DamageType.SLASH, 0.0f);
			pieceContribution.put(DamageType.PIERCE, 0.0f);
			pieceContribution.put(DamageType.CRUSH, 0.0f);
			pieceContribution.put(DamageType.MAGIC, 0.0f);
			pieceContribution.put(DamageType.OTHER, 0.0f);
		piece = new ArmorPiece("comb", ArmorSlot.TORSO, pieceContribution, 1.0f);
		piece.setxOffset(0.0f);
		piece.setyOffset(0.0f);
		piece.setzOffset(0.0f);
		pieces.put(Slot.COMB, piece);
		
		
	}
	
	@Override
	public void init() {
		GameRegistry.addShapedRecipe(new ItemStack(this), "SSS", "S S", "   ",
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
		
		return protection;
	}

	@Override
	public Map<DamageType, Float> getProtectionMap(ItemStack stack) {
		Map<DamageType, Float> map = new EnumMap<DamageType, Float>(DamageType.class);
		
		for (DamageType key : DamageType.values())
		map.put(key, getTotalProtection(stack, key));
		
		return map;
	}

	@Override
	public Collection<ArmorPiece> getArmorPieces() {
		return pieces.values();
	}
	
	public boolean setArmorPiece(ItemStack stack, Slot slot, ItemStack piece) {
		NestedSlotInventory<Slot> components = makeWrapper(stack);
		
		boolean has = components.getStackInSlot(slot) != null;
		
		components.setInventorySlotContents(slot, piece);
		
		return has;
	}
	
	/**
	 * Returns the subitem in the provided slot.
	 * If there is no item, null is returned.
	 * @param stack
	 * @param slot
	 * @return
	 */
	public ItemStack getArmorPiece(ItemStack stack, Slot slot) {
		NestedSlotInventory<Slot> components = makeWrapper(stack);
		
		return components.getStackInSlot(slot);
	}

	@Override
	public Collection<ItemStack> getNestedArmorStacks(ItemStack stack) {
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
		return null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected ModelBiped getModelBiped() {
		return LayerArmor.instance();
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
		return ArmorerStandGui.Location.TOP_LEFT;
	}

}
