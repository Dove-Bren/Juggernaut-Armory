package com.SkyIsland.Armory.items;

import com.SkyIsland.Armory.Armory;
import com.SkyIsland.Armory.api.ForgeManager;
import com.SkyIsland.Armory.api.ForgeManager.MetalRecord;
import com.SkyIsland.Armory.config.ModConfig;
import com.SkyIsland.Armory.items.MiscItems.Items;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;

/**
 * Metal held in tongs. Should not exist as an itemstack outside of in the
 * context of a pair of tongs.
 * @author Skyler
 *
 */
public class HeldMetal extends ItemBase {

	private static final String NBT_HEAT = "heat";
	
	private static final String NBT_METAL = "metal";
	
	private static final String NBT_MAP_PREFIX = "metalmap";
	
	private static final String NBT_LEFTOVER = "leftover";
	
	private String registryName;
	
	public HeldMetal(String unlocalizedName) {
		super();
		
		registryName = unlocalizedName;
		
		this.setMaxStackSize(1);
		this.setUnlocalizedName(unlocalizedName);
		
	}

	@Override
	public boolean isDamageable() {
		return false;
	}
	
	@Override
	public void init() {
		;
	}
	
	public void clientInit() {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
    	.register(this, 0, new ModelResourceLocation(Armory.MODID + ":" + this.registryName, "inventory"));
	}
	
	@Override
	public int getColorFromItemStack(ItemStack stack, int tintIndex) {
		
		return 0;
	}
	
//	@Override
	public void onHeatUpdate(ItemStack stack, int heatDelta, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		//super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
		float heat = getHeat(stack);
		
		//cool item down
		setHeat(stack, (heat == -1 ? 0 : heat - heatDelta));
		updateHeat(entityIn, stack);
	}
	
	///////////////NBT//////////////
	
	/**
	 * Fetches the heat for the given piece of metal.
	 * @param metal
	 * @return the heat, or -1 if the itemstack is invalid or there is no set heat
	 */
	public float getHeat(ItemStack metal) {
		if (metal == null || !(metal.getItem() instanceof HeldMetal))
			return -1;
		
		if (!metal.hasTagCompound())
			metal.setTagCompound(new NBTTagCompound());
		NBTTagCompound nbt = metal.getTagCompound();
		
		if (nbt.hasKey(NBT_HEAT, NBT.TAG_FLOAT))
			return nbt.getFloat(NBT_HEAT);
		
		return -1;
	}
	
	/**
	 * Sets the heat in the given piece of held metal.
	 * This method doesn't call an update, so metal will cool (if heat is
	 * low enough) on next tick
	 * @param metal
	 * @param heat
	 */
	public void setHeat(ItemStack metal, float heat) {
		if (metal == null || !(metal.getItem() instanceof HeldMetal))
			return;
		
		if (!metal.hasTagCompound())
			metal.setTagCompound(new NBTTagCompound());
		NBTTagCompound nbt = metal.getTagCompound();
		
		nbt.setFloat(NBT_HEAT, heat);
		
		//updateHeat(metal);
	}
	
	public ItemStack getMetal(ItemStack metal) {
		if (metal == null)
			return null;
		
		if (!metal.hasTagCompound())
			metal.setTagCompound(new NBTTagCompound());
		NBTTagCompound nbt = metal.getTagCompound();
		
		if (nbt.hasKey(NBT_METAL, NBT.TAG_COMPOUND)) {
//			List<ItemStack> metals = new LinkedList<ItemStack>();
//			
//			NBTTagList list = nbt.getTagList(NBT_METALS, NBT.TAG_COMPOUND);
//			NBTTagCompound sub;
//			while (!list.hasNoTags()) {
//				sub = (NBTTagCompound) list.removeTag(0);
//				metals.add(ItemStack.loadItemStackFromNBT(sub));
//			}
//			
//			return metals;
			return ItemStack.loadItemStackFromNBT(nbt.getCompoundTag(NBT_METAL));
		}
			
		return null;
	}
	
	public void setMetal(ItemStack holdingmetal, ItemStack metal) {
		if (holdingmetal == null || !(holdingmetal.getItem() instanceof HeldMetal)) {
			return;
		}
		
		if (!holdingmetal.hasTagCompound())
			holdingmetal.setTagCompound(new NBTTagCompound());
		NBTTagCompound nbt = holdingmetal.getTagCompound();
		
//		NBTTagList list = new NBTTagList();
//		NBTTagCompound sub;
//		for (ItemStack addedMetal : metals) {
//			sub = new NBTTagCompound();
//			addedMetal.writeToNBT(sub);
//			
//			list.appendTag(sub);
//		}
		
		NBTTagCompound tag = new NBTTagCompound();
		metal.writeToNBT(tag);
		nbt.setTag(NBT_METAL, tag);
		
		//updateHeat(metal);
	}
	
