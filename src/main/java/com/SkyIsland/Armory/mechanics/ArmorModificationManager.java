package com.SkyIsland.Armory.mechanics;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.potion.Potion;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.Mod.EventHandler;

/**
 * Stores map with all players in it and their respective armor, for lookups to
 * fetch the amount of armor a player has for a certain damage type
 * @author Skyler
 *
 */
public class ArmorModificationManager {
	
	private static ArmorModificationManager instance;
	
	public static ArmorModificationManager instance() {
		return instance;
	}
	
	private ArmorModificationManager() {
		
	}
	
	public static final void init() {
		instance = new ArmorModificationManager();
		
		MinecraftForge.EVENT_BUS.register(instance);
	}
	
	@EventHandler
	public void onEntityConstructing(EntityConstructing event)
	{
		/*
		Be sure to check if the entity being constructed is the correct type for the extended properties you're about to add! The null check may not be necessary - I only use it to make sure properties are only registered once per entity
		*/
		if (event.entity instanceof EntityLivingBase && ExtendedArmor.get((EntityLivingBase) event.entity) == null)
		// This is how extended properties are registered using our convenient method from earlier
			ExtendedArmor.register((EntityLivingBase) event.entity);
		// That will call the constructor as well as cause the init() method
		// to be called automatically
	}
	
	
	//   Whoops. this is what we'd do if we didn't want to do our own stuff
	//   when they got hit, and just wanted to affect the amount
//	@EventHandler
//	public void onEntityDamaged(LivingAttackEvent event) {
//		if (event.isCanceled())
//			return;
//		
//		//check incoming damage to see if it has a type
//		ItemStack inHand = null;
//		if (event.source.getEntity() instanceof EntityLivingBase) {
//			EntityLivingBase living = (EntityLivingBase) event.source.getEntity();
//			inHand = living.getHeldItem();
//		}
//		
//		DamageType type;
//		
//		if (event.source.getEntity() instanceof EntityArrow) {
//			type = DamageType.PIERCE;
//		}
//		else if (inHand == null)
//			type = DamageType.SLASH;
//		else if (inHand.getItem() instanceof Weapon) {
//			type = ((Weapon) inHand.getItem()).getDamageType();
//		} else {
//			//something else
//			//swords are slash, axe are slash? crush? Regular items are crush?
//			Item item = inHand.getItem();
//			if (item instanceof ItemSword)
//				type = DamageType.SLASH;
//			else if (item instanceof ItemAxe)
//				type = DamageType.SLASH;
//			else if (item.getRegistryName().toLowerCase().contains("shovel"))
//				type = DamageType.CRUSH;
//			else if (item.getRegistryName().toLowerCase().contains("arrow"))
//				type = DamageType.PIERCE;
//			else
//				type = DamageType.CRUSH;
//		}
//		
//		ExtendedArmor armor = ExtendedArmor.get(event.entityLiving);
//		if (armor == null) {
//			//no extended attributes!
//			return;
//		}
//		
//		
//		
//		float protection = armor.getProtection(type);
//		
//	}
	
