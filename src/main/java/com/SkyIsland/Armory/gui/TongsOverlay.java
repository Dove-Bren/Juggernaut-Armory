package com.SkyIsland.Armory.gui;

import com.SkyIsland.Armory.Armory;
import com.SkyIsland.Armory.items.ToolItems;
import com.SkyIsland.Armory.items.ToolItems.Tools;
import com.SkyIsland.Armory.items.tools.Tongs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TongsOverlay extends Gui implements IOverlay {

	private static final ResourceLocation TONGS_TEXT = new ResourceLocation(Armory.MODID + ":textures/items/tongs.png");
	
	private static final ResourceLocation TONGS_FULL_TEXT = new ResourceLocation(Armory.MODID + ":textures/items/tongs_full.png");
	
	private static final int TONGS_TEXT_HEIGHT = 32;
	
	private static final int TONGS_TEXT_WIDTH = 32;
	
	private EntityPlayer player;
	
	public TongsOverlay(EntityPlayer player) {
		this.player = player;
		
		if (player != null)
			MinecraftForge.EVENT_BUS.register(this);
		else
			System.out.println("player is null");
	}
	
	@Override
	public void drawOverlay() {
System.out.print(".");
		if (player.getHeldItem() == null || !(player.getHeldItem().getItem() instanceof Tongs)) {
			return;
		}
		
System.out.print(",");
		
		Tongs inst = (Tongs) ToolItems.getItem(Tools.TONGS);
		ItemStack tongs = player.getHeldItem();
		ItemStack held = inst.getHeldItem(tongs);
		
		Minecraft mc = Minecraft.getMinecraft();
		ScaledResolution resolution = new ScaledResolution(mc);
		int height = resolution.getScaledHeight();
		
		GlStateManager.pushMatrix();
		GlStateManager.pushAttrib();
		
		GlStateManager.color(1f, 1f, 1f, 1f);
		
		if (held == null)
			Minecraft.getMinecraft().getTextureManager().bindTexture(TONGS_TEXT);
		else
			Minecraft.getMinecraft().getTextureManager().bindTexture(TONGS_FULL_TEXT);
		
		this.drawTexturedModalRect(10, height - (TONGS_TEXT_HEIGHT + 10), 0, 0, TONGS_TEXT_WIDTH, TONGS_TEXT_HEIGHT);
		
		
		
		
		GlStateManager.popAttrib();
		GlStateManager.popMatrix();
	}
	
}
