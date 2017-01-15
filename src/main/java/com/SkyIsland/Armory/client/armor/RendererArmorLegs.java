package com.SkyIsland.Armory.client.armor;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.SkyIsland.Armory.Armory;
import com.SkyIsland.Armory.items.armor.ArmorLegs;
import com.SkyIsland.Armory.items.armor.ArmorLegs.Slot;
import com.SkyIsland.Armory.items.armor.ArmorPiece;
import com.SkyIsland.Armory.items.armor.ArmorSlot;
import com.google.common.collect.Lists;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * Less lossy wrapper for all leg model renderers
 * @author Skyler
 *
 */
public class RendererArmorLegs extends ModelBiped {

	private SmartModelRenderer CuisseLeft;
	private SmartModelRenderer CuisseLeftAccent;
	private SmartModelRenderer CuisseRight;
	private SmartModelRenderer CuisseRightAccent;
	private SmartModelRenderer PoleynLeft;
	private SmartModelRenderer PoleynLeftAccent;
	private SmartModelRenderer PoleynRight;
	private SmartModelRenderer PoleynRightAccent;
	private SmartModelRenderer SkirtLeftTop;
	private SmartModelRenderer SkirtLeft;
	private SmartModelRenderer SkirtRightTop;
	private SmartModelRenderer SkirtRight;
	private SmartModelRenderer SkirtBack;
	
	private ResourceLocation missingTexture
		= new ResourceLocation(Armory.MODID, "textures/models/armor/missing_legs.png");
	
	private Map<ArmorLegs.Slot, Collection<SmartModelRenderer>> pieceMap;
	private Set<SmartModelRenderer> activeRenderers;
	  
