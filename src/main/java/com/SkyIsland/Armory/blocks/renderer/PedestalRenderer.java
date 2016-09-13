package com.SkyIsland.Armory.blocks.renderer;

import org.lwjgl.opengl.GL11;

import com.SkyIsland.Armory.blocks.Pedestal.PedestalTileEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("deprecation")
@SideOnly(Side.CLIENT)
public class PedestalRenderer extends TileEntitySpecialRenderer<PedestalTileEntity> {

	@Override
	public void renderTileEntityAt(PedestalTileEntity te, double x, double y, double z, float partialTicks,
			int destroyStage) {

		if (te.getHeld() == null)
			return;
		
		boolean rotate = false;
		if (te.getBlockMetadata() > 3) //3 and 4 are W and E facings
			rotate = true;
		
		GlStateManager.pushMatrix();
		

		
		//don't use GL11 stuff, use the manager
		//GL11.glTranslated(x, y + 1.0f, z);
		GlStateManager.translate(x + 0.5, y + 0.85, z + 0.5);
		

		
		if (rotate) {
		GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
//			GlStateManager.rotate(
//					ModConfig.config.getTestValue(Key.ROTATE_ANGLE),
//					ModConfig.config.getTestValue(Key.ROTATE_X),
//					ModConfig.config.getTestValue(Key.ROTATE_Y),
//					ModConfig.config.getTestValue(Key.ROTATE_Z));
		}

//			if (te.heldRig.getItem() instanceof ItemSword
//					|| te.heldRig.getItem() instanceof Weapon)
		GlStateManager.rotate(225.0f, 0.0f, 0.0f, 1.0f);
			
		
		GlStateManager.enableRescaleNormal();
		GlStateManager.scale(1.0, 1.0, 1.0); //tweak for making smaller!
		
		Minecraft.getMinecraft().getRenderItem().renderItem(te.getHeld(), TransformType.GROUND);
		GlStateManager.disableRescaleNormal();
		
		GL11.glPopMatrix();
		
	}
	
}
