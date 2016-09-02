package com.SkyIsland.Armory.items.tools;

import com.SkyIsland.Armory.Armory;
import com.SkyIsland.Armory.forge.Brazier;
import com.SkyIsland.Armory.items.ItemBase;
import com.google.common.collect.Multimap;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class Tongs extends ItemBase {

	private static final double DAMAGE = 1.0f;
	
	private static final String NBT_HELD = "held";
	
	private String registryName;
	
	public Tongs(String unlocalizedName) {
		super();
		
		registryName = unlocalizedName;
		
		this.setMaxStackSize(1);
		this.setUnlocalizedName(unlocalizedName);
		this.setCreativeTab(Armory.creativeTab);
	}
	
	@Override
	public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
		super.onCreated(stack, worldIn, playerIn);
		
		//create nbt compound for itemstack
		if (!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
	}
	
	/**
	 * Fetches the item stack stored in the provided pair of tongs.
	 * If the item stack is not representative of a pair of tongs, or if
	 * there is not an item currently being held, will return null;
	 * @param stack
	 * @return
	 */
	public ItemStack getHeldItem(ItemStack stack) {
		if (stack == null || !(stack.getItem() instanceof Tongs))
			return null;
		
		if (!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		NBTTagCompound nbt = stack.getTagCompound();
		
		if (nbt.hasKey(NBT_HELD, NBT.TAG_COMPOUND))
			return ItemStack.loadItemStackFromNBT(nbt.getCompoundTag(NBT_HELD));
		
		return null;
	}
	
	/**
	 * Sets the provided stack to the given pair of tongs. Will overwrite
	 * any items still in the tongs.
	 * If stack is set to null, will free the tongs from an item.
	 * @param tongs
	 * @param stack
	 */
	protected void setHeldItem(ItemStack tongs, ItemStack stack) {
		if (tongs == null || !(tongs.getItem() instanceof Tongs))
			return;
		
		if (!tongs.hasTagCompound())
			tongs.setTagCompound(new NBTTagCompound());
		NBTTagCompound nbt = tongs.getTagCompound(),
				subtag = new NBTTagCompound();
		
		if (stack == null)
			nbt.removeTag(NBT_HELD);
		else
			nbt.setTag(NBT_HELD, stack.writeToNBT(subtag));
		
	}

	@Override
	public boolean isDamageable() {
		return false;
	}
	
	@Override
	public void init() {
		GameRegistry.addShapedRecipe(new ItemStack(this), new Object[]{" I ", "ILI", "II ", 'I', Items.iron_ingot, 'L', Items.leather});
	}
	
	public void clientInit() {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
    	.register(this, 0, new ModelResourceLocation(Armory.MODID + ":" + this.registryName, "inventory"));
	}
	
	@Override
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (stack == null || !(stack.getItem() instanceof Tongs))
			return false;
		
		IBlockState state = worldIn.getBlockState(pos);
		Block block = state.getBlock();
		
		if (block instanceof Brazier)
			return onBrazier(playerIn, stack, state);
		if (block instanceof BlockAnvil)
			return onAnvil(playerIn, stack, state);
		if (block instanceof BlockCauldron)
			return onCauldron(playerIn, stack, state);
//		if (block instanceof Trough)
//			return onCauldron(playerIn, stack, state);
//		if (block instanceof CuttingMachine)
//			return onCauldron(playerIn, stack, state);
//		if (block instanceof ConstructPedestal)
//			return onCauldron(playerIn, stack, state);
		
		return false;
	}
    
    private boolean onBrazier(EntityPlayer player, ItemStack tongs, IBlockState brazierBlock) {
    	System.out.println("Unimplemented method: Tongs#onBrazier()!!!!!");
    	return false;
    }
    
    private boolean onAnvil(EntityPlayer player, ItemStack tongs, IBlockState anvilBlock) {
    	System.out.println("Unimplemented method: Tongs#onAnvil()!!!!!");
    	return false;
    }
    
    private boolean onCauldron(EntityPlayer player, ItemStack tongs, IBlockState cauldronBlock) {
    	System.out.println("Unimplemented method: Tongs#onCauldron()!!!!!");
    	return false;
    }
    
    private boolean onTrough(EntityPlayer player, ItemStack tongs, IBlockState troughBlock) {
    	System.out.println("Unimplemented method: Tongs#onTrough()!!!!!");
    	return false;
    }
    
    private boolean onCutting(EntityPlayer player, ItemStack tongs, IBlockState cuttingBlock) {
    	System.out.println("Unimplemented method: Tongs#onCutting()!!!!!");
    	return false;
    }
    
    private boolean onPedestal(EntityPlayer player, ItemStack tongs, IBlockState pedestalBlock) {
    	System.out.println("Unimplemented method: Tongs#onPedestal()!!!!!");
    	return false;
    }
    
    public Multimap<String, AttributeModifier> getItemAttributeModifiers() {
        @SuppressWarnings("deprecation")
		Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers();
        multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(itemModifierUUID, "Tool modifier", DAMAGE, 0));
        return multimap;
    }
	
}
