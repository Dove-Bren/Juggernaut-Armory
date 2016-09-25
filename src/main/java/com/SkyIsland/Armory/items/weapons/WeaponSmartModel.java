package com.SkyIsland.Armory.items.weapons;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.ISmartItemModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("deprecation")
@SideOnly(Side.CLIENT)
public class WeaponSmartModel implements ISmartItemModel {
	
	//private ModelResourceLocation flatLocation;
	
	//private Collection<ItemStack> subItems;
	
	private Collection<ItemStack> subItems;
	
	public WeaponSmartModel() {
	}
	
	@Override
	public IBakedModel handleItemState(ItemStack stack) {
		if (stack != null && stack.getItem() instanceof Weapon) {
			subItems = ((Weapon) stack.getItem()).getWeaponComponents(stack);
		} else {
			subItems = new LinkedList<ItemStack>();
		}
		return this;
		
	}
	
	@Override
	public List<BakedQuad> getFaceQuads(EnumFacing facing) {
		
		List<BakedQuad> quadList = new LinkedList<BakedQuad>();
		
		ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
		
		if (subItems != null && !subItems.isEmpty())
		for (ItemStack stack : subItems) {
			IBakedModel model = mesher.getItemModel(stack);
			if (model == null || model == mesher.getModelManager().getMissingModel()) {
				System.out.println("Failed to get model for " + stack);
			} else
			quadList.addAll(
					Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
					.getItemModel(stack).getFaceQuads(facing)
					);
		}
		
		return quadList;
	}

	@Override
	public List<BakedQuad> getGeneralQuads() {
		List<BakedQuad> quadList = new LinkedList<BakedQuad>();
		
		ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
		
		if (subItems != null && !subItems.isEmpty())
		for (ItemStack stack : subItems) {
			IBakedModel model = mesher.getItemModel(stack);
			if (model == null || model == mesher.getModelManager().getMissingModel()) {
				System.out.println("Failed to get model for " + stack);
			} else
			quadList.addAll(
					model.getGeneralQuads()
					);
		}
		
		return quadList;
		
	}

	@Override
	public boolean isAmbientOcclusion() {
		return false;
	}

	@Override
	public boolean isGui3d() {
		return false;
	}

	@Override
	public boolean isBuiltInRenderer() {
		return false;
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return Minecraft.getMinecraft().getTextureMapBlocks()
			.getMissingSprite();
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
			return ItemCameraTransforms.DEFAULT;
	}
}
