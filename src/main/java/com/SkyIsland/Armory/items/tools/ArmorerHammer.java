package com.SkyIsland.Armory.items.tools;

import com.SkyIsland.Armory.Armory;
import com.SkyIsland.Armory.entity.EntityArmorerStand;
import com.SkyIsland.Armory.items.ItemBase;
import com.SkyIsland.Armory.items.armor.Armor;
import com.SkyIsland.Armory.items.armor.ArmorSlot;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ArmorerHammer extends ItemBase {

	private String registryName;
	
	public ArmorerHammer(String unlocalizedName) {
		super();
		
		registryName = unlocalizedName;
		
		this.setMaxStackSize(1);
		this.setUnlocalizedName(unlocalizedName);
		this.setCreativeTab(Armory.creativeTab);
	}

	@Override
	public boolean isDamageable() {
		return false;
	}
	
	@Override
	public void init() {
		GameRegistry.addShapedRecipe(new ItemStack(this), new Object[]{"##T", " S ", " S ", '#', Items.iron_ingot, 'T', Items.flint, 'S', Items.stick});
	}
	
	public void clientInit() {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
    	.register(this, 0, new ModelResourceLocation(Armory.MODID + ":" + this.registryName, "inventory"));
	}
	
	/**
     * Returns true if the item can be used on the given entity, e.g. shears on sheep.
     */
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target) {
    	if (target instanceof EntityArmorerStand) {
        	System.out.println("armor stand!");
        	
        	//is armor stand wearing any customizable armor pieces?
        	EntityArmorStand armorStand = (EntityArmorStand) target;
        	
        	ItemStack head, torso, legs, feet;
        	head = armorStand.getEquipmentInSlot(ArmorSlot.HELMET.getPlayerSlot());
        	torso = armorStand.getEquipmentInSlot(ArmorSlot.TORSO.getPlayerSlot());
        	legs = armorStand.getEquipmentInSlot(ArmorSlot.LEGS.getPlayerSlot());
        	feet = armorStand.getEquipmentInSlot(ArmorSlot.FEET.getPlayerSlot());
        	
        	if (head != null)
        		head = (head.getItem() instanceof Armor ? head : null);
        	if (torso != null)
        		torso = (torso.getItem() instanceof Armor ? torso : null);
        	if (legs != null)
        		legs = (legs.getItem() instanceof Armor ? legs : null);
        	if (feet != null)
        		feet = (legs.getItem() instanceof Armor ? legs : null);
        	
        	if ((head == null) && (torso == null) && (legs == null) && (feet == null)) {
            	System.out.println("everything's null");
        		return false;
        	}
        	
        	popupGui(playerIn, head, torso, legs, feet);
        	return true;
        } else {
        	System.out.println(target);
        }
    	
    	return false;
    }
    
//    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
//    	if (entity instanceof EntityArmorerStand) {
//        	System.out.println("armor stand!");
//        	
//        	//is armor stand wearing any customizable armor pieces?
//        	EntityArmorStand armorStand = (EntityArmorStand) entity;
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
//        	popupGui(player, head, torso, legs, feet);
//        	return true;
//        } else {
//        	System.out.println(entity);
//        }
//    	
//    	return false;
//    }
    
    /**
     * Creates and displays a gui for the player with the given customizable armor
     * pieces.
     * @param player
     * @param head The head armor piece, or null if it's not customizable (or is air)
     * @param torso
     * @param legs
     * @param feet
     */
    private void popupGui(EntityPlayer player, ItemStack head, ItemStack torso, ItemStack legs, ItemStack feet) {
    	System.out.println("head: " + (head != null));
    	System.out.println("torso: " + (torso != null));
    	System.out.println("legs: " + (legs != null));
    	System.out.println("feet: " + (feet != null));
    }
	
}
