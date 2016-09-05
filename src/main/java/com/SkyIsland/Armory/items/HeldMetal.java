package com.SkyIsland.Armory.items;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.SkyIsland.Armory.Armory;
import com.SkyIsland.Armory.config.ModConfig;
import com.SkyIsland.Armory.items.MiscItems.Items;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
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
	
	private static final String NBT_METALS = "metals";
	
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
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
		float heat = getHeat(stack);
		
		//cool item down
		setHeat(stack, (heat == -1 ? 0 : heat - 1));
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
		
		if (nbt.hasKey(NBT_HEAT, NBT.TAG_INT))
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
	
	public Collection<ItemStack> getMetals(ItemStack metal) {
		if (metal == null)
			return null;
		
		if (!metal.hasTagCompound())
			metal.setTagCompound(new NBTTagCompound());
		NBTTagCompound nbt = metal.getTagCompound();
		
		if (nbt.hasKey(NBT_METALS, NBT.TAG_LIST)) {
			List<ItemStack> metals = new LinkedList<ItemStack>();
			
			NBTTagList list = nbt.getTagList(NBT_METALS, NBT.TAG_COMPOUND);
			NBTTagCompound sub;
			while (!list.hasNoTags()) {
				sub = (NBTTagCompound) list.removeTag(0);
				metals.add(ItemStack.loadItemStackFromNBT(sub));
			}
			
			return metals;
		}
			
		return null;
	}
	
	public void setMetals(ItemStack metal, Collection<ItemStack> metals) {
		if (metal == null || !(metal.getItem() instanceof HeldMetal))
			return;
		
		if (!metal.hasTagCompound())
			metal.setTagCompound(new NBTTagCompound());
		NBTTagCompound nbt = metal.getTagCompound();
		
		NBTTagList list = new NBTTagList();
		NBTTagCompound sub;
		for (ItemStack addedMetal : metals) {
			sub = new NBTTagCompound();
			addedMetal.writeToNBT(sub);
			
			list.appendTag(sub);
		}
		
		nbt.setTag(NBT_METALS, list);
		
		//updateHeat(metal);
	}
	
	public ItemStack createStack(Collection<ItemStack> containedMetals, float heat) {
		ItemStack stack = new ItemStack(this);
		setHeat(stack, heat);
		setMetals(stack, containedMetals);
		
		return stack;
	}
	
	protected void updateHeat(Entity owner, ItemStack metal) {
		if (getHeat(metal) < ModConfig.config.getMinimumHeat()) {
			metal.setItem(MiscItems.getItem(Items.SCRAP));
			((ScrapMetal) metal.getItem()).setReturn(metal, 
					getRandomMetal(metal)
					);
			owner.playSound(Armory.MODID + ":item.metal.cool", 1.0f, 1.0f);
		}
	}
	
	protected ItemStack getRandomMetal(ItemStack metal) {
		Collection<ItemStack> metals = getMetals(metal);
		if (metals == null || metals.isEmpty())
			return null;
		
		Iterator<ItemStack> it = metals.iterator();
		int index = Armory.random.nextInt(metals.size());
		int i = 0;
		while (i < index) {
			it.next();
			i++;
		}
		
		return it.next();
	}
	
}
