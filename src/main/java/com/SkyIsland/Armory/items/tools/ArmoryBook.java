package com.SkyIsland.Armory.items.tools;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.SkyIsland.Armory.Armory;
import com.SkyIsland.Armory.api.WeaponManager;
import com.SkyIsland.Armory.gui.ArmoryBookScreen;
import com.SkyIsland.Armory.gui.IBookPage;
import com.SkyIsland.Armory.gui.PlainTextPage;
import com.SkyIsland.Armory.items.ItemBase;
import com.SkyIsland.Armory.items.ToolItems;
import com.SkyIsland.Armory.items.ToolItems.Tools;
import com.SkyIsland.Armory.mechanics.DamageType;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ArmoryBook extends ItemBase {

	public static ArmoryBook instance;
	
	private static final float DAMAGE = 1.0f;
	
	private static ArmoryBookScreen screen = null;
	
	private String registryName;
	
	public ArmoryBook(String unlocalizedName) {
		super();
		
		registryName = unlocalizedName;
		instance = this;
		
		this.setMaxStackSize(1);
		this.setUnlocalizedName(unlocalizedName);
		this.setCreativeTab(Armory.creativeTab);
		
		Map<DamageType, Float> map = DamageType.freshMap();
		map.put(DamageType.CRUSH, DAMAGE);
		WeaponManager.instance().registerWeapon(this, 
				map
				);
	}

	@Override
	public boolean isDamageable() {
		return false;
	}
	
	@Override
	public void init() {
		GameRegistry.addShapelessRecipe(new ItemStack(this), Items.iron_ingot, Items.book, ToolItems.getItem(Tools.ARMORER_HAMMER));
	}
	
	public void clientInit() {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
    	.register(this, 0, new ModelResourceLocation(Armory.MODID + ":" + this.registryName, "inventory"));
		
		List<IBookPage> pages = new LinkedList<IBookPage>();
		pages.add(new PlainTextPage("this is page 1"));
		pages.add(new PlainTextPage("this is page 2!!!!!!!!!!!!!!!!!!!!!!"));
		pages.add(new PlainTextPage("this is page 3!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"));
		ArmoryBook.screen = new ArmoryBookScreen(pages);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
		Armory.proxy.openArmoryBook();
		
		return itemStackIn;
	}
	
	public static ArmoryBookScreen getScreen() {
		return screen;
	}
	
}
