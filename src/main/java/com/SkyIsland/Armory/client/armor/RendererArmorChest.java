package com.SkyIsland.Armory.client.armor;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.SkyIsland.Armory.Armory;
import com.SkyIsland.Armory.config.ModConfig;
import com.SkyIsland.Armory.items.armor.ArmorPiece;
import com.SkyIsland.Armory.items.armor.ArmorSlot;
import com.SkyIsland.Armory.items.armor.ArmorTorso;
import com.SkyIsland.Armory.items.armor.ArmorTorso.Slot;
import com.google.common.collect.Lists;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * Less lossy wrapper for all head model renderers
 * @author Skyler
 *
 */
public class RendererArmorChest extends ModelBiped {

	private SmartModelRenderer PauldronLeftAccent;
	private SmartModelRenderer PauldronLeftTop;
	private SmartModelRenderer PauldronRightTop;
	private SmartModelRenderer PauldronRightAccent;
	private SmartModelRenderer VambraceLeft;
	private SmartModelRenderer VambraceLeftAccent;
	private SmartModelRenderer VambraceRight;
	private SmartModelRenderer VambraceRightAccent;
	private SmartModelRenderer Breastplate;
	private SmartModelRenderer BreastplateAccentRight;
	private SmartModelRenderer BreastplateAccentLeft;
	private SmartModelRenderer BreastplateAccentLeft2;
	private SmartModelRenderer BreastplateAccentRight2;
	private SmartModelRenderer CapeShoulderRight;
	private SmartModelRenderer CapeShoulderLeft;
	private SmartModelRenderer CapeShoulderConnection;
	private SmartModelRenderer CapeTopStripe;
	private SmartModelRenderer CapeMiddle;
	private SmartModelRenderer CapeLowerMiddle;
	private SmartModelRenderer CapeBottom;
	
	private ResourceLocation missingTexture
		= new ResourceLocation(Armory.MODID, "textures/models/armor/missing_torso.png");
	
	private Map<ArmorTorso.Slot, Collection<SmartModelRenderer>> pieceMap;
	private Set<SmartModelRenderer> activeRenderers;
	  
