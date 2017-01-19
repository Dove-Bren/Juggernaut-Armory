package com.SkyIsland.Armory.client.armor;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.SkyIsland.Armory.Armory;
import com.SkyIsland.Armory.items.armor.ArmorFeet;
import com.SkyIsland.Armory.items.armor.ArmorFeet.Slot;
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
public class RendererArmorFeet extends ModelBiped {

	private SmartModelRenderer SabatonLeft;
	private SmartModelRenderer SabatonRightSpikeBase;
	private SmartModelRenderer SabatonLeftFront;
	private SmartModelRenderer SabatonRightSpike;
	private SmartModelRenderer SabatonLeftAccent;
	private SmartModelRenderer SabatonRightAccent;
	private SmartModelRenderer SabatonLeftSpikeBase;
	private SmartModelRenderer SabatonRightFront;
	private SmartModelRenderer SabatonLeftSpike;
	private SmartModelRenderer SabatonRight;
	private SmartModelRenderer GreaveRight;
	private SmartModelRenderer GreaveLeft;
	private SmartModelRenderer GreaveLeftAccent;
	private SmartModelRenderer GreaveRightAccent;

	
	private ResourceLocation missingTexture
		= new ResourceLocation(Armory.MODID, "textures/models/armor/missing_feet.png");
	
	private Map<ArmorFeet.Slot, Collection<SmartModelRenderer>> pieceMap;
	private Set<SmartModelRenderer> activeRenderers;
	  
