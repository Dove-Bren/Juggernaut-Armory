package com.SkyIsland.Armory.client.armor;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.SkyIsland.Armory.Armory;
import com.SkyIsland.Armory.config.ModConfig;
import com.SkyIsland.Armory.items.armor.ArmorHead;
import com.SkyIsland.Armory.items.armor.ArmorHead.Slot;
import com.SkyIsland.Armory.items.armor.ArmorPiece;
import com.SkyIsland.Armory.items.armor.ArmorSlot;
import com.google.common.collect.Lists;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * Less lossy wrapper for all head model renderers
 * @author Skyler
 *
 */
public class RendererArmorHead extends ModelBiped {

	private SmartModelRenderer HelmFront;
	private SmartModelRenderer HelmTopBottom;
	private SmartModelRenderer HelmTopTop;
	private SmartModelRenderer HelmRight;
	private SmartModelRenderer HelmLeft;
	private SmartModelRenderer HelmBack;
	private SmartModelRenderer HelmHornRightTip;
	private SmartModelRenderer HelmHornRight;
	private SmartModelRenderer HelmHornLeftTip;
	private SmartModelRenderer HelmHornLeft;
	private SmartModelRenderer HelmHorn2Left;
	private SmartModelRenderer HelmHorn2Right;
	private SmartModelRenderer VisorFlat;
	private SmartModelRenderer VisorLeft;
	private SmartModelRenderer VisorRight;
	private SmartModelRenderer VisorPitchLeft;
	private SmartModelRenderer VisorPitchRight;
	private SmartModelRenderer BevorFlat;
	private SmartModelRenderer BevorPitchLeft;
	private SmartModelRenderer BevorPitchRight;
	private SmartModelRenderer BevorLeft;
	private SmartModelRenderer BevorRight;
	private SmartModelRenderer CombHornLeftBase3;
	private SmartModelRenderer CombHornLeftBase4;
	private SmartModelRenderer CombHornRightBase1;
	private SmartModelRenderer CombHornLeftBase2;
	private SmartModelRenderer CombHornLeftBase1;
	private SmartModelRenderer CombHornRightBase4;
	private SmartModelRenderer CombHornRightBase2;
	private SmartModelRenderer CombHornRightBase3;
	private SmartModelRenderer CombBase;
	private SmartModelRenderer CombCenturion1;
	private SmartModelRenderer CombCenturion2;
	private SmartModelRenderer CombCenturion3;
	private SmartModelRenderer CombCenturion4;
	private SmartModelRenderer CombLegionnaire1;
	private SmartModelRenderer CombLegionnaire2;
	private SmartModelRenderer CombLegionnaire3;
	private SmartModelRenderer CombLegionnaire4;

	
	private ResourceLocation missingTexture
		= new ResourceLocation(Armory.MODID, "textures/models/armor/missing_head.png");
	
	private Map<ArmorHead.Slot, Collection<SmartModelRenderer>> pieceMap;
	private Set<SmartModelRenderer> activeRenderers;
	  
