package com.SkyIsland.Armory.api;

import com.SkyIsland.Armory.api.ForgeManager.MetalRecord;
import com.SkyIsland.Armory.items.armor.ArmorPiece;

import net.minecraft.item.ItemStack;

public class ArmorPieceRecipe implements IForgeTemplate {

	public ArmorPiece piece;
	
	public ArmorPieceRecipe(ArmorPiece piece) {
		this.piece = piece;
	}
	
	@Override
	public ItemStack produce(MetalRecord metalRecord, float performance) {
		return piece.constructPiece(metalRecord.getMaterial(), performance);
	}

}
