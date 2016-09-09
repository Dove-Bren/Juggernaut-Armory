package com.SkyIsland.Armory.gui;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class ImagePage implements IBookPage {
	
	private ResourceLocation image;
	
	private int width;
	
	private int height;
	
	private int uoffset;
	
	private int voffset;
	
	private int textWidth;
	
	private int textHeight;
	
	private int widthCache;
	
	private int heightCache;
	
	private List<String> tooltip;
	
	public ImagePage(ResourceLocation image, int width, int height) {
		this(image, width, height, 0, 0);
	}
	
	public ImagePage(ResourceLocation image, int width, int height, List<String> tooltip) {
		this(image, width, height, 0, 0);
		this.tooltip = tooltip;
	}
	
	public ImagePage(ResourceLocation image, int width, int height, int uoffset, int voffset) {
		this(image, width, height, uoffset, voffset, -1, -1, null);
	}
	
	public ImagePage(ResourceLocation image, int width, int height, int uoffset, int voffset, int textWidth, int textHeight, List<String> tooltip) {
		this.image = image;
		this.width = width;
		this.height = height;
		this.uoffset = uoffset;
		this.voffset = voffset;
		this.textWidth = textWidth;
		this.textHeight = textHeight;
		this.tooltip = tooltip;
	}

	@Override
	public void draw(ArmoryBookScreen parent, FontRenderer fonter, int xoffset, int yoffset, int width, int height) {
		widthCache = width;
		heightCache = height;
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(image);
		
		int centerx = xoffset + (width / 2);
		int centery = yoffset + (height / 2);
		int x = centerx - (this.width / 2);
		int y = centery - (this.height / 2);
		
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		if (textWidth == -1 || textHeight == -1)
			parent.drawTexturedModalRect(x, y, uoffset, voffset, this.width, this.height);
		else
			Gui.drawModalRectWithCustomSizedTexture(x, y, uoffset, voffset, this.width, this.height, textWidth, textHeight);
	}

	@Override
	public void overlay(ArmoryBookScreen parent, FontRenderer fonter, int mouseX, int mouseY, int trueX, int trueY) {
		if (tooltip != null) {
			int centerx = widthCache / 2;
			int centery = heightCache / 2;
			int x = centerx - (this.width / 2);
			int y = centery - (this.height / 2);
			
			if (mouseX > x && mouseX < x + this.width)
			if (mouseY > y && mouseY < y + this.height)
				parent.renderTooltip(tooltip, trueX, trueY);

			fonter.drawString("x: " + x + "     y: " + y, 10, 50, 0xAACCFF);
			fonter.drawString("mx: " + mouseX + "    mouseY: " + mouseY, 10, 70, 0xCCFFAA);
		}
	}
	
}