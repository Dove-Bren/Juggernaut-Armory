package com.SkyIsland.Armory.items.weapons;

import com.SkyIsland.Armory.Armory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ASword extends Weapon {

	//gonna be abstract. just wanna figure out sword stuff for now
	
	public static ASword item;
	
	private static String name;
	
    public ASword(String unlocalizedName) {
        super();
        item = this;
        this.setUnlocalizedName(Armory.MODID + "_" + unlocalizedName);
        //this.setTextureName(Armory.MODID + ":" + unlocalizedName);
        
        this.maxStackSize = 1;
        
        name = unlocalizedName;
    }
    
    public static void clientInit() {
    	Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
    	.register(item, 0, new ModelResourceLocation(Armory.MODID + ":" + name, "inventory"));
    }

//    @Override
//    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List<String> list, boolean bool) {
//        list.add("Dynamic b");
//        list.add("its user to zip around the world as if");
//        list.add("they were flying. Requires CR2 as fuel.");
//    }

//    @Override
//    public boolean onItemUse(ItemStack tool,
//			EntityPlayer player, World world, int x, int y,
//			int z, int par7, float xFloat, float yFloat, float zFloat) {
//    	if (!world.isRemote)
//    	if (world.getBlock(x, y, z) == HeartBlock.block)
//    	if (player.isSneaking()) {
//    		world.setBlockToAir(x, y, z);
//    		HeartBlock.block.dropBlockAsItem(world, x, y, z, 0, 0);
//        	return true;
//    	}
//    	
//    	return false;
//    	
//    }
    
    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {

//    	if (player.isSneaking()) {
//    		return stack;
//    	}
//    	
//    	if (stack.getItemDamage() >= maxUses) {
//    		if (player.inventory.consumeInventoryItem(Armory.cr2)) {
//    			stack.setItemDamage(0);
//    		} else {
//    			//couldn't refil, don't fling
//    			return stack;
//    		}
//    	}
//    	
//    	stack.setItemDamage(stack.getItemDamage() + 1);
//    	
//        if (world.isRemote) {
//            Vec3 vec3 = Vec3.createVectorHelper(player.posX, player.posY, player.posZ);
//            Vec3 vec3a = player.getLook(1.0F);
//	        Vec3 vec3b = vec3.addVector(vec3a.xCoord * 32, vec3a.yCoord * 32, vec3a.zCoord * 32);
//	
//	        MovingObjectPosition mop = world.rayTraceBlocks(vec3, vec3b);
//	        
//	        if (mop != null && (mop.typeOfHit == MovingObjectType.BLOCK
//	        		|| mop.typeOfHit == MovingObjectType.ENTITY)) {
//		        Vec3 vec = player.getLookVec();
//		        double wantedH = 0.5D;
//		        double wantedY = 0.7D;
//		        
//		        player.addVelocity(vec.xCoord * wantedH, vec.yCoord * wantedY, vec.zCoord * wantedH);
//		        player.velocityChanged = true;
//		        
//	        }
//	        return stack;
//        } else {
//        
//	        //Vec3 vec3 = player.getPosition(0F);
//	        
//	        
//	        return stack;
//        }
    	
    	return stack;
    }

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTotalEnchantability() {
		// TODO Auto-generated method stub
		return 1;
	}
	
}
