package com.SkyIsland.Armory;

import java.util.Map;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.SkyIsland.Armory.api.ArmorManager;
import com.SkyIsland.Armory.api.ForgeManager;
import com.SkyIsland.Armory.api.WeaponManager;
import com.SkyIsland.Armory.blocks.Pedestal;
import com.SkyIsland.Armory.blocks.WhetstoneBlock;
import com.SkyIsland.Armory.config.ModConfig;
import com.SkyIsland.Armory.entity.EntityArmorerStand;
import com.SkyIsland.Armory.forge.Brazier;
import com.SkyIsland.Armory.forge.Forge;
import com.SkyIsland.Armory.forge.ForgeBlocks;
import com.SkyIsland.Armory.forge.Trough;
import com.SkyIsland.Armory.items.ArmorItems;
import com.SkyIsland.Armory.items.MiscItems;
import com.SkyIsland.Armory.items.ModelRegistry;
import com.SkyIsland.Armory.items.ToolItems;
import com.SkyIsland.Armory.items.WeaponItems;
import com.SkyIsland.Armory.items.armor.ExtendedMaterial;
import com.SkyIsland.Armory.listeners.ItemListener;
import com.SkyIsland.Armory.mechanics.ArmorModificationManager;
import com.SkyIsland.Armory.mechanics.DamageType;
import com.SkyIsland.Armory.proxy.CommonProxy;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = Armory.MODID, name = "Skylander Armory", version = Armory.VERSION, guiFactory = "com.SkyIsland.Armory.config.ConfigGuiFactory")
public class Armory {

	@Instance(value = Armory.MODID) //Tell Forge what instance to use.
    public static Armory instance;
	
    @SidedProxy(clientSide="com.SkyIsland.Armory.proxy.ClientProxy", serverSide="com.SkyIsland.Armory.proxy.CommonProxy")
    public static CommonProxy proxy;
	
    public static final String MODID = "skylander_armory";
    
    public static final String VERSION = "0.1";
    
    public static Logger logger = LogManager.getLogger(MODID);
    
    public static CreativeTabs creativeTab;	
    
    public static ItemListener itemLister;

	public static ExtendedMaterial material;
	
	public static final Random random = new Random();
	
	private static int modID = 1;
	
	public enum Gui_Type {
		BRAZIER,
		FORGE,
		ANVIL;
	}
	
	public static int genModID() {
		return modID++;
	}
 
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        // some example code
        //System.out.println("DIRT BLOCK >> "+Blocks.dirt.getUnlocalizedName());

	    ArmorManager.init();
	    WeaponManager.init();
	    ForgeManager.init();
    	
	    WeaponItems.initItems();
	    ArmorItems.initItems();
	    ToolItems.initItems();
	    MiscItems.initItems();
	    ForgeBlocks.initBlocks();
	    EntityArmorerStand.init();
	    proxy.init();
	    
	    ArmorModificationManager.init();
	    
	    Armory.itemLister = new ItemListener();
	    
	    ModelRegistry.instance.performInjection();
    }
    
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
	  	
	    Armory.creativeTab = new CreativeTabs(MODID){
		    	@Override
		        @SideOnly(Side.CLIENT)
		        public Item getTabIconItem(){
		            //return WhetstoneBlock.block.item;
		    		return Items.iron_sword;
		        }
		    };
	  	
	    //init blocks
	    WhetstoneBlock.preInit();
	    Pedestal.preInit();
	    Brazier.preInit();
	    Forge.preInit();
	    Trough.preInit();
	  	
	    proxy.preInit();
	    
	    new ModConfig(new Configuration(event.getSuggestedConfigurationFile()));
	    new ModelRegistry();
	    
	    Map<DamageType, Float> map = DamageType.freshMap();
		map.put(DamageType.SLASH, 14.0f);
		map.put(DamageType.PIERCE, 12.0f);
		map.put(DamageType.CRUSH, 10.0f);
		map.put(DamageType.MAGIC, 0.0f);
		map.put(DamageType.OTHER, 0.0f);
		Map<DamageType, Float> damageMap = DamageType.freshMap();
		map.put(DamageType.SLASH, 5.0f);
		map.put(DamageType.PIERCE, 2.0f);
		map.put(DamageType.CRUSH, 2.0f);
		map.put(DamageType.MAGIC, 0.0f);
		map.put(DamageType.OTHER, 0.0f);
	    
		material = new ExtendedMaterial(
				"iron",
				"iron",
				100,
				new float[]{.15f, .4f, .3f, .15f},
				map,
				damageMap,
				25,
				Items.iron_ingot
				);
		
	    material = new ExtendedMaterial(
				"void",
				"void",
				100,
				new float[]{.15f, .4f, .3f, .15f},
				map,
				damageMap,
				25,
				Items.iron_ingot
				);
	    
	}
	
    //Event handling and registration stuff from Age of Titans
    //    \/   \/     \/    \/   \/    \/    \/     \/
    
