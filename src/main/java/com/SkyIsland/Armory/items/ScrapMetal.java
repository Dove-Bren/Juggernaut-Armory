package com.SkyIsland.Armory.items;

import java.util.List;

import com.SkyIsland.Armory.Armory;
import com.SkyIsland.Armory.chat.ChatFormat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Piece of junk metal formed by failing to shape metal correctly or by letting
 * it cool before it was finished.
 * Scrap can be melted down (in Forge construct) to get back some of the input ingredients back. The
 * exact ingredient is set at construction, but defaults to iron.
 * @author Skyler
 *
 */
public class ScrapMetal extends ItemBase {

	private static final String NBT_METAL = "metal";
	
	private String registryName;
	
	public ScrapMetal(String unlocalizedName) {
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
	
	@Override
	public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
		setReturn(stack, new ItemStack(Items.iron_ingot));
	}
	
	public void clientInit() {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
    	.register(this, 0, new ModelResourceLocation(Armory.MODID + ":" + this.registryName, "inventory"));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
		if (stack == null || !(stack.getItem() instanceof ScrapMetal))
			return;
		
		ItemStack ret = getReturn(stack);
		if (ret == null)
			return;
		
		tooltip.add(ChatFormat.COMPONENTS.wrap(
				"Contains: " + ret.getDisplayName()
				));
    }
	
	///////////////NBT//////////////
	
	/**
	 * Fetches the metal that should be returned when smelted
	 * @param metal
	 * @return The ingot to produce, or null if there's an issue
	 */
	public ItemStack getReturn(ItemStack metal) {
		if (metal == null || !(metal.getItem() instanceof ScrapMetal))
			return null;
		
		if (!metal.hasTagCompound())
			metal.setTagCompound(new NBTTagCompound());
		NBTTagCompound nbt = metal.getTagCompound();
		
		if (nbt.hasKey(NBT_METAL, NBT.TAG_COMPOUND))
			return ItemStack.loadItemStackFromNBT(nbt.getCompoundTag(NBT_METAL));
		
		return null;
	}
	
	public void setReturn(ItemStack metal, ItemStack returnMetal) {
		if (metal == null || !(metal.getItem() instanceof ScrapMetal))
			return;
		if (returnMetal == null)
			return;
		
		if (!metal.hasTagCompound())
			metal.setTagCompound(new NBTTagCompound());
		NBTTagCompound nbt = metal.getTagCompound(),
				subtag = new NBTTagCompound();;
		returnMetal.writeToNBT(subtag);
				
		nbt.setTag(NBT_METAL, subtag);
	}
	
	public ItemStack wrap(ItemStack returnMetal) {
		ItemStack scrap = new ItemStack(this);
		setReturn(scrap, returnMetal);
		
		return scrap;
	}
	
	public static ItemStack produceScrap(ItemStack returnMetal) {
		ScrapMetal base = (ScrapMetal) MiscItems.getItem(MiscItems.Items.SCRAP);
		return base.wrap(returnMetal);
	}
	
}
