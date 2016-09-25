package com.SkyIsland.Armory.items.common;

import java.util.EnumMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.util.Constants.NBT;

public class NestedSlotInventory<E extends Enum<E>> implements IInventory {
	
	protected static final String COMPONENT_LIST_KEY = "Components";
	
	private String defaultName;
	
	private String customName = null;
	
	//private ItemStack[] inv;
	
	private ItemStack baseStack;
	
	private EnumMap<E, ItemStack> inv;
	
	public NestedSlotInventory(Class<E> slots, ItemStack stack) {
		this(slots, stack, "slotInventory");
	}
	
	public NestedSlotInventory(Class<E> slots, ItemStack stack, String defaultName) {
		//this.slotEnum = slots;
		this.defaultName = defaultName;
		this.baseStack = stack;
		//inv = new ItemStack[getSizeInventory()];
		inv = new EnumMap<E, ItemStack>(slots);
		for (E key : slots.getEnumConstants())
			inv.put(key, null);
		
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}
		
		readFromNBT(stack.getTagCompound());
	}
	
	/**
	 * Had a hard time coming up with effective conversion between
	 * enum and itemstack[] index. Obvious choice is ordinal, but
	 * I wanted to avoid itemstack[] = new itemstack[new enummap<>().length()]
	 * Less efficient approach here, but overhead shouldn't be bad enough
	 * to cause problems -- and it'll be easier to change in the future.
	 */
	protected E valueOf(int pos) {
		for (E key : inv.keySet()) {
			if (valueOf(key) == pos) {
				return key;
			}
		}
		
		return null;
	}
	
	protected int valueOf(E entry) {
		return entry.ordinal();
	}
	
	public int positionOf(E entry) {
		return valueOf(entry);
	}
	
	@Override
	public String getName() {
		return hasCustomName() ? customName : defaultName;
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
		return inv.size();
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return inv.get(valueOf(index));
	}

	public ItemStack getStackInSlot(E key) {
		return inv.get(key);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		//we know we have max size 1, so just remove it
		return removeStackFromSlot(index);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		ItemStack ret = getStackInSlot(index);
		setInventorySlotContents(index, null);
		
		markDirty();
		
		return ret;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		inv.put(valueOf(index), stack);
		markDirty();
	}

	public void setInventorySlotContents(E key, ItemStack stack) {
		inv.put(key, stack);
		markDirty();
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public void markDirty() {
		
		for (E key : inv.keySet()) {
			if (inv.get(key) != null && inv.get(key).stackSize <= 0)
				inv.put(key, null);
		}
		
//		for (int i = 0; i < inv.length; i++)
//		if (inv[i] != null && inv[i].stackSize <= 0)
//			inv[i] = null;
		
		writeToNBT(baseStack.getTagCompound());
	}

	private void writeToNBT(NBTTagCompound tagCompound) {
		//create base compount
		NBTTagCompound items = new NBTTagCompound();
		
		for (E key : inv.keySet()) {
			if (inv.get(key) != null) {
				NBTTagCompound item = new NBTTagCompound();
				
				inv.get(key).writeToNBT(item);
				
				items.setTag(key.name(), item);
			}
		}

//		for (Slot slot : Slot.values())
//		if (inv[slot.getInventoryPosition()] != null) {
//			NBTTagCompound item = new NBTTagCompound();
//			
//			inv[slot.getInventoryPosition()].writeToNBT(item);
//			
//			items.setTag(slot.getKey(), item);
//		}
		
		tagCompound.setTag(COMPONENT_LIST_KEY, items);
	}

	private void readFromNBT(NBTTagCompound tagCompound) {
		NBTTagCompound items = tagCompound.getCompoundTag(COMPONENT_LIST_KEY);

		
		/*
		 * for (E key : inv.keySet()) {
			if (inv.get(key) != null) {
				NBTTagCompound item = new NBTTagCompound();
				
				inv.get(key).writeToNBT(item);
				
				items.setTag(key.name(), item);
			}
		}
		 */
		
		for (E key : inv.keySet()) {
			if (items.hasKey(key.name(), NBT.TAG_COMPOUND)) {
				inv.put(key, ItemStack.loadItemStackFromNBT(
						items.getCompoundTag(key.name())
						));
			}
		}
		
		
//		for (Slot slot : Slot.values())
//		if (items.hasKey(slot.getKey(), NBT.TAG_COMPOUND)) {
//			//contains an item for this slot
//			inv[slot.getInventoryPosition()] = ItemStack.loadItemStackFromNBT(
//					items.getCompoundTag(slot.getKey()));
//		}
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
		
		E key = valueOf(index);
		
		if (key == null)
			return false; //not in a valid index
		
		//System.out.println("Testing for equals from slot (does this work??)");
		
		//TODO 
		return true;
		//return stack.getItem().equals(pieces.get(slot));
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
		for (E key : inv.keySet())
			inv.put(key, null);
	}
	
}