	public RendererArmorLegs(float scale) {
		super(scale, 0, 128, 128);
		textureWidth = 128;
		textureHeight = 64;
		activeRenderers = new HashSet<SmartModelRenderer>();
		
		CuisseLeft = new SmartModelRenderer(this, 0, 30, missingTexture);
		CuisseLeft.addBox(-2F, 0F, -3F, 4, 5, 6, scale);
		CuisseLeft.setRotationPoint(0F, 0F, -3F);
		CuisseLeft.setTextureSize(128, 64);
		CuisseLeft.mirror = true;
		setRotation(CuisseLeft, 0F, 0F, 0F);
		CuisseLeftAccent = new SmartModelRenderer(this, 0, 41, missingTexture);
		CuisseLeftAccent.addBox(-2F, 1F, -4F, 6, 1, 6, scale);
		CuisseLeftAccent.setRotationPoint(0F, 0F, 0F);
		CuisseLeftAccent.setTextureSize(128, 64);
		CuisseLeftAccent.mirror = true;
		setRotation(CuisseLeftAccent, 0F, 0F, 0F);
		CuisseRight = new SmartModelRenderer(this, 20, 30, missingTexture);
		CuisseRight.addBox(-3F, 0F, -3F, 4, 5, 6, scale);
		CuisseRight.setRotationPoint(0F, 0F, 0F);
		CuisseRight.setTextureSize(128, 64);
		CuisseRight.mirror = true;
		setRotation(CuisseRight, 0F, 0F, 0F);
		CuisseRightAccent = new SmartModelRenderer(this, 24, 41, missingTexture);
		CuisseRightAccent.addBox(-4F, 1F, -4F, 6, 1, 6, scale);
		CuisseRightAccent.setRotationPoint(0F, 0F, 0F);
		CuisseRightAccent.setTextureSize(128, 64);
		CuisseRightAccent.mirror = true;
		setRotation(CuisseRightAccent, 0F, 0F, 0F);
		PoleynLeft = new SmartModelRenderer(this, 0, 52, missingTexture);
		PoleynLeft.addBox(-2F, 4F, -4F, 4, 3, 2, scale);
		PoleynLeft.setRotationPoint(0F, 0F, 0F);
		PoleynLeft.setTextureSize(128, 64);
		PoleynLeft.mirror = true;
		setRotation(PoleynLeft, 0F, 0F, 0F);
		PoleynLeftAccent = new SmartModelRenderer(this, 0, 57, missingTexture);
		PoleynLeftAccent.addBox(2F, 5F, -3F, 1, 2, 4, scale);
		PoleynLeftAccent.setRotationPoint(0F, 0F, 0F);
		PoleynLeftAccent.setTextureSize(128, 64);
		PoleynLeftAccent.mirror = true;
		setRotation(PoleynLeftAccent, 0F, 0F, 0F);
		PoleynRight = new SmartModelRenderer(this, 12, 52, missingTexture);
		PoleynRight.addBox(-2F, 4F, -4F, 4, 3, 2, scale);
		PoleynRight.setRotationPoint(0F, 0F, 0F);
		PoleynRight.setTextureSize(128, 64);
		PoleynRight.mirror = true;
		setRotation(PoleynRight, 0F, 0F, 0F);
		PoleynRightAccent = new SmartModelRenderer(this, 12, 57, missingTexture);
		PoleynRightAccent.addBox(-3F, 5F, -3F, 1, 2, 4, scale);
		PoleynRightAccent.setRotationPoint(0F, 0F, 0F);
		PoleynRightAccent.setTextureSize(128, 64);
		PoleynRightAccent.mirror = true;
		setRotation(PoleynRightAccent, 0F, 0F, 0F);
		
		//skirt is slightly larger
		scale = scale + 0.05f;
		
		SkirtLeftTop = new SmartModelRenderer(this, 32, 0, missingTexture);
		SkirtLeftTop.addBox(5F, 11F, -3F, 1, 1, 6, scale);
		SkirtLeftTop.setRotationPoint(0F, 0F, 0F);
		SkirtLeftTop.setTextureSize(128, 64);
		SkirtLeftTop.mirror = true;
		setRotation(SkirtLeftTop, 0F, 0F, 0F);
		SkirtLeft = new SmartModelRenderer(this, 30, 7, missingTexture);
		SkirtLeft.addBox(6F, 11F, -4F, 1, 6, 7, scale);
		SkirtLeft.setRotationPoint(0F, 0F, 0F);
		SkirtLeft.setTextureSize(128, 64);
		SkirtLeft.mirror = true;
		setRotation(SkirtLeft, 0F, 0F, 0F);
		SkirtRightTop = new SmartModelRenderer(this, 46, 0, missingTexture);
		SkirtRightTop.addBox(-6F, 11F, -3F, 1, 1, 6, scale);
		SkirtRightTop.setRotationPoint(0F, 0F, 0F);
		SkirtRightTop.setTextureSize(128, 64);
		SkirtRightTop.mirror = true;
		setRotation(SkirtRightTop, 0F, 0F, 0F);
		SkirtRight = new SmartModelRenderer(this, 46, 7, missingTexture);
		SkirtRight.addBox(-7F, 11F, -4F, 1, 6, 7, scale);
		SkirtRight.setRotationPoint(0F, 0F, 0F);
		SkirtRight.setTextureSize(128, 64);
		SkirtRight.mirror = true;
		setRotation(SkirtRight, 0F, 0F, 0F);
		SkirtBack = new SmartModelRenderer(this, 66, 0, missingTexture);
		SkirtBack.addBox(-6F, 11F, 3F, 12, 7, 1, scale);
		SkirtBack.setRotationPoint(0F, 0F, 0F);
		SkirtBack.setTextureSize(128, 64);
		SkirtBack.mirror = true;
		setRotation(SkirtBack, 0F, 0F, 0F);

		
	      
	    bipedLeftLeg.addChild(CuisseLeft);
	    bipedLeftLeg.addChild(PoleynLeft);
	      
	    bipedRightLeg.addChild(CuisseRight);
	    bipedRightLeg.addChild(PoleynRight);
	      
	    bipedBody.addChild(SkirtBack);
	    bipedBody.addChild(SkirtLeft);
	    bipedBody.addChild(SkirtLeftTop);
	    bipedBody.addChild(SkirtRight);
	    bipedBody.addChild(SkirtRightTop);
	    
	    CuisseLeft.addChild(CuisseLeftAccent);
	    CuisseRight.addChild(CuisseRightAccent);
	    PoleynLeft.addChild(PoleynLeftAccent);
	    PoleynRight.addChild(PoleynRightAccent);
	    
	    
	    activeRenderers.add(CuisseLeft);
	    activeRenderers.add(CuisseRight);
	    activeRenderers.add(PoleynLeft);
	    activeRenderers.add(PoleynRight);
	    activeRenderers.add(SkirtBack);
	    activeRenderers.add(SkirtLeft);
	    activeRenderers.add(SkirtRight);
	    activeRenderers.add(SkirtRightTop);
	    activeRenderers.add(SkirtLeftTop);

		pieceMap = new EnumMap<ArmorLegs.Slot, Collection<SmartModelRenderer>>(ArmorLegs.Slot.class);
		
		pieceMap.put(ArmorLegs.Slot.CUISSE_LEFT,
				Lists.newArrayList(CuisseLeft, CuisseLeftAccent));
		pieceMap.put(ArmorLegs.Slot.CUISSE_RIGHT,
				Lists.newArrayList(CuisseRight, CuisseRightAccent));
		pieceMap.put(ArmorLegs.Slot.POLEYN_LEFT,
				Lists.newArrayList(PoleynLeft, PoleynLeftAccent));
		pieceMap.put(ArmorLegs.Slot.POLEYN_RIGHT,
				Lists.newArrayList(PoleynRight, PoleynRightAccent));
		pieceMap.put(ArmorLegs.Slot.SKIRT,
				Lists.newArrayList(SkirtBack, SkirtLeft, SkirtLeftTop, SkirtRight, SkirtRightTop));
	
    }
	  
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5){
		  
		
		//before rendering, provide sneaky textures to smart model renderers
		
		//step through equipment, even though we're not sure if they're
		//rendering chest
		if (entity instanceof EntityLivingBase) {
			EntityLivingBase e = (EntityLivingBase) entity;
			ItemStack item = e.getEquipmentInSlot(ArmorSlot.LEGS.getPlayerSlot() + 1);
			
			if (item != null && item.getItem() instanceof ArmorLegs) {
				ArmorLegs armor = (ArmorLegs) item.getItem();
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
							+ "_legs.png");
					
//					if (text == null) {
//						System.out.println("Returned null texture for piece: "
//								+ entry.getKey());
//					}
					
					for (SmartModelRenderer r : entry.getValue()) {
						r.provideTexture(text);
						r.showModel = true;
					}
				}
			} else {
				return;
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
		
		this.isSneak = entity.isSneaking();
		this.isRiding = entity.isRiding();
		
		if (entity instanceof EntityLivingBase) {
			EntityLivingBase ent = (EntityLivingBase) entity;
			this.isChild = ent.isChild();
			this.swingProgress = ent.swingProgress;
			if (ent instanceof EntitySkeleton) {
				this.aimedBow = ((EntitySkeleton) ent).getSkeletonType() == 1;
			} else if (ent instanceof AbstractClientPlayer && !((AbstractClientPlayer) ent).isSpectator()) {
				AbstractClientPlayer clientPlayer = (AbstractClientPlayer) ent;
				ItemStack itemstack = clientPlayer.inventory.getCurrentItem();
				this.heldItemRight = 0;
				if (itemstack == null) {
					this.heldItemRight = 0;
				} else {
					this.heldItemRight = 1;
					if (clientPlayer.getItemInUseCount() > 0)
	                {
	                    EnumAction enumaction = itemstack.getItemUseAction();

	                    if (enumaction == EnumAction.BLOCK)
	                    {
	                        heldItemRight = 3;
	                    }
	                    else if (enumaction == EnumAction.BOW)
	                    {
	                        aimedBow = true;
	                    }
	                }
				}
			}
		}
		
		
		this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		copyModelAngles(this.bipedBody, this.SkirtBack);
		copyModelAngles(this.bipedBody, this.SkirtLeft);
		copyModelAngles(this.bipedBody, this.SkirtLeftTop);
		copyModelAngles(this.bipedBody, this.SkirtRight);
		copyModelAngles(this.bipedBody, this.SkirtRightTop);
		copyModelAngles(this.bipedLeftLeg, this.CuisseLeft);
		copyModelAngles(this.bipedLeftLeg, this.PoleynLeft);
		copyModelAngles(this.bipedRightLeg, this.CuisseRight);
		copyModelAngles(this.bipedRightLeg, this.PoleynRight);

		GlStateManager.pushMatrix();

        if (this.isChild) {
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
//		//set cape living animation
//		if (!CapeTopStripe.isHidden) {
//			boolean windFlag = false;
//			if (e instanceof EntityLivingBase) {
//				windFlag = ( (EntityLivingBase) e).isSprinting();
//			}
//			
//
//			float windBase = .6f;
//			
//			if (!windFlag) {
//				if (!e.onGround && (e.motionY < -0.4)) {
//					windFlag = true;
//					windBase = 1.5f;
//				}
//			}
//			
//			float timeConst = 0.04F; //times f is angle
//			float range = 0.1f;
//			Float ret = capeRotations.get(e.getUniqueID());
//			float oldAngle = (ret == null ? 0.0f : ret);
//			
//			//float oldAngle = CapeTopStripe.rotateAngleX;
//			float targ = range * (float) ((Math.cos(f2 * timeConst) + 1.0) / 2.0);
//			
//			if (windFlag) {
//				//add angle offset for sprint
//				//use old angle to cap transition
//				targ = targ + windBase;
//			} else if (this.isSneak) {
//				targ = targ + 0.5f;
//			}
//			
//			float diff = 0.03f;
//			if (oldAngle - targ > diff) {
//				CapeTopStripe.rotateAngleX = oldAngle - diff;
//			} else if (targ - oldAngle > diff) {
//				CapeTopStripe.rotateAngleX = oldAngle + diff;
//			} else {
//				CapeTopStripe.rotateAngleX = targ;
//			}
//			
//			capeRotations.put(e.getUniqueID(), CapeTopStripe.rotateAngleX);
//		}

		super.setRotationAngles(f, f1, f2, f3, f4, f5, e);
	}

}
