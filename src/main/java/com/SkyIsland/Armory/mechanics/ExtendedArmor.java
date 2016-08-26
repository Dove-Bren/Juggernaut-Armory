package com.SkyIsland.Armory.mechanics;

import java.util.EnumMap;
import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.common.util.Constants.NBT;

public class ExtendedArmor implements IExtendedEntityProperties {

	private static final String PROP_KEY = "ARMOR_EXTENDED";
	
	private final EntityLivingBase entity;
	
	private Map<DamageType, Float> armorMap;
	
	private ExtendedArmor(EntityLivingBase entity) {
		this.entity = entity;
		armorMap = new EnumMap<DamageType, Float>(DamageType.class);
		
		for (DamageType key : DamageType.values())
			armorMap.put(key, 0.0f);
	}
	
	@Override
	public void saveNBTData(NBTTagCompound compound) {
		NBTTagCompound properties = new NBTTagCompound();

		for (DamageType key : armorMap.keySet()) {
			properties.setFloat(key.name(), armorMap.get(key));
		}

		compound.setTag(PROP_KEY, properties);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		if (!compound.hasKey(PROP_KEY, NBT.TAG_COMPOUND))
			return;
		
		NBTTagCompound properties = (NBTTagCompound) compound.getTag(PROP_KEY);

		for (DamageType key : armorMap.keySet()) {
			if (properties.hasKey(key.name(), NBT.TAG_FLOAT))
				armorMap.put(key, (float) properties.getFloat(key.name()));
		}
	}

	@Override
	public void init(Entity entity, World world) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	* Used to register these extended properties for the entity during EntityConstructing event
	* This method is for convenience only; it will make your code look nicer
	* !!Taken from http://www.minecraftforum.net/forums/mapping-and-modding/mapping-and-modding-tutorials/1571567-forge-1-6-4-1-8-eventhandler-and
	*/
	public static final void register(EntityLivingBase entity)
	{
		entity.registerExtendedProperties(ExtendedArmor.PROP_KEY, new ExtendedArmor(entity));
	}
	
	/**
	* Returns ExtendedArmor properties for entity.
	* If the entity doesn't have defined armor, a default is created with 0's for
	* protection values
	* @see #ExtendedArmor(EntityLivingBase)
	*/
	public static final ExtendedArmor get(EntityLivingBase entity)
	{
		IExtendedEntityProperties prop = entity.getExtendedProperties(PROP_KEY);
		if (prop == null)
			prop = new ExtendedArmor(entity);
		return (ExtendedArmor) (prop); 
	}
	
	/**
	 * Returns the protection to the given type.
	 * @param type Type of damage to get protection for
	 * @return the protection. If type is null, returns 0.0
	 */
	public float getProtection(DamageType type) {
		if (type == null)
			return 0.0f;
		
		refresh();
		
		System.out.println("Protection: " + armorMap.get(type));
		return armorMap.get(type);
	}
	
	public void refresh() {
		//reset armor map
		for (DamageType key : DamageType.values())
			armorMap.put(key, 0.0f);
		
		//go through equip'ed items and update protection values
		for (int i = 0; i < 4; i++) {
			ItemStack equip = entity.getEquipmentInSlot(i + 1);
			if (equip == null)
				continue;
			
			Map<DamageType, Float> protection = ArmorUtils.getValues(equip);
			for (DamageType key : DamageType.values())
				armorMap.put(key, armorMap.get(key) + protection.get(key));
		}
	}
	
	public static void refresh(EntityLivingBase entity) {
		ExtendedArmor instance = get(entity);
		instance.refresh();
	}

}
