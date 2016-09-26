package com.SkyIsland.Armory.items.weapons;

import java.util.Collection;
import java.util.Map;

import com.SkyIsland.Armory.Armory;
import com.SkyIsland.Armory.items.ModelRegistry;
import com.SkyIsland.Armory.items.common.ExtendedMaterial;
import com.SkyIsland.Armory.mechanics.DamageType;
import com.SkyIsland.Armory.proxy.ClientInitializable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Abstract weapon class. It could be a sword, it could be a mace. YOU'LL
 * NEVER KNOW!
 * @author Skyler
 *
 */
public abstract class Weapon extends Item implements ClientInitializable {

	private static final String DAMAGE_KEY = "DamageComponents";
	private static final String INTERNAL_DAMAGE = "component_damage";
	private static final String INTERNAL_MAXDAMAGE = "component_maxdamage";
	
	//private static final String COMPONENT_KEY = "SubComponents";
	
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
	
	protected Map<DamageType, Float> weaponModifierMap;
	
	protected String registryName;
	
    protected Weapon(String unlocalizedName) {
    	this(unlocalizedName, null, 1.0f, false, 0.0f);
    }
    
    protected Weapon(String unlocalizedName, Map<DamageType, Float> damageMap, float swingSpeed, boolean canBlock, float blockReduction) {
//        this.maxStackSize = 1;
//      this.setMaxDamage(material.getMaxUses());
//      this.setCreativeTab(CreativeTabs.tabCombat);
//      this.attackDamage = 4.0F + material.getDamageVsEntity();
        this.setCreativeTab(Armory.creativeTab);
        this.swingSpeed = swingSpeed;
        this.canBlock = canBlock;
        this.blockReduction = blockReduction;
        this.setUnlocalizedName(unlocalizedName);
        this.registryName = unlocalizedName;
        this.weaponModifierMap = DamageType.freshMap();
        if (damageMap != null)
        for (DamageType key : damageMap.keySet())
        	weaponModifierMap.put(key, damageMap.get(key));
        
        //set durability to 100 for nice float conversions
        this.setMaxDamage(100);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void clientInit() {
//    	Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
//    	.register(this, 0, new ModelResourceLocation(Armory.MODID + ":" + this.registry.getUnlocalizedName(), "inventory"));
    	
    	Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
    	.register(this, 0, new ModelResourceLocation(Armory.MODID + ":" + this.registryName, "inventory"));
    	
//    	if (getSmartModel() != null)
//    		ModelRegistry.instance.register(Armory.MODID, this.registryName, this.getSmartModel());
    	ModelRegistry.instance.register(Armory.MODID, this.registryName, new WeaponSmartModel());
    	
    }

    /**
     * Returns the amount of damage this item will deal. One heart of damage is equal to 2 damage points.
     */
    public float getDamageVsEntity()
    {
        return 1.0f;
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

    //handled in item listener now
//    /**
//     * Add default item lore stuff
//     */
//	@Override
//	public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List<String> list, boolean bool) {
//		list.add(EnumChatFormatting.DARK_RED + "Attack Damage: " + EnumChatFormatting.RESET + attackDamage);
//		list.add(EnumChatFormatting.GOLD + "Damage Type: " + EnumChatFormatting.RESET + damageType);
//		if (Math.abs(swingSpeed) < 0.001) {
//			list.add(EnumChatFormatting.DARK_GREEN + "Swing Speed: " + EnumChatFormatting.RESET + "+" + String.format("%.2f", 1.0f + swingSpeed) + "%");
//		}
//	    
//	}
	
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

	public float getSwingSpeed() {
		return swingSpeed;
	}

	public boolean isCanBlock() {
		return canBlock;
	}

	public float getBlockReduction() {
		return blockReduction;
	}
	
	/**
	 * Used to set in stone the damage values of the weapon. Intended to be
	 * used from a concrete weapon's creation call.
	 * @param base
	 * @param name
	 * @param damageMap
	 * @return
	 */
	protected static ItemStack constructWeaponFrom(Weapon base, String name, Map<DamageType, Float> damageMap, int durability) {
		ItemStack stack = new ItemStack(base);
		
		stack.setStackDisplayName(name);
		setDamageValues(stack, damageMap);
		setUnderlyingMaxDamage(stack, durability);
		setUnderlyingDamage(stack, 0);
		
		
		return stack;
	}
	
	@Deprecated
	protected static ItemStack constructWeapon(Weapon base, String name, ExtendedMaterial material, int durability) {
//		ItemStack stack = new ItemStack(base);
//		
//		stack.setStackDisplayName(name);
//		if (!stack.hasTagCompound())
//			stack.setTagCompound(new NBTTagCompound());
//		
//		if (!stack.getTagCompound().hasKey(DAMAGE_KEY, NBT.TAG_COMPOUND))
//			stack.getTagCompound().setTag(DAMAGE_KEY, new NBTTagCompound());
//		
//		NBTTagCompound nbt = stack.getTagCompound().getCompoundTag(DAMAGE_KEY);
//		
//		Map<DamageType, Float> damageMap = DamageType.freshMap(),
//				cache = base.getDamageModifierMap();
//		for (DamageType key : material.getDamageMap().keySet()) {
//			damageMap.put(key, 
//					material.getDamageMap().get(key) * cache.get(key) 
//					);
//		}
//		
//		for (DamageType type : DamageType.values())
//		if (damageMap.containsKey(type)) {
//			nbt.setFloat(type.name(), damageMap.get(type));
//		}
//		
//		return stack;
		return constructWeaponFrom(base, name, material.getDamageMap(), durability);
	}
	
	protected static void setDamageValues(ItemStack stack, Map<DamageType, Float> damageMap) {
		if (!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		
		if (!stack.getTagCompound().hasKey(DAMAGE_KEY, NBT.TAG_COMPOUND))
			stack.getTagCompound().setTag(DAMAGE_KEY, new NBTTagCompound());
		
		NBTTagCompound nbt = stack.getTagCompound().getCompoundTag(DAMAGE_KEY);
		
		for (DamageType type : DamageType.values())
		if (damageMap.containsKey(type)) {
			nbt.setFloat(type.name(), damageMap.get(type));
		}
	}
	
	public Map<DamageType, Float> getDamageModifierMap() {
		return this.weaponModifierMap;
	}

	@Override
	public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
		super.onCreated(stack, worldIn, playerIn);
		
		//create nbt compound for itemstack
		if (!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		
		NBTTagCompound tag = stack.getTagCompound();
		if (!tag.hasKey(DAMAGE_KEY, NBT.TAG_COMPOUND))
			tag.setTag(DAMAGE_KEY, new NBTTagCompound());
	}
	
	public Map<DamageType, Float> getDamageMap(ItemStack stack) {
		if (stack == null || !stack.hasTagCompound())
			return DamageType.freshMap();
		
		if (!stack.getTagCompound().hasKey(DAMAGE_KEY, NBT.TAG_COMPOUND))
			stack.getTagCompound().setTag(DAMAGE_KEY, new NBTTagCompound());
		
		Map<DamageType, Float> map = DamageType.freshMap();
		NBTTagCompound nbt = stack.getTagCompound().getCompoundTag(DAMAGE_KEY);
		
		for (DamageType type : DamageType.values()) {
			if (!nbt.hasKey(type.name(), NBT.TAG_FLOAT))
				map.put(type, 0.0f);
			else
				map.put(type, nbt.getFloat(type.name()));
		}
		
		return map;
	}
	
	public float getDamage(ItemStack stack, DamageType type) {
		return getDamageMap(stack).get(type);
	}
	
//	public Collection<ItemStack> getWeaponComponents(ItemStack weapon) {
//		if (weapon == null || !(weapon.getItem() instanceof Weapon))
//			return null;
//		
//		if (!weapon.hasTagCompound())
//			weapon.setTagCompound(new NBTTagCompound());
//		NBTTagCompound nbt = weapon.getTagCompound();
//		
//		LinkedList<ItemStack> list = new LinkedList<ItemStack>();
//		if (!nbt.hasKey(COMPONENT_KEY, NBT.TAG_LIST))
//			return list;
//		NBTTagList tags = nbt.getTagList(COMPONENT_KEY, NBT.TAG_COMPOUND);
//		NBTTagCompound sub;
//		while (!tags.hasNoTags()) {
//			sub = (NBTTagCompound) tags.removeTag(0);
//			list.add(ItemStack.loadItemStackFromNBT(sub));
//		}
//		
//		return list;
//	}
	
	public abstract Collection<ItemStack> getWeaponComponents(ItemStack weapon);
	
//	public void setWeaponComponents
	
//	public Collection<ModelResourceLocation> getWeaponComponentModels(ItemStack weapon) {
//		if (weapon == null || !(weapon.getItem() instanceof Weapon))
//			return null;
//		
//		Collection<ItemStack> comps = getWeaponComponents(weapon);
//		List<ModelResourceLocation> models = new LinkedList<ModelResourceLocation>();
//		
//		if (comps.isEmpty())
//			return models;
//		
//		
//		
//	}
	
	@Override
	public void setDamage(ItemStack stack, int damage) {
		int change = stack.getItemDamage() - damage;
		
		//damage secret internal damage, and update
		//itemstack damage to reflect it
		int actual = getUnderlyingDamage(stack),
		    max = getUnderlyingMaxDamage(stack);
		if (actual == -1 || actual >= max) {
			//error, bug, glitch, etc OR it is now broken
			super.setDamage(stack, 101); //set damage over break point to break
			return;
		}
		
		actual += change;
		if (actual < 0)
			actual = 0;
		setUnderlyingDamage(stack, actual);
		
		//                              \/ 99 because it shouldn't be broken, and we don't want it to end up rounding down and causing it to break
		super.setDamage(stack, Math.min(99, Math.round((float) actual / (float) max)));
	}
	
	/**
	 * Returns the nbt-stored damage this weapon has, if it exists
	 * @param stack
	 * @return the damage, or -1 if it can't be found
	 */
	protected static int getUnderlyingDamage(ItemStack stack) {
		if (!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt.hasKey(INTERNAL_DAMAGE, NBT.TAG_INT))
			return nbt.getInteger(INTERNAL_DAMAGE);
		
		return -1;
	}
	
	/**
	 * sets the nbt-stored damage this weapon has
	 * @param stack
	 * @param damage the amount of damage this weapon has
	 */
	protected static void setUnderlyingDamage(ItemStack stack, int damage) {
		if (!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		
		NBTTagCompound nbt = stack.getTagCompound();
		nbt.setInteger(INTERNAL_DAMAGE, damage);
	}
	
	/**
	 * Returns the nbt-stored max damage this weapon can have, if it exists
	 * @param stack
	 * @return the damage, or -1 if it can't be found
	 */
	protected static int getUnderlyingMaxDamage(ItemStack stack) {
		if (!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt.hasKey(INTERNAL_MAXDAMAGE, NBT.TAG_INT))
			return nbt.getInteger(INTERNAL_MAXDAMAGE);
		
		return -1;
	}
	
	/**
	 * sets the nbt-stored max damage this weapon has
	 * @param stack
	 * @param damage the amount of damage this weapon has
	 */
	protected static void setUnderlyingMaxDamage(ItemStack stack, int damage) {
		if (!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		
		NBTTagCompound nbt = stack.getTagCompound();
		nbt.setInteger(INTERNAL_MAXDAMAGE, damage);
	}

}
