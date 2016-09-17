package com.SkyIsland.Armory.gui;

import com.SkyIsland.Armory.Armory;
import com.SkyIsland.Armory.config.IConfigWatcher;
import com.SkyIsland.Armory.config.ModConfig;
import com.SkyIsland.Armory.items.ToolItems;
import com.SkyIsland.Armory.items.ToolItems.Tools;
import com.SkyIsland.Armory.items.tools.Tongs;
import com.SkyIsland.Armory.mechanics.ExtendedSmith;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.MinecraftForge;

public class TongsOverlay extends Gui implements IOverlay, IConfigWatcher {

	private static final ResourceLocation TONGS_TEXT = new ResourceLocation(Armory.MODID + ":textures/items/tongs.png");
	
	private static final ResourceLocation TONGS_FULL_TEXT = new ResourceLocation(Armory.MODID + ":textures/items/tongs_full.png");
	
	private static final int TONGS_TEXT_HEIGHT = 32;
	
	private static final int TONGS_TEXT_WIDTH = 32;
	
	private static final String PREFIX_UNLOCAL = "text.smith_level";
	
	private final String levelPrefix;
	
	private EntityPlayer player;
	
	private boolean showTongs;
	
	private boolean showLevel;
	
	public TongsOverlay(EntityPlayer player) {
		this.player = player;
		
		if (player != null)
			MinecraftForge.EVENT_BUS.register(this);
		else
			System.out.println("player is null");
		
		this.levelPrefix = StatCollector.translateToLocal(PREFIX_UNLOCAL);
		
		onConfigUpdate(ModConfig.config);
		
		ModConfig.config.registerWatcher(this);
	}
	
	@Override
	public void drawOverlay() {
		if (player.getHeldItem() == null || !(player.getHeldItem().getItem() instanceof Tongs)) {
			return;
		}
		
		
		Tongs inst = (Tongs) ToolItems.getItem(Tools.TONGS);
		ItemStack tongs = player.getHeldItem();
		ItemStack held = inst.getHeldItem(tongs);
		ExtendedSmith smithRecord = ExtendedSmith.get(player, true);
		
		Minecraft mc = Minecraft.getMinecraft();
		ScaledResolution resolution = new ScaledResolution(mc);
		int height = resolution.getScaledHeight();
		int width = resolution.getScaledWidth();
		
//		GlStateManager.pushMatrix();
//		GlStateManager.pushAttrib();
		
		GlStateManager.color(1f, 1f, 1f, 1f);
		
		if (showTongs) {
			if (held == null)
				Minecraft.getMinecraft().getTextureManager().bindTexture(TONGS_TEXT);
			else
				Minecraft.getMinecraft().getTextureManager().bindTexture(TONGS_FULL_TEXT);
			
			this.drawTexturedModalRect(width - (TONGS_TEXT_WIDTH + 10), height - (TONGS_TEXT_HEIGHT + 10), 0, 0, TONGS_TEXT_WIDTH, TONGS_TEXT_HEIGHT);
		}
		
		if (showLevel)
		if (smithRecord.getLevel() > 0 || smithRecord.getProgress() > 0) {
			this.drawCenteredString(mc.fontRendererObj,
					levelPrefix + " " + smithRecord.getLevel(), width/2, 20, 0xFF000000);
		}
		
//		
//		GlStateManager.popAttrib();
//		GlStateManager.popMatrix();
	}

	@Override
	public void onConfigUpdate(ModConfig config) {
		this.showLevel = config.showLevel();
		this.showTongs = config.showTongs();
	}
	
}
