package com.SkyIsland.Armory.items.weapons;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.SkyIsland.Armory.items.common.NestedSlotInventory;

import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;

public abstract class ASword extends Weapon {
	
	public static enum Slot {
		BLADE,
		CROSSBAR,
		HANDLE,
		POMMEL;
	}
	
	public static NestedSlotInventory<Slot> makeWrapper(ItemStack base) {
		return new NestedSlotInventory<Slot>(Slot.class, base);
	}

    public ASword(String unlocalizedName) {
        super(unlocalizedName);
        
        //forge setup
        this.maxStackSize = 1;

        /////////////param setup

        canBlock = true;
        blockReduction = 0.2f;
        
    }

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BLOCK;
	}

	@Override
	public int getTotalEnchantability() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public Collection<ItemStack> getWeaponComponents(ItemStack weapon) {
		NestedSlotInventory<Slot> components = makeWrapper(weapon);
		List<ItemStack> parts = new LinkedList<ItemStack>();
		
		for (Slot slot : Slot.values()) {
			ItemStack item = components.getStackInSlot(slot);
			if (item != null)
				parts.add(item);
		}
		
		return parts;
	}
	
}
