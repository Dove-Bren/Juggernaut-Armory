package com.SkyIsland.Armory.items.common;

import java.util.LinkedList;
import java.util.List;

import com.SkyIsland.Armory.Armory;
import com.SkyIsland.Armory.items.ModelRegistry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class AComponent extends Item {
	
	private static final String INTERNAL_DAMAGE = "component_damage";
	private static final String INTERNAL_MAXDAMAGE = "component_maxdamage";
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
	
	@Override
	public void setDamage(ItemStack stack, int damage) {
		int change = stack.getItemDamage() - damage;
		
		//damage secret internal damage, and update
		//itemstack damage to reflect it
		int actual = getUnderlyingDamage(stack),
		    max = getUnderlyingMaxDamage(stack);
		if (actual == -1 || actual >= max) {
			//error, bug, glitch, etc OR it is now broken
			super.setDamage(stack, 101); //set damage over break point to break
			return;
		}
		
		actual += change;
		if (actual < 0)
			actual = 0;
		setUnderlyingDamage(stack, actual);
		
		//                              \/ 99 because it shouldn't be broken, and we don't want it to end up rounding down and causing it to break
		super.setDamage(stack, Math.min(99, Math.round((float) actual / (float) max)));
	}
	
	/**
	 * Returns the nbt-stored damage this weapon has, if it exists
	 * @param stack
	 * @return the damage, or -1 if it can't be found
	 */
	protected int getUnderlyingDamage(ItemStack stack) {
		if (!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt.hasKey(INTERNAL_DAMAGE, NBT.TAG_INT))
			return nbt.getInteger(INTERNAL_DAMAGE);
		
		return -1;
	}
	
	/**
	 * sets the nbt-stored damage this weapon has
	 * @param stack
	 * @param damage the amount of damage this weapon has
	 */
	protected void setUnderlyingDamage(ItemStack stack, int damage) {
		if (!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		
		NBTTagCompound nbt = stack.getTagCompound();
		nbt.setInteger(INTERNAL_DAMAGE, damage);
	}
	
	/**
	 * Returns the nbt-stored max damage this weapon can have, if it exists
	 * @param stack
	 * @return the damage, or -1 if it can't be found
	 */
	protected int getUnderlyingMaxDamage(ItemStack stack) {
		if (!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt.hasKey(INTERNAL_MAXDAMAGE, NBT.TAG_INT))
			return nbt.getInteger(INTERNAL_MAXDAMAGE);
		
		return -1;
	}
	
	/**
	 * sets the nbt-stored max damage this weapon has
	 * @param stack
	 * @param damage the amount of damage this weapon has
	 */
	protected void setUnderlyingMaxDamage(ItemStack stack, int damage) {
		if (!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		
		NBTTagCompound nbt = stack.getTagCompound();
		nbt.setInteger(INTERNAL_MAXDAMAGE, damage);
	}
	
}
