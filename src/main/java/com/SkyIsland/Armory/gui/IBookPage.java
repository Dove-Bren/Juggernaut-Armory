package com.SkyIsland.Armory.gui;

import net.minecraft.client.gui.FontRenderer;

public interface IBookPage {

	public void draw(ArmoryBookScreen parent, FontRenderer fonter, int xoffset, int yoffset, int width, int height);

	public void overlay(ArmoryBookScreen parent, FontRenderer fonter, int mouseX, int mouseY, int trueX, int trueY);
	
}
