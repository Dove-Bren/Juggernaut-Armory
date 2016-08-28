package com.SkyIsland.Armory.blocks;

import org.lwjgl.opengl.GL11;

import com.SkyIsland.Armory.Armory;
import com.SkyIsland.Armory.items.weapons.Weapon;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
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
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

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

				System.out.print(".");
				if (te.heldRig == null)
					return;
				System.out.print(" ");
				
				GlStateManager.pushMatrix();
				
				//don't use GL11 stuff, use the manager
				//GL11.glTranslated(x, y + 1.0f, z);
				GlStateManager.translate(x + 0.5, y + 1.0, z + 0.5);
				
				if (te.heldRig.getItem() instanceof ItemSword
						|| te.heldRig.getItem() instanceof Weapon)
					GlStateManager.rotate(45.0f, 0.5f, 0.5f, 0.5f);
				
				GlStateManager.enableRescaleNormal();
				GlStateManager.scale(1.0, 1.0, 1.0); //tweak for making smaller!
				
				Minecraft.getMinecraft().getRenderItem().renderItem(te.heldRig, TransformType.GROUND);
				GlStateManager.disableRescaleNormal();
				
				GL11.glPopMatrix();
				
			}
			
		}

	}

	public static Block block;
	
	public static Material material;
	
	public static final String unlocalizedName = "pedestal";
	
	public static void preInit() {
	
		block = new Pedestal();
        
        GameRegistry.registerBlock(block, unlocalizedName);	
        GameRegistry.registerTileEntity(PedestalTileEntity.class, Armory.MODID + "_" + unlocalizedName);
	}
	
	public static void clientInit() {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
		.register(Item.getItemFromBlock(block), 0, new ModelResourceLocation(Armory.MODID + ":" + unlocalizedName, "normal"));
		ClientRegistry.bindTileEntitySpecialRenderer(PedestalTileEntity.class, new PedestalTileEntity.Renderer());
	}
	
	public Pedestal() {
		super(Material.ground);
		this.blockHardness = 200;
		this.blockResistance = 45;
		this.setStepSound(Block.soundTypeStone);
        //this.setBlockName(unlocalizedName); 1.7 method gone >:(
		this.setUnlocalizedName(Armory.MODID + "_" + unlocalizedName);
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
		return -1; //not sure why
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false; //might not fill up whole block		
	}
	
    /**
     * Called by ItemBlocks after a block is set in the world, to allow post-place logic
     */
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
    	worldIn.setBlockState(pos, state, 3);
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
    			enPedestal(ent, playerIn);
    		
    	}
    	
    	return true;
    }
    
    private void dePedestal(PedestalTileEntity ent, EntityPlayer player) {
    	if (ent.heldRig == null)
    		return;
    	player.inventory.addItemStackToInventory(ent.heldRig);
    	ent.heldRig = null;
    }
    
    private void enPedestal(PedestalTileEntity ent, EntityPlayer player) {
    	if (ent.heldRig != null) {
    		dePedestal(ent, player);
    		return;
    	}
    	
    	ent.heldRig = player.getHeldItem().splitStack(1);
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
}
