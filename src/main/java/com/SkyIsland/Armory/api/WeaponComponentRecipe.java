package com.SkyIsland.Armory.api;

import com.SkyIsland.Armory.api.ForgeManager.MetalRecord;
import com.SkyIsland.Armory.items.weapons.components.WeaponComponent;

import net.minecraft.item.ItemStack;

public class WeaponComponentRecipe implements IForgeTemplate {

	public WeaponComponent piece;
	
	public WeaponComponentRecipe(WeaponComponent piece) {
		this.piece = piece;
	}
	
	@Override
	public ItemStack produce(MetalRecord metalRecord, float performance) {
		return piece.constructPiece(metalRecord.getMaterial(), performance);
	}

}
