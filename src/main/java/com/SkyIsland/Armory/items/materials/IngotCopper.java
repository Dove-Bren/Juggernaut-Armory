package com.SkyIsland.Armory.items.materials;

import java.util.Map;

import com.SkyIsland.Armory.Armory;
import com.SkyIsland.Armory.api.ForgeManager;
import com.SkyIsland.Armory.api.ForgeManager.MetalRecord;
import com.SkyIsland.Armory.blocks.CopperOreBlock;
import com.SkyIsland.Armory.items.ItemBase;
import com.SkyIsland.Armory.items.armor.ExtendedMaterial;
import com.SkyIsland.Armory.mechanics.DamageType;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Piece of junk metal formed by failing to shape metal correctly or by letting
 * it cool before it was finished.
 * Scrap can be melted down (in Forge construct) to get back some of the input ingredients back. The
 * exact ingredient is set at construction, but defaults to iron.
 * @author Skyler
 *
 */
public class IngotCopper extends ItemBase {

	private String registryName;
	
	public IngotCopper(String unlocalizedName) {
		super();
		
		registryName = unlocalizedName;
		setCreativeTab(Armory.creativeTab);
		
		this.setMaxStackSize(64);
		this.setUnlocalizedName(unlocalizedName);
	}

	@Override
	public boolean isDamageable() {
		return false;
	}
	
	@Override
	public void init() {
		GameRegistry.addSmelting(CopperOreBlock.block, new ItemStack(this), 0.65f);
		
		ExtendedMaterial material;
		Map<DamageType, Float> map = DamageType.freshMap();
		map.put(DamageType.SLASH, 11.0f);
		map.put(DamageType.PIERCE, 9.0f);
		map.put(DamageType.CRUSH, 13.0f);
		map.put(DamageType.MAGIC, 0.0f);
		map.put(DamageType.OTHER, 0.0f);
		Map<DamageType, Float> damageMap = DamageType.freshMap();
		map.put(DamageType.SLASH, 3.0f);
		map.put(DamageType.PIERCE, 2.0f);
		map.put(DamageType.CRUSH, 3.0f);
		map.put(DamageType.MAGIC, 0.0f);
		map.put(DamageType.OTHER, 0.0f);
	    
		material = new ExtendedMaterial(
				"copper",
				"copper",
				60,
				new float[]{.15f, .3f, .4f, .15f},
				map,
				damageMap,
				25,
				this
				);
		
		ForgeManager.instance().registerInputMetal(new MetalRecord(
				this, material, 230, 1300
				));
	}
	
	public void clientInit() {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
    	.register(this, 0, new ModelResourceLocation(Armory.MODID + ":" + this.registryName, "inventory"));
	}
}
