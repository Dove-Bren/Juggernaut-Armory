package com.SkyIsland.Armory.items.armor;

import java.util.Map;
import java.util.UUID;

import com.SkyIsland.Armory.Armory;
import com.SkyIsland.Armory.items.common.AComponent;
import com.SkyIsland.Armory.items.common.ExtendedMaterial;
import com.SkyIsland.Armory.mechanics.DamageType;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ArmorPiece extends AComponent {
	
	//protected Map<DamageType, Float> protectionMap;
	//stored in nbt, not on item
	
	protected String itemKey;
	
	private UUID uniqueKey;
	
	/**
	 * How much of the overall armor material protection this
	 * piece gets, for each given damage type 
	 */
	protected Map<DamageType, Float> protectionRatios;
	
	/**
	 * Slot this piece resides in, used for rate lookups
	 */
	protected ArmorSlot parentSlot;
	
	protected float durabilityRate;
	
	protected float xOffset;
	
	protected float yOffset;
	
	protected float zOffset;

	/**
	 * Creates and <i><b>registers</b><i> a new armor piece. This is
	 * only intended to be called once per slot for each piece
	 * of complex armor.
	 * @param itemKey
	 * @param parentSlot
	 * @param ratios
	 * @param durabilityRate
	 */
	public ArmorPiece(String itemKey, ArmorSlot parentSlot, Map<DamageType, Float> ratios, float durabilityRate) {
		super();
		this.itemKey = itemKey;
		this.protectionRatios = ratios;
		this.parentSlot = parentSlot;
		this.durabilityRate = durabilityRate;
//		protectionMap = new EnumMap<DamageType, Float>(DamageType.class);
//		for (DamageType key : DamageType.values())
//			protectionMap.put(key, 0.0f);
		
		uniqueKey = UUID.randomUUID();
		
		this.maxStackSize = 1;
		this.setMaxDamage(100);
		//this.setCreativeTab(Armory.creativeTab);
		this.canRepair = false;
		this.setUnlocalizedName("armorpiece_" + itemKey);
	}
	
//	@Override
//	public ModelResourceLocation getModel(ItemStack stack, EntityPlayer player, int useRemaining) {
//		return this.constructModelLocation(stack, "body");
//	}
	
	public String getModelSuffix() {
		return this.itemKey;
	}
	
	public float getXOffset() {
		return xOffset;
	}
	
	public float getYOffset() {
		return yOffset;
	}
	
	public float getZOffset() {
		return zOffset;
	}
	
	public void setxOffset(float xOffset) {
		this.xOffset = xOffset;
	}

	public void setyOffset(float yOffset) {
		this.yOffset = yOffset;
	}

	public void setzOffset(float zOffset) {
		this.zOffset = zOffset;
	}

	@SideOnly(Side.CLIENT)
	public ModelResourceLocation constructModelLocation(ItemStack stack, String variant) {
		if (stack == null || !(stack.getItem() instanceof ArmorPiece))
			return null;
		
		String texturePrefix = getTexturePrefix(stack);//getUnderlyingMaterial(stack);
		return new ModelResourceLocation(Armory.MODID + ":" + texturePrefix + "_" + getModelSuffix(), variant);
	}
	
	public ItemStack constructPiece(ArmorMaterial material) {
		return constructPiece(ExtendedMaterial.wrap(material));
	}
	
	public ItemStack constructPiece(ExtendedMaterial material) {
		return constructPiece(material, 1.0f);
	}
	
	public ItemStack constructPiece(ExtendedMaterial material, float performance) {
		if (material == null) {
			//no material to build part from. return null
			return null;
		}
		Map<DamageType, Float> materialValues = material.getDamageReductionAmount(parentSlot);
		
		ItemStack stack = new ItemStack(this);
		stack.setStackDisplayName(material.getName() + " " + stack.getDisplayName());
		
		Map<DamageType, Float> outMap = DamageType.freshMap();
		for (DamageType type : DamageType.values()) {
			outMap.put(type, 
					(materialValues.get(type) == null ? 0.0f : materialValues.get(type))
				  * (protectionRatios.get(type) == null ? 0.0f : protectionRatios.get(type))
				  * performance //performance is how well it was made. 1.0f is best
					);
		}
		setProtection(stack, outMap);
		
		//SET MAX DAMAGE and CUR DAMAGE
		setUnderlyingDamage(stack, 0);
		setUnderlyingMaxDamage(stack, material.getDurability(durabilityRate));
		
		//Set up material name
		setUnderlyingMaterial(stack, material.getName());
					
		return stack;
	}
	
//	@Override
//	public ModelResourceLocation getModel(ItemStack stack, EntityPlayer player, int ticksRemaining) {
//		//fetch model for this piece, as per 
//		//return ModelRegistry.instance.getModel(Armory.MODID, fetchMaterial(stack).getTexturePrefix() + "_" + itemKey);
//		ModelRegistry mr = ModelRegistry.instance;
//		String prefix = fetchMaterial(stack).getTexturePrefix();
//		return mr.getModel(Armory.MODID, prefix + "_" + itemKey);
//	}
	
	public float getProtection(ItemStack stack, DamageType type) {
		if (!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		
		NBTTagCompound nbt = stack.getTagCompound();
		
		float protection = 0.0f;
		if (nbt.hasKey(type.nbtKey(), NBT.TAG_FLOAT))
			protection = nbt.getFloat(type.nbtKey());
		
		return protection;
	}
	
	private void setProtection(ItemStack stack, Map<DamageType, Float> map) {
		if (!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		
		NBTTagCompound nbt = stack.getTagCompound();
		
		for (DamageType type : DamageType.values()) {
			if (map.containsKey(type) && map.get(type) != null)
				nbt.setFloat(type.nbtKey(), map.get(type));
			else
				nbt.setFloat(type.nbtKey(), 0.0f);
		}
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		
		if (o instanceof ArmorPiece)
			return ((ArmorPiece) o).uniqueKey.equals(uniqueKey);
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return 1793
				+ uniqueKey.hashCode();
	}
	
}