//    @EventHandler
//    public void preInit(FMLPreInitializationEvent event)
//    {
//    	registerModEntity(Titan.class, "Titan");
//    	registerModEntity(FleshTitan.class, "FleshTitan");
//    	registerModEntity(Titan.TitanPart.class, "TitanPart");
//    	proxy.preInit();
//    	
//    	AgeOfTitans.creativeTab = new CreativeTabs("ageoftitans"){
//	    	@Override
//	        @SideOnly(Side.CLIENT)
//	        public Item getTabIconItem(){
//	            return vectorSword;
//	        }
//
//	        @Override
//	        @SideOnly(Side.CLIENT)
//	        public int func_151243_f()
//	        {
//	            return 0;
//	        }
//	    };
//    	
//    	vectorSword = new ItemVectorSword("vector_sword", Vectorium.Material);
//    	GameRegistry.registerItem(vectorSword, "vector_sword");
//    	cr2 = new CR2();
//    	GameRegistry.registerItem(cr2, CR2.unlocalizedName);
//    	titanHeart = new TitanHeart();
//    	GameRegistry.registerItem(titanHeart, TitanHeart.unlocalizedName);
//    	
//    	MinecraftForge.EVENT_BUS.register(new FleshTitan.DropHandler());   	    
//
//    	Vectorium.preInit();
//    	HeartBlock.preInit();
//    	
//	    
//    	
//    }
//       
//    @EventHandler
//    public void load(FMLInitializationEvent event)
//    {
//    	LanguageRegistry.instance().addStringLocalization("entity.Titan.name", "en_US","Titan");
//    	proxy.load();
//		Vectorium.registerAlloys();
//    	initRecipes();
//		
//    }
//       
//    @EventHandler
//    public void postInit(FMLPostInitializationEvent event)
//    {
//    	proxy.postInit();
//    }
//    
//    public void registerModEntity(Class parEntityClass, String parEntityName)
//    	{
//    	    EntityRegistry.registerModEntity(parEntityClass, parEntityName, modMobID++, 
//    	          AgeOfTitans.instance, 80, 3, false);
//    	    
//    	    BiomeGenBase[] biomes = new BiomeGenBase[0];
//    		biomes = ArrayUtils.addAll(biomes, BiomeDictionary.getBiomesForType(Type.CONIFEROUS));
//    		biomes = ArrayUtils.addAll(biomes, BiomeDictionary.getBiomesForType(Type.DEAD));
//    		biomes = ArrayUtils.addAll(biomes, BiomeDictionary.getBiomesForType(Type.DENSE));
//    		biomes = ArrayUtils.addAll(biomes, BiomeDictionary.getBiomesForType(Type.DRY));
//    		biomes = ArrayUtils.addAll(biomes, BiomeDictionary.getBiomesForType(Type.FOREST));
//    		biomes = ArrayUtils.addAll(biomes, BiomeDictionary.getBiomesForType(Type.HILLS));
//    		biomes = ArrayUtils.addAll(biomes, BiomeDictionary.getBiomesForType(Type.HOT));
//    		biomes = ArrayUtils.addAll(biomes, BiomeDictionary.getBiomesForType(Type.JUNGLE));
//    		biomes = ArrayUtils.addAll(biomes, BiomeDictionary.getBiomesForType(Type.LUSH));
//    		biomes = ArrayUtils.addAll(biomes, BiomeDictionary.getBiomesForType(Type.MESA));
//    		biomes = ArrayUtils.addAll(biomes, BiomeDictionary.getBiomesForType(Type.MAGICAL));
//    		biomes = ArrayUtils.addAll(biomes, BiomeDictionary.getBiomesForType(Type.MOUNTAIN));
//    		biomes = ArrayUtils.addAll(biomes, BiomeDictionary.getBiomesForType(Type.PLAINS));
//    		biomes = ArrayUtils.addAll(biomes, BiomeDictionary.getBiomesForType(Type.WASTELAND));
//    		biomes = ArrayUtils.addAll(biomes, BiomeDictionary.getBiomesForType(Type.SWAMP));
//    		biomes = ArrayUtils.addAll(biomes, BiomeDictionary.getBiomesForType(Type.SNOWY));
//    		biomes = ArrayUtils.addAll(biomes, BiomeDictionary.getBiomesForType(Type.SAVANNA));
//    		biomes = ArrayUtils.addAll(biomes, BiomeDictionary.getBiomesForType(Type.RIVER));
//    		biomes = ArrayUtils.addAll(biomes, BiomeDictionary.getBiomesForType(Type.SANDY));
//    		biomes = ArrayUtils.addAll(biomes, BiomeDictionary.getBiomesForType(Type.SPOOKY));
//    		biomes = ArrayUtils.addAll(biomes, BiomeDictionary.getBiomesForType(Type.SPARSE));
//    		EntityRegistry.addSpawn(Titan.class, 7, 1, 3, EnumCreatureType.monster, biomes);
//    		EntityRegistry.addSpawn(FleshTitan.class, 1, 1, 3, EnumCreatureType.monster, biomes);
//    	    //EntityRegistry.addSpawn(parEntityClass, 1, 1, 3, EnumCreatureType.monster, BiomeGenBase.plains);
//    	}
//    
//    private void initRecipes() {
//    	logger.info("Starting recipe registration");
//    	
//    	GameRegistry.addShapelessRecipe(new ItemStack(cr2, 1), new Object[]{Blocks.redstone_block,
//    			Blocks.coal_block, Items.quartz, Items.flint});
//    	
//    	GameRegistry.addShapelessRecipe(new ItemStack(TinkerArmor.heartCanister, 1, 1), 
//    			new Object[]{titanHeart});
//    	//4149:5002
//    	GameRegistry.addShapedRecipe(new ItemStack(vectorSword), " a ", "bcb", "def", 
//    			'a', new ItemStack(TinkerTools.largeSwordBlade, 1, Vectorium.id),
//    			'b', Items.fishing_rod, 'c', titanHeart, 'd', Blocks.lever, 
//    			'e', new ItemStack(TinkerTools.toolRod, 1, 6), 'f', Blocks.piston);
//    	
//    	GameRegistry.addShapedRecipe(new ItemStack(HeartBlock.block, 8), "aaa", "aba", "aaa",
//    			'a', Blocks.stone, 'b', titanHeart);
//    	
//    }
	
}
