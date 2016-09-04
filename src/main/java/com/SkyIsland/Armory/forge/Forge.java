package com.SkyIsland.Armory.forge;

import java.util.LinkedList;
import java.util.List;

import com.SkyIsland.Armory.Armory;
import com.SkyIsland.Armory.api.ForgeManager;
import com.SkyIsland.Armory.api.ForgeManager.MetalRecord;
import com.SkyIsland.Armory.blocks.BlockBase;
import com.SkyIsland.Armory.forge.Brazier.BrazierTileEntity;
import com.SkyIsland.Armory.items.HeldMetal;
import com.SkyIsland.Armory.items.MiscItems;
import com.SkyIsland.Armory.items.MiscItems.Items;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
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

public class Forge extends BlockBase implements ITileEntityProvider {
	
	public static Block block;
	public static Material material;
	public static final String unlocalizedName = "forge_block";
	
	public void clientInit() {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
		.register(Item.getItemFromBlock(this), 0, new ModelResourceLocation(Armory.MODID + ":" + unlocalizedName, "normal"));
	}
	
	public static void preInit() {
		GameRegistry.registerTileEntity(ForgeTileEntity.class, Armory.MODID + "_" + unlocalizedName);
	}
	
	public Forge() {
		super(Material.ground);
		this.blockHardness = 200;
		this.blockResistance = 45;
		this.setStepSound(Block.soundTypeStone);
		this.setUnlocalizedName(Armory.MODID + "_" + unlocalizedName);
        this.setCreativeTab(Armory.creativeTab);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new ForgeTileEntity();
	}
	
	/**
	 * Sets the forge in the given location to consider the brazier in the given
	 * direction as it's heat source. If <i>relativeDirection</i> is null,
	 * sets the forge to no longer be receiving heat from any brazier.
	 * @param world
	 * @param pos
	 * @param relativeDirection direction <strong> from the forge</strong> to the brazier
	 */
	public void setBrazier(World world, BlockPos pos, EnumFacing relativeDirection) {
		TileEntity entity = world.getTileEntity(pos);
		if (entity != null && entity instanceof ForgeTileEntity) {
			ForgeTileEntity et = (ForgeTileEntity) entity;
			
			TileEntity other = world.getTileEntity(pos.offset(relativeDirection));
			if (other != null && other instanceof BrazierTileEntity) {
				//if this is a valid forge location and the given direction points
				//to a valid brazier tile entity
				et.brazierLocation = relativeDirection;
			}
			
		}
	}
	
	public static class ForgeTileEntity extends TileEntity implements ITickable {
		
		protected ItemStack input;
		
		protected EnumFacing brazierLocation;
		
		protected List<ItemStack> meltedItems;
		
		/**
		 * Item that's been taken from input slot, but is still being 'melted'
		 */
		protected ItemStack currentMeltingItem;
		
		/**
		 * Ticks left till currentMeltingItem is finished and can be added to melted items
		 */
		protected int meltingTime;
		
		public ForgeTileEntity() {
			this.input = null;
			this.brazierLocation = null;
			meltedItems = new LinkedList<ItemStack>();
			currentMeltingItem = null;
		}
		
		
		@Override
		public void writeToNBT(NBTTagCompound tag) {
			if (input != null) {
				NBTTagCompound itemTag = new NBTTagCompound();
				input.writeToNBT(itemTag);
				tag.setTag("input", itemTag);
			}

			if (currentMeltingItem != null) {
				NBTTagCompound itemTag = new NBTTagCompound();
				currentMeltingItem.writeToNBT(itemTag);
				tag.setTag("current", itemTag);
			}

			if (meltedItems != null && meltedItems.size() > 0) {
				NBTTagList tagList = new NBTTagList();
				for (ItemStack sub : meltedItems) {
					NBTTagCompound itemTag = new NBTTagCompound();
					sub.writeToNBT(itemTag);
					tagList.appendTag(itemTag);
				}
				tag.setTag("melted", tagList);
			}
			
			if (brazierLocation != null)
				tag.setByte("facing", (byte) brazierLocation.getHorizontalIndex());
			
			tag.setInteger("meltingTime", meltingTime);
			
			super.writeToNBT(tag);
		}
		
		@Override
		public void readFromNBT(NBTTagCompound tag) {
			
			if (tag.hasKey("input", NBT.TAG_COMPOUND)) {
				input = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("input"));
			} else
				this.input = null;
			
