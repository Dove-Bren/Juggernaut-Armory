package com.SkyIsland.Armory.items.weapons;

import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;

public abstract class ASword extends Weapon {
	
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
	
}
