package com.SkyIsland.Armory.blocks;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import com.SkyIsland.Armory.Armory;
import com.SkyIsland.Armory.items.weapons.Weapon;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Pedestal extends BlockContainer {
	
	public static class PedestalTileEntity extends TileEntity {
		
		private ItemStack heldRig;
		
		public PedestalTileEntity() {
			this.heldRig = null;
		}
		
		@Override
		public void writeToNBT(NBTTagCompound tag) {
			if (heldRig != null) {
				NBTTagCompound itemTag = new NBTTagCompound();
				heldRig.writeToNBT(itemTag);
				tag.setTag("held", itemTag);
			}
			
			super.writeToNBT(tag);
		}
		
		@Override
		public void readFromNBT(NBTTagCompound tag) {
			
			if (tag.hasKey("held")) {
				NBTTagCompound nbt = tag.getCompoundTag("held");
				if (nbt != null)
					heldRig = ItemStack.loadItemStackFromNBT(nbt);
				else
					heldRig = null;
			}
			
			super.readFromNBT(tag);
		}
		
		@Override
		public Packet<INetHandlerPlayClient> getDescriptionPacket() {
			NBTTagCompound tagCompound = new NBTTagCompound();
		    writeToNBT(tagCompound);
		    //return new Packet132TileEntityData(xCoord, yCoord, zCoord, 1, tagCompound);
		    return new S35PacketUpdateTileEntity(this.pos, 1, tagCompound);
		}
		
		@Override
		public void onDataPacket(net.minecraft.network.NetworkManager net, net.minecraft.network.play.server.S35PacketUpdateTileEntity pkt)
	    {
			NBTTagCompound tag = pkt.getNbtCompound();
			readFromNBT(tag);
	    }
		
		@Override
		public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
		{
		    //return (oldState.getBlock() != newSate.getBlock());
			return true;
		}
		
		public static class Renderer extends TileEntitySpecialRenderer<PedestalTileEntity> {

			@SuppressWarnings("deprecation")
			@Override
			public void renderTileEntityAt(PedestalTileEntity te, double x, double y, double z, float partialTicks,
					int destroyStage) {

				if (te.heldRig == null)
					return;
				
				boolean rotate = false;
				if (te.getBlockMetadata() > 3) //3 and 4 are W and E facings
					rotate = true;
				
				GlStateManager.pushMatrix();
				

				
				//don't use GL11 stuff, use the manager
				//GL11.glTranslated(x, y + 1.0f, z);
				GlStateManager.translate(x + 0.5, y + 0.85, z + 0.5);
				

				
				if (rotate) {
				GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
//				GlStateManager.rotate(
//						ModConfig.config.getTestValue(Key.ROTATE_ANGLE),
//						ModConfig.config.getTestValue(Key.ROTATE_X),
//						ModConfig.config.getTestValue(Key.ROTATE_Y),
//						ModConfig.config.getTestValue(Key.ROTATE_Z));
				}

//				if (te.heldRig.getItem() instanceof ItemSword
//						|| te.heldRig.getItem() instanceof Weapon)
				GlStateManager.rotate(225.0f, 0.0f, 0.0f, 1.0f);
					
				
				GlStateManager.enableRescaleNormal();
				GlStateManager.scale(1.0, 1.0, 1.0); //tweak for making smaller!
				
				Minecraft.getMinecraft().getRenderItem().renderItem(te.heldRig, TransformType.GROUND);
				GlStateManager.disableRescaleNormal();
				
				GL11.glPopMatrix();
				
			}
			
		}

	}
	
	private static final float BLOCK_WIDTH = 0.5f;
	
	private static final float BLOCK_DEPTH = 0.26f;
	
	private static final float BLOCK_HEIGHT = 0.5f;
	
	private static final float BLOCK_HEIGHT_FULL = 1.3f;

	public static Block block;
	
	public static Material material;
	
	public static final String unlocalizedName = "pedestal";
	
	protected static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	
	public static void preInit() {
	
		block = new Pedestal();
        
        GameRegistry.registerBlock(block, unlocalizedName);	
        GameRegistry.registerTileEntity(PedestalTileEntity.class, Armory.MODID + "_" + unlocalizedName);
	}
	
	public static void clientInit() {
		System.out.println("Registering pedestal model: " + Armory.MODID + ":" + unlocalizedName);
		for (int i = 0; i < 6; i++) {
			Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
			.register(Item.getItemFromBlock(block), i, new ModelResourceLocation(Armory.MODID + ":" + unlocalizedName, "facing=north"));
		}
		ClientRegistry.bindTileEntitySpecialRenderer(PedestalTileEntity.class, new PedestalTileEntity.Renderer());
	}
	
	public Pedestal() {
		super(Material.ground);
		this.blockResistance = 45;
		
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
		
		this.setStepSound(Block.soundTypeStone);
        //this.setBlockName(unlocalizedName); 1.7 method gone >:(
		this.setUnlocalizedName(Armory.MODID + "_" + unlocalizedName);
		//this.setUnlocalizedName(unlocalizedName);
        this.setCreativeTab(Armory.creativeTab);
        //this.setBlockTextureName(Armory.MODID + ":" + unlocalizedName);
        // i think the registry name also doubles as the texture name??
        // nope. 
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new PedestalTileEntity();
	}
	
	@Override
	public int getRenderType() {
		return 3; //not sure why
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false; //might not fill up whole block		
	}
	
	private void setFacing(World worldIn, BlockPos pos, IBlockState state) {
		if (!worldIn.isRemote)
        {
            Block block = worldIn.getBlockState(pos.north()).getBlock();
            Block block1 = worldIn.getBlockState(pos.south()).getBlock();
            Block block2 = worldIn.getBlockState(pos.west()).getBlock();
            Block block3 = worldIn.getBlockState(pos.east()).getBlock();
            EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);

            if (enumfacing == EnumFacing.NORTH && block.isFullBlock() && !block1.isFullBlock())
            {
                enumfacing = EnumFacing.SOUTH;
            }
            else if (enumfacing == EnumFacing.SOUTH && block1.isFullBlock() && !block.isFullBlock())
            {
                enumfacing = EnumFacing.NORTH;
            }
            else if (enumfacing == EnumFacing.WEST && block2.isFullBlock() && !block3.isFullBlock())
            {
                enumfacing = EnumFacing.EAST;
            }
            else if (enumfacing == EnumFacing.EAST && block3.isFullBlock() && !block2.isFullBlock())
            {
                enumfacing = EnumFacing.WEST;
            }

            worldIn.setBlockState(pos, state.withProperty(FACING, enumfacing), 2);
        }
	}
	
	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		setFacing(worldIn, pos, state);
	}
	
	
	/**
     * Get the Item that this Block should drop when harvested.
     */
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(block);
    }

    /**
     * Called by ItemBlocks just before a block is actually set in the world, to allow for adjustments to the
     * IBlockstate
     */
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }
	
    /**
     * Called by ItemBlocks after a block is set in the world, to allow post-place logic
     */
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
    	worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
    }
    
    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
    	System.out.println("get state: " + meta);

        EnumFacing enumfacing = EnumFacing.getFront(meta);

        if (enumfacing.getAxis() == EnumFacing.Axis.Y)
        {
            enumfacing = EnumFacing.NORTH;
        }

        return this.getDefaultState().withProperty(FACING, enumfacing);
    }
    
    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        return ((EnumFacing)state.getValue(FACING)).getIndex();
    }

    protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {FACING});
    }
	
    /**
     * When the block is right clicked
     */
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
    	PedestalTileEntity ent = fetchTileEntity(worldIn, pos);
    	if (ent != null) {
    		
    		ItemStack inHand = playerIn.getHeldItem();
    		if (inHand == null) {
    			//remove
    			dePedestal(ent, playerIn);
    		} else
    			return enPedestal(ent, playerIn);
    		
    	}
    	
    	return true;
    }
    
    private void dePedestal(PedestalTileEntity ent, EntityPlayer player) {
    	if (ent.heldRig == null)
    		return;
    	player.inventory.addItemStackToInventory(ent.heldRig);
    	ent.heldRig = null;
    }
    
    private boolean enPedestal(PedestalTileEntity ent, EntityPlayer player) {
    	if (ent.heldRig != null) {
    		dePedestal(ent, player);
    		return true;
    	}
    	
    	if (player.getHeldItem().getItem() instanceof ItemSword
    			|| player.getHeldItem().getItem() instanceof Weapon) {
    		ent.heldRig = player.getHeldItem().splitStack(1);
    		return true;
    	}
    	else
    		return false;
    }
    
    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        PedestalTileEntity entity = fetchTileEntity(worldIn, pos);
        if (entity != null) {
        	if (entity.heldRig != null) {
        		EntityItem item = new EntityItem(
        				worldIn, (double) pos.getX(), (double) pos.getY(), (double) pos.getZ(),
        				ItemStack.copyItemStack(entity.heldRig));
        		worldIn.spawnEntityInWorld(item);
        		entity.heldRig = null;
        	}
        	
        }

        super.breakBlock(worldIn, pos, state);
    }
    
    /**
     * Fetches the tile entity for this block, or null if it doesn't exist.
     * This method error checks for null returns or tile entities that are not
     * pedestal tile entities
     * @return
     */
    private PedestalTileEntity fetchTileEntity(World world, BlockPos pos) {
    	TileEntity entity = world.getTileEntity(pos);
    	if (entity == null)
    		return null;
    	if (entity instanceof PedestalTileEntity)
    		return (PedestalTileEntity) entity;
    	
    	return null;
    }
    
    //bounding box stuff adapated from BlockDoor
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.getSelectedBoundingBox(worldIn, pos);
    }

    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.getCollisionBoundingBox(worldIn, pos, state);
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
        IBlockState state = worldIn.getBlockState(pos);
        
        if (state.getBlock() != this) {
        	return;
        }
        
        EnumFacing facing = state.getValue(FACING);
        
        boolean rotate = (facing == EnumFacing.WEST || facing == EnumFacing.EAST);
        
        float x = BLOCK_WIDTH;//ModConfig.config.getTestValue(Key.PEDESTAL_WIDTH);
        float y = BLOCK_HEIGHT;//ModConfig.config.getTestValue(Key.PEDESTAL_HEIGHT);
        float z = BLOCK_DEPTH;//ModConfig.config.getTestValue(Key.PEDESTAL_DEPTH);
        
        if (rotate) {
        	float temp = x;
        	x = z;
        	z = temp;
        }
        
        TileEntity e = worldIn.getTileEntity(pos);
        if (e != null && e instanceof PedestalTileEntity) {
        	if (((PedestalTileEntity) e).heldRig != null)
        		y = BLOCK_HEIGHT_FULL;//ModConfig.config.getTestValue(Key.PEDESTAL_ADDED_HEIGHT);
        }
        
        
        this.setBlockBounds(.5f - x, 0.0f, .5f - z, .5f + x, y, .5f + z);
    }
}
