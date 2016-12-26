package com.SkyIsland.Armory.items.materials;

import com.SkyIsland.Armory.Armory;
import com.SkyIsland.Armory.items.ItemBase;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * More durable and advanced piece of leather. Material for crafting.
 * @author Skyler
 *
 */
public class StuddedLeather extends ItemBase {

	private String registryName;
	
	public StuddedLeather(String unlocalizedName) {
		super();
		
		registryName = unlocalizedName;
		setCreativeTab(Armory.creativeTab);
		
		this.setMaxStackSize(64);
		this.setUnlocalizedName(unlocalizedName);
	}

	@Override
	public boolean isDamageable() {
		return false;
	}
	
	@Override
	public void init() {
		GameRegistry.addShapelessRecipe(new ItemStack(this), Items.iron_ingot, Items.iron_ingot, Items.iron_ingot, Items.leather);
	}
	
	public void clientInit() {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
    	.register(this, 0, new ModelResourceLocation(Armory.MODID + ":" + this.registryName, "inventory"));
	}
}