	@EventHandler
	public void onHitEntity(AttackEntityEvent event) {
		
		//Code from EntityPlayer's attackEntityWithCurrentItem method
		event.setCanceled(true);
		
		Entity targetEntity = event.target;
		if (targetEntity .canAttackWithItem())
        {
            if (!targetEntity.hitByEntity(event.entityPlayer))
            {
                float f = (float) event.entityPlayer.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
                int i = 0;
                float f1 = 0.0F;

                if (targetEntity instanceof EntityLivingBase)
                {
                    f1 = EnchantmentHelper.func_152377_a(event.entityPlayer.getHeldItem(), ((EntityLivingBase)targetEntity).getCreatureAttribute());
                }
                else
                {
                    f1 = EnchantmentHelper.func_152377_a(event.entityPlayer.getHeldItem(), EnumCreatureAttribute.UNDEFINED);
                }

                i = i + EnchantmentHelper.getKnockbackModifier(event.entityPlayer);

                if (event.entityPlayer.isSprinting())
                {
                    ++i;
                }

                if (f > 0.0F || f1 > 0.0F)
                {
                    boolean flag = event.entityPlayer.fallDistance > 0.0F && !event.entityPlayer.onGround && !event.entityPlayer.isOnLadder() && !event.entityPlayer.isInWater() && !event.entityPlayer.isPotionActive(Potion.blindness) && event.entityPlayer.ridingEntity == null && targetEntity instanceof EntityLivingBase;

                    if (flag && f > 0.0F)
                    {
                        f *= 1.5F;
                    }

                    f = f + f1;
                    boolean flag1 = false;
                    int j = EnchantmentHelper.getFireAspectModifier(event.entityPlayer);

                    if (targetEntity instanceof EntityLivingBase && j > 0 && !targetEntity.isBurning())
                    {
                        flag1 = true;
                        targetEntity.setFire(1);
                    }

                    double d0 = targetEntity.motionX;
                    double d1 = targetEntity.motionY;
                    double d2 = targetEntity.motionZ;
                    boolean flag2 = targetEntity.attackEntityFrom(DamageSource.causePlayerDamage(event.entityPlayer), f);

                    if (flag2)
                    {
                        if (i > 0)
                        {
                            targetEntity.addVelocity((double)(-MathHelper.sin(event.entityPlayer.rotationYaw * (float)Math.PI / 180.0F) * (float)i * 0.5F), 0.1D, (double)(MathHelper.cos(event.entityPlayer.rotationYaw * (float)Math.PI / 180.0F) * (float)i * 0.5F));
                            event.entityPlayer.motionX *= 0.6D;
                            event.entityPlayer.motionZ *= 0.6D;
                            event.entityPlayer.setSprinting(false);
                        }

                        if (targetEntity instanceof EntityPlayerMP && targetEntity.velocityChanged)
                        {
                            ((EntityPlayerMP)targetEntity).playerNetServerHandler.sendPacket(new S12PacketEntityVelocity(targetEntity));
                            targetEntity.velocityChanged = false;
                            targetEntity.motionX = d0;
                            targetEntity.motionY = d1;
                            targetEntity.motionZ = d2;
                        }

                        if (flag)
                        {
                        	event.entityPlayer.onCriticalHit(targetEntity);
                        }

                        if (f1 > 0.0F)
                        {
                        	event.entityPlayer.onEnchantmentCritical(targetEntity);
                        }

                        if (f >= 18.0F)
                        {
                        	event.entityPlayer.triggerAchievement(AchievementList.overkill);
                        }

                        event.entityPlayer.setLastAttacker(targetEntity);

                        if (targetEntity instanceof EntityLivingBase)
                        {
                            EnchantmentHelper.applyThornEnchantments((EntityLivingBase)targetEntity, event.entityPlayer);
                        }

                        EnchantmentHelper.applyArthropodEnchantments(event.entityPlayer, targetEntity);
                        ItemStack itemstack = event.entityPlayer.getCurrentEquippedItem();
                        Entity entity = targetEntity;

                        if (targetEntity instanceof EntityDragonPart)
                        {
                            IEntityMultiPart ientitymultipart = ((EntityDragonPart)targetEntity).entityDragonObj;

                            if (ientitymultipart instanceof EntityLivingBase)
                            {
                                entity = (EntityLivingBase)ientitymultipart;
                            }
                        }

                        if (itemstack != null && entity instanceof EntityLivingBase)
                        {
                            itemstack.hitEntity((EntityLivingBase)entity, event.entityPlayer);

                            if (itemstack.stackSize <= 0)
                            {
                            	event.entityPlayer.destroyCurrentEquippedItem();
                            }
                        }

                        if (targetEntity instanceof EntityLivingBase)
                        {
                        	event.entityPlayer.addStat(StatList.damageDealtStat, Math.round(f * 10.0F));

                            if (j > 0)
                            {
                                targetEntity.setFire(j * 4);
                            }
                        }

                        event.entityPlayer.addExhaustion(0.3F);
                    }
                    else if (flag1)
                    {
                        targetEntity.extinguish();
                    }
                }
            }
        }
		
	}
	
}