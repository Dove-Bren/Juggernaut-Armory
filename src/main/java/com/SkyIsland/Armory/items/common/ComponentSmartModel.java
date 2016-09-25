package com.SkyIsland.Armory.items.common;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.ISmartItemModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("deprecation")
@SideOnly(Side.CLIENT)
public class ComponentSmartModel implements ISmartItemModel {
	
	private AComponent originPiece;
	
	//private String texturePrefix;
	
	private IBakedModel baseModel;
	
	public ComponentSmartModel(AComponent piece, IBakedModel baseModel, float zheight) {
		this.originPiece = piece;
		this.baseModel = baseModel;
		//this.zheight = zheight;
		
	}
	
	@Override
	public IBakedModel handleItemState(ItemStack stack) {
		ModelManager manager = Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
				.getModelManager();
		
		IBakedModel model = manager.getModel(
						originPiece.constructModelLocation(stack, "inventory")
						);
		
		return model;
		
	}

	@Override
	public List<BakedQuad> getFaceQuads(EnumFacing p_177551_1_) {
		if (baseModel == null) {
			return new ArrayList<BakedQuad>();
		}
		return baseModel.getFaceQuads(p_177551_1_);
	}

	@Override
	public List<BakedQuad> getGeneralQuads() {
		return new LinkedList<BakedQuad>();
	}

	@Override
	public boolean isAmbientOcclusion() {
		if (baseModel == null)
			return false;
		return baseModel.isAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() {
		if (baseModel == null)
			return false;
		return baseModel.isGui3d();
	}

	@Override
	public boolean isBuiltInRenderer() {
		return false;
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		if (baseModel == null)
			return Minecraft.getMinecraft().getTextureMapBlocks()
				.getMissingSprite();
		
		return baseModel.getParticleTexture();
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		if (baseModel == null)
			return ItemCameraTransforms.DEFAULT;
		
		return baseModel.getItemCameraTransforms();
	}
	
}
