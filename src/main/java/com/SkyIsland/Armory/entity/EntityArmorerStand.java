package com.SkyIsland.Armory.entity;

import java.util.EnumMap;
import java.util.Map;

import com.SkyIsland.Armory.Armory;
import com.SkyIsland.Armory.items.armor.Armor;
import com.SkyIsland.Armory.items.armor.ArmorSlot;

import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class EntityArmorerStand extends EntityArmorStand {

	private Map<ArmorSlot, ItemStack> armors;
	
	public EntityArmorerStand(World worldIn, double posX, double posY, double posZ) {
		super(worldIn, posX, posY, posZ);
		armors = new EnumMap<ArmorSlot, ItemStack>(ArmorSlot.class);
	}
	
	public EntityArmorerStand(World worldIn) {
        super(worldIn);
        armors = new EnumMap<ArmorSlot, ItemStack>(ArmorSlot.class);
    }
	
	public static void init() {
		EntityRegistry.registerModEntity(EntityArmorerStand.class, "armorer_stand",
				Armory.genModID(), Armory.instance, 16, 3, false);
	}

	@Override
	public boolean interactAt(EntityPlayer player, Vec3 targetVec3) {
		//regardless of vector, open gui
		System.out.println("click");
		if (player.getHeldItem() == null || !(player.getHeldItem().getItem() instanceof Armor)) {
			if (player.isSneaking()) {
				System.out.println("sneaking");
				return false;
			}
			
			System.out.println("opengui");
			player.openGui(Armory.instance, Armory.Gui_Type.ARMORY_STAND.ordinal(), worldObj, (int) posX, (int) posY, (int) posZ);
			return true;
		}
		
		ItemStack held = player.getHeldItem();
		Armor inst = (Armor) held.getItem();
		setArmor(inst.getSlot(), held);
		
		if (player.isSneaking())
			return true;
		
		player.openGui(Armory.instance, Armory.Gui_Type.ARMORY_STAND.ordinal(), worldObj, (int) posX, (int) posY, (int) posZ);		
		return true;
    }
	
	/**
	 * Sets the armor in the slot.
	 * Performs NO checks on type. Make certain the stack is a piece of
	 * armor, or you'll get nasty cast errors
	 * @param slot
	 * @param stack
	 */
	private void setArmor(ArmorSlot slot, ItemStack stack) {
		armors.put(slot, stack);
		
		this.setCurrentItemOrArmor(slot.getPlayerSlot() + 1, stack);
	}
	
	public ItemStack[] getArmors() {
		return armors.values().toArray(new ItemStack[0]);
	}
	
}
