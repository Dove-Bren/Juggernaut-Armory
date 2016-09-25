package com.SkyIsland.Armory.items.weapons.components;

import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class WeaponComponent extends Item {
	
	/**
	 * Return the model to be used for this piece. The model
	 * is expected to already render at the correct offset. That's on you.
	 * @return
	 */
	@SideOnly(Side.CLIENT)
	public abstract IBakedModel getPieceModel();
	
}
