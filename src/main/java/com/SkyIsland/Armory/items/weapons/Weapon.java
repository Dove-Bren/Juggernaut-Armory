package com.SkyIsland.Armory.items.weapons;

import com.SkyIsland.Armory.Armory;
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

	private float attackDamage;

    public Weapon()
    {
//        this.maxStackSize = 1;
//      this.setMaxDamage(material.getMaxUses());
//      this.setCreativeTab(CreativeTabs.tabCombat);
//      this.attackDamage = 4.0F + material.getDamageVsEntity();
        this.setCreativeTab(Armory.creativeTab);
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
	
}
