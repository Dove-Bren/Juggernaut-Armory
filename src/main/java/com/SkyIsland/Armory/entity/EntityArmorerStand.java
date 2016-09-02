package com.SkyIsland.Armory.entity;

import com.SkyIsland.Armory.Armory;

import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class EntityArmorerStand extends EntityArmorStand {

	public EntityArmorerStand(World worldIn, double posX, double posY, double posZ) {
		super(worldIn, posX, posY, posZ);
	}
	
	public EntityArmorerStand(World worldIn)
    {
        super(worldIn);
    }
	
	public static void init() {
		EntityRegistry.registerModEntity(EntityArmorerStand.class, "armorer_stand",
				Armory.genModID(), Armory.instance, 16, 3, false);
	}

	@Override
	public boolean interactAt(EntityPlayer player, Vec3 targetVec3) {
		//regardless of vector, open gui
		if (player.getHeldItem() == null)
			return false;
		
		return player.getHeldItem().interactWithEntity(player, this);
		
    }
	
}
