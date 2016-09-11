package com.SkyIsland.Armory.entity;

import java.util.EnumMap;
import java.util.Map;

import com.SkyIsland.Armory.Armory;
import com.SkyIsland.Armory.items.armor.Armor;
import com.SkyIsland.Armory.items.armor.ArmorSlot;

import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class EntityArmorerStand extends EntityArmorStand {

	private static final String ARMORS_KEY = "armors";
	
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
	public void writeEntityToNBT(NBTTagCompound tag) {
		super.writeEntityToNBT(tag);
		
		NBTTagCompound nbt = new NBTTagCompound();
		
		for (ArmorSlot slot : ArmorSlot.values()) {
			if (armors.get(slot) != null) {
				NBTTagCompound sub = new NBTTagCompound();
				armors.get(slot).writeToNBT(sub);
				nbt.setTag(slot.name(), sub);
			}
		}
		
		tag.setTag(ARMORS_KEY, nbt);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound tagCompund) {
		super.readEntityFromNBT(tagCompund);
		
		if (!tagCompund.hasKey(ARMORS_KEY, NBT.TAG_COMPOUND))
			return;
		
		NBTTagCompound nbt = tagCompund.getCompoundTag(ARMORS_KEY);
		for (ArmorSlot slot : ArmorSlot.values()) {
			if (nbt.hasKey(slot.name(), NBT.TAG_COMPOUND)) {
				NBTTagCompound sub = nbt.getCompoundTag(slot.name());
				armors.put(slot, ItemStack.loadItemStackFromNBT(sub));
			}
		}
		
//		//leech of armor pieces
//		for (ArmorSlot slot : ArmorSlot.values()) {
//			if (this.getEquipmentInSlot(slot.getPlayerSlot() + 1) != null)
//				armors.put(slot, getEquipmentInSlot(slot.getPlayerSlot() + 1));
//		}
	}

	@Override
	public boolean interactAt(EntityPlayer player, Vec3 targetVec3) {
		//regardless of vector, open gui
		if (player.getHeldItem() == null || !(player.getHeldItem().getItem() instanceof Armor)) {
			if (player.isSneaking()) {
				if (player.getHeldItem() == null) {
					//extract an item based on vector
					ArmorSlot slot = getSlotAtPos(targetVec3);
					if (slot == null)
						return false;
					
					ItemStack ret = armors.get(slot);
					setArmor(slot, null);
					player.inventory.setInventorySlotContents(player.inventory.currentItem, ret);
					return true;
				}
				
				return false;
			}
			
			player.openGui(Armory.instance, Armory.Gui_Type.ARMORY_STAND.ordinal(), worldObj, (int) posX, (int) posY, (int) posZ);
			return true;
		}
		
		ItemStack held = player.getHeldItem();
		Armor inst = (Armor) held.getItem();
		if (armors.get(inst.getSlot()) == null) {
			setArmor(inst.getSlot(), held);
			player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
		}
		
		if (player.isSneaking()) {
			return true;
		}
		
		player.openGui(Armory.instance, Armory.Gui_Type.ARMORY_STAND.ordinal(), worldObj, (int) posX, (int) posY, (int) posZ);		
		return true;
    }
	
	private ArmorSlot getSlotAtPos(Vec3 vec) {
		double d0 = 0.2;
        double d1 = 0.9;
        double d2 = 1.7;
        if (vec.yCoord < d0 && armors.get(ArmorSlot.FEET) != null)
        	return ArmorSlot.FEET;
        if (vec.yCoord < d1 && armors.get(ArmorSlot.LEGS) != null)
        	return ArmorSlot.LEGS;
        if (vec.yCoord < d2 && armors.get(ArmorSlot.TORSO) != null)
        	return ArmorSlot.TORSO;
        if (armors.get(ArmorSlot.HELMET) != null)
        	return ArmorSlot.HELMET;
        
        return null;
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
