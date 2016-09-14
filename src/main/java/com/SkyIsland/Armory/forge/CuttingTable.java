package com.SkyIsland.Armory.forge;

import com.SkyIsland.Armory.Armory;
import com.SkyIsland.Armory.gui.table.CuttingTableGui;
import com.SkyIsland.Armory.items.tools.Tongs;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CuttingTable extends ForgeAnvil {
	
	public static Block block;
	public static Material material;
	public static final String unlocalizedName = "cutting_table_block";
	
	public void clientInit() {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
		.register(Item.getItemFromBlock(this), 0, new ModelResourceLocation(Armory.MODID + ":" + unlocalizedName, "normal"));
	}
	
	public static void preInit() {
		GameRegistry.registerTileEntity(CuttingTableTileEntity.class, Armory.MODID + "_" + unlocalizedName);
	}
	
	public void init() {
//		GameRegistry.addShapedRecipe(new ItemStack(block),
//				"BBB", "sis", "iii", 'B', Blocks.iron_block, 's', Blocks.iron_bars, 'i', Items.iron_ingot);
//		GameRegistry.addShapelessRecipe(new ItemStack(block),
//				Items.iron_ingot, Blocks.iron_bars, Blocks.iron_bars);
	}
	
	public CuttingTable() {
		super();
		this.blockHardness = 200;
		this.blockResistance = 45;
		this.setStepSound(Block.soundTypeStone);
		this.setUnlocalizedName(Armory.MODID + "_" + unlocalizedName);
        this.setCreativeTab(Armory.creativeTab);
        block = this;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new CuttingTableTileEntity();
	}
	
    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
    	
    	CuttingTableTileEntity entity = (CuttingTableTileEntity) worldIn.getTileEntity(pos);
        if (entity != null) {
        	ItemStack contained = entity.getItem(true);
        	if (contained != null) {
        		EntityItem ent = new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), contained);
        		worldIn.spawnEntityInWorld(ent);
        	}
        	
        }

        super.breakBlock(worldIn, pos, state);
    }
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state,
			EntityPlayer playerIn, EnumFacing side, float hitX,float hitY, float hitZ) {
		
		ItemStack inhand = playerIn.getHeldItem();
		if (inhand == null || !(inhand.getItem() instanceof Tongs))
			return false;
		
		TileEntity ent = worldIn.getTileEntity(pos);
		if (ent == null || !(ent instanceof CuttingTableTileEntity))
			return false;
		
		CuttingTableGui.displayGui(playerIn, (CuttingTableTileEntity) ent);
		
		return true;
	}
	
	public static ItemStack getHeld(World worldIn, BlockPos pos, IBlockState state, boolean take) {
		TileEntity e = worldIn.getTileEntity(pos);
		if (e == null || !(e instanceof CuttingTableTileEntity))
			return null;
		
		CuttingTableTileEntity te = (CuttingTableTileEntity) e;
		return te.getItem(take);
	}
	
	public static class CuttingTableTileEntity extends ForgeAnvil.AnvilTileEntity {
		
		public CuttingTableTileEntity() {
			super();
		}
		
		@Override
		public Packet<INetHandlerPlayClient> getDescriptionPacket() {
			NBTTagCompound tagCompound = new NBTTagCompound();
		    writeToNBT(tagCompound);
		    //return new Packet132TileEntityData(xCoord, yCoord, zCoord, 1, tagCompound);
		    return new S35PacketUpdateTileEntity(this.pos, 5, tagCompound);
		}
	}
}
