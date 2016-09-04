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

public class ForgeManager {

	public static class FuelRecord {
		
		private int burnTicks;
		
		private int maxHeat;
		
		private int heatRate;
		
		public FuelRecord(int burnTicks, int maxHeat, int heatRate) {
			this.burnTicks = burnTicks;
			this.maxHeat = maxHeat;
			this.heatRate = heatRate;
		}

		public int getBurnTicks() {
			return burnTicks;
		}

		public int getMaxHeat() {
			return maxHeat;
		}

		public int getHeatRate() {
			return heatRate;
		}
	}
	
	public static class MetalRecord {
		
		protected int burnTime;
		
		protected int requiredHeat;
		
		private Item metal;
		
		private int meta;
		
		public MetalRecord(Item metal, int burnTime, int requiredHeat) {
			this(metal, burnTime, requiredHeat, -1);
		}
		
		public MetalRecord(Item metal, int burnTime, int requiredHeat, int requiredMeta) {
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

		public int getRequiredHeat() {
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
			ItemStack ret = new ItemStack(result);
			ret.stackSize = resultAmount;
			return ret;
		}
		
		protected boolean matches(Collection<ItemStack> ingredients) {
			if (ingredients == null || ingredients.isEmpty())
				return false;
			
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
				return false; //size of items in doesn't match; invalid match
			
			for (Item key : reqMap.keySet()) {
				if (!inputMap.containsKey(key))
					return false;
			}
			
			/**
			 * TODO
			 * check for multiples
			 */
			
			//since size was the same and we didn't find an item that didn't match
			//we accept
			
			return true;
		}
	}
	
	private static ForgeManager instance;
	
	public static void init() {
		instance = new ForgeManager();
		setupVanilla();
	}
	
	private static void setupVanilla() {
		instance.registerFuel(Items.coal, new FuelRecord(1600, 1700, 1));
		instance.registerFuel(Item.getItemFromBlock(Blocks.coal_block), new FuelRecord(16000, 1700, 1));
		instance.registerFuel(Items.lava_bucket, new FuelRecord(20000, 3000, 4));
		
		
		/**
		 * if (item instanceof ItemBlock && Block.getBlockFromItem(item) != Blocks.air)
            {
                Block block = Block.getBlockFromItem(item);

                if (block == Blocks.wooden_slab)
                {
                    return 150;
                }

                if (block.getMaterial() == Material.wood)
                {
                    return 300;
                }

                if (block == Blocks.coal_block)
                {
                    return 16000;
                }
            }

            if (item instanceof ItemTool && ((ItemTool)item).getToolMaterialName().equals("WOOD")) return 200;
            if (item instanceof ItemSword && ((ItemSword)item).getToolMaterialName().equals("WOOD")) return 200;
            if (item instanceof ItemHoe && ((ItemHoe)item).getMaterialName().equals("WOOD")) return 200;
            if (item == Items.stick) return 100;
            if (item == Items.coal) return 1600;
            if (item == Items.lava_bucket) return 20000;
            if (item == Item.getItemFromBlock(Blocks.sapling)) return 100;
            if (item == Items.blaze_rod) return 2400;
		 */
	}
	
	public static ForgeManager instance() {
		return instance;
	}
	
	private Map<Item, FuelRecord> fuels;
	
	private List<MetalRecord> metals;
	
	private List<AlloyRecipe> recipes;
	
	private ForgeManager() {
		fuels = new HashMap<Item, FuelRecord>();
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
		
		for (AlloyRecipe recipe : recipes) {
			if (recipe.matches(inputMetals))
				return recipe.getResult();
		}
		
		return null;
	}
}