	public RendererArmorChest(float scale) {
		super(scale, 0, 128, 128);
		textureWidth = 128;
		textureHeight = 64;
		activeRenderers = new HashSet<SmartModelRenderer>();
	    
//	      Chest = new ModelRenderer(this, 0, 0);
//	      Chest.addBox(0F, 0F, 0F, 8, 12, 4);
//	      Chest.setRotationPoint(-4F, 0F, -1F);
//	      Chest.setTextureSize(textureWidth, textureHeight);
//	      Chest.mirror = true;
//	      setRotation(Chest, 0F, 0F, 0F);
//	      ArmLeft = new ModelRenderer(this, 0, 0);
//	      ArmLeft.addBox(0F, 0F, 0F, 4, 12, 4);
//	      ArmLeft.setRotationPoint(4F, 0F, -1F);
//	      ArmLeft.setTextureSize(textureWidth, textureHeight);
//	      ArmLeft.mirror = true;
//	      setRotation(ArmLeft, 0F, 0F, 0F);
//	      ArmRight = new ModelRenderer(this, 0, 0);
//	      ArmRight.addBox(0F, 0F, 0F, 4, 12, 4);
//	      ArmRight.setRotationPoint(-8F, 0F, -1F);
//	      ArmRight.setTextureSize(textureWidth, textureHeight);
//	      ArmRight.mirror = true;
//	      setRotation(ArmRight, 0F, 0F, 0F);
//	      LegLeft = new ModelRenderer(this, 0, 0);
//	      LegLeft.addBox(0F, 0F, 0F, 4, 12, 4);
//	      LegLeft.setRotationPoint(0F, 12F, -1F);
//	      LegLeft.setTextureSize(textureWidth, textureHeight);
//	      LegLeft.mirror = true;
//	      setRotation(LegLeft, 0F, 0F, 0F);
//	      LegRight = new ModelRenderer(this, 0, 0);
//	      LegRight.addBox(0F, 0F, 0F, 4, 12, 4);
//	      LegRight.setRotationPoint(-4F, 12F, -1F);
//	      LegRight.setTextureSize(textureWidth, textureHeight);
//	      LegRight.mirror = true;
//	      setRotation(LegRight, 0F, 0F, 0F);
	    PauldronLeftAccent = new SmartModelRenderer(this, 0, 56, missingTexture);
	    PauldronLeftAccent.addBox(1F, -4F, -2F, 7, 4, 4, scale);
	    //was 1, -3, 0
	    //next was 5, -3, -2
	    PauldronLeftAccent.setRotationPoint(0F, 0F, 0F);
	    //PauldronLeftAccent.setRotationPoint(4F, 0F, -1F);
	    PauldronLeftAccent.setTextureSize(textureWidth, textureHeight);
	    PauldronLeftAccent.mirror = true;
	    setRotation(PauldronLeftAccent, 0F, 0F, 0F);
	    PauldronLeftTop = new SmartModelRenderer(this, 23, 53, missingTexture);
	    PauldronLeftTop.addBox(0F, -3F, -3F, 6, 5, 6, scale);
	    //was 0, -2, -1
	    //next was 4
	    PauldronLeftTop.setRotationPoint(0F, 0F, 0F);// .setRotationPoint(4F, 0F, -1F);
	    PauldronLeftTop.setTextureSize(textureWidth, textureHeight);
	    PauldronLeftTop.mirror = true;
	    setRotation(PauldronLeftTop, 0F, 0F, 0F);
	    PauldronRightTop = new SmartModelRenderer(this, 47, 53, missingTexture);
	    PauldronRightTop.addBox(-6F, -3F, -3F, 6, 5, 6, scale);
	    //was -2, -2, -1
	    //next was -11
	    PauldronRightTop.setRotationPoint(0F, 0F, 0F);// .setRotationPoint(-8F, 0F, -1F);
	    PauldronRightTop.setTextureSize(textureWidth, textureHeight);
	    PauldronRightTop.mirror = true;
	    setRotation(PauldronRightTop, 0F, 0F, 0F);
	    PauldronRightAccent = new SmartModelRenderer(this, 71, 56, missingTexture);
	    PauldronRightAccent.addBox(-8F, -4F, -2F, 7, 4, 4, scale);
	    //was -4, -3, 0
	    //next was -13
	    PauldronRightAccent.setRotationPoint(0F, 0F, 0F);// .setRotationPoint(-8F, 0F, -1F);
	    PauldronRightAccent.setTextureSize(textureWidth, textureHeight);
	    PauldronRightAccent.mirror = true;
	    setRotation(PauldronRightAccent, 0F, 0F, 0F);
	    VambraceLeft = new SmartModelRenderer(this, 0, 43, missingTexture);
	    VambraceLeft.addBox(2F, 5F, -3F, 3, 5, 4, scale);
	    //was 2 6 -1
	    VambraceLeft.setRotationPoint(0F, 0F, 0F);// .setRotationPoint(4F, 0F, -1F);
	    VambraceLeft.setTextureSize(textureWidth, textureHeight);
	    VambraceLeft.mirror = true;
	    setRotation(VambraceLeft, 0F, 0F, 0F);
	    VambraceLeftAccent = new SmartModelRenderer(this, 16, 43, missingTexture);
	    VambraceLeftAccent.addBox(5F, 6F, -2F, 1, 3, 3, scale);
	    //was 5 7 0
	    VambraceLeftAccent.setRotationPoint(0F, 0F, 0F);// .setRotationPoint(4F, 0F, -1F);
	    VambraceLeftAccent.setTextureSize(textureWidth, textureHeight);
	    VambraceLeftAccent.mirror = true;
	    setRotation(VambraceLeftAccent, 0F, 0F, 0F);
	    VambraceRight = new SmartModelRenderer(this, 32, 43, missingTexture);
	    VambraceRight.addBox(-4F, 5F, -3F, 3, 5, 4, scale);
	    //was -1 6 -1
	    VambraceRight.setRotationPoint(0F, 0F, 0F);// .setRotationPoint(-8F, 0F, -1F);
	    VambraceRight.setTextureSize(textureWidth, textureHeight);
	    VambraceRight.mirror = true;
	    setRotation(VambraceRight, 0F, 0F, 0F);
	    VambraceRightAccent = new SmartModelRenderer(this, 24, 43, missingTexture);
	    VambraceRightAccent.addBox(-5F, 6F, -2F, 1, 3, 3, scale);
	    //was -2 7 0
	    VambraceRightAccent.setRotationPoint(0F, 0F, 0F);// .setRotationPoint(-8F, 0F, -1F);
	    VambraceRightAccent.setTextureSize(textureWidth, textureHeight);
	    VambraceRightAccent.mirror = true;
	    setRotation(VambraceRightAccent, 0F, 0F, 0F);
	    Breastplate = new SmartModelRenderer(this, 0, 25, missingTexture);
	    Breastplate.addBox(-5F, -1F, -3F, 10, 12, 6, scale);
	    Breastplate.setRotationPoint(0F, 0F, 0F);// .setRotationPoint(-4F, 0F, -1F);
	    Breastplate.setTextureSize(textureWidth, textureHeight);
	    Breastplate.mirror = true;
	    //-4 ~ -2
	    setRotation(Breastplate, 0F, 0F, 0F);
	    BreastplateAccentRight = new SmartModelRenderer(this, 32, 25, missingTexture);
	    BreastplateAccentRight.addBox(-6F, 11F, -4F, 4, 1, 8, scale);
	    BreastplateAccentRight.setRotationPoint(0F, 0F, 0F);// .setRotationPoint(-4F, 0F, -1F);
	    BreastplateAccentRight.setTextureSize(textureWidth, textureHeight);
	    BreastplateAccentRight.mirror = true;
	    setRotation(BreastplateAccentRight, 0F, 0F, 0F);
	    BreastplateAccentLeft = new SmartModelRenderer(this, 32, 34, missingTexture);
	    BreastplateAccentLeft.addBox(2F, 11F, -4F, 4, 1, 8, scale);
	    BreastplateAccentLeft.setRotationPoint(0F, 0F, 0F);// .setRotationPoint(-4F, 0F, -1F);
	    BreastplateAccentLeft.setTextureSize(textureWidth, textureHeight);
	    BreastplateAccentLeft.mirror = true;
	    setRotation(BreastplateAccentLeft, 0F, 0F, 0F);
	    BreastplateAccentLeft2 = new SmartModelRenderer(this, 56, 25, missingTexture);
	    BreastplateAccentLeft2.addBox(3F, 12F, -5F, 4, 1, 10, scale);
	    BreastplateAccentLeft2.setRotationPoint(0F, 0F, 0F);// .setRotationPoint(-4F, 0F, -1F);
	    BreastplateAccentLeft2.setTextureSize(textureWidth, textureHeight);
	    BreastplateAccentLeft2.mirror = true;
	    setRotation(BreastplateAccentLeft2, 0F, 0F, 0F);
	    BreastplateAccentRight2 = new SmartModelRenderer(this, 56, 36, missingTexture);
	    BreastplateAccentRight2.addBox(-7F, 12F, -5F, 4, 1, 10, scale);
	    BreastplateAccentRight2.setRotationPoint(0F, 0F, 0F);// .setRotationPoint(-4F, 0F, -1F);
	    BreastplateAccentRight2.setTextureSize(textureWidth, textureHeight);
	    BreastplateAccentRight2.mirror = true;
	    setRotation(BreastplateAccentRight2, 0F, 0F, 0F);
	    
	    CapeShoulderRight = new SmartModelRenderer(this, 32, 0, missingTexture);
	    CapeShoulderRight.addBox(-2F, -3F, -2F, 6, 2, 2);
	    CapeShoulderRight.setRotationPoint(0F, 0F, 0F);
	    CapeShoulderRight.setTextureSize(128, 64);
	    CapeShoulderRight.mirror = true;
	    setRotation(CapeShoulderRight, 0F, 0F, 0F);
	    CapeShoulderLeft = new SmartModelRenderer(this, 32, 4, missingTexture);
	    CapeShoulderLeft.addBox(6F, -3F, -2F, 6, 2, 2);
	    CapeShoulderLeft.setRotationPoint(0F, 0F, 0F);
	    CapeShoulderLeft.setTextureSize(128, 64);
	    CapeShoulderLeft.mirror = true;
	    setRotation(CapeShoulderLeft, 0F, 0F, 0F);
	    CapeShoulderConnection = new SmartModelRenderer(this, 32, 8, missingTexture);
	    CapeShoulderConnection.addBox(-1F, -1F, -1F, 12, 2, 1);
	    CapeShoulderConnection.setRotationPoint(0F, 0F, 0F);
	    CapeShoulderConnection.setTextureSize(128, 64);
	    CapeShoulderConnection.mirror = true;
	    setRotation(CapeShoulderConnection, 0F, 0F, 0F);
	    CapeTopStripe = new SmartModelRenderer(this, 48, 0, missingTexture);
	    CapeTopStripe.addBox(ModConfig.config.getTestValue(ModConfig.Key.CAPE_X),
	    		ModConfig.config.getTestValue(ModConfig.Key.CAPE_Y),
	    		ModConfig.config.getTestValue(ModConfig.Key.CAPE_Z), 10, 3, 1);
	    CapeTopStripe.setRotationPoint(0F, 0F, 0F);
	    CapeTopStripe.setTextureSize(128, 64);
	    CapeTopStripe.mirror = true;
	    setRotation(CapeTopStripe, 0F, 0F, 0F);
	    CapeMiddle = new SmartModelRenderer(this, 70, 0, missingTexture);
	    CapeMiddle.addBox(0F, 3F, 0F, 10, 7, 1);
	    CapeMiddle.setRotationPoint(0F, 0F, 0F);
	    CapeMiddle.setTextureSize(128, 64);
	    CapeMiddle.mirror = true;
	    setRotation(CapeMiddle, 0F, 0F, 0F);
	    CapeLowerMiddle = new SmartModelRenderer(this, 92, 0, missingTexture);
	    CapeLowerMiddle.addBox(-1F, 9F, 1F, 12, 9, 1);
	    CapeLowerMiddle.setRotationPoint(0F, 0F, 0F);
	    CapeLowerMiddle.setTextureSize(128, 64);
	    CapeLowerMiddle.mirror = true;
	    setRotation(CapeLowerMiddle, 0F, 0F, 0F);
	    CapeBottom = new SmartModelRenderer(this, 90, 10, missingTexture);
	    CapeBottom.addBox(-2F, 18F, 1F, 14, 1, 1);
	    CapeBottom.setRotationPoint(0F, 0F, 0F);
	    CapeBottom.setTextureSize(128, 64);
	    CapeBottom.mirror = true;
	    setRotation(CapeBottom, 0F, 0F, 0F);


	    
	    
	      
	    bipedLeftArm.addChild(PauldronLeftTop);
	    bipedLeftArm.addChild(PauldronLeftAccent);
	    bipedLeftArm.addChild(VambraceLeft);
	    bipedLeftArm.addChild(VambraceLeftAccent);
	      
	    bipedRightArm.addChild(PauldronRightTop);
	    bipedRightArm.addChild(PauldronRightAccent);
	    bipedRightArm.addChild(VambraceRight);
	    bipedRightArm.addChild(VambraceRightAccent);
	      
	    bipedBody.addChild(Breastplate);
	    bipedBody.addChild(CapeTopStripe);
	    
	    CapeTopStripe.addChild(CapeShoulderRight);
	    CapeTopStripe.addChild(CapeShoulderLeft);
	    CapeTopStripe.addChild(CapeShoulderConnection);
	    CapeTopStripe.addChild(CapeMiddle);
	    CapeTopStripe.addChild(CapeLowerMiddle);
	    CapeTopStripe.addChild(CapeBottom);
	    
	    
	    Breastplate.addChild(BreastplateAccentRight);
	    Breastplate.addChild(BreastplateAccentLeft);
	    Breastplate.addChild(BreastplateAccentLeft2);
	    Breastplate.addChild(BreastplateAccentRight2);
	    
	    activeRenderers.add(Breastplate);
	    activeRenderers.add(PauldronLeftTop);
	    activeRenderers.add(PauldronLeftAccent);
	    activeRenderers.add(VambraceLeft);
	    activeRenderers.add(VambraceLeftAccent);
	    activeRenderers.add(PauldronRightTop);
	    activeRenderers.add(PauldronRightAccent);
	    activeRenderers.add(VambraceRight);
	    activeRenderers.add(VambraceRightAccent);
	    activeRenderers.add(CapeTopStripe);
        

		pieceMap = new EnumMap<ArmorTorso.Slot, Collection<SmartModelRenderer>>(ArmorTorso.Slot.class);
		
		pieceMap.put(ArmorTorso.Slot.BREASTPLATE,
				Lists.newArrayList(Breastplate, BreastplateAccentRight, BreastplateAccentLeft, BreastplateAccentRight2, BreastplateAccentLeft2));
		pieceMap.put(ArmorTorso.Slot.PAULDRON_LEFT,
				Lists.newArrayList(PauldronLeftAccent, PauldronLeftTop));
		pieceMap.put(ArmorTorso.Slot.PAULDRON_RIGHT,
				Lists.newArrayList(PauldronRightAccent, PauldronRightTop));
		pieceMap.put(ArmorTorso.Slot.VAMBRACE_LEFT,
				Lists.newArrayList(VambraceLeftAccent, VambraceLeft));
		pieceMap.put(ArmorTorso.Slot.VAMBRACE_RIGHT,
				Lists.newArrayList(VambraceRightAccent, VambraceRight));
	
    }
	  
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5){
		  
		
		//before rendering, provide sneaky textures to smart model renderers
		
		//step through equipment, even though we're not sure if they're
		//rendering chest
		if (entity instanceof EntityLivingBase) {
			EntityLivingBase e = (EntityLivingBase) entity;
			ItemStack item = e.getEquipmentInSlot(ArmorSlot.TORSO.getPlayerSlot() + 1);
			
			if (item != null && item.getItem() instanceof ArmorTorso) {
				//torso armor in torso position. Set children attributes
				ArmorTorso armor = (ArmorTorso) item.getItem();
				ArmorPiece piece;
				for (Entry<Slot, Collection<SmartModelRenderer>> entry : pieceMap.entrySet()) {
					piece = armor.getComponentItem(entry.getKey());
					if (piece == null) {
						for (SmartModelRenderer r : entry.getValue()) {
							r.showModel = false;
						}
						continue;
					}
					
					ItemStack pieceItem = armor.getArmorPiece(item, entry.getKey());
					if (pieceItem == null) {
						//nothing in that slot
//						System.out.print("  empty slot");
						for (SmartModelRenderer r : entry.getValue()) {
							r.showModel = false;
						}
						continue;
					}
					
//					ResourceLocation text = piece.constructModelLocation(
//							pieceItem, "normal");
					ResourceLocation text = new ResourceLocation(
							Armory.MODID, "textures/models/armor/" +
							piece.getTexturePrefix(pieceItem)
							+ "_torso.png");
					
//					if (text == null) {
//						System.out.println("Returned null texture for piece: "
//								+ entry.getKey());
//					}
					
					for (SmartModelRenderer r : entry.getValue()) {
						r.provideTexture(text);
						r.showModel = true;
					}
				}
			}
		} else {
			System.out.print("|");
		}
		  
//		//allow parents to render, without distruption
//		super.render(entity, f, f1, f2, f3, f4, f5);
		
//		if (Math.abs(
//				ModConfig.config.getTestValue(ModConfig.Key.ARMORBIPED)) < 0.5f) {
//			super.render(entity, f, f1, f2, f3, f4, f5);
//		} else {
		this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		copyModelAngles(this.bipedBody, this.Breastplate);
		copyModelAngles(this.bipedLeftArm, this.PauldronLeftAccent);
		copyModelAngles(this.bipedLeftArm, this.PauldronLeftTop);
		copyModelAngles(this.bipedLeftArm, this.VambraceLeft);
		copyModelAngles(this.bipedLeftArm, this.VambraceLeftAccent);

		copyModelAngles(this.bipedRightArm, this.PauldronRightAccent);
		copyModelAngles(this.bipedRightArm, this.PauldronRightTop);
		copyModelAngles(this.bipedRightArm, this.VambraceRight);
		copyModelAngles(this.bipedRightArm, this.VambraceRightAccent);
		
		GlStateManager.pushMatrix();

        if (entity instanceof EntityLivingBase && ((EntityLivingBase) entity).isChild()) {
            float s = 2.0F;
            GlStateManager.scale(1.0F / s, 1.0F / s, 1.0F / s);
            GlStateManager.translate(0.0F, 24.0F * f5, 0.0F);
            for (ModelRenderer r : activeRenderers)
    			r.render(f5);
        } else {
        	if (entity.isSneaking()) {
                GlStateManager.translate(0.0F, 0.2F, 0.0F);
            }
        	
            for (ModelRenderer r : activeRenderers)
    			r.render(f5);
        }
        
        GlStateManager.popMatrix();
		
		
//		for (ModelRenderer r : activeRenderers)
//			r.render(f5);
//		}
		  
	    
	    //Don't think this is needed; renderes will call children
	    //To be tested
//	    setRotationAngles(f, f1, f2, f3, f4, f5, entity);
//	    Chest.render(f5);
//	    ArmLeft.render(f5);
//	    ArmRight.render(f5);
//	    LegLeft.render(f5);
//	    LegRight.render(f5);
//	    PauldronLeftAccent.render(f5);
//	    PauldronLeftTop.render(f5);
//	    PauldronRightTop.render(f5);
//	    PauldronRightAccent.render(f5);
//	    VambraceLeft.render(f5);
//	    VambraceLeftAccent.render(f5);
//	    VambraceRight.render(f5);
//	    VambraceRightAccent.render(f5);
//	    Breastplate.render(f5);
//	    BreastplateAccentRight.render(f5);
//	    BreastplateAccentLeft.render(f5);
//	    BreastplateAccentLeft2.render(f5);
//	    BreastplateAccentRight2.render(f5);
    }
	  
	  private void setRotation(ModelRenderer model, float x, float y, float z)
	  {
	    model.rotateAngleX = x;
	    model.rotateAngleY = y;
	    model.rotateAngleZ = z;
	  }
	  
	  @Override
	  public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity e)
	  {
	    super.setRotationAngles(f, f1, f2, f3, f4, f5, e);
	  }

}
