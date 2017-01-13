package com.SkyIsland.Armory.items.common;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import com.SkyIsland.Armory.Armory;
import com.SkyIsland.Armory.config.ModConfig;
import com.SkyIsland.Armory.items.armor.ArmorSlot;
import com.SkyIsland.Armory.mechanics.DamageType;

import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;

/**
 * Extended armor/weapon material that breaks down protection and damage
 * into different damage types.
 * @author Skyler
 * @see {@link net.minecraft.item.ItemArmor.ArmorMaterial}
 */
public class ExtendedMaterial {
	
	public static final String textureLocation = "";
	public static final String modelTextureLocation = "textures/models/armor/";
	
	private String name;
	
    private int baseDurability;
    
    private float[] partRatio;
    
    private Map<DamageType, Float> reductionMap;
    
    /**
     * Base damage components for this material.
     */
    private Map<DamageType, Float> damageMap;
    
    /** Return the enchantability factor of the material */
    //taken from ArmorMaterial
    private int enchantability;

    private Item baseMaterial = null;
    
    private String texturePrefix;
    
    /**
     * Once we wrap a material, store it in cache so we don't have to do it again
     */
    private static Map<ArmorMaterial, ExtendedMaterial> vanillaArmorMaterialCache = new HashMap<ArmorMaterial, ExtendedMaterial>();
    
    private static Map<ToolMaterial, ExtendedMaterial> vanillaWeaponMaterialCache = new HashMap<ToolMaterial, ExtendedMaterial>();
    
    private static Map<String, ExtendedMaterial> registeredMaterials = new HashMap<String, ExtendedMaterial>();
    
    /**
     * Creates a new Extended material from the provided values
     * @param name Name of the material
     * @param texturePrefix prefix to all textures of this material
     * @param baseDurability Durability based. Part durability ratios are multiplied by this to get actual part durability
     * @param majorPartRatios Part-to-part ratio of protection amongst the four armor pieces. Arrangement is per {@link ArmorSlot}
     * @param fullProtectionMap Map between damage types and the <i>total protection from a complete set</i> made from this material.
     * @param enchantability
     * @param baseMaterial Material used to create and repair things
     */
    public ExtendedMaterial(String name, String texturePrefix, int baseDurability, float[] majorPartRatios, Map<DamageType, Float> fullProtectionMap, Map<DamageType, Float> damageMap, int enchantability, Item baseMaterial) {
        this.name = name;
        this.enchantability = enchantability;
        this.baseMaterial = baseMaterial;
        this.baseDurability = baseDurability;
        this.texturePrefix = texturePrefix;
        
        //process map, on teh off chance it's a sparse tree
        this.reductionMap = DamageType.freshMap();//fullProtectionMap;
        for (DamageType key : DamageType.values()) {
        	this.reductionMap.put(key, 
        			fullProtectionMap.containsKey(key) ? fullProtectionMap.get(key)
        											   : 0.0f
        			);
        }
        this.damageMap = DamageType.freshMap();
        for (DamageType key : DamageType.values()) {
        	this.damageMap.put(key,
        			damageMap.containsKey(key) ? damageMap.get(key)
        					                   : 0.0f
        			);
        }
        
        //normalize part ratios
        partRatio = new float[4];
        float total = 0.0f;
        for (int i = 0; i < 4; i++)
        	total += Math.abs(majorPartRatios[i]);

        if (total <= 0) {
        	//map is all 0's, so...
        	partRatio = defaultRatios();
        } else {
	        for (int i = 0; i < 4; i++)
	        	partRatio[i] = Math.abs(majorPartRatios[i]) / total;
        }
        
        if (registeredMaterials.containsKey(name)) {
        	Armory.logger.warn("Duplicate material being registered: " + name
        			+ "! Taking newest version as version");
        }
        Armory.proxy.registerMaterial(this);
        
        registeredMaterials.put(name, this);
        //ModelRegistry.instance.registerTexture(new ResourceLocation(Armory.MODID + ":" + textureLocation + texturePrefix));
        //moved into client proxy
    }
    
    private static float[] defaultRatios() {
    	return new float[]{0.175f, 0.4f, 0.3f, 0.125f};
    }

    /**
     * Returns the durability for an armor piece with the given rate. This is
     * a straight multiplication (return baseDurability * armorPieceRate)
     * @param armorPieceRate This armor piece's takeaway from material durability.
     */
    public int getDurability(float armorPieceRate)
    {
        return Math.round(baseDurability * armorPieceRate);
    }
    
    public float getRawDurability() {
    	return baseDurability;
    }

    /*
     * Takeaways:
     * armor piece durabilities should be around 1, for each piece.
     * protection map is the protection for a FULL SET in the given material
     * part ratios apply to head, chest, etc pieces. They are not per-part-piece
     */
    
    /**
     * Calculate the total protection a certain major piece of armor should give.
     * This is a not the protection a single piece should give, but the total
     * for a given piece of armor
     * @param armorType The vanilla armor type constant (0 = head, 1 = torso, etc)
     * @see {@link #getDamageReductionAmount(ArmorSlot)}
     * @see {@link ArmorSlot}
     */
    public Map<DamageType, Float> getDamageReductionAmount(int armorType) {
        return getDamageReductionAmount(ArmorSlot.getSlot(armorType));
    }
    
