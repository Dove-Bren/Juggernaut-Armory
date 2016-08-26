package com.SkyIsland.Armory.mechanics;

import java.util.EnumMap;
import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.common.util.Constants.NBT;

public class ExtendedArmor implements IExtendedEntityProperties {

	private static final String PROP_KEY = "ARMOR_EXTENDED";
	
	private final EntityLivingBase entity;
	
	private Map<DamageType, Float> armorMap;
	
	private ExtendedArmor(EntityLivingBase entity) {
		this(entity, 0.0f, 0.0f, 0.0f);
	}
	
	private ExtendedArmor(EntityLivingBase entity, float slash, float pierce, float crush) {
		this.entity = entity;
		armorMap = new EnumMap<DamageType, Float>(DamageType.class);
		
		armorMap.put(DamageType.SLASH, slash);
		armorMap.put(DamageType.PIERCE, pierce);
		armorMap.put(DamageType.CRUSH, crush);
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
	* Returns ExtendedArmor properties for entity
	* This method is for convenience only; it will make your code look nicer
	*/
	public static final ExtendedArmor get(EntityLivingBase entity)
	{
		return (ExtendedArmor) entity.getExtendedProperties(PROP_KEY);
	}
	
	public float getProtection(DamageType type) {
		return armorMap.get(type);
	}

}