	/**
	 * Sets the map of metal location. This map signifies the squares which have
	 * metal on them. If the map is not in the right proportions (10x10), it will
	 * be expanded or chopped down to fit.
	 * @param stack
	 * @param metalMap
	 */
	public void setMetalMap(ItemStack stack, boolean[][] metalMap) {
		if (stack == null || !(stack.getItem() instanceof HeldMetal))
			return;
		
		if (!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		
		int[] ints = map2Int(metalMap);

		
//		System.out.println("setting map--------------------");
//		printArray(ints);
//		System.out.println("-----");
//		printArray(metalMap);
		
		NBTTagCompound nbt = stack.getTagCompound();
		
		for (int i = 0; i < 4; i++) {
			nbt.setInteger(NBT_MAP_PREFIX + i, ints[i]);
		}
	}
	
	/**
	 * Returns the metal location map. This is guaranteed to be a 10x10
	 * @param stack
	 * @return a 2d boolean array representing where the metal lies. [y][x]
	 */
	public boolean[][] getMetalMap(ItemStack stack) {
		if (stack == null || !(stack.getItem() instanceof HeldMetal))
			return null;
		
		if (!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		
		int[] ints = new int[]{0,0,0,0};
		NBTTagCompound nbt = stack.getTagCompound();
		
		for (int i = 0; i < 4; i++) {
			if (nbt.hasKey(NBT_MAP_PREFIX + i, NBT.TAG_INT))
				ints[i] = nbt.getInteger(NBT_MAP_PREFIX + i);
		}
		
		boolean[][] map = int2Map(ints[0], ints[1], ints[2], ints[3]);
//		System.out.println("fetch mapxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
//		printArray(ints);
//		System.out.println("-----");
//		printArray(map);
		return map;
			
	}

	public int getSpreadableMetal(ItemStack stack) {
		if (stack == null || !(stack.getItem() instanceof HeldMetal))
			return 0;
		
		if (!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		
		NBTTagCompound nbt = stack.getTagCompound();
		
		if (nbt.hasKey(NBT_LEFTOVER, NBT.TAG_INT))
			return nbt.getInteger(NBT_LEFTOVER);
		
		return 0;
	}
	
	public void setSpreadableMetal(ItemStack stack, int spreadableMetal) {
		if (stack == null || !(stack.getItem() instanceof HeldMetal))
			return;
		
		if (!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		
		NBTTagCompound nbt = stack.getTagCompound();
		
		nbt.setInteger(NBT_LEFTOVER, spreadableMetal);
	}
	
	public static boolean[][] int2Map(int int1, int int2, int int3, int int4) {
		int index = 0;
		int eval;
		boolean[][] map = new boolean[10][10];
		for (; index < 100; index++) {
			if (index < 32)
				eval = int1;
			else if (index < 64)
				eval = int2;
			else if (index < 96)
				eval = int3;
			else
				eval = int4;
			
			map[index / 10][index % 10] = ((eval & (1 << (index % 32))) != 0);
		}
		
		return map;
	}
	
	/**
	 * Assumes 10x10!
	 * @param map
	 * @return 4 ints, with [0] being first (bits 0-31), etc
	 */
	public static int[] map2Int(boolean[][] map) {
		int intcount = 0;
		int index = 0;
		int[] ret = new int[]{0,0,0,0};
		for (; index < 100; index++) {
			intcount = (index / 32); //what int to put into
			ret[intcount] = (ret[intcount] << 1) | (map[index/10][index % 10] ? 1 : 0);
		}
//		System.out.println("backwards ints:");
//		printArray(ret);
		
		//reflect bits since we shifted same direction
		for (int i = 0; i < 4; i++) {
			int val = 0;
			for (int j = 0; j < 32; j++) {
				val |= (ret[i] >> j) & 1;
				if (j != 31)
					val <<= 1;
			}
			ret[i] = val;
		}
		
		return ret;
	}
	
	public ItemStack createStack(ItemStack containedMetal, float heat, int maxSpread) {
		ItemStack stack = new ItemStack(this);
		setHeat(stack, heat);
		setMetal(stack, containedMetal);
		setSpreadableMetal(stack, maxSpread);
		boolean[][] map = new boolean[10][10];
		map[Armory.random.nextBoolean() ? 4 : 5][Armory.random.nextBoolean() ? 4 : 5] = true;
		setMetalMap(stack, map);
		
		return stack;
	}
	
	/**
	 * Final step in moving held metal to an active useful part. This method takes
	 * the held metal and turns it into an item of the right type, as defined
	 * by the shape the metal is in and it's cooling method.
	 * This method does not check that the metal is such that it should be cast.
	 * @return
	 */
	public ItemStack cast(ItemStack containerMetal) {
		ItemStack held = getMetal(containerMetal);
		if (held == null)
			return null;
		MetalRecord record = ForgeManager.instance().getMetalRecord(held);
		if (record == null)
			return null;
		
		return ForgeManager.instance().getForgeResult(record, getMetalMap(containerMetal));
	}
	
	/**
	 * Checks the heat of the held metal and updates accordingly.
	 * Does not check if an item is being held or not. That's p to you.
	 * @param owner
	 * @param metal
	 * @return true if the metal <strong>just cooled</strong>
	 */
	protected void updateHeat(Entity owner, ItemStack metal) {
		if (getHeat(metal) < ModConfig.config.getMinimumHeat()) {
			ItemStack ret = getMetal(metal);
			ret.stackSize = 1;
			metal.setItem(MiscItems.getItem(Items.SCRAP));
			((ScrapMetal) metal.getItem()).setReturn(metal, 
					ret
					);
			owner.playSound(Armory.MODID + ":item.metal.cool", 1.0f, 1.0f);
			return;
		}
		
		return;
	}
	
//	private static final void printArray(boolean[][] map) {
//		String out;
//		for (boolean[] row : map) {
//			out = "[ ";
//			
//			for (boolean b : row) {
//				out += (b + " ");
//			}
//			
//			System.out.println(out + "]");
//		}
//	}
//	
//	private static final void printArray(int[] ints) {
//		String out = "[ ";
//		for (int i : ints)
//			out += (i + " ");
//		System.out.println(out + "]");
//	}
}
