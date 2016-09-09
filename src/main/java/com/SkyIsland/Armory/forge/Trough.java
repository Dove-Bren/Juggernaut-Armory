package com.SkyIsland.Armory.forge;

import org.lwjgl.opengl.GL11;

import com.SkyIsland.Armory.Armory;
import com.SkyIsland.Armory.api.ForgeManager;
import com.SkyIsland.Armory.api.ForgeManager.CoolantRecord;
import com.SkyIsland.Armory.blocks.BlockBase;
import com.SkyIsland.Armory.forge.ForgeBlocks.ArmoryBlocks;
import com.SkyIsland.Armory.items.HeldMetal;
import com.SkyIsland.Armory.items.MiscItems;
import com.SkyIsland.Armory.items.ScrapMetal;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
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
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class Trough extends BlockBase implements ITileEntityProvider {
	
	public static Block block;
	
	public static final String unlocalizedName = "forge_trough";
	
	//protected FluidStack fluid;
	
    protected int capacity;
    
    protected TroughTileEntity tile;
	
	public void clientInit() {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
		.register(Item.getItemFromBlock(this), 0, new ModelResourceLocation(Armory.MODID + ":" + unlocalizedName, "normal"));
	}
	
	public static void preInit() {
		GameRegistry.registerTileEntity(TroughTileEntity.class, Armory.MODID + "_" + unlocalizedName);
		ClientRegistry.bindTileEntitySpecialRenderer(TroughTileEntity.class, new TroughTileEntity.Renderer());
	}
	
	public void init() {
		GameRegistry.addShapedRecipe(new ItemStack(ForgeBlocks.getBlock(ArmoryBlocks.TROUGH)),
				new Object[]{"B B", "IUI", "III", 'I', Items.iron_ingot, 'B', Item.getItemFromBlock(Blocks.iron_bars), 'U', Items.bucket});
	}
	
	public Trough() {
		super(Material.ground);
		this.blockHardness = 200;
		this.blockResistance = 45;
		this.setStepSound(Block.soundTypeStone);
		this.setUnlocalizedName(Armory.MODID + "_" + unlocalizedName);
        this.setCreativeTab(Armory.creativeTab);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TroughTileEntity();
	}
	
	public static FluidStack getFluid(World worldIn, BlockPos pos, IBlockState blockState, boolean empty) {
		if (!(blockState.getBlock() instanceof Trough)) {
			return null;
		}
		
		TileEntity tent = worldIn.getTileEntity(pos);
		if (tent == null || !(tent instanceof TroughTileEntity)) {
			return null;
		}
		
		TroughTileEntity te = (TroughTileEntity) tent;
		return te.getFluidStack(empty);
	}
	
	public static ItemStack takeHeldItem(World worldIn, BlockPos pos, IBlockState blockState) {
		if (!(blockState.getBlock() instanceof Trough)) {
			return null;
		}
		
		TileEntity tent = worldIn.getTileEntity(pos);
		if (tent == null || !(tent instanceof TroughTileEntity)) {
			return null;
		}
		
		TroughTileEntity te = (TroughTileEntity) tent;
		return te.takeItem();
	}
	
	@Override
	public boolean onBlockActivated(
			World worldIn, BlockPos pos,
			IBlockState state,
			EntityPlayer playerIn,
			EnumFacing side,
			float hitX,
			float hitY,
			float hitZ) {
		
		TileEntity tent = worldIn.getTileEntity(pos);
		if (tent == null || !(tent instanceof TroughTileEntity)) {
			return false;
		}
		
		TroughTileEntity tank = (TroughTileEntity) tent;
		
		FluidUtil.interactWithTank(playerIn.getHeldItem(), playerIn, tank, side);
		
		return true;
	}
	
	public static class TroughTileEntity extends TileEntity implements IFluidHandler, IFluidTank, ITickable {
		
		protected FluidStack fluid;
		
		protected ItemStack heldItem;
		
		private static final int capacity = 1000;
		
		public TroughTileEntity() {
			this.fluid = null;
			heldItem = null;
		}
		
		
		@Override
		public void writeToNBT(NBTTagCompound tag) {
			
			if (fluid != null)
				fluid.writeToNBT(tag);
			else {
	            tag.setString("Empty", "");
	        }
			
			if (heldItem != null) {
				NBTTagCompound nbt = new NBTTagCompound();
				heldItem.writeToNBT(nbt);
				tag.setTag("held", nbt);
			}
				
			
			super.writeToNBT(tag);
		}
		
		@Override
		public void readFromNBT(NBTTagCompound tag) {
			
	        if (!tag.hasKey("Empty"))
	        	fluid = FluidStack.loadFluidStackFromNBT(tag);
	        else
	        	fluid = null;
	        
	        if (tag.hasKey("held", NBT.TAG_COMPOUND))
	        	heldItem = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("held"));
			
			super.readFromNBT(tag);
		}
		
		@Override
		public Packet<INetHandlerPlayClient> getDescriptionPacket() {
			NBTTagCompound tagCompound = new NBTTagCompound();
		    writeToNBT(tagCompound);
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
		
		public FluidStack getFluidStack(boolean empty) {
			if (fluid == null)
				return null;
			
			FluidStack ret = fluid;
			if (empty)
				this.fluid = null;
			return ret;
		}
		
		@Override
		public void update() {
			//if holding an item (and have coolant left), cool it off
			if (heldItem != null && fluid != null && fluid.amount > 0) {

				if (!(heldItem.getItem() instanceof HeldMetal))
					return;
				
				CoolantRecord record = ForgeManager.instance().getCoolant(fluid.getFluid());
				if (record == null)
					return;
				
				HeldMetal inst = ((HeldMetal) MiscItems.getItem(MiscItems.Items.HELD_METAL));
				inst.setHeat(heldItem, inst.getHeat(heldItem) - record.getCoolingRate());
				
				if (inst.getHeat(heldItem) <= 0) {
					ItemStack newItem = inst.cast(heldItem);
					if (newItem == null) {
						//make it scrap
						ScrapMetal scrap = (ScrapMetal) MiscItems.getItem(MiscItems.Items.SCRAP);
						newItem = new ItemStack(scrap);
						ItemStack ret = inst.getMetal(heldItem);
						ret.stackSize = 1;
						scrap.setReturn(newItem, 
								ret
								);
						
						getWorld().playSound(pos.getX(), pos.getY(), pos.getZ(),
							Armory.MODID + ":item.metal.cool", 1.0f, 1.0f, false);
					}
					
					this.heldItem = newItem;
				}
			}
		}
		
		public ItemStack takeItem() {
			ItemStack ret = heldItem;
			heldItem = null;
			
			getWorld().markBlockForUpdate(pos);
			markDirty();
			return ret;
		}
		
		public boolean offerItem(ItemStack item) {
			if (heldItem != null)
				return false;
			heldItem = item;
			getWorld().markBlockForUpdate(pos);
			markDirty();
			return true;
		}
		
		public static class Renderer extends TileEntitySpecialRenderer<TroughTileEntity> {

			@Override
			public void renderTileEntityAt(TroughTileEntity te, double x, double y, double z, float partialTicks,
					int destroyStage) {

				if (te.fluid == null)
					return;
				
//				boolean rotate = false;
//				if (te.getBlockMetadata() > 3) //3 and 4 are W and E facings
//					rotate = true;
				
				GlStateManager.pushMatrix();
				

				
				//don't use GL11 stuff, use the manager
				//GL11.glTranslated(x, y + 1.0f, z);
				GlStateManager.translate(x + 0.5, y + 1.85, z + 0.5);
				

				
//				if (rotate) {
				//GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
//				GlStateManager.rotate(
//						ModConfig.config.getTestValue(Key.ROTATE_ANGLE),
//						ModConfig.config.getTestValue(Key.ROTATE_X),
//						ModConfig.config.getTestValue(Key.ROTATE_Y),
//						ModConfig.config.getTestValue(Key.ROTATE_Z));
//				}

//				if (te.heldRig.getItem() instanceof ItemSword
//						|| te.heldRig.getItem() instanceof Weapon)
				//GlStateManager.rotate(225.0f, 0.0f, 0.0f, 1.0f);
					
				
				GlStateManager.enableRescaleNormal();
				GlStateManager.scale(1.0, 1.0, 1.0); //tweak for making smaller!
				
				Minecraft.getMinecraft().getRenderItem().renderItem(te.heldItem, TransformType.GROUND);
				GlStateManager.disableRescaleNormal();
				
				GL11.glPopMatrix();
				
			}
			
		}

		@Override
		public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
			return fill(resource, doFill);
		}


		@Override
		public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
			if (resource == null || !resource.isFluidEqual(getFluid()))
	        {
	            return null;
	        }
	        return drain(resource.amount, doDrain);
		}


		@Override
		public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
			return drain(maxDrain, doDrain);
		}


		@Override
		public boolean canFill(EnumFacing from, Fluid fluid) {
			return true;
		}


		@Override
		public boolean canDrain(EnumFacing from, Fluid fluid) {
			return true;
		}


		@Override
		public FluidTankInfo[] getTankInfo(EnumFacing from) {
			return new FluidTankInfo[] { getInfo() };
		}


		@Override
		public FluidStack getFluid() {
			return fluid;
		}


		@Override
		public int getFluidAmount() {
			if (fluid == null)
				return 0;
			
			return fluid.amount;
		}


		@Override
		public int getCapacity() {
			return capacity;
		}


		@Override
		public FluidTankInfo getInfo() {
			return new FluidTankInfo(this);
		}


		@Override
		public int fill(FluidStack resource, boolean doFill) {
			if (resource == null)
	        {
	            return 0;
	        }
			
			if (null == ForgeManager.instance().getCoolant(resource.getFluid())) {
				return 0;
			}

	        if (!doFill)
	        {
	            if (fluid == null)
	            {
	                return Math.min(capacity, resource.amount);
	            }

	            if (!fluid.isFluidEqual(resource))
	            {
	                return 0;
	            }

	            return Math.min(capacity - fluid.amount, resource.amount);
	        }

	        if (fluid == null)
	        {
	            fluid = new FluidStack(resource, Math.min(capacity, resource.amount));

                FluidEvent.fireEvent(new FluidEvent.FluidFillingEvent(fluid, getWorld(), getPos(), this, fluid.amount));
	            return fluid.amount;
	        }

	        if (!fluid.isFluidEqual(resource))
	        {
	            return 0;
	        }
	        int filled = capacity - fluid.amount;

	        if (resource.amount < filled)
	        {
	            fluid.amount += resource.amount;
	            filled = resource.amount;
	        }
	        else
	        {
	            fluid.amount = capacity;
	        }

            FluidEvent.fireEvent(new FluidEvent.FluidFillingEvent(fluid, getWorld(), getPos(), this, filled));
	        return filled;
		}


		@Override
		public FluidStack drain(int maxDrain, boolean doDrain) {
			if (fluid == null)
	        {
	            return null;
	        }

	        int drained = maxDrain;
	        if (fluid.amount < drained)
	        {
	            drained = fluid.amount;
	        }

	        FluidStack stack = new FluidStack(fluid, drained);
	        if (doDrain)
	        {
	            fluid.amount -= drained;
	            if (fluid.amount <= 0)
	            {
	                fluid = null;
	            }

                FluidEvent.fireEvent(new FluidEvent.FluidDrainingEvent(fluid, getWorld(), getPos(), this, drained));
	        }
	        return stack;
		}
	}
}
