package com.SkyIsland.Armory.items.armor;

import java.util.Collection;
import java.util.Map;

import com.SkyIsland.Armory.Armory;
import com.SkyIsland.Armory.gui.ArmorerStandGui;
import com.SkyIsland.Armory.gui.ArmorerStandGui.StandContainer;
import com.SkyIsland.Armory.items.ModelRegistry;
import com.SkyIsland.Armory.mechanics.DamageType;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ISmartItemModel;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Custom created armor with defined protection values
 * @author Skyler
 *
 */
public abstract class Armor extends ItemArmor {
	
	protected static final String BASE_SUFFIX = "_base";
	
	private static final ArmorMaterial material = EnumHelper.addArmorMaterial("armor_null_material", "none", 1, new int[] {1, 1, 1, 1}, 1);
	
	protected String registryName;
	
	private ArmorSlot slot;
	
	protected Armor(ArmorSlot slot, String unlocalizedName) {
		super(material, 0, slot.getSlot());
		this.slot = slot;
		
		this.setUnlocalizedName(unlocalizedName);
		this.registryName = unlocalizedName;
		this.setCreativeTab(Armory.creativeTab);
//		protectionMap = new EnumMap<DamageType, Float>(DamageType.class);
//		for (DamageType key : DamageType.values())
//			protectionMap.put(key, 0.0f);
	}
    
	@SideOnly(Side.CLIENT)
    public void clientInit() {
    	Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
    	.register(this, 0, new ModelResourceLocation(Armory.MODID + ":" + this.registryName, "inventory"));
    	
    	ModelBakery.registerItemVariants(this, new ModelResourceLocation(Armory.MODID + ":" + this.registryName, "base"));
    	
    	if (getSmartModel() != null)
    		ModelRegistry.instance.register(Armory.MODID, this.registryName, this.getSmartModel());
    }
    
    public void init() {
    	;
    }
    
    /**
     * Return a smart model that can be used to render this armor piece.
     * @return
     */
    @SideOnly(Side.CLIENT)
    protected abstract ISmartItemModel getSmartModel();
    
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
	
	/**
	 * Returns the name of the underlying armor texture
	 * @return
	 */
	public abstract String getBaseArmorTexture();

	/**
	 * Returns a ModelBiped that will render the armor on a player
	 * @return
	 */
	@SideOnly(Side.CLIENT)
	protected abstract ModelBiped getModelBiped();
	
	/**
	 * Add gui overlay and buttons to the containing gui at the given location.
	 * @param gui
	 * @param xoffset
	 * @param yoffset
	 * @param width
	 * @param height
	 */
	@SideOnly(Side.CLIENT)
	public abstract void decorateGui(ArmorerStandGui.StandGui gui, ItemStack stack, int xoffset, int yoffset, int width, int height);
	
	public abstract void setupContainer(StandContainer gui, ItemStack stack, int xoffset, int yoffset, int width, int height);
	
	/**
	 * Where this gui element should be located. Governs where the gui offsets given
	 * in the decorateGui method point to
	 * @return
	 */
	@SideOnly(Side.CLIENT)
	public abstract ArmorerStandGui.Location getGuiLocation();
	
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
		
//		NBTTagCompound tag = stack.getTagCompound();
//		if (!tag.hasKey(COMPONENT_LIST_KEY, NBT.TAG_COMPOUND))
//			tag.setTag(COMPONENT_LIST_KEY, new NBTTagCompound());
	}
	
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		return false; //TODO use material item? Or special repair stuff?
	}
	
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        
		return this.getBaseArmorTexture();
		
    }
	
	@SideOnly(Side.CLIENT)
	@Override
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot, net.minecraft.client.model.ModelBiped default_model) {
        ModelBiped model = this.getModelBiped();
        
        model.isSneak = default_model.isSneak;
        model.isRiding = default_model.isRiding;
        model.isChild = default_model.isChild;
        
        return model;
    }
	
	public ArmorSlot getSlot() {
		return slot;
	}
	
}
