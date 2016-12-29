package com.SkyIsland.Armory.client.armor;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;

/**
 * Less lossy wrapper for all head model renderers
 * @author Skyler
 *
 */
public class RendererArmorChest extends ModelBiped {


	private ModelRenderer pauldronLeft;
	private ModelRenderer pauldronLeftAccent;
	private ModelRenderer pauldronRight;
	private ModelRenderer pauldronRightAccent;
	private ModelRenderer vambraceLeft;
	private ModelRenderer vambraceLeftAccent;
	private ModelRenderer vambraceRight;
	private ModelRenderer vambraceRightAccent;
	private ModelRenderer breastplate;
	private ModelRenderer breastplateLeftAccent;
	private ModelRenderer breastplateLeftAccent2;
	private ModelRenderer breastplateRightAccent;
	private ModelRenderer breastplateRightAccent2;
	
	public RendererArmorChest(int x, int y) {
		float scale = 1.0f;
		pauldronLeft = new ModelRenderer(this);
		pauldronLeft.setRotationPoint(rotationPointXIn, rotationPointYIn, rotationPointZIn);
	}

}
