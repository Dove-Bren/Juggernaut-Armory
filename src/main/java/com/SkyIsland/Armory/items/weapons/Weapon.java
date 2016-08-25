package com.SkyIsland.Armory.items.weapons;

import java.util.List;

import com.SkyIsland.Armory.Armory;
import com.SkyIsland.Armory.mechanics.DamageType;
import com.google.common.collect.Multimap;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Abstract weapon class. It could be a sword, it could be a mace. YOU'LL
 * NEVER KNOW!
 * @author Skyler
 *
 */
public abstract class Weapon extends Item {

	/**
	 * Damage inflicted per hit
	 */
	protected float attackDamage;
	
	/**
	 * Number of seconds it takes to swing and be able to swing again. 0.0f results
	 * in no change in swing speed
	 */
	protected float swingSpeed;
	
	protected boolean canBlock;
	
	/**
	 * Percentage of damage reduced. If a negative number, the damage is rounded
	 * to an integer and reduced as a constant
	 */
	protected float blockReduction;
	
	protected DamageType damageType;

    protected Weapon() {
    	this(1.0f, 1.0f, false, 0.0f, DamageType.SLASH);
    }
    
    protected Weapon(float attackDamage, float swingSpeed, boolean canBlock, float blockReduction, DamageType damageType) {
//        this.maxStackSize = 1;
//      this.setMaxDamage(material.getMaxUses());
//      this.setCreativeTab(CreativeTabs.tabCombat);
//      this.attackDamage = 4.0F + material.getDamageVsEntity();
        this.setCreativeTab(Armory.creativeTab);
        this.attackDamage = attackDamage;
        this.swingSpeed = swingSpeed;
        this.canBlock = canBlock;
        this.blockReduction = blockReduction;
        this.damageType = damageType;
    }

    /**
     * Returns the amount of damage this item will deal. One heart of damage is equal to 2 damage points.
     */
    public float getDamageVsEntity()
    {
        return attackDamage;
    }

    public float getStrVsBlock(ItemStack stack, Block block)
    {
        if (block == Blocks.web)
        {
            return 15.0F;
        }
        else
        {
            Material material = block.getMaterial();
            return material != Material.plants && material != Material.vine && material != Material.coral && material != Material.leaves && material != Material.gourd ? 1.0F : 1.5F;
        }
    }

    /**
     * Current implementations of this method in child classes do not use the entry argument beside ev. They just raise
     * the damage on the stack.
     */
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
        stack.damageItem(1, attacker);
        return true;
    }

    /**
     * Called when a Block is destroyed using this Item. Return true to trigger the "Use Item" statistic.
     */
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, Block blockIn, BlockPos pos, EntityLivingBase playerIn)
    {
        if ((double)blockIn.getBlockHardness(worldIn, pos) != 0.0D)
        {
            stack.damageItem(2, playerIn);
        }

        return true;
    }

    /**
     * Returns True is the item is renderer in full 3D when hold.
     */
    @SideOnly(Side.CLIENT)
    public boolean isFull3D()
    {
        return true;
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    @Override
    public abstract EnumAction getItemUseAction(ItemStack stack);

    /**
     * How long it takes to use or consume an item
     */
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 72000;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
    {
        playerIn.setItemInUse(itemStackIn, this.getMaxItemUseDuration(itemStackIn));
        return itemStackIn;
    }

    /**
     * Check whether this Item can harvest the given Block
     */
    public boolean canHarvestBlock(Block blockIn)
    {
        return blockIn == Blocks.web;
    }
    
    @Override
    public int getItemEnchantability()
    {
        return this.getTotalEnchantability();
    }
    
    public abstract int getTotalEnchantability();

    /**
     * Return whether this item is repairable in an anvil.
     */
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
    {
        return false;
    }

    @SuppressWarnings("deprecation")
	public Multimap<String, AttributeModifier> getItemAttributeModifiers()
    {
        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers();
        multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(itemModifierUUID, "Weapon modifier", (double)this.attackDamage, 0));
        return multimap;
    }
    
    /**
     * Add default item lore stuff
     */
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List<String> list, boolean bool) {
		list.add(EnumChatFormatting.DARK_RED + "Attack Damage: " + EnumChatFormatting.RESET + attackDamage);
		list.add(EnumChatFormatting.GOLD + "Damage Type: " + EnumChatFormatting.RESET + damageType);
		if (Math.abs(swingSpeed) < 0.001) {
			list.add(EnumChatFormatting.DARK_GREEN + "Swing Speed: " + EnumChatFormatting.RESET + "+" + String.format("%.2f", 1.0f + swingSpeed) + "%");
		}
	    
	}
	
