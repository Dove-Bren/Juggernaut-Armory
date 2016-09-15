package com.SkyIsland.Armory.items.tools;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.SkyIsland.Armory.Armory;
import com.SkyIsland.Armory.api.ForgeManager;
import com.SkyIsland.Armory.api.ForgeManager.ForgeRecipe;
import com.SkyIsland.Armory.gui.ArmoryBookScreen;
import com.SkyIsland.Armory.gui.HSplitPage;
import com.SkyIsland.Armory.gui.IBookPage;
import com.SkyIsland.Armory.gui.ImagePage;
import com.SkyIsland.Armory.gui.PlainTextPage;
import com.SkyIsland.Armory.gui.TableRecipePage;
import com.SkyIsland.Armory.items.ToolItems;
import com.SkyIsland.Armory.items.ToolItems.Tools;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ForgeRecipeBook extends ArmoryBook {

	public ForgeRecipeBook(String unlocalizedName) {
		super(unlocalizedName);
	}

	@Override
	public void init() {
		GameRegistry.addShapelessRecipe(new ItemStack(this), Items.iron_ingot, Items.book, ToolItems.getItem(Tools.TONGS));
	}
	
	@Override
	public void clientInit() {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
    	.register(this, 0, new ModelResourceLocation(Armory.MODID + ":" + this.registryName, "inventory"));
	}
	
//	private static final IBookPage generateFuelPage(Item item, FuelRecord record) {
//		return new VDynamicSplitPage(
//				new ItemPage(new ItemStack(item)),
//				new LinedTextPage(Lists.newArrayList("", "", "Max Heat: " + (int) record.getMaxHeat(), "Heat Rate: " + String.format("%.2f", record.getHeatRate()), "Burn Time: " + record.getBurnTicks())),
//				30
//				);
//	}
//	
//	private static final IBookPage generateMetalPage(MetalRecord record) {
//		return new VDynamicSplitPage(
//				new ItemPage(new ItemStack(record.getMetal())),
//				new LinedTextPage(Lists.newArrayList("", "", "", "Heat: " + (int) record.getRequiredHeat())),
//				30
//				);
//	}
	
	@Override
	public ArmoryBookScreen getScreen(EntityPlayer player) {
		return generateGui(player);
	}
	
	private ArmoryBookScreen generateGui(EntityPlayer player) {
		List<IBookPage> pages = new LinkedList<IBookPage>();
		pages.add(new PlainTextPage(""));
		pages.add(new HSplitPage(new ImagePage(new ResourceLocation(Armory.MODID + ":textures/gui/recipe_book_front.png"), 75, 75, 0, 0, 75, 75, null), new PlainTextPage("A Smith's Guide")));
		
		pages.add(new PlainTextPage("Within this book are your notes on what you've learned about smithing. You've jotted down what you've discovered, and what you think each piece should look like."));
		pages.add(new PlainTextPage("You are certain you can learn more as you gain experience, and will commit your knowledge here as you learn more."));
		
		for (ForgeRecipe recipe : ForgeManager.instance().getForgeRecipes()) {
			pages.add(generateRecipePage(player, recipe));
		}
		
		pages.add(new TableRecipePage(ForgeRecipe.drawMap(new String[]{
			" ", "  ..  ..", "  ......", "   ....", "   ....", "   ....",
			"    ..", "   ....", "  ......", " "
		})));

		
		
		return new ArmoryBookScreen(pages);
	}
	
	private IBookPage generateRecipePage(EntityPlayer player, ForgeRecipe recipe) {
		boolean[][] map = recipe.getRawMap();
		
		long seed = player.getUniqueID().getLeastSignificantBits()
				^ player.getUniqueID().getMostSignificantBits();
				//xor the longs to get the seed
		//offset seed based on armor piece
		seed += recipe.getSeedOffset();
		
		
		Random rand = new Random(seed);
		
		//TODO mess up map based on rand AND player skill
		//Make player skills!
		//unlock pages due to player skills!
		
		return new TableRecipePage(map);
	}
}
