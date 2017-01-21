package com.SkyIsland.Armory.items.weapons.components;

import java.util.Map;

import com.SkyIsland.Armory.Armory;
import com.SkyIsland.Armory.items.common.AComponent;
import com.SkyIsland.Armory.items.common.ExtendedMaterial;
import com.SkyIsland.Armory.mechanics.DamageType;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WeaponComponent extends AComponent {
	
	private static final String NBT_DAMAGE_PREFIX = "damageValues";
	private static final String NBT_COMP_ID = "wcomp_id";
	
	protected String modelSuffix;
	protected Map<DamageType, Float> damageRatios;
	protected float durabilityRate;
	
	protected WeaponComponent(String modelSuffix, float durabilityRate) {
		this(modelSuffix, null, durabilityRate);
	}
	
	protected WeaponComponent(String modelSuffix, Map<DamageType, Float> damageRatios, float durabilityRate) {
		super();
		this.modelSuffix = modelSuffix;
		this.damageRatios = damageRatios;
		this.durabilityRate = durabilityRate;

		this.maxStackSize = 1;
		this.setMaxDamage(100);
		this.setCreativeTab(Armory.creativeTab);
		this.canRepair = false;
		this.setUnlocalizedName("weaponcomp_" + modelSuffix);
	}

	@SideOnly(Side.CLIENT)
	public ModelResourceLocation constructModelLocation(ItemStack stack, String variant) {
		if (stack == null || !(stack.getItem() instanceof WeaponComponent))
			return null;
		
		String texturePrefix = getTexturePrefix(stack);//getUnderlyingMaterial(stack);
		return new ModelResourceLocation(Armory.MODID + ":" + texturePrefix + "_" + getModelSuffix(), variant);
	}

	@Override
	public String getModelSuffix() {
		return modelSuffix;
	}
	
	/**
	 * Returns the nbt-stored max damage this weapon can have, if it exists
	 * @param stack
	 * @return the damage, or -1 if it can't be found
	 */
	public Map<DamageType, Float> getDamageValues(ItemStack stack) {
		if (!stack.hasTagCompound()
				|| !stack.getTagCompound().hasKey(NBT_DAMAGE_PREFIX, NBT.TAG_COMPOUND))
			return null;
		
		NBTTagCompound nbt = stack.getTagCompound()
				.getCompoundTag(NBT_DAMAGE_PREFIX);
		
		Map<DamageType, Float> map = DamageType.freshMap();
		
		for (DamageType damage : map.keySet())
		if (nbt.hasKey(damage.nbtKey(), NBT.TAG_FLOAT))
			map.put(damage, nbt.getFloat(damage.nbtKey()));
		
		return map;
	}
	
	/**
	 * sets the nbt-stored damage values this weapon has.
	 * @param stack
	 * @param damage the amount of damage this weapon has
	 */
	private void setDamageValues(ItemStack stack, Map<DamageType, Float> damages) {
		if (!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		
		NBTTagCompound nbt = stack.getTagCompound();
		
		NBTTagCompound tag = new NBTTagCompound();
		
		for (DamageType damage : damages.keySet())
			tag.setFloat(damage.nbtKey(), damages.get(damage));
		
		nbt.setTag(NBT_DAMAGE_PREFIX, tag);
	}
	
	public ItemStack constructPiece(ExtendedMaterial material, float performance) {
		if (material == null) {
			//no material to build part from. return null
			return null;
		}

		ItemStack stack = new ItemStack(this);
		stack.setStackDisplayName(material.getName() + " " + stack.getDisplayName());
		
		if (damageRatios != null) {
			Map<DamageType, Float> materialValues = material.getDamageMap();
			
			Map<DamageType, Float> outMap = DamageType.freshMap();
			for (DamageType type : DamageType.values()) {
				outMap.put(type, 
						(materialValues.get(type) == null ? 0.0f : materialValues.get(type))
					  * (damageRatios.get(type) == null ? 0.0f : damageRatios.get(type))
					  * performance //performance is how well it was made. 1.0f is best
						);
			}
			setDamageValues(stack, outMap);
		}
		
		//Set up material name
		setUnderlyingMaterial(stack, material.getName());
					
		return stack;
	}
	
	public float getDurabilityRate() {
		return this.durabilityRate;
	}
	
	/**
	 * Takes a weapon component, grabs its materials, and returns that
	 * multiplied by its durability rate. In other words, returns the
	 * durability this part contributes to a weapon.
	 * @param stack
	 * @return 0.0f if missing a material, the durability otherwise
	 */
	public float getFactoredDurability(ItemStack stack) {
		ExtendedMaterial material = this.fetchMaterial(stack);
		if (material == null)
			return 0.0f;
		
		return durabilityRate * material.getRawDurability();
	}
	
	public void stampCompID(ItemStack component) {
		NBTTagCompound nbt = component.getTagCompound();
		if (nbt == null) {
			nbt = new NBTTagCompound();
			component.setTagCompound(nbt);
		}
		
		nbt.setString(NBT_COMP_ID, modelSuffix);
		
		component.setTagCompound(nbt);
	}
	
	public String getCompID(ItemStack component) {
		NBTTagCompound nbt = component.getTagCompound();
		if (nbt == null) {
			nbt = new NBTTagCompound();
			component.setTagCompound(nbt);
		}
		
		return nbt.getString(NBT_COMP_ID);
	}
	
	/**
	 * Checks whether the given item is an instance of this same
	 * component.
	 * @return true if the item were that like the result of
	 * a {@link #constructPiece(ExtendedMaterial, float)} method call.
	 */
	public boolean matchesComponent(ItemStack component) {
		String id = getCompID(component);
		if (id == null || id.trim().isEmpty())
			return false;
		
		return id.equals(modelSuffix);
	}
	
}