	public RendererArmorHead(float scale) {
		super(scale, 0, 128, 128);
		textureWidth = 128;
		textureHeight = 128;
		activeRenderers = new HashSet<SmartModelRenderer>();

		//set scale up a small fraction, to do away with zfighting
		scale += 0.01;
		float offsetX, offsetZ, offsetY;
//		offsetX = -8;
//		offsetZ = -10;
//		offsetY = -14;
		offsetX = offsetZ = 0F;
		offsetY = 16F;
		
		
//		OFFSET by -8, 0, 8
		
		HelmFront = new SmartModelRenderer(this, 88, 30, missingTexture);
		HelmFront.addBox(offsetX + -1F, -2F+ offsetY, offsetZ + -1F, 18, 3, 2);
		HelmFront.setRotationPoint(-8F, -32F, -9F);
		HelmFront.setTextureSize(textureWidth, textureHeight);
		HelmFront.mirror = true;
		setRotation(HelmFront, 0F, 0F, 0F);
		HelmTopBottom = new SmartModelRenderer(this, 0, 0, missingTexture);
		HelmTopBottom.addBox(offsetX + -1F, -2F+ offsetY, offsetZ + 0F, 18, 2, 18);
		HelmTopBottom.setRotationPoint(-8F, -32F, -8F);
		HelmTopBottom.setTextureSize(textureWidth, textureHeight);
		HelmTopBottom.mirror = true;
		setRotation(HelmTopBottom, 0F, 0F, 0F);
		HelmTopTop = new SmartModelRenderer(this, 0, 20, missingTexture);
		HelmTopTop.addBox(offsetX + 0F, -4F+ offsetY, offsetZ + 1F, 16, 2, 16);
		HelmTopTop.setRotationPoint(-8F, -32F, -8F);
		HelmTopTop.setTextureSize(textureWidth, textureHeight);
		HelmTopTop.mirror = true;
		setRotation(HelmTopTop, 0F, 0F, 0F);
		HelmRight = new SmartModelRenderer(this, 72, 0, missingTexture);
		HelmRight.addBox(offsetX + -2F, 0F+ offsetY, offsetZ + 4F, 2, 18, 12);
		HelmRight.setRotationPoint(-8F, -32F, -8F);
		HelmRight.setTextureSize(textureWidth, textureHeight);
		HelmRight.mirror = true;
		setRotation(HelmRight, 0F, 0F, 0F);
		HelmLeft = new SmartModelRenderer(this, 100, 0, missingTexture);
		HelmLeft.addBox(offsetX + 16F, 0F+ offsetY, offsetZ + 4F, 2, 18, 12);
		HelmLeft.setRotationPoint(-8F, -32F, -8F);
		HelmLeft.setTextureSize(textureWidth, textureHeight);
		HelmLeft.mirror = true;
		setRotation(HelmLeft, 0F, 0F, 0F);
		HelmBack = new SmartModelRenderer(this, 0, 35, missingTexture);
		HelmBack.addBox(offsetX + -2F, 0F+ offsetY, offsetZ + 16F, 20, 18, 2);
		HelmBack.setRotationPoint(-8F, -32F, -8F);
		HelmBack.setTextureSize(textureWidth, textureHeight);
		HelmBack.mirror = true;
		setRotation(HelmBack, 0F, 0F, 0F);
		HelmHornRightTip = new SmartModelRenderer(this, 64, 28, missingTexture);
		HelmHornRightTip.addBox(offsetX + 22F, -2F+ offsetY, offsetZ + 9F, 2, 6, 2);
		HelmHornRightTip.setRotationPoint(-8F, -32F, -8F);
		HelmHornRightTip.setTextureSize(textureWidth, textureHeight);
		HelmHornRightTip.mirror = true;
		setRotation(HelmHornRightTip, 0F, 0F, 0F);
		HelmHornRight = new SmartModelRenderer(this, 100, 35, missingTexture);
		HelmHornRight.addBox(offsetX + 18F, 2F+ offsetY, offsetZ + 9F, 4, 2, 2);
		HelmHornRight.setRotationPoint(-8F, -32F, -8F);
		HelmHornRight.setTextureSize(textureWidth, textureHeight);
		HelmHornRight.mirror = true;
		setRotation(HelmHornRight, 0F, 0F, 0F);
		HelmHornLeftTip = new SmartModelRenderer(this, 64, 20, missingTexture);
		HelmHornLeftTip.addBox(offsetX + -8F, -2F+ offsetY, offsetZ + 9F, 2, 6, 2);
		HelmHornLeftTip.setRotationPoint(-8F, -32F, -8F);
		HelmHornLeftTip.setTextureSize(textureWidth, textureHeight);
		HelmHornLeftTip.mirror = true;
		setRotation(HelmHornLeftTip, 0F, 0F, 0F);
		HelmHornLeft = new SmartModelRenderer(this, 88, 35, missingTexture);
		HelmHornLeft.addBox(offsetX + -6F, 2F+ offsetY, offsetZ + 9F, 4, 2, 2);
		HelmHornLeft.setRotationPoint(-8F, -32F, -8F);
		HelmHornLeft.setTextureSize(textureWidth, textureHeight);
		HelmHornLeft.mirror = true;
		setRotation(HelmHornLeft, 0F, 0F, 0F);
		HelmHorn2Left = new SmartModelRenderer(this, 44, 48, missingTexture);
		HelmHorn2Left.addBox(offsetX + 18F, -4F+ offsetY, offsetZ + 5F, 2, 6, 2);
		HelmHorn2Left.setRotationPoint(-8F, -32F, -8F);
		HelmHorn2Left.setTextureSize(textureWidth, textureHeight);
		HelmHorn2Left.mirror = true;
		setRotation(HelmHorn2Left, 0F, 0F, 0F);
		HelmHorn2Right = new SmartModelRenderer(this, 54, 48, missingTexture);
		HelmHorn2Right.addBox(offsetX + -4F, -4F+ offsetY, offsetZ + 5F, 2, 6, 2);
		HelmHorn2Right.setRotationPoint(-8F, -32F, -8F);
		HelmHorn2Right.setTextureSize(textureWidth, textureHeight);
		HelmHorn2Right.mirror = true;
		setRotation(HelmHorn2Right, 0F, 0F, 0F);
		VisorFlat = new SmartModelRenderer(this, 90, 40, missingTexture);
		VisorFlat.addBox(offsetX + -1F, 2F+ offsetY, offsetZ + -1F, 18, 6, 1);
		VisorFlat.setRotationPoint(-8F, -32F, -8F);
		VisorFlat.setTextureSize(textureWidth, textureHeight);
		VisorFlat.mirror = true;
		setRotation(VisorFlat, 0F, 0F, 0F);
		VisorLeft = new SmartModelRenderer(this, 44, 38, missingTexture);
		VisorLeft.addBox(offsetX + 16F, 3F+ offsetY, offsetZ + 0F, 1, 5, 4);
		VisorLeft.setRotationPoint(-8F, -32F, -8F);
		VisorLeft.setTextureSize(textureWidth, textureHeight);
		VisorLeft.mirror = true;
		setRotation(VisorLeft, 0F, 0F, 0F);
		VisorRight = new SmartModelRenderer(this, 54, 38, missingTexture);
		VisorRight.addBox(offsetX + -1F, 3F+ offsetY, offsetZ + 0F, 1, 5, 4);
		VisorRight.setRotationPoint(-8F, -32F, -8F);
		VisorRight.setTextureSize(textureWidth, textureHeight);
		VisorRight.mirror = true;
		setRotation(VisorRight, 0F, 0F, 0F);
		VisorPitchLeft = new SmartModelRenderer(this, 106, 48, missingTexture);
		VisorPitchLeft.addBox(offsetX + 0F, 2F+ offsetY, offsetZ + -10F, 1, 6, 10);
		VisorPitchLeft.setRotationPoint(8F, -32F, -8F);
		VisorPitchLeft.setTextureSize(textureWidth, textureHeight);
		VisorPitchLeft.mirror = true;
		setRotation(VisorPitchLeft, 0F, 0.9599311F, 0F); //0.9599311F was
		VisorPitchRight = new SmartModelRenderer(this, 66, 32, missingTexture);
		VisorPitchRight.addBox(offsetX + -1F, 2F+ offsetY, offsetZ + -10F, 1, 6, 10);
		VisorPitchRight.setRotationPoint(-8F, -32F, -8F);
		VisorPitchRight.setTextureSize(textureWidth, textureHeight);
		VisorPitchRight.mirror = true;
		setRotation(VisorPitchRight, 0F, -0.9599311F, 0F); //was -0.9599311F
		BevorFlat = new SmartModelRenderer(this, 20, 57, missingTexture);
		BevorFlat.addBox(offsetX + -1F, 10F+ offsetY, offsetZ + -1F, 18, 6, 1);
		BevorFlat.setRotationPoint(-8F, -32F, -8F);
		BevorFlat.setTextureSize(textureWidth, textureHeight);
		BevorFlat.mirror = true;
		setRotation(BevorFlat, 0F, 0F, 0F);
		BevorPitchLeft = new SmartModelRenderer(this, 62, 48, missingTexture);
		BevorPitchLeft.addBox(offsetX + 0F, 10F+ offsetY, offsetZ + -10F, 1, 6, 10);
		BevorPitchLeft.setRotationPoint(8F, -32F, -8F);
		BevorPitchLeft.setTextureSize(textureWidth, textureHeight);
		BevorPitchLeft.mirror = true;
		setRotation(BevorPitchLeft, 0F, 0.9599311F, 0F);
		BevorPitchRight = new SmartModelRenderer(this, 84, 48, missingTexture);
		BevorPitchRight.addBox(offsetX + -1F, 10F+ offsetY, offsetZ + -10F, 1, 6, 10);
		BevorPitchRight.setRotationPoint(-8F, -32F, -8F);
		BevorPitchRight.setTextureSize(textureWidth, textureHeight);
		BevorPitchRight.mirror = true;
		setRotation(BevorPitchRight, 0F, -0.9599311F, 0F);
		BevorLeft = new SmartModelRenderer(this, 0, 55, missingTexture);
		BevorLeft.addBox(offsetX + 16F, 10F+ offsetY, offsetZ + 0F, 1, 5, 4);
		BevorLeft.setRotationPoint(-8F, -32F, -8F);
		BevorLeft.setTextureSize(textureWidth, textureHeight);
		BevorLeft.mirror = true;
		setRotation(BevorLeft, 0F, 0F, 0F);
		BevorRight = new SmartModelRenderer(this, 10, 55, missingTexture);
		BevorRight.addBox(offsetX + -1F, 10F+ offsetY, offsetZ + 0F, 1, 5, 4);
		BevorRight.setRotationPoint(-8F, -32F, -8F);
		BevorRight.setTextureSize(textureWidth, textureHeight);
		BevorRight.mirror = true;
		setRotation(BevorRight, 0F, 0F, 0F);
		CombHornLeftBase3 = new SmartModelRenderer(this, 24, 69, missingTexture);
		CombHornLeftBase3.addBox(offsetX + 2F, -8F+ offsetY, offsetZ + -5F, 2, 2, 2);
		CombHornLeftBase3.setRotationPoint(-8F, -32F, -8F);
		CombHornLeftBase3.setTextureSize(textureWidth, textureHeight);
		CombHornLeftBase3.mirror = true;
		setRotation(CombHornLeftBase3, 0F, 0F, 0F);
		CombHornLeftBase4 = new SmartModelRenderer(this, 32, 65, missingTexture);
		CombHornLeftBase4.addBox(offsetX + 2F, -12F+ offsetY, offsetZ + -7F, 2, 6, 2);
		CombHornLeftBase4.setRotationPoint(-8F, -32F, -8F);
		CombHornLeftBase4.setTextureSize(textureWidth, textureHeight);
		CombHornLeftBase4.mirror = true;
		setRotation(CombHornLeftBase4, 0F, 0F, 0F);
		CombHornRightBase1 = new SmartModelRenderer(this, 42, 67, missingTexture);
		CombHornRightBase1.addBox(offsetX + 12F, -4F+ offsetY, offsetZ + -3F, 2, 2, 4);
		CombHornRightBase1.setRotationPoint(-8F, -32F, -8F);
		CombHornRightBase1.setTextureSize(textureWidth, textureHeight);
		CombHornRightBase1.mirror = true;
		setRotation(CombHornRightBase1, 0F, 0F, 0F);
		CombHornLeftBase2 = new SmartModelRenderer(this, 12, 67, missingTexture);
		CombHornLeftBase2.addBox(offsetX + 2F, -6F+ offsetY, offsetZ + -5F, 2, 2, 4);
		CombHornLeftBase2.setRotationPoint(-8F, -32F, -8F);
		CombHornLeftBase2.setTextureSize(textureWidth, textureHeight);
		CombHornLeftBase2.mirror = true;
		setRotation(CombHornLeftBase2, 0F, 0F, 0F);
		CombHornLeftBase1 = new SmartModelRenderer(this, 0, 67, missingTexture);
		CombHornLeftBase1.addBox(offsetX + 2F, -4F+ offsetY, offsetZ + -3F, 2, 2, 4);
		CombHornLeftBase1.setRotationPoint(-8F, -32F, -8F);
		CombHornLeftBase1.setTextureSize(textureWidth, textureHeight);
		CombHornLeftBase1.mirror = true;
		setRotation(CombHornLeftBase1, 0F, 0F, 0F);
		CombHornRightBase4 = new SmartModelRenderer(this, 74, 65, missingTexture);
		CombHornRightBase4.addBox(offsetX + 12F, -12F+ offsetY, offsetZ + -7F, 2, 6, 2);
		CombHornRightBase4.setRotationPoint(-8F, -32F, -8F);
		CombHornRightBase4.setTextureSize(textureWidth, textureHeight);
		CombHornRightBase4.mirror = true;
		setRotation(CombHornRightBase4, 0F, 0F, 0F);
		CombHornRightBase2 = new SmartModelRenderer(this, 54, 67, missingTexture);
		CombHornRightBase2.addBox(offsetX + 12F, -6F+ offsetY, offsetZ + -5F, 2, 2, 4);
		CombHornRightBase2.setRotationPoint(-8F, -32F, -8F);
		CombHornRightBase2.setTextureSize(textureWidth, textureHeight);
		CombHornRightBase2.mirror = true;
		setRotation(CombHornRightBase2, 0F, 0F, 0F);
		CombHornRightBase3 = new SmartModelRenderer(this, 66, 69, missingTexture);
		CombHornRightBase3.addBox(offsetX + 12F, -8F+ offsetY, offsetZ + -5F, 2, 2, 2);
		CombHornRightBase3.setRotationPoint(-8F, -32F, -8F);
		CombHornRightBase3.setTextureSize(textureWidth, textureHeight);
		CombHornRightBase3.mirror = true;
		setRotation(CombHornRightBase3, 0F, 0F, 0F);
		CombBase = new SmartModelRenderer(this, 0, 75, missingTexture);
		CombBase.addBox(offsetX + 7F, -6F+ offsetY, offsetZ + 8F, 2, 2, 2);
		CombBase.setRotationPoint(-8F, -32F, -8F);
		CombBase.setTextureSize(textureWidth, textureHeight);
		CombBase.mirror = true;
		setRotation(CombBase, 0F, 0F, 0F);
		CombCenturion1 = new SmartModelRenderer(this, 0, 82, missingTexture);
		CombCenturion1.addBox(offsetX + -1F, -10F+ offsetY, offsetZ + 8F, 18, 4, 2);
		CombCenturion1.setRotationPoint(-8F, -32F, -8F);
		CombCenturion1.setTextureSize(textureWidth, textureHeight);
		CombCenturion1.mirror = true;
		setRotation(CombCenturion1, 0F, 0F, 0F);
		CombCenturion2 = new SmartModelRenderer(this, 40, 83, missingTexture);
		CombCenturion2.addBox(offsetX + 1F, -13F+ offsetY, offsetZ + 8F, 14, 3, 2);
		CombCenturion2.setRotationPoint(-8F, -32F, -8F);
		CombCenturion2.setTextureSize(textureWidth, textureHeight);
		CombCenturion2.mirror = true;
		setRotation(CombCenturion2, 0F, 0F, 0F);
		CombCenturion3 = new SmartModelRenderer(this, 72, 83, missingTexture);
		CombCenturion3.addBox(offsetX + 3F, -16F+ offsetY, offsetZ + 8F, 10, 3, 2);
		CombCenturion3.setRotationPoint(-8F, -32F, -8F);
		CombCenturion3.setTextureSize(textureWidth, textureHeight);
		CombCenturion3.mirror = true;
		setRotation(CombCenturion3, 0F, 0F, 0F);
		CombCenturion4 = new SmartModelRenderer(this, 96, 84, missingTexture);
		CombCenturion4.addBox(offsetX + 6F, -18F+ offsetY, offsetZ + 8F, 4, 2, 2);
		CombCenturion4.setRotationPoint(-8F, -32F, -8F);
		CombCenturion4.setTextureSize(textureWidth, textureHeight);
		CombCenturion4.mirror = true;
		setRotation(CombCenturion4, 0F, 0F, 0F);
		CombLegionnaire1 = new SmartModelRenderer(this, 0, 90, missingTexture);
		CombLegionnaire1.addBox(offsetX + 7F, -10F+ offsetY, offsetZ + 0F, 2, 4, 18);
		CombLegionnaire1.setRotationPoint(-8F, -32F, -8F);
		CombLegionnaire1.setTextureSize(textureWidth, textureHeight);
		CombLegionnaire1.mirror = true;
		setRotation(CombLegionnaire1, 0F, 0F, 0F);
		CombLegionnaire2 = new SmartModelRenderer(this, 40, 95, missingTexture);
		CombLegionnaire2.addBox(offsetX + 7F, -13F+ offsetY, offsetZ + 2F, 2, 3, 14);
		CombLegionnaire2.setRotationPoint(-8F, -32F, -8F);
		CombLegionnaire2.setTextureSize(textureWidth, textureHeight);
		CombLegionnaire2.mirror = true;
		setRotation(CombLegionnaire2, 0F, 0F, 0F);
		CombLegionnaire3 = new SmartModelRenderer(this, 72, 99, missingTexture);
		CombLegionnaire3.addBox(offsetX + 7F, -16F+ offsetY, offsetZ + 4F, 2, 3, 10);
		CombLegionnaire3.setRotationPoint(-8F, -32F, -8F);
		CombLegionnaire3.setTextureSize(textureWidth, textureHeight);
		CombLegionnaire3.mirror = true;
		setRotation(CombLegionnaire3, 0F, 0F, 0F);
		CombLegionnaire4 = new SmartModelRenderer(this, 96, 106, missingTexture);
		CombLegionnaire4.addBox(offsetX + 7F, -18F+ offsetY, offsetZ + 7F, 2, 2, 4);
		CombLegionnaire4.setRotationPoint(-8F, -32F, -8F);
		CombLegionnaire4.setTextureSize(textureWidth, textureHeight);
		CombLegionnaire4.mirror = true;
		setRotation(CombLegionnaire4, 0F, 0F, 0F);

		
		
	      
	    bipedHead.addChild(HelmFront);
	    bipedHead.addChild(HelmTopBottom);
	    bipedHead.addChild(HelmTopTop);
	    bipedHead.addChild(HelmRight);
	    bipedHead.addChild(HelmLeft);
	    bipedHead.addChild(HelmBack);
	    bipedHead.addChild(HelmHornRightTip);
	    bipedHead.addChild(HelmHornLeftTip);
	    bipedHead.addChild(HelmHornRight);
	    bipedHead.addChild(HelmHornLeft);
	    bipedHead.addChild(HelmHorn2Left);
	    bipedHead.addChild(HelmHorn2Right);
	    bipedHead.addChild(VisorFlat);
	    bipedHead.addChild(VisorLeft);
	    bipedHead.addChild(VisorRight);
	    bipedHead.addChild(VisorPitchLeft);
	    bipedHead.addChild(VisorPitchRight);
	    bipedHead.addChild(BevorFlat);
	    bipedHead.addChild(BevorPitchLeft);
	    bipedHead.addChild(BevorPitchRight);
	    bipedHead.addChild(BevorLeft);
	    bipedHead.addChild(BevorRight);
	    bipedHead.addChild(CombHornLeftBase3);
	    bipedHead.addChild(CombHornLeftBase4);
	    bipedHead.addChild(CombHornRightBase1);
	    bipedHead.addChild(CombHornLeftBase2);
	    bipedHead.addChild(CombHornLeftBase1);
	    bipedHead.addChild(CombHornRightBase4);
	    bipedHead.addChild(CombHornRightBase2);
	    bipedHead.addChild(CombHornRightBase3);
	    bipedHead.addChild(CombBase);
	    bipedHead.addChild(CombCenturion1);
	    bipedHead.addChild(CombCenturion2);
	    bipedHead.addChild(CombCenturion3);
	    bipedHead.addChild(CombCenturion4);
	    bipedHead.addChild(CombLegionnaire1);
	    bipedHead.addChild(CombLegionnaire2);
	    bipedHead.addChild(CombLegionnaire3);
	    bipedHead.addChild(CombLegionnaire4);
	    
	    
	    activeRenderers.add(HelmFront);
	    activeRenderers.add(HelmTopBottom);
	    activeRenderers.add(HelmTopTop);
	    activeRenderers.add(HelmRight);
	    activeRenderers.add(HelmLeft);
	    activeRenderers.add(HelmBack);
	    activeRenderers.add(HelmHornRightTip);
	    activeRenderers.add(HelmHornLeftTip);
	    activeRenderers.add(HelmHornRight);
	    activeRenderers.add(HelmHornLeft);
	    activeRenderers.add(HelmHorn2Left);
	    activeRenderers.add(HelmHorn2Right);
	    activeRenderers.add(VisorFlat);
	    activeRenderers.add(VisorLeft);
	    activeRenderers.add(VisorRight);
	    activeRenderers.add(VisorPitchLeft);
	    activeRenderers.add(VisorPitchRight);
	    activeRenderers.add(BevorFlat);
	    activeRenderers.add(BevorPitchLeft);
	    activeRenderers.add(BevorPitchRight);
	    activeRenderers.add(BevorLeft);
	    activeRenderers.add(BevorRight);
	    activeRenderers.add(CombHornLeftBase3);
	    activeRenderers.add(CombHornLeftBase4);
	    activeRenderers.add(CombHornRightBase1);
	    activeRenderers.add(CombHornLeftBase2);
	    activeRenderers.add(CombHornLeftBase1);
	    activeRenderers.add(CombHornRightBase4);
	    activeRenderers.add(CombHornRightBase2);
	    activeRenderers.add(CombHornRightBase3);
	    activeRenderers.add(CombBase);
	    activeRenderers.add(CombCenturion1);
	    activeRenderers.add(CombCenturion2);
	    activeRenderers.add(CombCenturion3);
	    activeRenderers.add(CombCenturion4);
	    activeRenderers.add(CombLegionnaire1);
	    activeRenderers.add(CombLegionnaire2);
	    activeRenderers.add(CombLegionnaire3);
	    activeRenderers.add(CombLegionnaire4);

		pieceMap = new EnumMap<ArmorHead.Slot, Collection<SmartModelRenderer>>(ArmorHead.Slot.class);
		
		pieceMap.put(ArmorHead.Slot.HELM,
				Lists.newArrayList(HelmFront, HelmTopBottom, HelmTopTop, HelmRight, HelmLeft, HelmBack, HelmHornRightTip, HelmHornRight, HelmHornLeftTip, HelmHornLeft, HelmHorn2Left, HelmHorn2Right));
		pieceMap.put(ArmorHead.Slot.VISOR,
				Lists.newArrayList(VisorFlat, VisorLeft, VisorRight, VisorPitchLeft, VisorPitchRight));
		pieceMap.put(ArmorHead.Slot.BEVOR,
				Lists.newArrayList(BevorFlat, BevorPitchLeft, BevorPitchRight, BevorLeft, BevorRight));
		pieceMap.put(ArmorHead.Slot.COMB,
				Lists.newArrayList(CombHornLeftBase3, CombHornLeftBase4, CombHornRightBase1, CombHornLeftBase2, CombHornLeftBase1, CombHornRightBase4, CombCenturion2, CombLegionnaire4, CombBase, CombCenturion1, CombCenturion2, CombCenturion3, CombCenturion4, CombLegionnaire1, CombLegionnaire2, CombLegionnaire3, CombLegionnaire4));
		
		
	
    }
	  
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5){
		  
		
		//before rendering, provide sneaky textures to smart model renderers
		
		//step through equipment, even though we're not sure if they're
		//rendering chest
		if (entity instanceof EntityLivingBase) {
			EntityLivingBase e = (EntityLivingBase) entity;
			ItemStack item = e.getEquipmentInSlot(ArmorSlot.HELMET.getPlayerSlot() + 1);
			
			if (item != null && item.getItem() instanceof ArmorHead) {
				ArmorHead armor = (ArmorHead) item.getItem();
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
							+ "_head.png");
					
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
		
		
		/*
		 * Instead, try pushing and then doing
		 * 
		 * if (this.rotateAngleZ != 0.0F)
                    {
                        GlStateManager.rotate(this.rotateAngleZ * (180F / (float)Math.PI), 0.0F, 0.0F, 1.0F);
                    }

                    if (this.rotateAngleY != 0.0F)
                    {
                        GlStateManager.rotate(this.rotateAngleY * (180F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
                    }

                    if (this.rotateAngleX != 0.0F)
                    {
                        GlStateManager.rotate(this.rotateAngleX * (180F / (float)Math.PI), 1.0F, 0.0F, 0.0F);
                    }
		 * 
		 */
		
//		for (Slot s : ArmorHead.Slot.values())
//		for (SmartModelRenderer r : pieceMap.get(s)) {
//			copyModelAngles(this.bipedHead, r);
//		}
		
		GlStateManager.pushMatrix();
		
		if (ModConfig.config.getTestValue(ModConfig.Key.CAPE_DIFF) < 0.0F) {
			GlStateManager.translate(bipedHead.rotationPointX, bipedHead.rotationPointY, bipedHead.rotationPointZ);
		}
		
		GlStateManager.scale(0.5f, 0.5f, 0.5f);
		if (bipedHead.rotateAngleZ != 0.0F)
        {
            GlStateManager.rotate(bipedHead.rotateAngleZ * (180F / (float)Math.PI), 0.0F, 0.0F, 1.0F);
        }

        if (bipedHead.rotateAngleY != 0.0F)
        {
            GlStateManager.rotate(bipedHead.rotateAngleY * (180F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
        }

        if (bipedHead.rotateAngleX != 0.0F)
        {
            GlStateManager.rotate(bipedHead.rotateAngleX * (180F / (float)Math.PI), 1.0F, 0.0F, 0.0F);
        }

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
		
		if (e instanceof EntityArmorStand)
        {
            EntityArmorStand entityarmorstand = (EntityArmorStand)e;
            this.bipedHead.rotateAngleX = 0.017453292F * entityarmorstand.getHeadRotation().getX();
            this.bipedHead.rotateAngleY = 0.017453292F * entityarmorstand.getHeadRotation().getY();
            this.bipedHead.rotateAngleZ = 0.017453292F * entityarmorstand.getHeadRotation().getZ();
            this.bipedHead.setRotationPoint(0.0F, 1.0F, 0.0F);
            this.bipedBody.rotateAngleX = 0.017453292F * entityarmorstand.getBodyRotation().getX();
            this.bipedBody.rotateAngleY = 0.017453292F * entityarmorstand.getBodyRotation().getY();
            this.bipedBody.rotateAngleZ = 0.017453292F * entityarmorstand.getBodyRotation().getZ();
            this.bipedLeftArm.rotateAngleX = 0.017453292F * entityarmorstand.getLeftArmRotation().getX();
            this.bipedLeftArm.rotateAngleY = 0.017453292F * entityarmorstand.getLeftArmRotation().getY();
            this.bipedLeftArm.rotateAngleZ = 0.017453292F * entityarmorstand.getLeftArmRotation().getZ();
            this.bipedRightArm.rotateAngleX = 0.017453292F * entityarmorstand.getRightArmRotation().getX();
            this.bipedRightArm.rotateAngleY = 0.017453292F * entityarmorstand.getRightArmRotation().getY();
            this.bipedRightArm.rotateAngleZ = 0.017453292F * entityarmorstand.getRightArmRotation().getZ();
            this.bipedLeftLeg.rotateAngleX = 0.017453292F * entityarmorstand.getLeftLegRotation().getX();
            this.bipedLeftLeg.rotateAngleY = 0.017453292F * entityarmorstand.getLeftLegRotation().getY();
            this.bipedLeftLeg.rotateAngleZ = 0.017453292F * entityarmorstand.getLeftLegRotation().getZ();
            this.bipedLeftLeg.setRotationPoint(1.9F, 11.0F, 0.0F);
            this.bipedRightLeg.rotateAngleX = 0.017453292F * entityarmorstand.getRightLegRotation().getX();
            this.bipedRightLeg.rotateAngleY = 0.017453292F * entityarmorstand.getRightLegRotation().getY();
            this.bipedRightLeg.rotateAngleZ = 0.017453292F * entityarmorstand.getRightLegRotation().getZ();
            this.bipedRightLeg.setRotationPoint(-1.9F, 11.0F, 0.0F);
            copyModelAngles(this.bipedHead, this.bipedHeadwear);
        }
		
	}

}
