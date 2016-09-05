package com.SkyIsland.Armory.items.tools;

import java.util.Map;

import com.SkyIsland.Armory.Armory;
import com.SkyIsland.Armory.api.WeaponManager;
import com.SkyIsland.Armory.forge.Brazier;
import com.SkyIsland.Armory.forge.Forge;
import com.SkyIsland.Armory.forge.Forge.ForgeTileEntity;
import com.SkyIsland.Armory.items.HeldMetal;
import com.SkyIsland.Armory.items.ItemBase;
import com.SkyIsland.Armory.mechanics.DamageType;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class Tongs extends ItemBase {

	private static final float DAMAGE = 1.0f;
	
	private static final String NBT_HELD = "held";
	
	private String registryName;
	
	public Tongs(String unlocalizedName) {
		super();
		
		registryName = unlocalizedName;
		
		this.setMaxStackSize(1);
		this.setUnlocalizedName(unlocalizedName);
		this.setCreativeTab(Armory.creativeTab);
		
		Map<DamageType, Float> map = DamageType.freshMap();
		map.put(DamageType.PIERCE, DAMAGE);
		WeaponManager.instance().registerWeapon(this, 
				map
				);

		
		MinecraftForge.EVENT_BUS.register(this);
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
		
		if (stack == null) {
			nbt.removeTag(NBT_HELD);
			tongs.setItemDamage(0);
		} else {
			nbt.setTag(NBT_HELD, stack.writeToNBT(subtag));
			tongs.setItemDamage(1);
		}
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
		ModelBakery.registerItemVariants(this, new ModelResourceLocation(Armory.MODID + ":" + this.registryName, "inventory"),
				new ModelResourceLocation(Armory.MODID + ":" + this.registryName + "_full" , "inventory"));
		
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
    	.register(this, 0, new ModelResourceLocation(Armory.MODID + ":" + this.registryName, "inventory"));
		
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
    	.register(this, 1, new ModelResourceLocation(Armory.MODID + ":" + this.registryName + "_full" , "inventory"));
	}
	
//	@Override
//    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
//		if (stack == null || !(stack.getItem() instanceof Tongs))
//			return false;
//		
//		IBlockState state = worldIn.getBlockState(pos);
//		Block block = state.getBlock();
//		
//		if (block instanceof Brazier)
//			return onBrazier(playerIn, stack, state);
//		if (block instanceof BlockAnvil)
//			return onAnvil(playerIn, stack, state);
//		if (block instanceof BlockCauldron)
//			return onCauldron(playerIn, stack, state);
////		if (block instanceof Trough)
////			return onCauldron(playerIn, stack, state);
////		if (block instanceof CuttingMachine)
////			return onCauldron(playerIn, stack, state);
////		if (block instanceof ConstructPedestal)
////			return onCauldron(playerIn, stack, state);
//		
//		return false;
//	}
	
	@SubscribeEvent
	public void onBlockClick(PlayerInteractEvent event) {
		if (event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)
			return;
		
		if (event.entityPlayer.getHeldItem() == null
				|| !(event.entityPlayer.getHeldItem().getItem() instanceof Tongs)) {
			return;
		}
		
		IBlockState state = event.world.getBlockState(event.pos);
		Block block = state.getBlock();
		EntityPlayer playerIn = event.entityPlayer;
		ItemStack stack = event.entityPlayer.getHeldItem();
		
		if (block instanceof Brazier)
			onBrazier(playerIn, stack, state, event.pos);
		else if (block instanceof Forge)
			onForge(playerIn, stack, state, event.pos);
		else if (block instanceof BlockAnvil)
			onAnvil(playerIn, stack, state);
		else if (block instanceof BlockCauldron)
			onCauldron(playerIn, stack, state);
//		else if (block instanceof Trough)
//			return onCauldron(playerIn, stack, state);
//		else if (block instanceof CuttingMachine)
//			return onCauldron(playerIn, stack, state);
//		else if (block instanceof ConstructPedestal)
//			return onCauldron(playerIn, stack, state);
	}
    
    private boolean onBrazier(EntityPlayer player, ItemStack tongs, IBlockState brazierBlock, BlockPos pos) {
    	System.out.println("Unimplemented method: Tongs#onBrazier()!!!!!");
    	return false;
    }
    
    private boolean onForge(EntityPlayer player, ItemStack tongs, IBlockState forgeBlock, BlockPos pos) {
    	if (getHeldItem(tongs) != null) {
    		//already has something in tongs, so do nothing
    		return false;
    	}
    	
    	//nothing in tongs. Try to get something out of the furnace
    	TileEntity te = player.getEntityWorld().getTileEntity(pos);
    	if (te == null || !(te instanceof ForgeTileEntity)) {
    		return false;
    	}
    	
    	ForgeTileEntity ent = (ForgeTileEntity) te;
    	ItemStack item = ent.gatherMetals();
    	
    	if (item == null) {
    		return false;
    	}
    	
    	//got return item. What is it??
		setHeldItem(tongs, item);
    	
    	
    	return true;
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
    
    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    	
    	ItemStack held = getHeldItem(stack);
    	if (held != null) {
    		held.getItem().onUpdate(held, worldIn, entityIn, itemSlot, isSelected);
    		
    		if (!(held.getItem() instanceof HeldMetal)) {
    			
    			if (entityIn instanceof EntityPlayer) {
    				((EntityPlayer) entityIn).inventory.addItemStackToInventory(held);
    			} else {
    				worldIn.spawnEntityInWorld(new EntityItem(worldIn, entityIn.posX, entityIn.posY, entityIn.posZ, held));
    			}
    			
    			stack.setItemDamage(0);
    			held = null;
    		}
    		setHeldItem(stack, held);
    	}
    }
    
//    public Multimap<String, AttributeModifier> getItemAttributeModifiers() {
//        @SuppressWarnings("deprecation")
//		Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers();
//        multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(itemModifierUUID, "Tool modifier", DAMAGE, 0));
//        return multimap;
//    }
	
}
