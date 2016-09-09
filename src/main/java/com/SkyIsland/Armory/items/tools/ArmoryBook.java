package com.SkyIsland.Armory.items.tools;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.SkyIsland.Armory.Armory;
import com.SkyIsland.Armory.api.ForgeManager;
import com.SkyIsland.Armory.api.ForgeManager.FuelRecord;
import com.SkyIsland.Armory.api.ForgeManager.MetalRecord;
import com.SkyIsland.Armory.api.WeaponManager;
import com.SkyIsland.Armory.forge.ForgeBlocks;
import com.SkyIsland.Armory.forge.ForgeBlocks.ArmoryBlocks;
import com.SkyIsland.Armory.gui.ArmoryBookScreen;
import com.SkyIsland.Armory.gui.HSplitPage;
import com.SkyIsland.Armory.gui.IBookPage;
import com.SkyIsland.Armory.gui.ImagePage;
import com.SkyIsland.Armory.gui.ItemPage;
import com.SkyIsland.Armory.gui.LinedTextPage;
import com.SkyIsland.Armory.gui.PlainTextPage;
import com.SkyIsland.Armory.gui.VDynamicSplitPage;
import com.SkyIsland.Armory.gui.VSplitPage;
import com.SkyIsland.Armory.items.ItemBase;
import com.SkyIsland.Armory.items.ToolItems;
import com.SkyIsland.Armory.items.ToolItems.Tools;
import com.SkyIsland.Armory.mechanics.DamageType;
import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

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
		pages.add(new PlainTextPage(""));
		pages.add(new HSplitPage(new ImagePage(new ResourceLocation(Armory.MODID + ":textures/gui/armory_book_front.png"), 75, 75, 0, 0, 75, 75, null), new PlainTextPage("A Smith's Guide")));
		
		pages.add(new PlainTextPage("The Armory is a structure that allows you to melt down metals and shape them into complex weapons and armor. From the left pauldron on your chestplate to the visor on your helm to the head on your mace, each piece is picked and crafted by you. Everything starts with a forge. To construct it,"));
		pages.add(new HSplitPage(new PlainTextPage("you'll need both a forge block and a brazier. Once you have crafted each block, place them next to eachother and add some fuel to the brazier."),
				new VSplitPage(new ImagePage(new ResourceLocation(Armory.MODID + ":textures/gui/armory_book_11.png"), 45, 45, 0, 0, 45, 45, null), new ItemPage(new ItemStack(ForgeBlocks.getBlock(ArmoryBlocks.FORGE))))));
		pages.add(new HSplitPage(new PlainTextPage("The type of fuel you chose determines the speed the forge heats up, and how hot it can get. You can check how hot the forge is by opening up the forge GUI."),
				new VSplitPage(new ImagePage(new ResourceLocation(Armory.MODID + ":textures/gui/armory_book_21.png"), 45, 45, 0, 0, 45, 45, null), new ItemPage(new ItemStack(ForgeBlocks.getBlock(ArmoryBlocks.BRAZIER))))));
		
		IBookPage page1 = null, page2 = null;
		for (Entry<Item, FuelRecord> entry : ForgeManager.instance().getFuelRecords().entrySet()) {
			if (page1 == null) {
				page1 = generateFuelPage(entry.getKey(), entry.getValue());
			} else {
				page2 = generateFuelPage(entry.getKey(), entry.getValue());
				
				//pour out pages
				pages.add(new HSplitPage(page1, page2));
				page1 = null;
				page2 = null;
			}
		}
		
		if (page1 != null) {
			//got one part but not enough to fit another
			pages.add(new HSplitPage(page1, new PlainTextPage("")));
			page1 = null;
		}
		
		pages.add(new PlainTextPage("As your brazier burns, your forge will gather heat. This heat is used to melt down different metals. Each metal has a temperature the forge must be at before it can melt. In the forge, place a piece of metal into the input slot. If the metal can be smelted, the heat reading will change from orange"));
		pages.add(new PlainTextPage("to either red or green. If the heat is red, the forge is not hot enough to melt the metal. If it's green, the metal will be melted as soon as the forge is done with its current piece of metal. Each piece of metal melted adds to the current pool of melted metal in the forge."));
		
		for (MetalRecord record : ForgeManager.instance().getMetalRecords()) {
			if (page1 == null) {
				page1 = generateMetalPage(record);
			} else {
				page2 = generateMetalPage(record);
				
				//pour out pages
				pages.add(new HSplitPage(page1, page2));
				page1 = null;
				page2 = null;
			}
		}
		
		if (page1 != null) {
			//got one part but not enough to fit another
			pages.add(new HSplitPage(page1, new PlainTextPage("")));
			page1 = null;
		}
		
//		pages.add(new PlainTextPage("this is page 1"));
//		pages.add(new HSplitPage(new PlainTextPage("This is the new and improved page 2 description"), new ImagePage(new ResourceLocation(Armory.MODID + ":textures/gui/image1.png"), 50, 50, 0, 0, 50, 50, null), true));
//		pages.add(new PlainTextPage("this is page 3!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"));
//		pages.add(new ImagePage(new ResourceLocation(Armory.MODID + ":textures/gui/image1.png"), 50, 50, 0, 0, 50, 50, Lists.asList("Cool image tooltip", new String[0])));
//		pages.add(new HSplitPage(new PlainTextPage("This page is nearly identical to page 2"), new ImagePage(new ResourceLocation(Armory.MODID + ":textures/gui/image1.png"), 50, 50, 0, 0, 50, 50, null), false));
//		pages.add(new ItemPage(new ItemStack(Items.iron_axe, 2)));
		ArmoryBook.screen = new ArmoryBookScreen(pages);
	}
	
	private static final IBookPage generateFuelPage(Item item, FuelRecord record) {
		return new VDynamicSplitPage(
				new ItemPage(new ItemStack(item)),
				new LinedTextPage(Lists.newArrayList("", "", "Max Heat: " + (int) record.getMaxHeat(), "Heat Rate: " + String.format("%.2f", record.getHeatRate()), "Burn Time: " + record.getBurnTicks())),
				30
				);
	}
	
	private static final IBookPage generateMetalPage(MetalRecord record) {
		return new VDynamicSplitPage(
				new ItemPage(new ItemStack(record.getMetal())),
				new LinedTextPage(Lists.newArrayList("", "", "", "Heat: " + (int) record.getRequiredHeat())),
				30
				);
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
