package com.SkyIsland.Armory.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

public class PlainTextPage implements IBookPage {
	
	private String text;
	
	public PlainTextPage(String text) {
		this.text = text;
	}

	@Override
	public void draw(Gui parent, FontRenderer fonter, int xoffset, int yoffset, int width, int height) {
		fonter.drawSplitString(text, xoffset, yoffset, width, 0x000000);
	}
	
}
