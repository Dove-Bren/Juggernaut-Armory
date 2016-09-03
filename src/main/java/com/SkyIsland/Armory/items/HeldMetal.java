package com.SkyIsland.Armory.items;

import com.SkyIsland.Armory.Armory;
import com.SkyIsland.Armory.config.ModConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants.NBT;

/**
 * Metal held in tongs. Should not exist as an itemstack outside of in the
 * context of a pair of tongs.
 * @author Skyler
 *
 */
public class HeldMetal extends ItemBase {

	private static final String NBT_HEAT = "heat";
	
	private static final String NBT_METALS = "metals";
	
	private String registryName;
	
	public HeldMetal(String unlocalizedName) {
		super();
		
		registryName = unlocalizedName;
		
		this.setMaxStackSize(1);
		this.setUnlocalizedName(unlocalizedName);
		
	}

	@Override
	public boolean isDamageable() {
		return false;
	}
	
	@Override
	public void init() {
		;
	}
	
	public void clientInit() {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
    	.register(this, 0, new ModelResourceLocation(Armory.MODID + ":" + this.registryName, "inventory"));
	}
	
	@Override
	public int getColorFromItemStack(ItemStack stack, int tintIndex) {
		
		return 0;
	}
	
	///////////////NBT//////////////
	
	/**
	 * Fetches the heat for the given piece of metal.
	 * @param metal
	 * @return the heat, or -1 if the itemstack is invalid or there is no set heat
	 */
	public int getHeat(ItemStack metal) {
		if (metal == null || !(metal.getItem() instanceof HeldMetal))
			return -1;
		
		if (!metal.hasTagCompound())
			metal.setTagCompound(new NBTTagCompound());
		NBTTagCompound nbt = metal.getTagCompound();
		
		if (nbt.hasKey(NBT_HEAT, NBT.TAG_INT))
			return nbt.getInteger(NBT_HEAT);
		
		return -1;
	}
	
	public void setHeat(ItemStack metal, int heat) {
		if (metal == null || !(metal.getItem() instanceof HeldMetal))
			return;
		
		if (!metal.hasTagCompound())
			metal.setTagCompound(new NBTTagCompound());
		NBTTagCompound nbt = metal.getTagCompound();
		
		nbt.setInteger(NBT_HEAT, heat);
		
		updateHeat(metal);
	}
	
	protected void updateHeat(ItemStack metal) {
		if (getHeat(metal) < ModConfig.config.getMinimumHeat()) {
			
		}
	}
	
}
