package com.SkyIsland.Armory.items.tools;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.SkyIsland.Armory.Armory;
import com.SkyIsland.Armory.api.ForgeManager;
import com.SkyIsland.Armory.api.ForgeManager.ForgeRecipe;
import com.SkyIsland.Armory.config.ModConfig;
import com.SkyIsland.Armory.gui.ArmoryBookScreen;
import com.SkyIsland.Armory.gui.HSplitPage;
import com.SkyIsland.Armory.gui.IBookPage;
import com.SkyIsland.Armory.gui.ImagePage;
import com.SkyIsland.Armory.gui.PlainTextPage;
import com.SkyIsland.Armory.gui.TableRecipePage;
import com.SkyIsland.Armory.items.ToolItems;
import com.SkyIsland.Armory.items.ToolItems.Tools;
import com.SkyIsland.Armory.mechanics.ExtendedSmith;

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
		pages.add(new HSplitPage(new ImagePage(new ResourceLocation(Armory.MODID + ":textures/gui/recipe_book_front.png"), 75, 75, 0, 0, 75, 75, null), new PlainTextPage("The Smith's Recipe Book")));
		
		pages.add(new PlainTextPage("Within this book are your notes on what you've learned about smithing. You've jotted down what you've discovered, and what you think each piece should look like."));
		pages.add(new PlainTextPage("You are certain you can learn more as you gain experience, and will commit your knowledge here as you learn more."));
		
		ExtendedSmith prec = ExtendedSmith.get(player, true);
		for (ForgeRecipe recipe : ForgeManager.instance().getForgeRecipes()) {
			if (prec.getLevel() < recipe.getRequiredLevel())
				continue;
			pages.add(generateRecipePage(player, recipe));
		}
		
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
		int playerLevel = ExtendedSmith.get(player, true).getLevel();
		map = produceMess(map, playerLevel, recipe.getRequiredLevel(), rand);
		
		return new TableRecipePage(recipe.getTitle(), map);
	}
	
	private boolean[][] produceMess(boolean[][] original, int level, int reqLevel, Random random) {
		if (level >= reqLevel + ModConfig.config.getSmithTolerance())
			return original;
		
		float chance = 1.0f;
		if (level >= reqLevel) {
			//adjust chance. less chance the closer level is to reqLevel + tolerance
			float rate = (1.0f / (float) ModConfig.config.getSmithTolerance());
			chance =  1.0f - (rate * (float) (level - reqLevel));
		}
		boolean[][] ret = new boolean[10][10];
		
		for (int i = 0; i < 10; i++)
		for (int j = 0; j < 10; j++) {
			if (!original[i][j])
				continue;
			if (random.nextFloat() > chance) {
				//transcribe it true
				ret[i][j] = true;
				continue;
			}

			//need to mess it up
			//turn it off, turn on a neighbor. any direction
			// > ret[i][j] = false  //false by default
			
			//pick a side to mess up
			boolean horizontal = random.nextBoolean();
			boolean up = random.nextBoolean(); //if horizontal, means right
			if (horizontal) {
				if (up && i < 10)
					ret[i + 1][j] = true;
				if (!up && i > 0)
					ret[i - 1][j] = true;
			} else {
				if (up && j < 10)
					ret[i][j + 1] = true;
				if (!up && j > 0)
					ret[i][j - 1] = true;
				
			}
		}
		
		return ret;
	}
}
