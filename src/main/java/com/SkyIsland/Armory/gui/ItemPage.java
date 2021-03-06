package com.SkyIsland.Armory.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;

public class ItemPage implements IBookPage {
	
	private ItemStack item;
	
	private int widthCache;
	
	private int heightCache;
	
	public ItemPage(ItemStack item) {
		this.item = item;
	}
	
	@Override
	public void draw(ArmoryBookScreen parent, FontRenderer fonter, int xoffset, int yoffset, int width, int height) {
		widthCache = width;
		heightCache = height;
		
		int centerx = xoffset + (width / 2);
		int centery = yoffset + (height / 2);
		centerx -= 8; //offset for 16x16 item icon
		centery -= 8;
		
		GlStateManager.pushMatrix();
		
		RenderItem itemRender = parent.getRenderItem();
		GlStateManager.translate(0.0F, 0.0F, 32.0F);
        itemRender.zLevel = 200.0F;
        net.minecraft.client.gui.FontRenderer font = null;
        if (item != null) font = item.getItem().getFontRenderer(item);
        if (font == null) font = fonter;
        itemRender.renderItemAndEffectIntoGUI(item, centerx, centery);
        itemRender.zLevel = 0.0F;
		
		GlStateManager.popMatrix();
	}

	@Override
	public void overlay(ArmoryBookScreen parent, FontRenderer fonter, int mouseX, int mouseY, int trueX, int trueY) {
		if (item != null) {
			int centerx = widthCache / 2;
			int centery = heightCache / 2;
			int x = centerx - 8;
			int y = centery - 8;
			
			if (mouseX > x && mouseX < x + 16)
			if (mouseY > y && mouseY < y + 16)
				parent.renderTooltip(item, trueX, trueY);
		}
		
		
	}
	
}
