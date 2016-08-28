package com.SkyIsland.Armory.items.armor;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import com.SkyIsland.Armory.Armory;
import com.SkyIsland.Armory.items.ModelRegistry;
import com.SkyIsland.Armory.mechanics.DamageType;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.common.util.EnumHelper;

/**
 * Custom created armor with defined protection values
 * @author Skyler
 *
 */
public abstract class Armor extends ItemArmor {
	
	protected static final String COMPONENT_LIST_KEY = "Components";
	
	public static final class ArmorPiece extends Item {
		
		private static final String INTERNAL_DAMAGE = "component_damage";
		
		private static final String INTERNAL_MAXDAMAGE = "component_maxdamage";
		
		private static final String INTERNAL_MATNAME = "component_material";
		
		//protected Map<DamageType, Float> protectionMap;
		//stored in nbt, not on item
		
		protected String itemKey;
		
		private UUID uniqueKey;
		
		/**
		 * How much of the overall armor material protection this
		 * piece gets, for each given damage type 
		 */
		protected Map<DamageType, Float> protectionRatios;
		
		/**
		 * Slot this piece resides in, used for rate lookups
		 */
		protected ArmorSlot parentSlot;
		
		protected float durabilityRate;

		/**
		 * Creates and <i><b>registers</b><i> a new armor piece. This is
		 * only intended to be called once per slot for each piece
		 * of complex armor.
		 * @param itemKey
		 * @param parentSlot
		 * @param ratios
		 * @param durabilityRate
		 */
		public ArmorPiece(String itemKey, ArmorSlot parentSlot, Map<DamageType, Float> ratios, float durabilityRate) {
			this.itemKey = itemKey;
			this.protectionRatios = ratios;
			this.parentSlot = parentSlot;
			this.durabilityRate = durabilityRate;
//			protectionMap = new EnumMap<DamageType, Float>(DamageType.class);
//			for (DamageType key : DamageType.values())
//				protectionMap.put(key, 0.0f);
			
			uniqueKey = UUID.randomUUID();
			
			this.maxStackSize = 1;
			this.setMaxDamage(100);
			this.setCreativeTab(Armory.creativeTab);
			this.canRepair = false;
			this.setUnlocalizedName("armorpiece_" + itemKey);
			
			Armory.proxy.registerArmorPiece(this);
		}
		
		public void clientInit() {
			Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
	    	.register(this, 0, new ModelResourceLocation(Armory.MODID + ":" + "armorlllllllllpiece_" + itemKey, "inventory"));
		}
		
		public ItemStack constructPiece(ArmorMaterial material) {
			return constructPiece(ExtendedArmorMaterial.wrap(material));
		}
		
		public ItemStack constructPiece(ExtendedArmorMaterial material) {
			Map<DamageType, Float> materialValues = material.getDamageReductionAmount(parentSlot);
			
			ItemStack stack = new ItemStack(this);
			
			Map<DamageType, Float> outMap = DamageType.freshMap();
			for (DamageType type : DamageType.values()) {
				outMap.put(type, 
						(materialValues.get(type) == null ? 0.0f : materialValues.get(type))
					  * (protectionRatios.get(type) == null ? 0.0f : protectionRatios.get(type))
						);
			}
			setProtection(stack, outMap);
			
			//SET MAX DAMAGE and CUR DAMAGE
			setUnderlyingDamage(stack, 0);
			setUnderlyingMaxDamage(stack, material.getDurability(durabilityRate));
			
			//Set up material name
			setUnderlyingMaterial(stack, material.getName());
						
			return stack;
		}
		
		@Override
		public ModelResourceLocation getModel(ItemStack stack, EntityPlayer player, int ticksRemaining) {
			//fetch model for this piece, as per 
			//return ModelRegistry.instance.getModel(Armory.MODID, fetchMaterial(stack).getTexturePrefix() + "_" + itemKey);
			ModelRegistry mr = ModelRegistry.instance;
			String prefix = fetchMaterial(stack).getTexturePrefix();
			return mr.getModel(Armory.MODID, prefix + "_" + itemKey);
		}
		
		public float getProtection(ItemStack stack, DamageType type) {
			if (!stack.hasTagCompound())
				stack.setTagCompound(new NBTTagCompound());
			
			NBTTagCompound nbt = stack.getTagCompound();
			
			float protection = 0.0f;
			if (nbt.hasKey(type.nbtKey(), NBT.TAG_FLOAT))
				protection = nbt.getFloat(type.nbtKey());
			
			return protection;
		}
		
		private void setProtection(ItemStack stack, Map<DamageType, Float> map) {
			if (!stack.hasTagCompound())
				stack.setTagCompound(new NBTTagCompound());
			
			NBTTagCompound nbt = stack.getTagCompound();
			
			for (DamageType type : DamageType.values()) {
				if (map.containsKey(type) && map.get(type) != null)
					nbt.setFloat(type.nbtKey(), map.get(type));
				else
					nbt.setFloat(type.nbtKey(), 0.0f);
			}
		}
		
		@Override
		public boolean equals(Object o) {
			if (o == null)
				return false;
			
			if (o instanceof ArmorPiece)
				return ((ArmorPiece) o).uniqueKey.equals(uniqueKey);
			
			return false;
		}
		
