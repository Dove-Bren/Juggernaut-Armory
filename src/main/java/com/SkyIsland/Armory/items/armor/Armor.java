package com.SkyIsland.Armory.items.armor;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import com.SkyIsland.Armory.Armory;
import com.SkyIsland.Armory.mechanics.DamageType;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
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
		
		//protected Map<DamageType, Float> protectionMap;
		//stored in nbt, not on item
		
		protected String itemKey;
		
		private UUID uniqueKey;
		
		/**
		 * Creates and <i><b>registers</b><i> a new armor piece. This is
		 * only intended to be called once per slot for each piece
		 * of complex armor.
		 * @param itemKey
		 */
		public ArmorPiece(String itemKey) {
			this.itemKey = itemKey;
//			protectionMap = new EnumMap<DamageType, Float>(DamageType.class);
//			for (DamageType key : DamageType.values())
//				protectionMap.put(key, 0.0f);
			
			uniqueKey = UUID.randomUUID();
			
			this.maxStackSize = 1;
			this.setCreativeTab(Armory.creativeTab);
			this.canRepair = false;
			this.setUnlocalizedName(Armory.MODID + "_armorpiece_" + itemKey);
			
			Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
	    	.register(this, 0, new ModelResourceLocation(Armory.MODID + ":" + Armory.MODID + "_armorpiece_" + itemKey, "inventory"));
		}
		
		public float getProtection(ItemStack stack, DamageType type) {
			return protectionMap.get(type);
		}
		
		@Override
		public boolean equals(Object o) {
			if (o instanceof ArmorPiece)
				return ((ArmorPiece) o).uniqueKey.equals(uniqueKey);
			
			return false;
		}
		
		@Override
		public int hashCode() {
			return 1793
					+ uniqueKey.hashCode();
		}
		
	}
	
	private static final ArmorMaterial material = EnumHelper.addArmorMaterial("armor_null_material", "none", 1, new int[] {1, 1, 1, 1}, 1);
	
	protected Armor(int armorType) {
		super(material, 0, armorType);
//		protectionMap = new EnumMap<DamageType, Float>(DamageType.class);
//		for (DamageType key : DamageType.values())
//			protectionMap.put(key, 0.0f);
	}
	
	/**
	 * Returns the total amount of protection this piece of armor provides
	 * against a certain type of damage
	 * @param type
	 * @return Total protection
	 */
	public abstract float getTotalProtection(DamageType type);
	
	/**
	 * Returns a full map that maps a Damage Type to a float. These values
	 * are guaranteed to be non-null.
	 * @return
	 */
	protected abstract Map<DamageType, Float> getProtectionMap();
	
	/**
	 * Returns all armor pieces that are a part of this piece of armor.
	 * @return
	 */
	public abstract Collection<ArmorPiece> getArmorPieces();
	
	protected abstract int getNumberOfPieces();
	
	@Override
	public int getSizeInventory() {
		return getNumberOfPieces();
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return true;
	}
	
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
	public void damage(ItemStack stack, DamageType damageType) {
		short damage = 0;
		for (ArmorPiece piece : getArmorPieces()) {
			damage = (short) Math.round(Math.ceil(piece.getProtection(damageType)));
			
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
