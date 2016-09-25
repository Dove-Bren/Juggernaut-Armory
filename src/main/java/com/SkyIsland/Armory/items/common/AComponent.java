package com.SkyIsland.Armory.items.common;

import java.util.LinkedList;
import java.util.List;

import com.SkyIsland.Armory.Armory;
import com.SkyIsland.Armory.items.ModelRegistry;
import com.SkyIsland.Armory.items.armor.ExtendedMaterial;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class AComponent extends Item {
	
	private static final String INTERNAL_MATNAME = "component_material";
	private static List<AComponent> registeredComponents = new LinkedList<AComponent>();

	protected AComponent() {
		//Armory.proxy.registerComponent(this);
		registeredComponents.add(this);
	}
	
	public static final void registerComponents() {
		for (AComponent comp : registeredComponents)
			Armory.proxy.registerComponent(comp);
	}
	
	@SideOnly(Side.CLIENT)
	public void clientInit() {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
    	.register(this, 0, new ModelResourceLocation(Armory.MODID + ":" + getModelSuffix(), "inventory"));

//		Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
//    	.register(this, 0, new ModelResourceLocation(Armory.MODID + ":" + getModelSuffix(), "body"));
		
		ComponentSmartModel model = new ComponentSmartModel(this, 
				Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
				.getModelManager().getModel(new ModelResourceLocation(Armory.MODID + ":" + getModelSuffix(), "inventory"))
				, 0.5f);
		
		ModelRegistry.instance.registerComponent(this, getModelSuffix(), model);
	}
	
	public abstract String getModelSuffix();
	
	@SideOnly(Side.CLIENT)
	public abstract ModelResourceLocation constructModelLocation(ItemStack stack, String variant);
	
	/**
	 * Returns nbt-stored material name. This is NOT the texture prefix!
	 * To get the texture prefix, use #getTexturePrefix
	 * @param stack
	 * @return the material name, or "" if no name was found
	 */
	public String getUnderlyingMaterial(ItemStack stack) {
		if (!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt.hasKey(INTERNAL_MATNAME, NBT.TAG_STRING))
			return nbt.getString(INTERNAL_MATNAME);
		
		return "";
	}
	
	public ExtendedMaterial fetchMaterial(ItemStack stack) {
		String materialName = getUnderlyingMaterial(stack);
		if (materialName == null || materialName.isEmpty())
			return null;
		
		return ExtendedMaterial.lookupMaterial(materialName);
	}
	
	public String getTexturePrefix(ItemStack stack) {
		ExtendedMaterial material = fetchMaterial(stack);
		if (material == null)
			return "";
		
		return material.getTexturePrefix();
	}
	
	/**
	 * sets the nbt-stored max damage this weapon has
	 * @param stack
	 * @param damage the amount of damage this weapon has
	 */
	protected void setUnderlyingMaterial(ItemStack stack, String materialName) {
		if (!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		
		NBTTagCompound nbt = stack.getTagCompound();
		nbt.setString(INTERNAL_MATNAME, materialName);
	}
	
}
