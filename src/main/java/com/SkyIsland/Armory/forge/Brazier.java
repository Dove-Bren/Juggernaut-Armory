package com.SkyIsland.Armory.forge;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import com.SkyIsland.Armory.Armory;
import com.SkyIsland.Armory.api.ForgeManager;
import com.SkyIsland.Armory.api.ForgeManager.FuelRecord;
import com.SkyIsland.Armory.blocks.BlockBase;
import com.SkyIsland.Armory.config.ModConfig;
import com.SkyIsland.Armory.items.HeldMetal;
import com.SkyIsland.Armory.items.MiscItems;
import com.SkyIsland.Armory.items.tools.Tongs;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Brazier extends BlockBase implements ITileEntityProvider {

	public static Material material;
	public static final String unlocalizedName = "brazier_block";
	
	private static Brazier onBlock;
	
	private static Brazier offBlock;
	
	protected boolean keepInventory = false;
	
	protected static final PropertyBool STANDALONE = PropertyBool.create("standalone");
	
	protected static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	
	public void clientInit() {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
		.register(Item.getItemFromBlock(this), 0, new ModelResourceLocation(Armory.MODID + ":" + unlocalizedName, "facing=north,standalone=true"));
		
		ModelBakery.registerItemVariants(Item.getItemFromBlock(this), 
				new ModelResourceLocation(Armory.MODID + ":" + unlocalizedName, "facing=north,standalone=true"),
				new ModelResourceLocation(Armory.MODID + ":" + unlocalizedName, "facing=north,standalone=false"));

		ClientRegistry.bindTileEntitySpecialRenderer(BrazierTileEntity.class, new BrazierTileEntity.Renderer());
		
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
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		TileEntity te = worldIn.getTileEntity(pos);
		if (te instanceof BrazierTileEntity) {
			keepInventory = true;
			if (this == offBlock &&
					((BrazierTileEntity) te).burnTime > 0) {
				//change to on block
				worldIn.setBlockState(pos, onBlock.getDefaultState()
							.withProperty(FACING, state.getValue(FACING))
							.withProperty(STANDALONE, state.getValue(STANDALONE)));
			} else if (this == onBlock &&
					((BrazierTileEntity) te).burnTime <= 0) {
//				System.out.println("random tick set");
				worldIn.setBlockState(pos, offBlock.getDefaultState()
						.withProperty(FACING, state.getValue(FACING))
						.withProperty(STANDALONE, state.getValue(STANDALONE)));
			}
			keepInventory = false;
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
    
    @Override
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
    	
    	if (!keepInventory) {
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
		
		//randomDisplayTick(worldIn, pos, state, new Random());
    }
    
    public void setFuel(World world, BlockPos pos, IBlockState state, ItemStack fuel) {
//    	System.out.println("setfuel");
    	BrazierTileEntity entity = (BrazierTileEntity) world.getTileEntity(pos);
        if (entity == null)
        	return;
        
//        System.out.println("fuel being set!");
        entity.fuel = fuel;
    }
    
    public boolean onBlockActivated(
    								World worldIn, BlockPos pos,
    								IBlockState state,
    								EntityPlayer playerIn,
    								EnumFacing side,
    								float hitX,
    								float hitY,
    								float hitZ) {
        // Open GUI!
        if (!worldIn.isRemote && 
        		(playerIn.getCurrentEquippedItem() == null || !(playerIn.getCurrentEquippedItem().getItem() instanceof Tongs)  )) {
        	System.out.println("Opening gui...");
        	playerIn.openGui(Armory.instance, Armory.Gui_Type.BRAZIER.ordinal(), worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }
	
	public static class BrazierTileEntity extends TileEntityLockable implements ITickable {
		
		protected ItemStack fuel;
		
		protected int burnTime;
		
		protected boolean isStandalone;
		
		private boolean hasTicked;
		
		//forge variables \/
		private float heat;
		
		private ItemStack heatingElement;
		
		private float currentHeatRate;
		
		private float heatMax;
		
		private EnumFacing face;
		
		
		public BrazierTileEntity() {
			this.fuel = null;
			burnTime = 0;
			isStandalone = true;
			hasTicked = false;
			heat = 0f;
			heatingElement = null;
			currentHeatRate = 0;
			heatMax = 0;
			face = EnumFacing.NORTH;
		}
		
		protected float getHeat() {
			return heat;
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
				tag.setFloat("heat", heat);
				tag.setFloat("heatRate", currentHeatRate);
				tag.setFloat("heatMax", heatMax);
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
				heat = tag.getFloat("heat");
				currentHeatRate = tag.getFloat("heatRate");
				heatMax = tag.getFloat("heatMax");
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
		public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
		{
		    //return (oldState.getBlock() != newSate.getBlock());
//			System.out.println("refresh [" + newState.getBlock() + "]? -> " + !(newState.getBlock() instanceof Brazier));
			return !(newState.getBlock() instanceof Brazier);
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
			if (!isStandalone) //took out facing == direction
				return;
			
			//either not already standalone, or facing a different direction now
			this.isStandalone = false;
			this.face = direction;
			
			//hook into Forge entity, update it's brazier to us
			//Forge forge = (Forge) ForgeBlocks.getBlock(ArmoryBlocks.FORGE);
			Forge.setBrazier(getWorld(), getPos().offset(direction),
					direction.getOpposite());
			
			updateContainer();
		}
		
		protected void breakFromForge() {
			if (isStandalone)
				return;
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
			
			Forge.setBrazier(getWorld(), getPos().offset(face), null);
			
			updateContainer();
		}
		
		private void updateContainer() {
			if (pos == null || getWorld().getBlockState(pos) == null)
				return;
			if (!(getWorld().getBlockState(pos).getBlock() instanceof Brazier))
				return;
			
			((Brazier) getWorld().getBlockState(pos).getBlock())
				.keepInventory = true;
			
			getWorld().setBlockState(pos, 
					getWorld().getBlockState(pos).withProperty(STANDALONE, isStandalone)
					                             .withProperty(FACING, face));
			
			((Brazier) getWorld().getBlockState(pos).getBlock())
			.keepInventory = false;
			
			//getWorld().setTileEntity(pos, this);
			
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
				heat = Math.max(0f, heat - ModConfig.config.getHeatLoss());
				
				/**
				 * Update heating element (metal piece) if heat is greater
				 */
				if (heatingElement != null) {
					//have something we're heating. What's it's heat?
					float itemHeat = ((HeldMetal) MiscItems.getItem(MiscItems.Items.HELD_METAL))
					.getHeat(heatingElement);
					
					if (heat > itemHeat) {
						//add some of our heat to the item heat
						((HeldMetal) MiscItems.getItem(MiscItems.Items.HELD_METAL))
						.setHeat(heatingElement, itemHeat + 0.2f);
					}
				}
			}
			
			if (burnTime <= 0) {
				//check for fuel
				if (fuel != null) {
					if (isStandalone) {
						if (getVanillaFuel(fuel) > 0) {
							burnTime = getVanillaFuel(fuel);
							fuel.stackSize--;
						} else if (GameRegistry.getFuelValue(fuel) > 0) {
							burnTime = GameRegistry.getFuelValue(fuel);
							fuel.stackSize--;
						} else if (ForgeManager.instance().getFuelRecord(fuel.getItem()) != null) {
							burnTime = ForgeManager.instance().getFuelRecord(fuel.getItem()).getBurnTicks();
							fuel.stackSize--;
						}
						
						if (fuel.stackSize <= 0)
							fuel = null;
						
//						System.out.println("cook time: " + burnTime);
					} else if (/*!isStandalone && */ ForgeManager.instance().getFuelRecord(fuel.getItem()) != null) {
						
						FuelRecord record = ForgeManager.instance().getFuelRecord(fuel.getItem());
						burnTime = record.getBurnTicks();
						currentHeatRate = record.getHeatRate();
						this.heatMax = record.getMaxHeat();
						
						fuel.stackSize--;
						
						if (fuel.stackSize <= 0)
							fuel = null;
						
					}
				}
			}
		}

		private int getVanillaFuel(ItemStack fuel2) {
			Item item = fuel2.getItem();

            if (item instanceof ItemBlock && Block.getBlockFromItem(item) != Blocks.air)
            {
                Block block = Block.getBlockFromItem(item);

                if (block == Blocks.wooden_slab)
                {
                    return 150;
                }

                if (block.getMaterial() == Material.wood)
                {
                    return 300;
                }

                if (block == Blocks.coal_block)
                {
                    return 16000;
                }
            }

            if (item instanceof ItemTool && ((ItemTool)item).getToolMaterialName().equals("WOOD")) return 200;
            if (item instanceof ItemSword && ((ItemSword)item).getToolMaterialName().equals("WOOD")) return 200;
            if (item instanceof ItemHoe && ((ItemHoe)item).getMaterialName().equals("WOOD")) return 200;
            if (item == Items.stick) return 100;
            if (item == Items.coal) return 1600;
            if (item == Items.lava_bucket) return 20000;
            if (item == Item.getItemFromBlock(Blocks.sapling)) return 100;
            if (item == Items.blaze_rod) return 2400;
            
            return 0;
		}
		
		/**
		 * Sets the item this forge is heating.
		 * This method performs a check to make certain the item is some held metal.
		 * @param stack
		 * @return whether the item was accepted
		 */
		public boolean offerHeatingElement(ItemStack stack) {
			if (heatingElement != null)
				return false;
			
			if (stack == null || !(stack.getItem() instanceof HeldMetal))
				return false;
			
			this.heatingElement = stack;
			return true;
		}
		
		public ItemStack collectHeatingElement() {
			if (heatingElement == null)
				return null;
			
			ItemStack ret = this.heatingElement;
			this.heatingElement = null;
			return ret;
		}

		//TileEntityLockable Methods
		//TODO Sort Through and see what we need to change
		
		/**
		 * Indicates that container texture should be drawn
		 * @param parIInventory
		 * @return
		 */
		@SideOnly(Side.CLIENT)
		public static boolean func_174903_a(IInventory parIInventory) {
			return true;
		}
		
		@Override
		public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
			// TODO Auto-generated method stub
			return null;
		}


		@Override
		public String getGuiID() {
			return "armory:brazier";
		}


		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return null;
		}


		@Override
		public boolean hasCustomName() {
			// TODO Auto-generated method stub
			return false;
		}


		@Override
		public int getSizeInventory() {
			// TODO Auto-generated method stub
			return 1;
		}


		@Override
		public ItemStack getStackInSlot(int index) {
			if (index == 0) {
				return this.fuel;
			}
			return null;
		}


		@Override
		public ItemStack decrStackSize(int index, int count) {
			if (index == 0) {
				ItemStack ItemReturn = this.fuel.splitStack(count);
				return ItemReturn;
			}
			return null;
		}


		@Override
		public ItemStack removeStackFromSlot(int index) {
			//System.out.println("Attempting to remove at " + index);
			if (index == 0) {
				ItemStack ItemReturn = this.fuel;
				this.fuel = (ItemStack) null;
				System.out.println("Removing itemstack");
				return ItemReturn;
			}
			return null;
		}


		@Override
		public void setInventorySlotContents(int index, ItemStack stack) {
			if (index == 0) {
				this.fuel = stack;
			}
		}


		@Override
		public int getInventoryStackLimit() {
			// TODO Auto-generated method stub
			return 64;
		}


		@Override
		public boolean isUseableByPlayer(EntityPlayer player) {
			// TODO Auto-generated method stub
			return true;
		}


		@Override
		public void openInventory(EntityPlayer player) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void closeInventory(EntityPlayer player) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public boolean isItemValidForSlot(int index, ItemStack stack) {
			// TODO Auto-generated method stub
			return true;
		}


		@Override
		public int getField(int id) {
			// TODO Auto-generated method stub
			return 0;
		}


		@Override
		public void setField(int id, int value) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public int getFieldCount() {
			// TODO Auto-generated method stub
			return 0;
		}


		@Override
		public void clear() {
			// TODO Auto-generated method stub
			
		}
		
		public static class Renderer extends TileEntitySpecialRenderer<BrazierTileEntity> {
		
			@Override
			public void renderTileEntityAt(BrazierTileEntity te, double x, double y, double z, float partialTicks,
					int destroyStage) {

				if (te.isStandalone || te.heatingElement == null)
					return;
				
				GlStateManager.pushMatrix();
				

				
				//don't use GL11 stuff, use the manager
				//GL11.glTranslated(x, y + 1.0f, z);
				GlStateManager.translate(x + 0.5, y + 0.35, z + 0.5);
				

				
				
//						GlStateManager.rotate(90.0f * (te.face.getOpposite().getHorizontalIndex()) 
//								, 0.0f, 1.0f, 0.0f);
//				GlStateManager.rotate(
//						ModConfig.config.getTestValue(Key.ROTATE_ANGLE),
//						ModConfig.config.getTestValue(Key.ROTATE_X),
//						ModConfig.config.getTestValue(Key.ROTATE_Y),
//						ModConfig.config.getTestValue(Key.ROTATE_Z));

//				if (te.heldRig.getItem() instanceof ItemSword
//						|| te.heldRig.getItem() instanceof Weapon)
//						GlStateManager.rotate(225.0f, 0.0f, 0.0f, 1.0f);
				
				
					
				
				GlStateManager.enableRescaleNormal();
				GlStateManager.scale(1.0, 1.0, 1.0); //tweak for making smaller!
				
				Minecraft.getMinecraft().getRenderItem().renderItem(te.heatingElement, TransformType.GROUND);
				GlStateManager.disableRescaleNormal();
				
				GL11.glPopMatrix();
				
			}
			
		}

		
	}
	
	/**
	 * This is a Container for the Brazier
	 * Who knows what this actually does.
	 * @author William Fong
	 *
	 */
	public static class BrazierContainer extends Container {
		
		private BrazierTileEntity brazier;
		
		public BrazierContainer(IInventory playerInv, BrazierTileEntity brazier) {
			this.brazier = brazier;
			short slotID = 0;
			
			// Construct slots for player to interact with
			// Brazier only needs '1' inventory slot to interact with
			// Uses slot ID 0
			this.addSlotToContainer(new Slot(brazier, 0, 17, 30));
			slotID = 1;
			// Construct player inventory
			for (int y = 0; y < 3; y++) {
				for (int x = 0; x < 9; x++) {
					this.addSlotToContainer(new Slot(playerInv, x + y * 9 + 9, 8 + (x * 18), 84 + (y * 18)));
					slotID++;
				}
			}
			// Construct player hotbar
			for (int x = 0; x < 9; x++) {
				this.addSlotToContainer(new Slot(playerInv, x, 8 + x * 18, 142));
				slotID++;
			}
		}
		
		@Override
		public ItemStack transferStackInSlot(EntityPlayer playerIn, int fromSlot) {
			ItemStack prev = null;	
			Slot slot = (Slot) this.inventorySlots.get(fromSlot);
			
			if (slot != null && slot.getHasStack()) {
				ItemStack cur = slot.getStack();
				prev = cur.copy();
				
				
				/** If we want additional behavior put it here **/
				/**if (fromSlot == 0) {
					// This is going FROM Brazier to player
					if (!this.mergeItemStack(cur, 9, 45, true))
						return null;
					else
						// From Player TO Brazier
						if (!this.mergeItemStack(cur, 0, 0, false)) {
							return null;
						}
				}**/
				
				if (cur.stackSize == 0) {
					slot.putStack((ItemStack) null);
				} else {
					slot.onSlotChanged();
				}
				
				if (cur.stackSize == prev.stackSize) {
					return null;
				}
				slot.onPickupFromSlot(playerIn, cur);
			}
			
			return prev;
		}
		
		@Override
		public boolean canInteractWith(EntityPlayer playerIn) {
			// TODO Auto-generated method stub
			return true;
		}

	}
	
	@SideOnly(Side.CLIENT)
	public static class BrazierGui extends GuiContainer {

		private static final ResourceLocation GuiImageLocation =
				new ResourceLocation(Armory.MODID + ":textures/gui/container/brazier.png");
		
		public BrazierGui(Brazier.BrazierContainer brazier) {
			super(brazier);
		}
		
		@Override
		protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
			// TODO Draw Gui
			GlStateManager.color(1.0F,  1.0F, 1.0F, 1.0F);
			mc.getTextureManager().bindTexture(GuiImageLocation);
			int horizontalMargin = (width - xSize) / 2;
			int verticalMargin = (height - ySize) / 2;
			drawTexturedModalRect(horizontalMargin, verticalMargin, 0,0, xSize, ySize);
		}
		
		
	}
}
