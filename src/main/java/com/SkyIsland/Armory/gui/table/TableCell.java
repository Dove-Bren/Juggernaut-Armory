package com.SkyIsland.Armory.gui.table;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;

public class TableCell extends GuiButton {

	private static final Color filledColor = new Color(0x20, 0x20, 0x10, 0x40);

	private static final int CELL_SIZE = 15;
	
	private int x;
	
	private int y;
	
	private TableGui parent;
	
	public TableCell(int id, TableGui parent, int x, int y, int displayX, int displayY) {
		super(id, displayX, displayY, null);
		this.x = x;
		this.y = y;
		this.parent = parent;
		
		//TODO set width, height
	}
	
	public boolean getFilled() {
		if (parent.metalMap == null)
			return false;
		
		return parent.metalMap[x][y];
	}
	
	/**
     * Draws this button to the screen.
     */
    @Override
    public void drawButton(Minecraft mc, int parX, int parY) {
        if (visible && getFilled())
        {
        	GlStateManager.pushMatrix();
    		
    		this.drawGradientRect(x, y, x + CELL_SIZE, y + CELL_SIZE, filledColor.brighter().getRGB(), filledColor.getRGB());
    		
    		GlStateManager.popMatrix();
            
        }
    }
	
}
