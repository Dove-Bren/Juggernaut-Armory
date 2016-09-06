package com.SkyIsland.Armory.items.proxy;

import com.SkyIsland.Armory.items.HeldMetal;
import com.SkyIsland.Armory.items.MiscItems;
import com.SkyIsland.Armory.items.MiscItems.Items;
import com.SkyIsland.Armory.items.proxy.network.TongsCoolMessage;
import com.SkyIsland.Armory.items.tools.Tongs;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ServerTongProxy extends CommonTongProxy {

	public ServerTongProxy() {
		super();
	}
	
	@Override
	public void updateTongs(Tongs inst, ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		//actually update tongs.
		
		System.out.print(".");
		ItemStack held = inst.getHeldItem(stack);
    	if (held != null) {
    		held.getItem().onUpdate(held, worldIn, entityIn, itemSlot, isSelected);
    		
    		if (!(held.getItem() instanceof HeldMetal)) {
    			System.out.print("|");
    			
    			if (entityIn instanceof EntityPlayer) {
    				((EntityPlayer) entityIn).inventory.addItemStackToInventory(held);
    			} else {
    				worldIn.spawnEntityInWorld(new EntityItem(worldIn, entityIn.posX, entityIn.posY, entityIn.posZ, held));
    			}
    			
    			stack.setItemDamage(0);
    			held = null;
    			
    			channel.sendToAll(new TongsCoolMessage(
    					entityIn.getUniqueID(),
    					inst.getID(stack)
    					));
    		} else {
    			HeldMetal hm = (HeldMetal) MiscItems.getItem(Items.HELD_METAL);
    			System.out.println("heat: " + hm.getHeat(held));
    		}
    		inst.setHeldItem(stack, held);
    	}
	}
	
}