		@Override
		public int hashCode() {
			return 1793
					+ uniqueKey.hashCode();
		}
		
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
		private int getUnderlyingDamage(ItemStack stack) {
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
		private void setUnderlyingDamage(ItemStack stack, int damage) {
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
		private int getUnderlyingMaxDamage(ItemStack stack) {
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
		private void setUnderlyingMaxDamage(ItemStack stack, int damage) {
			if (!stack.hasTagCompound())
				stack.setTagCompound(new NBTTagCompound());
			
			NBTTagCompound nbt = stack.getTagCompound();
			nbt.setInteger(INTERNAL_MAXDAMAGE, damage);
		}
		
		/**
		 * Returns nbt-stored material name
		 * @param stack
		 * @return the material name, or "" if no name was found
		 */
		private String getUnderlyingMaterial(ItemStack stack) {
			if (!stack.hasTagCompound())
				stack.setTagCompound(new NBTTagCompound());
			
			NBTTagCompound nbt = stack.getTagCompound();
			if (nbt.hasKey(INTERNAL_MATNAME, NBT.TAG_STRING))
				return nbt.getString(INTERNAL_MATNAME);
			
			return "";
		}
		
		private ExtendedArmorMaterial fetchMaterial(ItemStack stack) {
			String materialName = getUnderlyingMaterial(stack);
			if (materialName == null || materialName.isEmpty())
				return null;
			
			return ExtendedArmorMaterial.lookupMaterial(materialName);
		}
		
		/**
		 * sets the nbt-stored max damage this weapon has
		 * @param stack
		 * @param damage the amount of damage this weapon has
		 */
		private void setUnderlyingMaterial(ItemStack stack, String materialName) {
			if (!stack.hasTagCompound())
				stack.setTagCompound(new NBTTagCompound());
			
			NBTTagCompound nbt = stack.getTagCompound();
			nbt.setString(INTERNAL_MATNAME, materialName);
		}
		
	}
	
	private static final ArmorMaterial material = EnumHelper.addArmorMaterial("armor_null_material", "none", 1, new int[] {1, 1, 1, 1}, 1);
	
	protected Armor(int armorType, String unlocalizedName) {
		super(material, 0, armorType);
		
		this.setUnlocalizedName(unlocalizedName);
//		protectionMap = new EnumMap<DamageType, Float>(DamageType.class);
//		for (DamageType key : DamageType.values())
//			protectionMap.put(key, 0.0f);
	}
    
    public void clientInit() {
    	Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
    	.register(this, 0, new ModelResourceLocation(Armory.MODID + ":" + this.getUnlocalizedName(), "inventory"));
    }
	
	/**
	 * Returns the total amount of protection this piece of armor provides
	 * against a certain type of damage
	 * @param type
	 * @return Total protection
	 */
	public abstract float getTotalProtection(ItemStack stack, DamageType type);
	
	/**
	 * Returns a full map that maps a Damage Type to a float. These values
	 * are guaranteed to be non-null.
	 * @return
	 */
	public abstract Map<DamageType, Float> getProtectionMap(ItemStack stack);
	
	/**
	 * Returns all armor pieces that are a part of this piece of armor.
	 * @return
	 */
	public abstract Collection<ArmorPiece> getArmorPieces();
	
	/**
	 * Returns a collection of the itemstacks that make up the components
	 * of this piece of armor. This should exclude any null elements
	 * @return
	 */
	public abstract Collection<ItemStack> getNestedArmorStacks(ItemStack stack);
	
	@Override
	public void setDamage(ItemStack stack, int damage) {
		//  Important!
		//  Do nothing, because we handle this ourselves so that we can
		//  figure out the type of damage.
		//  see ArmorModificationManager#onEntityHurt
		
		return;
	}
	
	/**
	 * Deal damage to the piece of armor. Each piece comprising this armor
	 * item is visit and given damage based on their contribution to the overall
	 * armor piece protection.<br>
	 * Each call to this method does 1 point of damage, spread out. Multiple calls
	 * are expected when more damage is dealt to armor.
	 */
	public void damage(EntityLivingBase owningEntity, ItemStack stack, DamageType damageType) {
		
		if (getNestedArmorStacks(stack).isEmpty()) {
			owningEntity.renderBrokenItemStack(stack);
			if (stack.stackSize > 0)
				stack.stackSize = 0;
			return;
		}
		
		int damage = 0;
		for (ItemStack piece : getNestedArmorStacks(stack)) {
			damage = (int) Math.round(Math.ceil(
					((ArmorPiece) piece.getItem()).getProtection(piece, damageType)));
			piece.damageItem(damage, owningEntity);
		}
	}
	
	@Override
	public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
		super.onCreated(stack, worldIn, playerIn);
		
		//create nbt compound for itemstack
		if (!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		
		NBTTagCompound tag = stack.getTagCompound();
		if (!tag.hasKey(COMPONENT_LIST_KEY, NBT.TAG_COMPOUND))
			tag.setTag(COMPONENT_LIST_KEY, new NBTTagCompound());
	}
	
}