	public RendererArmorFeet(float scale) {
		super(scale, 0, 128, 128);
		textureWidth = 128;
		textureHeight = 64;
		activeRenderers = new HashSet<SmartModelRenderer>();
		
		scale += .01; //extend beyond feet box
		
		SabatonLeft = new SmartModelRenderer(this, 0, 55, missingTexture);
	    SabatonLeft.addBox(-2F, 9F, -3F, 5, 3, 6, scale);
	    SabatonLeft.setRotationPoint(2F, 11F, 0F);
	    SabatonLeft.setTextureSize(128, 64);
	    SabatonLeft.mirror = true;
	    setRotation(SabatonLeft, 0F, 0F, 0F);
	    SabatonLeftFront = new SmartModelRenderer(this, 39, 59, missingTexture);
	    SabatonLeftFront.addBox(-3F, -1F, -6F, 3, 2, 3, scale);
	    SabatonLeftFront.setRotationPoint(2F, 11F, 0F);
	    SabatonLeftFront.setTextureSize(128, 64);
	    SabatonLeftFront.mirror = true;
	    setRotation(SabatonLeftFront, 0F, 0F, 0F);
	    SabatonLeftAccent = new SmartModelRenderer(this, 0, 51, missingTexture);
	    SabatonLeftAccent.addBox(-3F, -2F, 3F, 2, 3, 1, scale);
	    SabatonLeftAccent.setRotationPoint(2F, 11F, 0F);
	    SabatonLeftAccent.setTextureSize(128, 64);
	    SabatonLeftAccent.mirror = true;
	    setRotation(SabatonLeftAccent, 0F, 0F, 0F);
	    SabatonLeftSpikeBase = new SmartModelRenderer(this, 0, 49, missingTexture);
	    SabatonLeftSpikeBase.addBox(-1F, 0F, 3F, 1, 1, 1, scale);
	    SabatonLeftSpikeBase.setRotationPoint(2F, 11F, 0F);
	    SabatonLeftSpikeBase.setTextureSize(128, 64);
	    SabatonLeftSpikeBase.mirror = true;
	    setRotation(SabatonLeftSpikeBase, 0F, 0F, 0F);
	    SabatonLeftSpike = new SmartModelRenderer(this, 8, 48, missingTexture);
	    SabatonLeftSpike.addBox(-1F, -1F, 4F, 1, 2, 1, scale);
	    SabatonLeftSpike.setRotationPoint(2F, 11F, 0F);
	    SabatonLeftSpike.setTextureSize(128, 64);
	    SabatonLeftSpike.mirror = true;
	    setRotation(SabatonLeftSpike, 0F, 0F, 0F);
	    
	    
	    SabatonRight = new SmartModelRenderer(this, 17, 55, missingTexture);
	    SabatonRight.addBox(-3F, 9F, -3F, 5, 3, 6, scale);
	    SabatonRight.setRotationPoint(-2F, 11F, 0F);
	    SabatonRight.setTextureSize(128, 64);
	    SabatonRight.mirror = true;
	    setRotation(SabatonRight, 0F, 0F, 0F);
	    SabatonRightFront = new SmartModelRenderer(this, 48, 59, missingTexture);
	    SabatonRightFront.addBox(0F, -1F, -6F, 3, 2, 3, scale);
	    SabatonRightFront.setRotationPoint(-2F, 11F, 0F);
	    SabatonRightFront.setTextureSize(128, 64);
	    SabatonRightFront.mirror = true;
	    setRotation(SabatonRightFront, 0F, 0F, 0F);
	    SabatonRightAccent = new SmartModelRenderer(this, 6, 51, missingTexture);
	    SabatonRightAccent.addBox(1F, -2F, 3F, 2, 3, 1, scale);
	    SabatonRightAccent.setRotationPoint(-2F, 11F, 0F);
	    SabatonRightAccent.setTextureSize(128, 64);
	    SabatonRightAccent.mirror = true;
	    setRotation(SabatonRightAccent, 0F, 0F, 0F);
	    SabatonRightSpike = new SmartModelRenderer(this, 12, 48, missingTexture);
	    SabatonRightSpike.addBox(0F, -1F, 4F, 1, 2, 1, scale);
	    SabatonRightSpike.setRotationPoint(-2F, 11F, 0F);
	    SabatonRightSpike.setTextureSize(128, 64);
	    SabatonRightSpike.mirror = true;
	    setRotation(SabatonRightSpike, 0F, 0F, 0F);
	    SabatonRightSpikeBase = new SmartModelRenderer(this, 4, 49, missingTexture);
	    SabatonRightSpikeBase.addBox(0F, 0F, 3F, 1, 1, 1, scale);
	    SabatonRightSpikeBase.setRotationPoint(-2F, 11F, 0F);
	    SabatonRightSpikeBase.setTextureSize(128, 64);
	    SabatonRightSpikeBase.mirror = true;
	    setRotation(SabatonRightSpikeBase, 0F, 0F, 0F);
	    
	    GreaveRight = new SmartModelRenderer(this, 22, 32, missingTexture);
	    GreaveRight.addBox(-3F, 6F, -3F, 5, 3, 6, scale);
	    GreaveRight.setRotationPoint(-2F, 11F, 0F);
	    GreaveRight.setTextureSize(128, 64);
	    GreaveRight.mirror = true;
	    setRotation(GreaveRight, 0F, 0F, 0F);
	    GreaveLeft = new SmartModelRenderer(this, 0, 32, missingTexture);
	    GreaveLeft.addBox(-2F, 6F, -3F, 5, 3, 6, scale);
	    GreaveLeft.setRotationPoint(2F, 11F, 0F);
	    GreaveLeft.setTextureSize(128, 64);
	    GreaveLeft.mirror = true;
	    setRotation(GreaveLeft, 0F, 0F, 0F);
	    GreaveLeftAccent = new SmartModelRenderer(this, 0, 26, missingTexture);
	    GreaveLeftAccent.addBox(1F, -3F, -2F, 1, 1, 4, scale);
	    GreaveLeftAccent.setRotationPoint(2F, 11F, 0F);
	    GreaveLeftAccent.setTextureSize(128, 64);
	    GreaveLeftAccent.mirror = true;
	    setRotation(GreaveLeftAccent, 0F, 0F, 0F);
	    GreaveRightAccent = new SmartModelRenderer(this, 10, 26, missingTexture);
	    GreaveRightAccent.addBox(-2F, -3F, -2F, 1, 1, 4, scale);
	    GreaveRightAccent.setRotationPoint(-2F, 11F, 0F);
	    GreaveRightAccent.setTextureSize(128, 64);
	    GreaveRightAccent.mirror = true;
	    setRotation(GreaveRightAccent, 0F, 0F, 0F);
		
	      
	    bipedLeftLeg.addChild(SabatonLeft);
	    bipedLeftLeg.addChild(GreaveLeft);
	      
	    bipedRightLeg.addChild(SabatonRight);
	    bipedRightLeg.addChild(GreaveRight);
	      
	    SabatonLeft.addChild(SabatonLeftFront);
	    SabatonLeft.addChild(SabatonLeftAccent);
	    SabatonLeft.addChild(SabatonLeftSpikeBase);
	    SabatonLeft.addChild(SabatonLeftSpike);
	    SabatonRight.addChild(SabatonRightFront);
	    SabatonRight.addChild(SabatonRightAccent);
	    SabatonRight.addChild(SabatonRightSpikeBase);
	    SabatonRight.addChild(SabatonRightSpike);
	    
	    GreaveLeft.addChild(GreaveLeftAccent);
	    GreaveRight.addChild(GreaveRightAccent);
	    
	    
	    activeRenderers.add(GreaveLeft);
	    activeRenderers.add(GreaveRight);
	    activeRenderers.add(SabatonLeft);
	    activeRenderers.add(SabatonRight);

		pieceMap = new EnumMap<ArmorFeet.Slot, Collection<SmartModelRenderer>>(ArmorFeet.Slot.class);
		
		pieceMap.put(ArmorFeet.Slot.GREAVE_LEFT,
				Lists.newArrayList(GreaveLeft, GreaveLeftAccent));
		pieceMap.put(ArmorFeet.Slot.GREAVE_RIGHT,
				Lists.newArrayList(GreaveRight, GreaveRightAccent));
		pieceMap.put(ArmorFeet.Slot.SABATON_LEFT,
				Lists.newArrayList(SabatonLeft, SabatonLeftAccent, SabatonLeftFront, SabatonLeftSpikeBase, SabatonLeftSpike));
		pieceMap.put(ArmorFeet.Slot.SABATON_RIGHT,
				Lists.newArrayList(SabatonRight, SabatonRightAccent, SabatonRightFront, SabatonRightSpikeBase, SabatonRightSpike));
	
    }
	  
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5){
		  
		
		//before rendering, provide sneaky textures to smart model renderers
		
		//step through equipment, even though we're not sure if they're
		//rendering chest
		if (entity instanceof EntityLivingBase) {
			EntityLivingBase e = (EntityLivingBase) entity;
			ItemStack item = e.getEquipmentInSlot(ArmorSlot.FEET.getPlayerSlot() + 1);
			
			if (item != null && item.getItem() instanceof ArmorFeet) {
				ArmorFeet armor = (ArmorFeet) item.getItem();
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
							+ "_feet.png");
					
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
		copyModelAngles(this.bipedLeftLeg, this.GreaveLeft);
		copyModelAngles(this.bipedLeftLeg, this.SabatonLeft);
		copyModelAngles(this.bipedRightLeg, this.GreaveRight);
		copyModelAngles(this.bipedRightLeg, this.SabatonRight);

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
