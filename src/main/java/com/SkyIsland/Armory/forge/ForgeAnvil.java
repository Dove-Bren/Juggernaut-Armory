package com.SkyIsland.Armory.forge;

import com.SkyIsland.Armory.Armory;
import com.SkyIsland.Armory.blocks.BlockBase;
import com.SkyIsland.Armory.config.ModConfig;
import com.SkyIsland.Armory.gui.table.TableGui;
import com.SkyIsland.Armory.items.HeldMetal;
import com.SkyIsland.Armory.items.MiscItems;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ForgeAnvil extends BlockBase implements ITileEntityProvider {
	
	public static Block block;
	public static Material material;
	public static final String unlocalizedName = "forge_anvil";
	
	public void clientInit() {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
		.register(Item.getItemFromBlock(this), 0, new ModelResourceLocation(Armory.MODID + ":" + unlocalizedName, "inventory"));
	}
	
	public static void preInit() {
		GameRegistry.registerTileEntity(AnvilTileEntity.class, Armory.MODID + "_" + unlocalizedName);
	}
	
	public void init() {
		GameRegistry.addShapedRecipe(new ItemStack(block),
				"BBB", "sis", "iii", 'B', Blocks.iron_block, 's', Blocks.iron_bars, 'i', Items.iron_ingot);
		GameRegistry.addShapelessRecipe(new ItemStack(block),
				Items.iron_ingot, Blocks.iron_bars, Blocks.iron_bars);
	}
	
	public ForgeAnvil() {
		super(Material.ground);
		this.blockHardness = 200;
		this.blockResistance = 45;
		this.setStepSound(Block.soundTypeStone);
		this.setUnlocalizedName(Armory.MODID + "_" + unlocalizedName);
        this.setCreativeTab(Armory.creativeTab);
        block = this;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new AnvilTileEntity();
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state,
			EntityPlayer playerIn, EnumFacing side, float hitX,float hitY, float hitZ) {
		
		TileEntity ent = worldIn.getTileEntity(pos);
		if (ent == null || !(ent instanceof AnvilTileEntity))
			return false;
		
		TableGui.displayGui(playerIn, (AnvilTileEntity) ent);
		
		return true;
	}
	
	public static ItemStack getHeld(World worldIn, BlockPos pos, IBlockState state, boolean take) {
		TileEntity e = worldIn.getTileEntity(pos);
		if (e == null || !(e instanceof AnvilTileEntity))
			return null;
		
		AnvilTileEntity te = (AnvilTileEntity) e;
		return te.getItem(take);
	}
	
	public static class AnvilTileEntity extends TileEntity implements ITickable {
		
		protected ItemStack held;
		
		protected float heat;
		
		public AnvilTileEntity() {
			this.held = null;
		}
		
		
		@Override
		public void writeToNBT(NBTTagCompound tag) {
			if (held != null) {
				NBTTagCompound itemTag = new NBTTagCompound();
				held.writeToNBT(itemTag);
				tag.setTag("held", itemTag);
			}
			
			tag.setFloat("heat", heat);

			super.writeToNBT(tag);
		}
		
		@Override
		public void readFromNBT(NBTTagCompound tag) {
			
			if (tag.hasKey("held", NBT.TAG_COMPOUND)) {
				held = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("held"));
			} else
				this.held = null;
			
			if (tag.hasKey("heat", NBT.TAG_FLOAT))
				this.heat = tag.getFloat("heat");
			else
				this.heat = 0.0f;
			
			super.readFromNBT(tag);
		}
		
		@Override
		public Packet<INetHandlerPlayClient> getDescriptionPacket() {
			NBTTagCompound tagCompound = new NBTTagCompound();
		    writeToNBT(tagCompound);
		    //return new Packet132TileEntityData(xCoord, yCoord, zCoord, 1, tagCompound);
		    return new S35PacketUpdateTileEntity(this.pos, 4, tagCompound);
		}
		
		@Override
		public void onDataPacket(net.minecraft.network.NetworkManager net, net.minecraft.network.play.server.S35PacketUpdateTileEntity pkt)
	    {
			NBTTagCompound tag = pkt.getNbtCompound();
			readFromNBT(tag);
	    }
		
		@Override
		public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
		{
		    return (oldState.getBlock() != newState.getBlock());
		}
		
		public ItemStack getItem(boolean take) {
			HeldMetal inst = (HeldMetal) MiscItems.getItem(MiscItems.Items.HELD_METAL);
			inst.setHeat(held, heat);
			ItemStack ret = held;
			if (take) {
				heat = 0;
				held = null;
			}
			return ret;
		}
		
		public void setItem(ItemStack item, boolean updateHeat) {
			this.held = item;
			if (updateHeat) {
				if (item == null || !(item.getItem() instanceof HeldMetal))
					return;
				
				HeldMetal inst = (HeldMetal) MiscItems.getItem(MiscItems.Items.HELD_METAL);
				this.heat = inst.getHeat(item);
			}
		}
		
		public float getHeat() {
			return heat;
		}
		
		public void setHeat(float heat) {
			this.heat = heat;
		}
		
		@Override
		public void update() {
			if (held == null || !(held.getItem() instanceof HeldMetal))
				return;
			
			if (heat > ModConfig.config.getMinimumHeat()) {
				heat = Math.max(0f, heat - ModConfig.config.getHeatLoss());
				
				if (heat <= ModConfig.config.getMinimumHeat()) {
					//just cooled off!
					worldObj.playSound(pos.getX(), pos.getY(), pos.getZ(), Armory.MODID + ":item.metal.cool", 1.0f, 1.0f, false);
					heat = 0.0f;
					
					HeldMetal inst = (HeldMetal) MiscItems.getItem(MiscItems.Items.HELD_METAL);
					held = inst.getScrap(held);
				}
			}
		}
		
	}
}
