package com.SkyIsland.Armory.gui;

import java.awt.Color;

import com.SkyIsland.Armory.Armory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class TableRecipePage implements IBookPage {
	
	private static final ResourceLocation TEXT = new ResourceLocation(Armory.MODID + ":textures/gui/table_recipe.png");
	
	private static final int TEXT_WIDTH = 95;
	
	private static final int TEXT_HEIGHT = 95;
	
	private static final int CELL_SIZE = 7;
	
	private static final int CELL_HOFFSET = 8;
	
	private static final int CELL_VOFFSET = 8;
	
	private static final int COLOR = new Color(31, 68, 20).getRGB();
	
	private boolean[][] map;
	
//	private int widthCache;
//	
//	private int heightCache;
	
	public TableRecipePage(boolean[][] map) {
		this.map = map;
	}

	@Override
	public void draw(ArmoryBookScreen parent, FontRenderer fonter, int xoffset, int yoffset, int width, int height) {
//		widthCache = width;
//		heightCache = height;
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(TEXT);
		
		int centerx = xoffset + (width / 2);
		int centery = yoffset + (height / 2);
		int x = centerx - (TEXT_WIDTH / 2);
		int y = centery - (TEXT_HEIGHT / 2);
		
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, TEXT_WIDTH, TEXT_HEIGHT, TEXT_WIDTH, TEXT_HEIGHT);
		
		//now draw cells
		int xpos, ypos;
		ypos = (CELL_VOFFSET) + y;
		for (int i = 0; i < 10; i++) {
			xpos = (CELL_HOFFSET) + x;
			for (int j = 0; j < 10; j++) {
				if (!map[i][j]) {
					xpos += 1 + CELL_SIZE;
					continue;
				}
				
				Gui.drawRect(xpos, ypos, xpos + CELL_SIZE, ypos + CELL_SIZE,
						COLOR);
				xpos += 1 + CELL_SIZE;
			}
			ypos += 1 + CELL_SIZE;
		}
	}

	@Override
	public void overlay(ArmoryBookScreen parent, FontRenderer fonter, int mouseX, int mouseY, int trueX, int trueY) {
		;
	}
	
}
