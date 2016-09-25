package com.SkyIsland.Armory.gui;

import com.SkyIsland.Armory.Armory;
import com.SkyIsland.Armory.config.IConfigWatcher;
import com.SkyIsland.Armory.config.ModConfig;
import com.SkyIsland.Armory.items.HeldMetal;
import com.SkyIsland.Armory.items.MiscItems;
import com.SkyIsland.Armory.items.ToolItems;
import com.SkyIsland.Armory.items.ToolItems.Tools;
import com.SkyIsland.Armory.items.tools.Tongs;
import com.SkyIsland.Armory.mechanics.ExtendedSmith;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.MinecraftForge;

public class TongsOverlay implements IOverlay, IConfigWatcher {

	private static final ResourceLocation TONGS_TEXT = new ResourceLocation(Armory.MODID + ":textures/items/tongs.png");
	
	private static final ResourceLocation TONGS_FULL_TEXT = new ResourceLocation(Armory.MODID + ":textures/items/tongs_full.png");
	
	private static final int TONGS_TEXT_HEIGHT = 32;
	
	private static final int TONGS_TEXT_WIDTH = 32;
	
	private static final String PREFIX_UNLOCAL = "text.smith_level";
	
	private static final int TOP_COLOR = 0xCC333333;
	
	private static final int BOTTOM_COLOR = 0x99333333;
	
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
		HeldMetal heldInst = (HeldMetal) MiscItems.getItem(MiscItems.Items.HELD_METAL);
		ItemStack tongs = player.getHeldItem();
		ItemStack held = inst.getHeldItem(tongs);
		ExtendedSmith smithRecord = ExtendedSmith.get(player, true);
		
		Minecraft mc = Minecraft.getMinecraft();
		ScaledResolution resolution = new ScaledResolution(mc);
		int height = resolution.getScaledHeight();
		int width = resolution.getScaledWidth();
		
//		GlStateManager.pushMatrix();
		
//		if (ModConfig.config.usePushpop())
//			GlStateManager.pushAttrib();
		GlStateManager.color(1f, 1f, 1f, 1f);
		
		if (showTongs) {
			if (held == null)
				Minecraft.getMinecraft().getTextureManager().bindTexture(TONGS_TEXT);
			else
				Minecraft.getMinecraft().getTextureManager().bindTexture(TONGS_FULL_TEXT);
			

			GlStateManager.enableAlpha();
			GlStateManager.enableBlend();
			
			Gui.drawModalRectWithCustomSizedTexture(
					width - (TONGS_TEXT_WIDTH + 10), height - (TONGS_TEXT_HEIGHT + 10), 0, 0,
					TONGS_TEXT_WIDTH, TONGS_TEXT_HEIGHT, TONGS_TEXT_WIDTH, TONGS_TEXT_HEIGHT);
			
			
			GlStateManager.disableAlpha();
			
			if (held != null && held.getItem() instanceof HeldMetal) {
				//draw rectangle and details on tectangle
				
				int left = width - (TONGS_TEXT_WIDTH + 80); //width 70
				int top = height - (50); //height 45
				
				this.drawGradientRect(left, top,
						left + 70, top + 45, TOP_COLOR, BOTTOM_COLOR);
				
				FontRenderer fonter = mc.fontRendererObj;
				int anchor;
				String str = held.getDisplayName();
				anchor = fonter.getStringWidth(str);
				
				this.drawString(fonter, str, left + (70 - anchor) - 5, top + 10, 
						0xFFC0CCC0, false);
				
				str = "Heat: " + (int) Math.round(heldInst.getHeat(held));
				anchor = fonter.getStringWidth(str);
				
				this.drawString(fonter, str, left + (70 - anchor) - 5, top + 30, 
						0xFFC0CCC0, false);
			}
		}
		
		if (showLevel)
		if (smithRecord.getLevel() > 0 || smithRecord.getProgress() > 0) {
			FontRenderer fonter = mc.fontRendererObj;
			String str = levelPrefix + " " + smithRecord.getLevel();
			int center = width/2;
			int strhalf = fonter.getStringWidth(str) / 2;
			int strheight = fonter.FONT_HEIGHT / 2;
			
			//add to pad
			strhalf += 5;
			strheight += 2;
			
			this.drawGradientRect(center - strhalf, 10 - strheight, center + strhalf, 10 + strheight, TOP_COLOR, BOTTOM_COLOR);
			this.drawCenteredString(fonter, str, center, 10 - (fonter.FONT_HEIGHT / 2),
					0xFFA0A0A0, true);
		}
		
		if (ModConfig.config.usePushpop())
			GlStateManager.popAttrib();
//		GlStateManager.popMatrix();
	}

	@Override
	public void onConfigUpdate(ModConfig config) {
		this.showLevel = config.showLevel();
		this.showTongs = config.showTongs();
	}
	
	/**
     * Draws a rectangle with a vertical gradient between the specified colors (ARGB format). Args : x1, y1, x2, y2,
     * topColor, bottomColor
     * Adopted from Gui class
     */
    protected void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor) {
//    	if (ModConfig.config.usePushpop())
//    		GlStateManager.pushAttrib();
        float f = (float)(startColor >> 24 & 255) / 255.0F;
        float f1 = (float)(startColor >> 16 & 255) / 255.0F;
        float f2 = (float)(startColor >> 8 & 255) / 255.0F;
        float f3 = (float)(startColor & 255) / 255.0F;
        float f4 = (float)(endColor >> 24 & 255) / 255.0F;
        float f5 = (float)(endColor >> 16 & 255) / 255.0F;
        float f6 = (float)(endColor >> 8 & 255) / 255.0F;
        float f7 = (float)(endColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
//        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
//        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
//        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos((double)right, (double)top, 0).color(f1, f2, f3, f).endVertex();
        worldrenderer.pos((double)left, (double)top, 0).color(f1, f2, f3, f).endVertex();
        worldrenderer.pos((double)left, (double)bottom, 0).color(f5, f6, f7, f4).endVertex();
        worldrenderer.pos((double)right, (double)bottom, 0).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
//        GlStateManager.shadeModel(7424);
//        GlStateManager.disableBlend();
//        if (ModConfig.config.usePushpop()) {
//        	GlStateManager.popAttrib();
//        } else {
        	GlStateManager.enableAlpha();
        	GlStateManager.enableTexture2D();
//        }
        	
//        GlStateManager.popAttrib();
    }
    
    protected void drawString(FontRenderer fonter, String str, int x, int y, int color, boolean drop) {
//    	GlStateManager.pushAttrib();
//    	GlStateManager.disableBlend();
    	fonter.drawString(str, x, y, color, drop);
//    	GlStateManager.enableBlend();
//    	GlStateManager.popAttrib();
    }
    
    protected void drawCenteredString(FontRenderer fonter, String str, int x, int y, int color, boolean drop) {
    	int centerx = x - (fonter.getStringWidth(str) / 2);
    	drawString(fonter, str, centerx, y, color, drop);
    }
	
}
