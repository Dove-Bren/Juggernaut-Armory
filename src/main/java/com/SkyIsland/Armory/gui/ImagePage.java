package com.SkyIsland.Armory.gui;

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
	
	public ImagePage(ResourceLocation image, int width, int height) {
		this(image, width, height, 0, 0);
	}
	
	public ImagePage(ResourceLocation image, int width, int height, int uoffset, int voffset) {
		this(image, width, height, uoffset, voffset, -1, -1);
	}
	
	public ImagePage(ResourceLocation image, int width, int height, int uoffset, int voffset, int textWidth, int textHeight) {
		this.image = image;
		this.width = width;
		this.height = height;
		this.uoffset = uoffset;
		this.voffset = voffset;
		this.textWidth = textWidth;
		this.textHeight = textHeight;
	}

	@Override
	public void draw(Gui parent, FontRenderer fonter, int xoffset, int yoffset, int width, int height) {
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
	
}