//	/**
//     * Attacks for the player the targeted entity with the currently equipped item.  The equipped item has hitEntity
//     * called on it. Args: targetEntity
//     */
//    public void attackTargetEntity(EntityPlayer attacker, Entity targetEntity)
//    {
//        if (!net.minecraftforge.common.ForgeHooks.onPlayerAttackTarget(attacker, targetEntity)) return;
//        if (targetEntity.canAttackWithItem())
//        {
//            if (!targetEntity.hitByEntity(attacker))
//            {
//                float f = (float)attacker.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
//                int i = 0;
//                float f1 = 0.0F;
//
//                if (targetEntity instanceof EntityLivingBase)
//                {
//                    f1 = EnchantmentHelper.func_152377_a(attacker.getHeldItem(), ((EntityLivingBase)targetEntity).getCreatureAttribute());
//                }
//                else
//                {
//                    f1 = EnchantmentHelper.func_152377_a(attacker.getHeldItem(), EnumCreatureAttribute.UNDEFINED);
//                }
//
//                i = i + EnchantmentHelper.getKnockbackModifier(attacker);
//
//                if (attacker.isSprinting())
//                {
//                    ++i;
//                }
//
//                if (f > 0.0F || f1 > 0.0F)
//                {
//                    boolean flag = attacker.fallDistance > 0.0F && !attacker.onGround && !attacker.isOnLadder() && !attacker.isInWater() && !attacker.isPotionActive(Potion.blindness) && attacker.ridingEntity == null && targetEntity instanceof EntityLivingBase;
//
//                    if (flag && f > 0.0F)
//                    {
//                        f *= 1.5F;
//                    }
//
//                    f = f + f1;
//                    boolean flag1 = false;
//                    int j = EnchantmentHelper.getFireAspectModifier(attacker);
//
//                    if (targetEntity instanceof EntityLivingBase && j > 0 && !targetEntity.isBurning())
//                    {
//                        flag1 = true;
//                        targetEntity.setFire(1);
//                    }
//
//                    double d0 = targetEntity.motionX;
//                    double d1 = targetEntity.motionY;
//                    double d2 = targetEntity.motionZ;
//                    boolean flag2 = targetEntity.attackEntityFrom(DamageSource.causePlayerDamage(attacker), f);
//
//                    if (flag2)
//                    {
//                        if (i > 0)
//                        {
//                            targetEntity.addVelocity((double)(-MathHelper.sin(attacker.rotationYaw * (float)Math.PI / 180.0F) * (float)i * 0.5F), 0.1D, (double)(MathHelper.cos(attacker.rotationYaw * (float)Math.PI / 180.0F) * (float)i * 0.5F));
//                            attacker.motionX *= 0.6D;
//                            attacker.motionZ *= 0.6D;
//                            attacker.setSprinting(false);
//                        }
//
//                        if (targetEntity instanceof EntityPlayerMP && targetEntity.velocityChanged)
//                        {
//                            ((EntityPlayerMP)targetEntity).playerNetServerHandler.sendPacket(new S12PacketEntityVelocity(targetEntity));
//                            targetEntity.velocityChanged = false;
//                            targetEntity.motionX = d0;
//                            targetEntity.motionY = d1;
//                            targetEntity.motionZ = d2;
//                        }
//
//                        if (flag)
//                        {
//                        	attacker.onCriticalHit(targetEntity);
//                        }
//
//                        if (f1 > 0.0F)
//                        {
//                        	attacker.onEnchantmentCritical(targetEntity);
//                        }
//
//                        if (f >= 18.0F)
//                        {
//                        	attacker.triggerAchievement(AchievementList.overkill);
//                        }
//
//                        attacker.setLastAttacker(targetEntity);
//
//                        if (targetEntity instanceof EntityLivingBase)
//                        {
//                            EnchantmentHelper.applyThornEnchantments((EntityLivingBase)targetEntity, attacker);
//                        }
//
//                        EnchantmentHelper.applyArthropodEnchantments(attacker, targetEntity);
//                        ItemStack itemstack = attacker.getCurrentEquippedItem();
//                        Entity entity = targetEntity;
//
//                        if (targetEntity instanceof EntityDragonPart)
//                        {
//                            IEntityMultiPart ientitymultipart = ((EntityDragonPart)targetEntity).entityDragonObj;
//
//                            if (ientitymultipart instanceof EntityLivingBase)
//                            {
//                                entity = (EntityLivingBase)ientitymultipart;
//                            }
//                        }
//
//                        if (itemstack != null && entity instanceof EntityLivingBase)
//                        {
//                            itemstack.hitEntity((EntityLivingBase)entity, attacker);
//
//                            if (itemstack.stackSize <= 0)
//                            {
//                            	attacker.destroyCurrentEquippedItem();
//                            }
//                        }
//
//                        if (targetEntity instanceof EntityLivingBase)
//                        {
//                        	attacker.addStat(StatList.damageDealtStat, Math.round(f * 10.0F));
//
//                            if (j > 0)
//                            {
//                                targetEntity.setFire(j * 4);
//                            }
//                        }
//
//                        attacker.addExhaustion(0.3F);
//                    }
//                    else if (flag1)
//                    {
//                        targetEntity.extinguish();
//                    }
//                }
//            }
//        }
//        
//        
//    }

	public float getAttackDamage() {
		return attackDamage;
	}

	public float getSwingSpeed() {
		return swingSpeed;
	}

	public boolean isCanBlock() {
		return canBlock;
	}

	public float getBlockReduction() {
		return blockReduction;
	}

	public DamageType getDamageType() {
		return damageType;
	}
	
}
