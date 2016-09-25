package com.SkyIsland.Armory.items.weapons.components;

import com.SkyIsland.Armory.Armory;
import com.SkyIsland.Armory.items.armor.ArmorPiece;
import com.SkyIsland.Armory.items.common.AComponent;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WeaponComponent extends AComponent {
	
	protected String modelSuffix;
	
	protected WeaponComponent(String modelSuffix) {
		super();
		this.modelSuffix = modelSuffix;

		this.maxStackSize = 1;
		this.setMaxDamage(100);
		this.setCreativeTab(Armory.creativeTab);
		this.canRepair = false;
		this.setUnlocalizedName("weaponcomp_" + modelSuffix);
	}

	@SideOnly(Side.CLIENT)
	public ModelResourceLocation constructModelLocation(ItemStack stack, String variant) {
		if (stack == null || !(stack.getItem() instanceof ArmorPiece))
			return null;
		
		String texturePrefix = getTexturePrefix(stack);//getUnderlyingMaterial(stack);
		return new ModelResourceLocation(Armory.MODID + ":" + texturePrefix + "_" + getModelSuffix(), variant);
	}

	@Override
	public String getModelSuffix() {
		return modelSuffix;
	}
	
}
