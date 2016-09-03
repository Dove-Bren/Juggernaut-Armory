package com.SkyIsland.Armory.forge;

import java.util.Random;

import com.SkyIsland.Armory.Armory;
import com.SkyIsland.Armory.blocks.BlockBase;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.item.EntityItem;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Brazier extends BlockBase implements ITileEntityProvider {

	public static Material material;
	public static final String unlocalizedName = "brazier_block";
	
	private static Brazier onBlock;
	
	private static Brazier offBlock;
	
	protected static final PropertyBool STANDALONE = PropertyBool.create("standalone");
	
	public void clientInit() {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
		.register(Item.getItemFromBlock(this), 0, new ModelResourceLocation(Armory.MODID + ":" + unlocalizedName, "standalone=true"));
		
		ModelBakery.registerItemVariants(Item.getItemFromBlock(this), 
				new ModelResourceLocation(Armory.MODID + ":" + unlocalizedName, "standalone=true"),
				new ModelResourceLocation(Armory.MODID + ":" + unlocalizedName, "standalone=false"));

//		Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
//		.register(Item.getItemFromBlock(this), 0, new ModelResourceLocation(Armory.MODID + ":" + unlocalizedName + "_attached", "normal"));
//		
//		Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
//		.register(Item.getItemFromBlock(this), 1, new ModelResourceLocation(Armory.MODID + ":" + unlocalizedName + "_standalone", "normal"));
	}
	
	public static void preInit() {
		GameRegistry.registerTileEntity(BrazierTileEntity.class, Armory.MODID + "_" + unlocalizedName);
	}
	
	public Brazier(boolean on) {
		super(Material.ground);
		this.blockHardness = 200;
		this.blockResistance = 45;
		this.setStepSound(Block.soundTypeStone);
		this.setUnlocalizedName(Armory.MODID + "_" + unlocalizedName);
        this.setCreativeTab(Armory.creativeTab);
        this.isBlockContainer = true;
        
        this.setDefaultState(this.blockState.getBaseState().withProperty(STANDALONE, true));
        
        if (on) {
        	this.setLightLevel(0.8f);
        	Brazier.onBlock = this;
        } else
        	Brazier.offBlock = this;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		TileEntity te = worldIn.getTileEntity(pos);
		if (te instanceof BrazierTileEntity) {
			if (this == offBlock &&
					((BrazierTileEntity) te).burnTime > 0) {
				//change to on block
				worldIn.setBlockState(pos, onBlock.getDefaultState());
			} else if (this == onBlock &&
					((BrazierTileEntity) te).burnTime <= 0) {
				worldIn.setBlockState(pos, offBlock.getDefaultState());
			}
		}
	}
	
	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		worldIn.setBlockState(pos, state.withProperty(STANDALONE, true), 2);
	}
	
	/**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(STANDALONE, meta == 0);
    }
    
    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        return (state.getValue(STANDALONE) ? 0 : 1);
    }
	
	protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] {STANDALONE});
    }

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new BrazierTileEntity();
	}
    
    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
    	BrazierTileEntity entity = (BrazierTileEntity) worldIn.getTileEntity(pos);
        if (entity != null) {
        	if (entity.fuel != null) {
        		EntityItem item = new EntityItem(
        				worldIn, (double) pos.getX(), (double) pos.getY(), (double) pos.getZ(),
        				ItemStack.copyItemStack(entity.fuel));
        		worldIn.spawnEntityInWorld(item);
        		entity.fuel = null;
        	}
        	
        }

        super.breakBlock(worldIn, pos, state);
    }
	
	public static class BrazierTileEntity extends TileEntity implements ITickable {
		
		protected ItemStack fuel;
		
		protected int burnTime;
		
		protected boolean isStandalone;
		
		private boolean hasTicked;
		
		public BrazierTileEntity() {
			this.fuel = null;
			burnTime = 0;
			isStandalone = true;
			hasTicked = false;
		}
		
		
		@Override
		public void writeToNBT(NBTTagCompound tag) {
			if (fuel != null) {
				NBTTagCompound itemTag = new NBTTagCompound();
				fuel.writeToNBT(itemTag);
				tag.setTag("fuel", itemTag);
			}
			if (burnTime > 0) {
				tag.setInteger("burnTime", burnTime);
			}
			
			tag.setBoolean("standalone", isStandalone);
			
			super.writeToNBT(tag);
		}
		
		@Override
		public void readFromNBT(NBTTagCompound tag) {
			
			if (tag.hasKey("fuel")) {
				NBTTagCompound nbt = tag.getCompoundTag("fuel");
				if (nbt != null)
					fuel = ItemStack.loadItemStackFromNBT(nbt);
				else
					fuel = null;
			} else
				fuel = null;
			
			if (tag.hasKey("burnTime", NBT.TAG_INT)) {
				burnTime = tag.getInteger("burnTime");
			} else
				burnTime = 0;
			
			if (tag.hasKey("standalone"))
				isStandalone = tag.getBoolean("standalone");
			else
				isStandalone = false;
			
			//updateContainer();
			
			super.readFromNBT(tag);
		}
		
		@Override
		public Packet<INetHandlerPlayClient> getDescriptionPacket() {
			NBTTagCompound tagCompound = new NBTTagCompound();
		    writeToNBT(tagCompound);
		    //return new Packet132TileEntityData(xCoord, yCoord, zCoord, 1, tagCompound);
		    return new S35PacketUpdateTileEntity(this.pos, 2, tagCompound);
		}
		
		@Override
		public void onDataPacket(net.minecraft.network.NetworkManager net, net.minecraft.network.play.server.S35PacketUpdateTileEntity pkt)
	    {
			NBTTagCompound tag = pkt.getNbtCompound();
			readFromNBT(tag);
			updateContainer();
	    }
		
		@Override
		public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
		{
		    return (oldState.getBlock() != newSate.getBlock());
			//return true;
		}

		public void onFirstTick() {
			//check to see whether this should be standalone or not
			EnumFacing direction = null;
			if (this.getWorld().getBlockState(pos.north()).getBlock()
					instanceof Forge)
				direction = EnumFacing.NORTH;
			else if (this.getWorld().getBlockState(pos.south()).getBlock()
					instanceof Forge)
				direction = EnumFacing.SOUTH;
			else if (this.getWorld().getBlockState(pos.east()).getBlock()
					instanceof Forge)
				direction = EnumFacing.EAST;
			else if (this.getWorld().getBlockState(pos.west()).getBlock()
					instanceof Forge)
				direction = EnumFacing.WEST;
			
			if (direction != null) {
				joinForge(direction);
			}
		}
		
		private void joinForge(EnumFacing direction) {
			this.isStandalone = false;
			updateContainer();
		}
		
		private void updateContainer() {
			if (pos == null || getWorld().getBlockState(pos) == null)
				return;
			getWorld().setBlockState(pos, 
					getWorld().getBlockState(pos).withProperty(STANDALONE, isStandalone));
		}

		@Override
		public void update() {
			if (!hasTicked) {
				hasTicked = true;
				onFirstTick();
			}
			if (burnTime > 0) {
				burnTime--;
			}
			
			if (burnTime <= 0) {
				//check for fuel
				if (fuel != null &&
						GameRegistry.getFuelValue(fuel) > 0) {
					fuel.splitStack(1);
					burnTime = GameRegistry.getFuelValue(fuel);
				}
			}
		}
		
	}
}
