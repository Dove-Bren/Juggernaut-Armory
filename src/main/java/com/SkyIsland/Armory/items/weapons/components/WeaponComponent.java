package com.SkyIsland.Armory.items.weapons.components;

import com.SkyIsland.Armory.Armory;
import com.SkyIsland.Armory.items.armor.ArmorPiece;
import com.SkyIsland.Armory.items.common.AComponent;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class WeaponComponent extends AComponent {
	
	protected WeaponComponent() {
		super();
	}

	@SideOnly(Side.CLIENT)
	public ModelResourceLocation constructModelLocation(ItemStack stack, String variant) {
		if (stack == null || !(stack.getItem() instanceof ArmorPiece))
			return null;
		
		String texturePrefix = getTexturePrefix(stack);//getUnderlyingMaterial(stack);
		return new ModelResourceLocation(Armory.MODID + ":" + texturePrefix + "_" + getModelSuffix(), variant);
	}
	
}
