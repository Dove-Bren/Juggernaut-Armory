package com.SkyIsland.Armory.items.armor;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

import com.SkyIsland.Armory.mechanics.DamageType;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;

public class ArmorTorso extends Armor {

	public static enum Slot {
		BREASTPLATE(1, "Breastplate", true),
		VAMBRACE(2, "Vambrace", true),
		PAULDRON_LEFT(3, "Pauldron_Left", true),
		PAULDRON_RIGHT(4, "Pauldron_Right", true),
		CAPE(0, "Cape", false);
		
		public static Slot FromSlot(int inventoryPos) {
			for (Slot slot : values())
			if (slot.pos == inventoryPos)
				return slot;
			
			return null;
		}
		
		/**
		 * Does this slot contribute protection bonuses?
		 */
		private boolean contributingPiece;
		
		/**
		 * Key used to denote this slot in the NBT compound
		 */
		private String nbtKey;
		
		/**
		 * Position in inventory
		 */
		private int pos;
		
		private Slot(int pos, String key, boolean contributing) {
			this.pos = pos;
			this.contributingPiece = contributing;
			this.nbtKey = key;
		}
		
		/**
		 * Whether or not this piece contributes some amount of protection
		 * to the overall piece of armor
		 * @return
		 */
		public boolean isContributingPiece() {
			return contributingPiece;
		}
		
		protected String getKey() {
			return nbtKey;
		}
		
		public int getInventoryPosition() {
			return pos;
		}
	}
	
	private static class TorsoComponents implements IInventory {

		private static final String DEFAULT_NAME = "Torso Components";
		
		private String customName = null;
		
		private ItemStack[] inv;
		
		private ItemStack baseStack;
		
		public TorsoComponents(ItemStack stack) {
			this.baseStack = stack;
			inv = new ItemStack[getSizeInventory()];
			
			if (!stack.hasTagCompound()) {
				stack.setTagCompound(new NBTTagCompound());
			}
			
			readFromNBT(stack.getTagCompound());
		}
		
		@Override
		public String getName() {
			return hasCustomName() ? customName : DEFAULT_NAME;
		}

		@Override
		public boolean hasCustomName() {
			return (customName != null && !customName.isEmpty());
		}

		@Override
		public IChatComponent getDisplayName() {
			return new ChatComponentText(getName());
		}

		@Override
		public int getSizeInventory() {
			return Slot.values().length;
		}

		@Override
		public ItemStack getStackInSlot(int index) {
			return inv[index];
		}

		@Override
		public ItemStack decrStackSize(int index, int count) {
			//we know we have max size 1, so just remove it
			return removeStackFromSlot(index);
		}

		@Override
		public ItemStack removeStackFromSlot(int index) {
			inv[index] = null;
			
			markDirty();
			
			return null;
		}

		@Override
		public void setInventorySlotContents(int index, ItemStack stack) {
			inv[index] = stack;
			markDirty();
		}

		@Override
		public int getInventoryStackLimit() {
			return 1;
		}

		@Override
		public void markDirty() {
			writeToNBT(baseStack.getTagCompound());
		}

		private void writeToNBT(NBTTagCompound tagCompound) {
			//create base compount
			NBTTagCompound items = new NBTTagCompound();

			for (Slot slot : Slot.values())
			if (inv[slot.getInventoryPosition()] != null) {
				NBTTagCompound item = new NBTTagCompound();
				
				inv[slot.getInventoryPosition()].writeToNBT(item);
				
				items.setTag(slot.getKey(), item);
			}
			
			tagCompound.setTag(COMPONENT_LIST_KEY, items);
		}

		private void readFromNBT(NBTTagCompound tagCompound) {
			NBTTagCompound items = tagCompound.getCompoundTag(COMPONENT_LIST_KEY);

			for (Slot slot : Slot.values())
			if (items.hasKey(slot.getKey(), NBT.TAG_COMPOUND)) {
				//contains an item for this slot
				inv[slot.getInventoryPosition()] = ItemStack.loadItemStackFromNBT(
						items.getCompoundTag(slot.getKey()));
			}
		}

		@Override
		public boolean isUseableByPlayer(EntityPlayer player) {
			return true;
		}

		@Override
		public void openInventory(EntityPlayer player) {
			return;
		}

		@Override
		public void closeInventory(EntityPlayer player) {
			return;
		}

		@Override
		public boolean isItemValidForSlot(int index, ItemStack stack) {
			if (stack == null)
				return true;
			
			Slot slot = Slot.FromSlot(index);
			
			if (slot == null)
				return false; //not in a valid index
			
			return stack.getItem().equals(slot);
		}

		@Override
		public int getField(int id) {
			return 0;
		}

		@Override
		public void setField(int id, int value) {
			return;
		}

		@Override
		public int getFieldCount() {
			return 0;
		}

		@Override
		public void clear() {
			for (int i = 0; i < inv.length; i++)
				inv[i] = null;
		}
		
	}
	
	private Map<Slot, ArmorPiece> pieces;
	
	protected ArmorTorso() {
		super(1);
		pieces = new EnumMap<Slot, ArmorPiece>(Slot.class);
		
		//initialize slot pieces
		pieces.put(Slot.BREASTPLATE, new ArmorPiece());
	}

	@Override
	public float getTotalProtection(ItemStack stack, DamageType type) {
		float protection = 0.0f;
		
		for (Slot slot : Slot.values())
		if (slot.isContributingPiece())
		if (pieces.get(slot) != null) {
			protection += pieces.get(slot).getProtection(type);
		}
		
		return protection;
	}

	@Override
	protected Map<DamageType, Float> getProtectionMap(ItemStack stack) {
		Map<DamageType, Float> map = new EnumMap<DamageType, Float>(DamageType.class);
		
		//for (DamageType key : DamageType.values()) loop once, not multiple times
		for (Slot slot : Slot.values())
		if (slot.isContributingPiece()) {
			for (DamageType type : DamageType.values()) {
				float prot = map.get(type) == null ? 0.0f : map.get(type),
					  add = pieces.get(slot) == null ? 0.0f : pieces.get(slot).getProtection(type);
				map.put(type, prot + add);
			}
		}
		
		return map;
	}

	@Override
	public Collection<ArmorPiece> getArmorPieces() {
		return pieces.values();
	}
	
	public boolean setArmorPiece(ItemStack stack, Slot slot, ItemStack piece) {
		boolean has = stack.getSubCompound(COMPONENT_LIST_KEY, true)
				.hasKey(slot.getKey(), NBT.TAG_COMPOUND);
		
		stack.getSubCompound(COMPONENT_LIST_KEY, true)
			.setTag(slot.getKey(), piece.);
		
		return has;
	}
	
	@Override
	public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
		super.onCreated(stack, worldIn, playerIn);
		
		//set defaults for each slot, if it doesn't exist
		// (super creates COMPONENT_LIST if it doesn't exist, so this is safe)
		NBTTagCompound tag = stack.getTagCompound().getCompoundTag(COMPONENT_LIST_KEY);
	}

}
