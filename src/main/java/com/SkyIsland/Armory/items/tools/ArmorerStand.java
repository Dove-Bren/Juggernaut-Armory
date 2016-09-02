package com.SkyIsland.Armory.items.tools;

import java.util.List;
import java.util.Random;

import com.SkyIsland.Armory.Armory;
import com.SkyIsland.Armory.entity.EntityArmorerStand;
import com.SkyIsland.Armory.items.ItemBase;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Rotations;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ArmorerStand extends ItemBase {

	private String registryName;
	
	public ArmorerStand(String unlocalizedName) {
		super();
		
		registryName = unlocalizedName;
		
		this.setMaxStackSize(16);
		this.setUnlocalizedName(unlocalizedName);
		this.setCreativeTab(Armory.creativeTab);
	}

	@Override
	public boolean isDamageable() {
		return false;
	}
	
	@Override
	public void init() {
		GameRegistry.addShapedRecipe(new ItemStack(this), new Object[]{" # ", "#T#", '#', Items.iron_ingot, 'T', Items.armor_stand});
	}
	
	public void clientInit() {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
    	.register(this, 0, new ModelResourceLocation(Armory.MODID + ":" + this.registryName, "inventory"));
	}
	
	/**
     * Returns true if the item can be used on the given entity, e.g. shears on sheep.
     */
//    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target) {
//        if (target instanceof EntityArmorStand) {
//        	System.out.println("armor stand!");
//        	
//        	//is armor stand wearing any customizable armor pieces?
//        	EntityArmorStand armorStand = (EntityArmorStand) target;
//        	
//        	ItemStack head, torso, legs, feet;
//        	head = armorStand.getEquipmentInSlot(ArmorSlot.HELMET.getPlayerSlot());
//        	torso = armorStand.getEquipmentInSlot(ArmorSlot.TORSO.getPlayerSlot());
//        	legs = armorStand.getEquipmentInSlot(ArmorSlot.LEGS.getPlayerSlot());
//        	feet = armorStand.getEquipmentInSlot(ArmorSlot.FEET.getPlayerSlot());
//        	
//        	head = (head.getItem() instanceof Armor ? head : null);
//        	torso = (torso.getItem() instanceof Armor ? torso : null);
//        	legs = (legs.getItem() instanceof Armor ? legs : null);
//        	feet = (legs.getItem() instanceof Armor ? legs : null);
//        	
//        	if ((head == null) && (torso == null) && (legs == null) && (feet == null)) {
//            	System.out.println("everything's null");
//        		return false;
//        	}
//        	
//        	popupGui(playerIn, head, torso, legs, feet);
//        } else {
//        	System.out.println(target);
//        }
//    	
//        return false;
//    }
    
    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
    	if (side == EnumFacing.DOWN)
        {
            return false;
        }
        else
        {
            boolean flag = worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos);
            BlockPos blockpos = flag ? pos : pos.offset(side);

            if (!playerIn.canPlayerEdit(blockpos, side, stack))
            {
                return false;
            }
            else
            {
                BlockPos blockpos1 = blockpos.up();
                boolean flag1 = !worldIn.isAirBlock(blockpos) && !worldIn.getBlockState(blockpos).getBlock().isReplaceable(worldIn, blockpos);
                flag1 = flag1 | (!worldIn.isAirBlock(blockpos1) && !worldIn.getBlockState(blockpos1).getBlock().isReplaceable(worldIn, blockpos1));

                if (flag1)
                {
                    return false;
                }
                else
                {
                    double d0 = (double)blockpos.getX();
                    double d1 = (double)blockpos.getY();
                    double d2 = (double)blockpos.getZ();
                    List<Entity> list = worldIn.getEntitiesWithinAABBExcludingEntity((Entity)null, AxisAlignedBB.fromBounds(d0, d1, d2, d0 + 1.0D, d1 + 2.0D, d2 + 1.0D));

                    if (list.size() > 0)
                    {
                        return false;
                    }
                    else
                    {
                        if (!worldIn.isRemote)
                        {
                            worldIn.setBlockToAir(blockpos);
                            worldIn.setBlockToAir(blockpos1);
                            EntityArmorerStand entityarmorstand = new EntityArmorerStand(worldIn, d0 + 0.5D, d1, d2 + 0.5D);
                            float f = (float)MathHelper.floor_float((MathHelper.wrapAngleTo180_float(playerIn.rotationYaw - 180.0F) + 22.5F) / 45.0F) * 45.0F;
                            entityarmorstand.setLocationAndAngles(d0 + 0.5D, d1, d2 + 0.5D, f, 0.0F);
                            this.applyRandomRotations(entityarmorstand, worldIn.rand);
                            NBTTagCompound nbttagcompound = stack.getTagCompound();

                            if (nbttagcompound != null && nbttagcompound.hasKey("EntityTag", 10))
                            {
                                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                                entityarmorstand.writeToNBTOptional(nbttagcompound1);
                                nbttagcompound1.merge(nbttagcompound.getCompoundTag("EntityTag"));
                                entityarmorstand.readFromNBT(nbttagcompound1);
                            }

                            worldIn.spawnEntityInWorld(entityarmorstand);
                        }

                        --stack.stackSize;
                        return true;
                    }
                }
            }
        }
    	
    	
    } 
	
    /**
     * Took from vanilla ItemArmorStand
     * @param armorStand
     * @param rand
     */
    private void applyRandomRotations(EntityArmorStand armorStand, Random rand)
    {
        Rotations rotations = armorStand.getHeadRotation();
        float f = rand.nextFloat() * 5.0F;
        float f1 = rand.nextFloat() * 20.0F - 10.0F;
        Rotations rotations1 = new Rotations(rotations.getX() + f, rotations.getY() + f1, rotations.getZ());
        armorStand.setHeadRotation(rotations1);
        rotations = armorStand.getBodyRotation();
        f = rand.nextFloat() * 10.0F - 5.0F;
        rotations1 = new Rotations(rotations.getX(), rotations.getY() + f, rotations.getZ());
        armorStand.setBodyRotation(rotations1);
    }
}
