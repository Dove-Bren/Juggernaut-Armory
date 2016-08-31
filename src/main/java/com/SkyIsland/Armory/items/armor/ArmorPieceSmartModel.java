package com.SkyIsland.Armory.items.armor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.SkyIsland.Armory.Armory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.ISmartItemModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("deprecation")
public class ArmorPieceSmartModel implements ISmartItemModel {
	
	@SideOnly(Side.CLIENT)
	private static FaceBakery bakery;
	
	private ArmorPiece originPiece;
	
	private String texturePrefix;
	
	private IBakedModel baseModel;
	
	public ArmorPieceSmartModel(ArmorPiece piece, IBakedModel baseModel, float zheight) {
		this.originPiece = piece;
		this.baseModel = baseModel;
		//this.zheight = zheight;
		
	}
	
	@Override
	public IBakedModel handleItemState(ItemStack stack) {
		texturePrefix = "";
		if (stack != null && stack.getItem() instanceof ArmorPiece) {
			texturePrefix = ((ArmorPiece) stack.getItem()).getUnderlyingMaterial(stack);
		}
		//return this;
		
		ModelManager manager = Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
				.getModelManager();
		
		IBakedModel model = manager.getModel(
						new ModelResourceLocation(Armory.MODID + ":" + texturePrefix + "_" + originPiece.getModelSuffix(), "inventory")
						);
		
		//Minecraft.getMinecraft().getRenderItem().getItemModelMesher().
		
//		if (model == null || model == manager.getMissingModel()) {
//			System.out.println("Failed to fetch model for: "
//					+ Armory.MODID + ":" + texturePrefix + "_" + originPiece.getModelSuffix());
//		} else {
//			System.out.println("Successful [" + Armory.MODID + ":" + texturePrefix + "_" + originPiece.getModelSuffix() + "]");
//		}
		
		return model;
		
//		TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(
//				Armory.MODID + ":" + texturePrefix + "_" + originPiece.getModelSuffix());
//		
//		if (sprite == null) {
//			System.out.println("Null model for [" + Armory.MODID + ":" + texturePrefix + "_" + originPiece.getModelSuffix() + "]");
//			return quadList;
//		} else {
//			System.out.println("Got model for " + originPiece.getModelSuffix());
//		}
		
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
//		if (bakery == null)
//			bakery = new FaceBakery();
//		
//		List<BakedQuad> quadList = new LinkedList<BakedQuad>();
//		
//		if (baseModel != null)
//			quadList.addAll(baseModel.getGeneralQuads());
//		
//		TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(
//				Armory.MODID + ":" + texturePrefix + "_" + originPiece.getModelSuffix());
//		
//		if (sprite == null) {
//			System.out.println("Null model for [" + Armory.MODID + ":" + texturePrefix + "_" + originPiece.getModelSuffix() + "]");
//			return quadList;
//		} else {
//			System.out.println("Got model for " + originPiece.getModelSuffix());
//		}
//		
////		quadList.add(bakery.makeBakedQuad(new Vector3f(0, zheight, 0), new Vector3f(1, zheight, 1),
////				new BlockPartFace(null, 0, "", new BlockFaceUV(new float[]{0, 0, 16.0f, 16.0f}, 0)),
////				sprite,
////				EnumFacing.UP,
////				ModelRotation.X0_Y0, null, true, true));
//		quadList.add(createBakedQuadForFace(
//				0.5f,
//				1.0f,
//				0.5f,
//				1.0f,
//				//0.0f,
//				ModConfig.config.getTestValue(ModConfig.Key.DEPTH_N),
//				0,
//				sprite,
//				EnumFacing.NORTH
//				));
//		quadList.add(createBakedQuadForFace(
//				0.5f,
//				1.0f,
//				0.5f,
//				1.0f,
//				//0.001f,
//				ModConfig.config.getTestValue(ModConfig.Key.DEPTH_S),
//				0,
//				sprite,
//				EnumFacing.SOUTH
//				));
//		/**
//		   // Creates a baked quad for the given face.
//		   // When you are directly looking at the face, the quad is centred at [centreLR, centreUD]
//		   // The left<->right "width" of the face is width, the bottom<-->top "height" is height.
//		   // The amount that the quad is displaced towards the viewer i.e. (perpendicular to the flat face you can see) is forwardDisplacement
//		   //   - for example, for an EAST face, a value of 0.00 lies directly on the EAST face of the cube.  a value of 0.01 lies
//		   //     slightly to the east of the EAST face (at x=1.01).  a value of -0.01 lies slightly to the west of the EAST face (at x=0.99).
//		   // The orientation of the faces is as per the diagram on this page
//		   //   http://greyminecraftcoder.blogspot.com.au/2014/12/block-models-texturing-quads-faces.html
//		   // Read this page to learn more about how to draw a textured quad
//		   //   http://greyminecraftcoder.blogspot.co.at/2014/12/the-tessellator-and-worldrenderer-18.html
//		   * @param centreLR the centre point of the face left-right
//		   * @param width    width of the face
//		   * @param centreUD centre point of the face top-bottom
//		   * @param height height of the face from top to bottom
//		   * @param forwardDisplacement the displacement of the face (towards the front)
//		   * @param itemRenderLayer which item layer the quad is on
//		   * @param texture the texture to use for the quad
//		   * @param face the face to draw this quad on
//		   * @return
//		   */
//		System.out.println(quadList.size() + " quads");
		
		//return quadList;
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
	
	
	
	
	//////////////////////////////////////////
	
//	/**
//	   // Creates a baked quad for the given face.
//	   // When you are directly looking at the face, the quad is centred at [centreLR, centreUD]
//	   // The left<->right "width" of the face is width, the bottom<-->top "height" is height.
//	   // The amount that the quad is displaced towards the viewer i.e. (perpendicular to the flat face you can see) is forwardDisplacement
//	   //   - for example, for an EAST face, a value of 0.00 lies directly on the EAST face of the cube.  a value of 0.01 lies
//	   //     slightly to the east of the EAST face (at x=1.01).  a value of -0.01 lies slightly to the west of the EAST face (at x=0.99).
//	   // The orientation of the faces is as per the diagram on this page
//	   //   http://greyminecraftcoder.blogspot.com.au/2014/12/block-models-texturing-quads-faces.html
//	   // Read this page to learn more about how to draw a textured quad
//	   //   http://greyminecraftcoder.blogspot.co.at/2014/12/the-tessellator-and-worldrenderer-18.html
//	   * @param centreLR the centre point of the face left-right
//	   * @param width    width of the face
//	   * @param centreUD centre point of the face top-bottom
//	   * @param height height of the face from top to bottom
//	   * @param forwardDisplacement the displacement of the face (towards the front)
//	   * @param itemRenderLayer which item layer the quad is on
//	   * @param texture the texture to use for the quad
//	   * @param face the face to draw this quad on
//	   * @return
//	   */
//	  private BakedQuad createBakedQuadForFace(float centreLR, float width, float centreUD, float height, float forwardDisplacement,
//	                                           int itemRenderLayer,
//	                                           TextureAtlasSprite texture, EnumFacing face)
//	  {
//	    float x1, x2, x3, x4;
//	    float y1, y2, y3, y4;
//	    float z1, z2, z3, z4;
//	    final float CUBE_MIN = 0.0F;
//	    final float CUBE_MAX = 1.0F;
//
//	    switch (face) {
//	      case UP: {
//	        x1 = x2 = centreLR + width/2.0F;
//	        x3 = x4 = centreLR - width/2.0F;
//	        z1 = z4 = centreUD + height/2.0F;
//	        z2 = z3 = centreUD - height/2.0F;
//	        y1 = y2 = y3 = y4 = CUBE_MAX + forwardDisplacement;
//	        break;
//	      }
//	      case DOWN: {
//	        x1 = x2 = centreLR + width/2.0F;
//	        x3 = x4 = centreLR - width/2.0F;
//	        z1 = z4 = centreUD - height/2.0F;
//	        z2 = z3 = centreUD + height/2.0F;
//	        y1 = y2 = y3 = y4 = CUBE_MIN - forwardDisplacement;
//	        break;
//	      }
//	      case WEST: {
//	        z1 = z2 = centreLR + width/2.0F;
//	        z3 = z4 = centreLR - width/2.0F;
//	        y1 = y4 = centreUD - height/2.0F;
//	        y2 = y3 = centreUD + height/2.0F;
//	        x1 = x2 = x3 = x4 = CUBE_MIN - forwardDisplacement;
//	        break;
//	      }
//	      case EAST: {
//	        z1 = z2 = centreLR - width/2.0F;
//	        z3 = z4 = centreLR + width/2.0F;
//	        y1 = y4 = centreUD - height/2.0F;
//	        y2 = y3 = centreUD + height/2.0F;
//	        x1 = x2 = x3 = x4 = CUBE_MAX + forwardDisplacement;
//	        break;
//	      }
//	      case NORTH: {
//	        x1 = x2 = centreLR - width/2.0F;
//	        x3 = x4 = centreLR + width/2.0F;
//	        y1 = y4 = centreUD - height/2.0F;
//	        y2 = y3 = centreUD + height/2.0F;
//	        z1 = z2 = z3 = z4 = CUBE_MIN - forwardDisplacement;
//	        break;
//	      }
//	      case SOUTH: {
//	        x1 = x2 = centreLR + width/2.0F;
//	        x3 = x4 = centreLR - width/2.0F;
//	        y1 = y4 = centreUD - height/2.0F;
//	        y2 = y3 = centreUD + height/2.0F;
//	        z1 = z2 = z3 = z4 = CUBE_MAX + forwardDisplacement;
//	        break;
//	      }
//	      default: {
//	        assert false : "Unexpected facing in createBakedQuadForFace:" + face;
//	        return null;
//	      }
//	    }
//
//	    return new BakedQuad(Ints.concat(vertexToInts(x1, y1, z1, Color.WHITE.getRGB(), texture, 16, 16),
//	                                     vertexToInts(x2, y2, z2, Color.WHITE.getRGB(), texture, 16, 0),
//	                                     vertexToInts(x3, y3, z3, Color.WHITE.getRGB(), texture, 0, 0),
//	                                     vertexToInts(x4, y4, z4, Color.WHITE.getRGB(), texture, 0, 16)),
//	                         itemRenderLayer, face);
//	  }
//
//	  /**
//	   * Converts the vertex information to the int array format expected by BakedQuads.
//	   * @param x x coordinate
//	   * @param y y coordinate
//	   * @param z z coordinate
//	   * @param color RGBA colour format - white for no effect, non-white to tint the face with the specified colour
//	   * @param texture the texture to use for the face
//	   * @param u u-coordinate of the texture (0 - 16) corresponding to [x,y,z]
//	   * @param v v-coordinate of the texture (0 - 16) corresponding to [x,y,z]
//	   * @return
//	   */
//	  private int[] vertexToInts(float x, float y, float z, int color, TextureAtlasSprite texture, float u, float v)
//	  {
//	    return new int[] {
//	            Float.floatToRawIntBits(x),
//	            Float.floatToRawIntBits(y),
//	            Float.floatToRawIntBits(z),
//	            color,
//	            Float.floatToRawIntBits(texture.getInterpolatedU(u)),
//	            Float.floatToRawIntBits(texture.getInterpolatedV(v)),
//	            0
//	    };
//	}
	
}
