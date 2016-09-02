package com.SkyIsland.Armory.items.armor;

import java.util.Collection;

import com.SkyIsland.Armory.Armory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

@SuppressWarnings("deprecation")
public class ArmorPlayerModel extends ModelBiped {

	//private Collection<IBakedModel> models;
	
	//private ArmorSlot slot;
	
	private Armor armor;
	
	public ArmorPlayerModel(Armor armor) {
		this.armor = armor;
	}
	
	@Override
	public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale) {
		if (!(entityIn instanceof EntityLivingBase)) {
			System.out.println("entity error: " + entityIn);
			return;
		}
		
		EntityLivingBase living = (EntityLivingBase) entityIn;
		ItemStack stack = living.getEquipmentInSlot(armor.getSlot().getPlayerSlot() + 1);
		
		if (stack == null) {
			System.out.println("No item in slot: " + (armor.getSlot().getPlayerSlot() + 1));
		}
		
		if (!(stack.getItem() instanceof Armor)) {
			System.out.print(".");
			return;
		}
		
		Collection<ItemStack> subs = armor.getNestedArmorStacks(stack);
		IBakedModel model;
		for (ItemStack sub : subs) {
			if (sub == null || !(sub.getItem() instanceof ArmorPiece))
				continue;
			
			ArmorPiece piece = ((ArmorPiece) (sub.getItem()));
			
			ModelResourceLocation loc = piece.constructModelLocation(sub, "inventory");
			
			
			model = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager()
					.getModel(
					loc
					);
			//Minecraft.getMinecraft().getRenderItem().renderItem(stack, model);
			renderModel(sub, model, new ResourceLocation(Armory.MODID + ":items/empty_torso"), living, piece.getXOffset(), piece.getYOffset(), piece.getZOffset());
			
		}
	}
	
	private void renderModel(ItemStack stack, IBakedModel model, ResourceLocation textureLocation, EntityLivingBase parent, float transx, float transy, float transz) {
		
		GlStateManager.pushMatrix();
//	
		GlStateManager.rotate(parent.renderYawOffset, 0f, 1.0f, 0.0f);
		GlStateManager.translate(transx, transy, transz);
//		
		
		//Minecraft.getMinecraft().getRenderItem().renderItem(stack, model);
		Minecraft.getMinecraft().getItemRenderer().renderItem(parent, stack, TransformType.FIXED);
//		Tessellator tessellator = Tessellator.getInstance();
//        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
//        
//        //bind texture
//        Minecraft.getMinecraft().getTextureManager().bindTexture(textureLocation);
//        
//        worldrenderer.begin(7, DefaultVertexFormats.ITEM);
//
//        for (EnumFacing enumfacing : EnumFacing.values())
//        {
//            this.renderQuads(worldrenderer, model.getFaceQuads(enumfacing), -1, stack);
//        }
//
//        this.renderQuads(worldrenderer, model.getGeneralQuads(), -1, stack);
//        tessellator.draw();
        
        GlStateManager.popMatrix();
	}
	
//	private void renderQuads(WorldRenderer renderer, List<BakedQuad> quads, int color, ItemStack stack) {
//        boolean flag = color == -1 && stack != null;
//        int i = 0;
//
//        for (int j = quads.size(); i < j; ++i)
//        {
//            BakedQuad bakedquad = (BakedQuad)quads.get(i);
//            int k = color;
//
//            if (flag && bakedquad.hasTintIndex())
//            {
//                k = stack.getItem().getColorFromItemStack(stack, bakedquad.getTintIndex());
//
//                if (EntityRenderer.anaglyphEnable)
//                {
//                    k = TextureUtil.anaglyphColor(k);
//                }
//
//                k = k | -16777216;
//            }
//
//            net.minecraftforge.client.model.pipeline.LightUtil.renderQuadColor(renderer, bakedquad, k);
//        }
//    }
}
