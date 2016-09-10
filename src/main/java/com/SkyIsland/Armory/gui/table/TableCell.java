package com.SkyIsland.Armory.gui.table;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;

public class TableCell extends GuiButton {

	private static final Color filledColor = new Color(0x3E, 0x12, 0x09, 0xA0);
	
	private static final Color scrapColor = new Color(0x3D, 0x32, 0x30, 0xA0);

	private static final int CELL_SIZE = 15;
	
	private int x;
	
	private int y;
	
	private TableGui parent;
	
	public TableCell(int id, TableGui parent, int x, int y, int displayX, int displayY) {
		super(id, displayX, displayY, 15, 15, null);
		this.x = x;
		this.y = y;
		this.parent = parent;
		
		//TODO set width, height
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
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
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (visible && getFilled())
        {
        	GlStateManager.pushMatrix();
    		
        	Color color = filledColor;
        	if (parent.isCooled())
        		color = scrapColor;
        	
    		this.drawGradientRect(xPosition, yPosition, xPosition + CELL_SIZE, yPosition + CELL_SIZE, color.brighter().getRGB(), color.getRGB());
    		
    		GlStateManager.popMatrix();
            
        }
        
        //this.drawCenteredString(Minecraft.getMinecraft().fontRendererObj, x + "|" + y, xPosition, yPosition, 0xFFFFFFFF);
        //super.drawButton(mc, mouseX, mouseY);
    }
	
}