    /**
     * Calculate the total protection a certain major piece of armor should give.
     * This is a not the protection a single piece should give, but the total
     * for a given piece of armor
     * @param slot The armor slot piece to account for
     */
    public Map<DamageType, Float> getDamageReductionAmount(ArmorSlot slot) {
        Map<DamageType, Float> protectionMap = DamageType.freshMap();
        
        for (DamageType type : DamageType.values()) {
        	protectionMap.put(type,
        			reductionMap.get(type) * partRatio[slot.getSlot()]
        			);
        }
        
        return protectionMap;
    }
    
    /**
     * Returns this material's damage values
     * @return
     */
    public Map<DamageType, Float> getDamageMap() {
    	return damageMap;
    }

    /**
     * Return the enchantability factor of the material.
     */
    public int getEnchantability()
    {
        return this.enchantability;
    }

    /**
     * Get a main crafting component of this Armor Material (example is Items.iron_ingot)
     */
    public Item getRepairItem()
    {
        return baseMaterial;
    }

    public String getName() {
        return this.name;
    }
    
    public String getTexturePrefix() {
    	return this.texturePrefix;
    }

	public static ExtendedMaterial wrap(ArmorMaterial material) {
		//see if we have a cached material
		if (ExtendedMaterial.vanillaArmorMaterialCache.containsKey(material)) {
			return vanillaArmorMaterialCache.get(material);
		}
		
		//create an impromptu material around an old-school ArmorMaterial
		float[] ratios = new float[]{
				material.getDamageReductionAmount(0),
				material.getDamageReductionAmount(1),
				material.getDamageReductionAmount(2),
				material.getDamageReductionAmount(3),
		};
		
		float points = 0.0f;
		for (int i = 0; i < 4; i++) {
			points += material.getDamageReductionAmount(i);
		}
		
		//the armor will not be getting all of that tho; reduce it down to default ratio
		points *= ModConfig.config.getDefaultRatio();
		
		Map<DamageType, Float> map = new EnumMap<DamageType, Float>(DamageType.class);
		Map<DamageType, Float> dMap = DamageType.freshMap();
		
		for (DamageType type : DamageType.values())
		if (type.isByDefault())
			map.put(type, points);
		else
			map.put(type, 0.0f);
		
//		EnumHelper.addToolMaterial(name, harvestLevel, maxUses, efficiency, damage, enchantability)
//		for (DamageType type : DamageType.values())
//		if (type.isByDefault())
//			dMap.put(type, points);
//		else
//			dMap.put(type, 0.0f);
		
		ExtendedMaterial newmaterial = new ExtendedMaterial(
				material.name(), material.name(), material.getDurability(2), //2 is closest to what we want
				ratios, map, dMap,
				material.getEnchantability(), material.getRepairItem()
				);
		
		//store in cache so we don't have to do it again
		vanillaArmorMaterialCache.put(material, newmaterial);
		
		return newmaterial;
		
	}
	
	public ExtendedMaterial wrap(ToolMaterial material) {
		//see if we have a cached material
		if (ExtendedMaterial.vanillaWeaponMaterialCache.containsKey(material)) {
			return vanillaWeaponMaterialCache.get(material);
		}
		
		
		
		Map<DamageType, Float> map = new EnumMap<DamageType, Float>(DamageType.class);
		Map<DamageType, Float> aMap = DamageType.freshMap();
		
		float damage = material.getDamageVsEntity();
		
		for (DamageType type : DamageType.values())
		if (type.isByDefault())
			map.put(type, damage);
		else
			map.put(type, 0.0f);
		
//				EnumHelper.addToolMaterial(name, harvestLevel, maxUses, efficiency, damage, enchantability)
//				for (DamageType type : DamageType.values())
//				if (type.isByDefault())
//					dMap.put(type, points);
//				else
//					dMap.put(type, 0.0f);
		
		ExtendedMaterial newmaterial = new ExtendedMaterial(
				material.name(), material.name(), material.getMaxUses(), //2 is closest to what we want
				new float[]{.25f, .25f, .25f, .25f}, aMap, map,
				material.getEnchantability(), material.getRepairItemStack().getItem()
				);
		
		//store in cache so we don't have to do it again
		vanillaWeaponMaterialCache.put(material, newmaterial);
		
		return newmaterial;
	}
	
	/**
	 * Looks up a material value by it's name. This only works if the 
	 * value has been instantiated in this runtime.
	 * @param name
	 * @return the material, or null if no such named material exists
	 */
	public static ExtendedMaterial lookupMaterial(String name) {
		return registeredMaterials.get(name);
	}
	
	/**
	 * Finds a registered material by the material registered as it's repair
	 * item. If multiple exist, the first is returned.
	 * @param baseMaterial
	 * @return null if none found, else first ExtendedMaterial such
	 * that baseMaterial.equals(material.getRepairItem())
	 */
	public static ExtendedMaterial lookupMaterial(Item baseMaterial) {
		if (registeredMaterials.isEmpty())
			return null;
		
		for (ExtendedMaterial material : registeredMaterials.values()) {
			if (baseMaterial.equals(material.getRepairItem()))
				return material;
		}
		
		return null;
	}
	
	
}
