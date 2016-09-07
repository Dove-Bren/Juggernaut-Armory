package com.SkyIsland.Armory.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

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
		
		private int meta;
		
		public MetalRecord(Item metal, int burnTime, float requiredHeat) {
			this(metal, burnTime, requiredHeat, -1);
		}
		
		public MetalRecord(Item metal, int burnTime, float requiredHeat, int requiredMeta) {
			this.metal = metal;
			this.burnTime = burnTime;
			this.requiredHeat = requiredHeat;
			this.meta = requiredMeta;
		}
		
		protected boolean accepts(ItemStack stack) {
			return (stack != null && stack.getItem() == metal
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
		instance.registerInputMetal(new MetalRecord(Items.iron_ingot, 200, 1500));
		instance.registerInputMetal(new MetalRecord(Items.gold_ingot, 160, 900));
		instance.registerInputMetal(new MetalRecord(Items.coal, 400, 1800));
		
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
	
	private ForgeManager() {
		fuels = new HashMap<Item, FuelRecord>();
		coolants = new HashMap<Fluid, CoolantRecord>();
		metals = new LinkedList<MetalRecord>();
		recipes = new LinkedList<AlloyRecipe>();
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
}
