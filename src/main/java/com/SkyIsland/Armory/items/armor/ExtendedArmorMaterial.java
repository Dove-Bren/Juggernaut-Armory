package com.SkyIsland.Armory.items.armor;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import com.SkyIsland.Armory.Armory;
import com.SkyIsland.Armory.config.ModConfig;
import com.SkyIsland.Armory.items.ModelRegistry;
import com.SkyIsland.Armory.mechanics.DamageType;

import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.util.ResourceLocation;

/**
 * Extended armor material that breaks down protection into different damage types.
 * @author Skyler
 * @see {@link net.minecraft.item.ItemArmor.ArmorMaterial}
 */
public class ExtendedArmorMaterial {
	
	private static final String textureLocation = "";
	
	private String name;
	
    private int baseDurability;
    
    private float[] partRatio;
    
    private Map<DamageType, Float> reductionMap;
    
    /** Return the enchantability factor of the material */
    //taken from ArmorMaterial
    private int enchantability;

    private Item baseMaterial = null;
    
    private String texturePrefix;
    
    /**
     * Once we wrap a material, store it in cache so we don't have to do it again
     */
    private static Map<ArmorMaterial, ExtendedArmorMaterial> vanillaMaterialCache = new HashMap<ArmorMaterial, ExtendedArmorMaterial>();
    
    private static Map<String, ExtendedArmorMaterial> registeredMaterials = new HashMap<String, ExtendedArmorMaterial>();
    
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
    public ExtendedArmorMaterial(String name, String texturePrefix, int baseDurability, float[] majorPartRatios, Map<DamageType, Float> fullProtectionMap, int enchantability, Item baseMaterial) {
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
        ModelRegistry.instance.registerTexture(new ResourceLocation(Armory.MODID + ":" + textureLocation + texturePrefix));
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

	public static ExtendedArmorMaterial wrap(ArmorMaterial material) {
		//see if we have a cached material
		if (ExtendedArmorMaterial.vanillaMaterialCache.containsKey(material)) {
			return vanillaMaterialCache.get(material);
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
		
		for (DamageType type : DamageType.values())
		if (type.isByDefault())
			map.put(type, points);
		else
			map.put(type, 0.0f);
		
		ExtendedArmorMaterial newmaterial = new ExtendedArmorMaterial(
				material.name(), material.name(), material.getDurability(2), //2 is closest to what we want
				ratios, map,
				material.getEnchantability(), material.getRepairItem()
				);
		
		//store in cache so we don't have to do it again
		vanillaMaterialCache.put(material, newmaterial);
		
		return newmaterial;
		
	}
	
	/**
	 * Looks up a material value by it's name. This only works if the 
	 * value has been instantiated in this runtime.
	 * @param name
	 * @return the material, or null if no such named material exists
	 */
	public static ExtendedArmorMaterial lookupMaterial(String name) {
		return registeredMaterials.get(name);
	}
	
}