			if (tag.hasKey("current")) {
				this.currentMeltingItem = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("current"));
			} else
				this.currentMeltingItem = null;
			
			if (tag.hasKey("meltedItems", NBT.TAG_LIST)) {
				meltedItems = new LinkedList<ItemStack>();
				
				NBTTagList list = tag.getTagList("meltedItems", NBT.TAG_COMPOUND);
				NBTTagCompound sub;
				while (!list.hasNoTags()) {
					sub = (NBTTagCompound) list.removeTag(0);
					meltedItems.add(ItemStack.loadItemStackFromNBT(sub));
				}
			} else {
				meltedItems = null;
			}

			if (tag.hasKey("facing", NBT.TAG_BYTE)) {
				byte index = tag.getByte("facing");
				this.brazierLocation = EnumFacing.getHorizontal(index);
			} else
				this.brazierLocation = null;
			
			meltingTime = tag.getInteger("meltingTime");
			
			super.readFromNBT(tag);
		}
		
		@Override
		public Packet<INetHandlerPlayClient> getDescriptionPacket() {
			NBTTagCompound tagCompound = new NBTTagCompound();
		    writeToNBT(tagCompound);
		    //return new Packet132TileEntityData(xCoord, yCoord, zCoord, 1, tagCompound);
		    return new S35PacketUpdateTileEntity(this.pos, 3, tagCompound);
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
//			System.out.println("refresh [" + newState.getBlock() + "]? -> " + !(newState.getBlock() instanceof Brazier));
//			return !(newState.getBlock() instanceof Brazier);
			//return true;
		}
		
		/**
		 * Returns whether this forge can accept and consume the given itemstack
		 * @param item
		 * @return
		 */
		public boolean canAccept(ItemStack item) {
			return (ForgeManager.instance().getMetalRecord(item) != null);
			
		}

		protected void joinBrazier(EnumFacing direction) {
			this.brazierLocation = direction;
		}
		
		protected void breakFromForge() {
			this.brazierLocation = null;
		}
		
		@Override
		public void update() {
			if (meltingTime > 0) {
				meltingTime --;
				
				if (meltingTime == 0) {
					//just finished melting
					addToMelted(currentMeltingItem);
					currentMeltingItem = null;
				}
			}
			
			if (currentMeltingItem == null && input != null) {
				//ready to accept item, if it has enough heat
				if (brazierLocation != null) {
					int heat = getHeat();
					if (heat > -1) {
						
						//lookup metal record for input
						MetalRecord record = ForgeManager.instance().getMetalRecord(input);
						if (record == null)
							return; //don't accept, since it doens't appear to be
								    //valid input
						
						if (record.getRequiredHeat() <= heat) {
							//process the input
							consumeInput(record.getBurnTime());
						}
						
					}
					
				} //else no heat. Cannot accept new metal
			}
			
		}
		
		/**
		 * Gets heat of current setup. Returns -1 if no heat info is available.
		 * @return
		 */
		private int getHeat() {
			if (brazierLocation == null)
				return -1;
			
			TileEntity ent = getWorld().getTileEntity(pos.offset(brazierLocation));
			if (ent != null && ent instanceof BrazierTileEntity) {
				BrazierTileEntity te = (BrazierTileEntity) ent;
				return te.getHeat();
			}
			
			return -1;
		}
		
		/**
		 * Consumes an item from the input. Does not check that it is possible
		 * to be consumed (heat too low, no metal record, etc). That's up
		 * to you. This is a private method, after all
		 */
		private void consumeInput(int burnTime) {
			currentMeltingItem = input.splitStack(1);
			
			if (input.stackSize <= 0)
				input = null;
			
			meltingTime = burnTime;
		}
		
		protected void addToMelted(ItemStack melted) {
			this.meltedItems.add(melted);
		}
		
		protected void clearMelted() {
			meltedItems.clear();
		}
		
		protected ItemStack gatherMetals() {
			//get all melted items and put into held metal
			if (meltedItems.isEmpty())
				return null;
			
			ItemStack stack = ((HeldMetal) MiscItems.getItem(Items.HELD_METAL))
					.createStack(meltedItems, getHeat());
			
			/**
			 * TODO
			 * make certain that the melted items are all of one item type (and
			 * that there is a material for that item type for shaping) OR
			 * it's an alloy. If not, produce scrap
			 */
			
			return stack;
		}
		
	}
}
