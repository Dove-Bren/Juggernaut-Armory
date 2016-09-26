package com.SkyIsland.Armory.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.SkyIsland.Armory.Armory;
import com.SkyIsland.Armory.config.ModConfig;
import com.SkyIsland.Armory.items.common.ExtendedMaterial;
import com.SkyIsland.Armory.mechanics.DamageType;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class ForgeManager {

	public static class FuelRecord {
		
		private int burnTicks;
		
		private float maxHeat;
		
		private float heatRate;
		
		public FuelRecord(int burnTicks, float maxHeat, float heatRate) {
			this.burnTicks = burnTicks;
			this.maxHeat = maxHeat;
			this.heatRate = heatRate;
		}

		public int getBurnTicks() {
			return burnTicks;
		}

		public float getMaxHeat() {
			return maxHeat;
		}

		public float getHeatRate() {
			return heatRate;
		}
	}
	
	public static class CoolantRecord {
		
		private float coolingRate;
		
		public CoolantRecord(float coolingRate) {
			this.coolingRate = coolingRate;
		}
		
		public float getCoolingRate() {
			return this.coolingRate;
		}
	}
	
	public static class MetalRecord {
		
		protected int burnTime;
		
		protected float requiredHeat;
		
		private Item metal;
		
		private ExtendedMaterial material;
		
		private int meta;
		
		public MetalRecord(Item metal, ExtendedMaterial material, int burnTime, float requiredHeat) {
			this(metal, material, burnTime, requiredHeat, -1);
		}
		
		public MetalRecord(Item metal, ExtendedMaterial material, int burnTime, float requiredHeat, int requiredMeta) {
			this.metal = metal;
			this.burnTime = burnTime;
			this.requiredHeat = requiredHeat;
			this.meta = requiredMeta;
			this.material = material;
		}
		
		protected boolean accepts(ItemStack stack) {
			return (stack != null && OreDictionary.itemMatches(stack, new ItemStack(metal), meta != -1)
					&& (meta == -1 || meta == stack.getItemDamage()));
			//return whether the stack is the same item and (requiredMeta is -1 or the same)
		}

		public int getBurnTime() {
			return burnTime;
		}

		public float getRequiredHeat() {
			return requiredHeat;
		}

		public Item getMetal() {
			return metal;
		}

		public int getMeta() {
			return meta;
		}
		
		public ExtendedMaterial getMaterial() {
			return material;
		}
	}
	
	public static class AlloyRecipe {
		
		protected Item result;
		
		private List<ItemStack> inputs;
		
		private int resultAmount;
		
		public AlloyRecipe(Item result, Collection<ItemStack> inputs, int amount) {
			this.result = result;
			this.inputs = new ArrayList<ItemStack>(inputs);
			this.resultAmount = amount;
		}
		
		/**
		 * Create an itemstack result for this alloy
		 * @return
		 */
		protected ItemStack getResult() {
			return this.getResult(1);
		}
		
		/**
		 * Create an itemstack result for this alloy, with the quantity multiplied
		 * by the multiplier.
		 * @param multiplier
		 * @return
		 */
		protected ItemStack getResult(int multiplier) {
			if (multiplier < 1)
				return null;
			
			ItemStack ret = new ItemStack(result);
			ret.stackSize = resultAmount;
			return ret;
		}
		
		/**
		 * Checks whether the alloy matches the given ingredients, and returns
		 * the multiplier. For example, if bronze was 1 copper 1 tin and the input
		 * was 2 tin and 2 copper, should return '2'. 
		 * If the inputs don't match this alloy, return -1.
		 * <p>
		 * Since we receive integer coefficients, this method is expected to round
		 * down to the nearest number. For example, if steel is 2 coal and 1 iron,
		 * and the input is 3 coal, 1 iron, the result is still just 1.
		 * </p>
		 * @param ingredients
		 * @return
		 */
		protected int matches(Collection<ItemStack> ingredients) {
			if (ingredients == null || ingredients.isEmpty())
				return -1;
			
//			//make lists we can ruin
//			List<ItemStack> ins = new LinkedList<ItemStack>(ingredients);
//			List<ItemStack> reqs = new LinkedList<ItemStack>(inputs);
//			
//			Iterator<ItemStack> it;
//			ItemStack inspect;
//			boolean found;
//			for (ItemStack input : ins) {
//				//for every input, go through requirements and 'mark off' what we have
//				if (reqs.isEmpty()) {
//					//already exhausted requirements. Doesn't match.
//					return false;
//				}
//				found = false;
//				it = reqs.listIterator(0);
//				while (it.hasNext()) {
//					//look through reqs for a match
//					inspect = it.next();
//					if (inspect.getIsItemStackEqual(input)) {
//						it.remove();
//						break;
//					}
//				}
//				
//				if (!found) {
//					//didn't find match. not a match
//					return false;
//				}
//				
//				//else we found it and marked it off. success, continue to next input
//			}
//			
//			//finished inputs. If it is a match, reqs should be empty, too
//			return reqs.isEmpty();
			
			//use summary maps instead
			Map<Item, Integer> inputMap = new HashMap<Item, Integer>();
			Map<Item, Integer> reqMap = new HashMap<Item, Integer>();
			
			if (ingredients != null && !ingredients.isEmpty())
			for (ItemStack stack : ingredients) {
				if (!inputMap.containsKey(stack.getItem()))
					inputMap.put(stack.getItem(), new Integer(0));
				inputMap.put(stack.getItem(), inputMap.get(stack.getItem()) + 1);
			}
			
			if (inputs != null && !inputs.isEmpty())
			for (ItemStack stack : inputs) {
				if (!reqMap.containsKey(stack.getItem()))
					reqMap.put(stack.getItem(), new Integer(0));
				reqMap.put(stack.getItem(), reqMap.get(stack.getItem()) + 1);
			}
			
			if (reqMap.keySet().size() != inputMap.keySet().size())
				return -1; //size of items in doesn't match; invalid match
			
			for (Item key : reqMap.keySet()) {
				if (!inputMap.containsKey(key))
					return -1;
			}
			
			//since size was the same and we didn't find an item that didn't match
			//we accept.
			
			//now figure out multiplier
			int mult = 9999999;
			for (Item key : inputMap.keySet()) {
				if (inputMap.get(key) / reqMap.get(reqMap) < mult)
					mult = inputMap.get(key) / reqMap.get(reqMap);
				
				if (mult < 0)
					return -1;
				if (mult == 0)
					return 0;
			}
			
			return mult;
		}
	}
	
	/**
	 * Recipe template used when finalizing hot metal into final products.
	 */
	public static class ForgeRecipe {
		
		private static int seedOffsetIndex = 0;
		
		private long seedOffset;
		
		private int requiredLevel;
		
		private boolean[][] metalMap;
		
		private IForgeTemplate template;
		
		private int fullCache;
		
		public ForgeRecipe(int requiredLevel, boolean[][] metalMap, IForgeTemplate template) {
			fullCache = 0;
			this.requiredLevel = requiredLevel;
			seedOffset = seedOffsetIndex++ * 1702;
			if (metalMap != null) {
				this.metalMap = new boolean[10][10];
				int i = 0;
				for (boolean[] row : metalMap) {
					if (i > 10) {
						Armory.logger.warn("Forge recipe registered with too large of a MetalMap. This will be chopped to a 10x10");
						break;
					}
					
					if (row.length != 10) {
						Armory.logger.warn("  Found row in metalMap that was not 10 long. This will be expanded (or truncated) to 10");
						for (int j = 0; j < 10; j++) {
							if (j < row.length) {
								this.metalMap[i][j] = row[j];
								if (row[j]) fullCache++; //increment count of full
							} else
								this.metalMap[i][j] = false;
						}
					} else {
						//this.metalMap[i] = row;
						for (int j = 0; j < 10; j++) {
							this.metalMap[i][j] = row[j];
							if (row[j]) fullCache++;
						}
					}
					i++;
				}
				
				if (i < 10) {
					//didn't reach 10 rows
					Armory.logger.warn("  MetalMap recipe found with fewer than 10 rows. This will be expanded to fit 10 rows.");
					for (; i < 10; i++) {
						this.metalMap[i] = new boolean[]{false, false, false, false, false, false, false, false, false, false};
					}
				}
			}
			
			this.template = template;
		}
		
		public boolean[][] getRawMap() {
			return metalMap;
		}
		
		/**
		 * Runs and checks whether the given metal map matches this recipe.
		 * If it does, returns the user's relative performance. (ranges from 0 to 1).
		 * If it does not, returns -1;
		 * @param inputMap
		 * @return [0-1] on success, -1 on failure
		 * @throws IllegalArgumentException when the input map is not the right size
		 */
		public float matches(boolean[][] inputMap) throws IllegalArgumentException {
			if (inputMap.length != 10 || inputMap[0].length != 10) {
				throw new IllegalArgumentException("Invalid input map size: " + inputMap.length + " x " + inputMap[0].length);
			}
			int misses = 0;
			for (int i = 0; i < 10; i++)
			for (int j = 0; j < 10; j++) {
				if (inputMap[i][j] == metalMap[i][j])
					continue;
				misses++;
			}
			
			//misses now has the number of cells that didn't match
			int maxMisses = Math.max(0, Math.round(
					ModConfig.config.getRecipeTolerance() * (float) fullCache));
			System.out.println("Recipe check. Misses: " + misses + " (max " + maxMisses + ")");
			
			if (misses > maxMisses)
				return -1f;
			
			return 1 - ((float) misses / (float) maxMisses);
		}
		
		public ItemStack produce(MetalRecord baseMetal, float performance) {
			return template.produce(baseMetal, performance);
		}
		
		public long getSeedOffset() {
			return seedOffset;
		}
		
		public int getRequiredLevel() {
			return requiredLevel;
		}
		
		/**
		 * Makes a 10x10 grid of booleans based on the passed rows. spaces
		 * represent false's, and anything else represents a true. Example:
		 * ".. . ...  " is [true,true,false,true,false,true,true,true,false,false]
		 * @return
		 */
		public static final boolean[][] drawMap(String[] rows) {
			boolean[][] map = new boolean[10][10];
			
			int i, pos;
			for (i = 0; i < rows.length; i++) {
				if (i >= 10)
					break;
				
				boolean[] row = new boolean[10];
				pos = 0;
				for (char c :rows[i].toCharArray()) {
					if (c == ' ')
						;
					else
						row[pos] = true;
					
					pos++;
				}
				map[i] = row;
			}
			
			// if (i < 10)
			// dont' need to insert blanks, as they default to false
			
			return map;
		}
	}
	
	private static ForgeManager instance;
	
	public static void init() {
		instance = new ForgeManager();
		setupVanilla();
	}
	
	private static void setupVanilla() {
		instance.registerFuel(Items.coal, new FuelRecord(1600, 1700, 0.2f));
		instance.registerFuel(Item.getItemFromBlock(Blocks.coal_block), new FuelRecord(16000, 1700, 0.2f));
		instance.registerFuel(Items.lava_bucket, new FuelRecord(20000, 3000, 0.8f));
		
		instance.registerFuel(Item.getItemFromBlock(Blocks.log), new FuelRecord(600, 1200, 0.2f));
		instance.registerFuel(Item.getItemFromBlock(Blocks.log2), new FuelRecord(600, 1200, 0.2f));
		instance.registerFuel(Item.getItemFromBlock(Blocks.planks), new FuelRecord(300, 1000, 0.4f));
		instance.registerFuel(Items.blaze_powder, new FuelRecord(4000, 2500, 1f));
		
		//register metals
		Map<DamageType, Float> map = DamageType.freshMap();
		map.put(DamageType.SLASH, 14.0f);
		map.put(DamageType.PIERCE, 12.0f);
		map.put(DamageType.CRUSH, 10.0f);
		map.put(DamageType.MAGIC, 0.0f);
		map.put(DamageType.OTHER, 0.0f);
		Map<DamageType, Float> damageMap = DamageType.freshMap();
		damageMap.put(DamageType.SLASH, 5.0f);
		damageMap.put(DamageType.PIERCE, 2.0f);
		damageMap.put(DamageType.CRUSH, 2.0f);
		damageMap.put(DamageType.MAGIC, 0.0f);
		damageMap.put(DamageType.OTHER, 0.0f);
	    
		ExtendedMaterial material;
		material = new ExtendedMaterial(
				"Iron",
				"iron",
				100,
				new float[]{.15f, .4f, .3f, .15f},
				map,
				damageMap,
				25,
				Items.iron_ingot
				);
		
		
		instance.registerInputMetal(new MetalRecord(Items.iron_ingot, material, 200, 1500));
		instance.registerInputMetal(new MetalRecord(Items.gold_ingot, null, 160, 900));
		instance.registerInputMetal(new MetalRecord(Items.coal, null, 400, 1800));
		
		//register coolants
		instance.registerCoolant(FluidRegistry.WATER, new CoolantRecord(2.0f));
	}
	
	public static ForgeManager instance() {
		return instance;
	}
	
	private Map<Item, FuelRecord> fuels;
	
	private Map<Fluid, CoolantRecord> coolants;
	
	private List<MetalRecord> metals;
	
	private List<AlloyRecipe> recipes;
	
	private List<ForgeRecipe> forgeRecipes;
	
	private ForgeManager() {
		fuels = new HashMap<Item, FuelRecord>();
		coolants = new HashMap<Fluid, CoolantRecord>();
		metals = new LinkedList<MetalRecord>();
		recipes = new LinkedList<AlloyRecipe>();
		forgeRecipes = new LinkedList<ForgeRecipe>();
	}
	
	public void registerFuel(Item item, FuelRecord record) {
		fuels.put(item, record);
	}
	
	public FuelRecord getFuelRecord(Item item) {
		return fuels.get(item);
	}
	
	public void registerInputMetal(MetalRecord record) {
		this.metals.add(record);
	}
	
	public void registerAlloyRecipe(AlloyRecipe recipe) {
		recipes.add(recipe);
	}
	
	/**
	 * Attempts a lookup for the given itemstack. Returns the first
	 * record (in order of registration) that accepts the given itemstack, or
	 * null if no such record is found.
	 * @param metal
	 * @return The first record that accepted the stack, or null if none were found
	 * @see {@link MetalRecord}
	 * @see {@link MetalRecord#accepts(ItemStack)}
	 */
	public MetalRecord getMetalRecord(ItemStack metal) {
		if (metals.isEmpty())
			return null;
		
		for (MetalRecord record : metals) {
			if (record.accepts(metal))
				return record;
		}
		
		return null;
	}
	
	/**
	 * Returns the first registered alloy that accepts on the given inputs.
	 * @param inputMetals
	 * @return The resultant alloy, or null if none were found
	 */
	public ItemStack getAlloy(Collection<ItemStack> inputMetals) {
		if (recipes.isEmpty())
			return null;
		
		int mult;
		for (AlloyRecipe recipe : recipes) {
			if ((mult = recipe.matches(inputMetals)) != -1)
				return recipe.getResult(mult);
		}
		
		return null;
	}
	
	public void registerCoolant(Fluid fluid, CoolantRecord record) {
		this.coolants.put(fluid, record);
	}
	
	public CoolantRecord getCoolant(Fluid fluid) {
		return coolants.get(fluid);
	}
	
	public void registerForgeRecipe(ForgeRecipe recipe) {
		this.forgeRecipes.add(recipe);
	}
	
	/**
	 * Attempts to match the given input map to a registered. Finds the recipe
	 * that matched with the highest performance (or the first registered in
	 * case of a tie!) and uses it to produce a result with the given baseMetal.
	 * @param baseMetal
	 * @param inputMap
	 * @return A new forged item, or null if no matches were found (including when
	 * no recipes were <emphasis>close enough</emphasis>
	 * @see {@link ModConfig#getRecipeTolerance()}
	 */
	public ItemStack getForgeResult(MetalRecord baseMetal, boolean[][] inputMap) {
		if (forgeRecipes.isEmpty())
			return null;
		
		float[] perf = new float[forgeRecipes.size()];
		int i = 0;
		
		//Below relies on same iteration order. Since linked list, is good.
		//Be careful not to change it...
		for (ForgeRecipe recipe : forgeRecipes) {
			try {
			perf[i] = recipe.matches(inputMap);
			} catch (IllegalArgumentException e) {
				Armory.logger.warn("Unable to parse forge recipe:");
				Armory.logger.warn(e.getMessage());
				perf[i] = -1;
			}
			i++;
		}
		
		int bestIndex = -1; //keep track of best match
		float best = -1f; //keep track of best performance
		for (i = 0; i < forgeRecipes.size(); i++) {
			if (perf[i] > best) {
				best = perf[i];
				bestIndex = i;
			}
		}
		
		if (bestIndex == -1 || best < 0) {
			//no real matches. everything sucked
			return null;
		}
		
		return forgeRecipes.get(bestIndex).produce(baseMetal, perf[bestIndex]);
	}
	
	public Map<Item, FuelRecord> getFuelRecords() {
		return fuels;
	}
	
	public Map<Fluid, CoolantRecord> getCoolantRecords() {
		return coolants;
	}
	
	public List<MetalRecord> getMetalRecords() {
		return metals;
	}
	
	public List<AlloyRecipe> getAlloyRecipes() {
		return recipes;
	}
	
	public List<ForgeRecipe> getForgeRecipes() {
		return forgeRecipes;
	}
}
