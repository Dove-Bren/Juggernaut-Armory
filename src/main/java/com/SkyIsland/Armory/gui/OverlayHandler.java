package com.SkyIsland.Armory.gui;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class OverlayHandler {

	private static TongsOverlay tongsOverlay;
	
	public OverlayHandler() {
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void onGuiDraw(RenderGameOverlayEvent.Post event) 
	{
		if (tongsOverlay == null)
			tongsOverlay = new TongsOverlay(Minecraft.getMinecraft().thePlayer);
		
		tongsOverlay.drawOverlay();
	}
}
