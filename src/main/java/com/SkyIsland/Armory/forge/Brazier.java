package com.SkyIsland.Armory.forge;

import java.util.Random;

import com.SkyIsland.Armory.Armory;
import com.SkyIsland.Armory.api.ForgeManager;
import com.SkyIsland.Armory.api.ForgeManager.FuelRecord;
import com.SkyIsland.Armory.blocks.BlockBase;
import com.SkyIsland.Armory.config.ModConfig;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
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
	
	protected static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	
	public void clientInit() {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
		.register(Item.getItemFromBlock(this), 0, new ModelResourceLocation(Armory.MODID + ":" + unlocalizedName, "facing=north,standalone=true"));
		
		ModelBakery.registerItemVariants(Item.getItemFromBlock(this), 
				new ModelResourceLocation(Armory.MODID + ":" + unlocalizedName, "facing=north,standalone=true"),
				new ModelResourceLocation(Armory.MODID + ":" + unlocalizedName, "facing=north,standalone=false"));

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
        
        this.setDefaultState(this.blockState.getBaseState().withProperty(STANDALONE, true)
        		.withProperty(FACING, EnumFacing.NORTH));
        
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
		worldIn.setBlockState(pos, state.withProperty(STANDALONE, true).withProperty(FACING, EnumFacing.NORTH), 2);
	}
	
	/**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
    	//bottom 3 bits are important. bottom 2 are facing (0-3), and 3rd is
    	//boolean: !standalone
        return this.getDefaultState().withProperty(STANDALONE, (meta & 4) == 0)
        		.withProperty(FACING, EnumFacing.getHorizontal(meta & 3));
    }
    
    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        return (state.getValue(STANDALONE) ? 0 : 4)
        		| (state.getValue(FACING).getHorizontalIndex());
    }
	
	protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] {FACING, STANDALONE});
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
        	if (!entity.isStandalone)
        		entity.breakFromForge();
        	
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
    
    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
    	BrazierTileEntity entity = (BrazierTileEntity) worldIn.getTileEntity(pos);
        if (entity == null)
        	return;
    	EnumFacing direction = null;
		if (worldIn.getBlockState(pos.north()).getBlock()
				instanceof Forge)
			direction = EnumFacing.NORTH;
		else if (worldIn.getBlockState(pos.south()).getBlock()
				instanceof Forge)
			direction = EnumFacing.SOUTH;
		else if (worldIn.getBlockState(pos.east()).getBlock()
				instanceof Forge)
			direction = EnumFacing.EAST;
		else if (worldIn.getBlockState(pos.west()).getBlock()
				instanceof Forge)
			direction = EnumFacing.WEST;
		
		if (direction != null) {
			entity.joinForge(direction);
		} else if (!entity.isStandalone) {
			entity.breakFromForge();
		}
    }
	
	public static class BrazierTileEntity extends TileEntity implements ITickable {
		
		protected ItemStack fuel;
		
		protected int burnTime;
		
		protected boolean isStandalone;
		
		private boolean hasTicked;
		
		//forge variables \/
		private int heat;
		
		private ItemStack heatingElement;
		
		private int currentHeatRate;
		
		private int heatMax;
		
		private EnumFacing face;
		
		public BrazierTileEntity() {
			this.fuel = null;
			burnTime = 0;
			isStandalone = true;
			hasTicked = false;
			heat = 0;
			heatingElement = null;
			currentHeatRate = 0;
			heatMax = 0;
			face = EnumFacing.NORTH;
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
			
			if (!isStandalone) {
				tag.setInteger("heat", heat);
				tag.setInteger("heatRate", currentHeatRate);
				tag.setInteger("heatMax", heatMax);
				if (heatingElement != null) {
					NBTTagCompound itemTag = new NBTTagCompound();
					heatingElement.writeToNBT(itemTag);
					tag.setTag("element", itemTag);
				}
				tag.setByte("facing", (byte) face.getHorizontalIndex());
			}
			
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
				isStandalone = true;
			
			if (!isStandalone) {
				heat = tag.getInteger("heat");
				currentHeatRate = tag.getInteger("heatRate");
				heatMax = tag.getInteger("heatMax");
				face = EnumFacing.getHorizontal(tag.getByte("facing"));
				
				if (tag.hasKey("element", NBT.TAG_COMPOUND)) {
					heatingElement = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("element"));
				}
			}
			
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
		
		protected void joinForge(EnumFacing direction) {
			if (!isStandalone && face == direction)
				return;
			
			//either not already standalone, or facing a different direction now
			this.isStandalone = false;
			this.face = direction;
			updateContainer();
		}
		
		protected void breakFromForge() {
			this.isStandalone = true;
			this.heat = 0;
			this.heatMax = 0;
			this.currentHeatRate = 0;
			
			if (heatingElement != null) {
				EntityItem item = new EntityItem(
	    				getWorld(), (double) pos.getX(), (double) pos.getY(), (double) pos.getZ(),
	    				ItemStack.copyItemStack(heatingElement));
	    		getWorld().spawnEntityInWorld(item);
			}
			
			updateContainer();
		}
		
		private void updateContainer() {
			if (pos == null || getWorld().getBlockState(pos) == null)
				return;
			getWorld().setBlockState(pos, 
					getWorld().getBlockState(pos).withProperty(STANDALONE, isStandalone)
					                             .withProperty(FACING, face));
		}

		@Override
		public void update() {
			if (!hasTicked) {
				hasTicked = true;
				onFirstTick();
			}
			if (burnTime > 0) {
				burnTime--;
				
				if (!isStandalone) {
					//add heat
					heat = Math.min(heatMax, heat + currentHeatRate);
				}
			} else if (!isStandalone && heat > 0) {
				heat = Math.max(0, heat - ModConfig.config.getHeatLoss());
			}
			
			if (burnTime <= 0) {
				//check for fuel
				if (fuel != null) {
					if (isStandalone && GameRegistry.getFuelValue(fuel) > 0) {
						fuel.splitStack(1);
						burnTime = GameRegistry.getFuelValue(fuel);
					} else if (!isStandalone && ForgeManager.instance().getFuelRecord(fuel.getItem()) != null) {
						
						FuelRecord record = ForgeManager.instance().getFuelRecord(fuel.getItem());
						burnTime = record.getBurnTicks();
						currentHeatRate = record.getHeatRate();
						
					}
				}
			}
		}
		
	}
}
