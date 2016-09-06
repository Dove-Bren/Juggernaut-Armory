package com.SkyIsland.Armory.items.proxy;

import com.SkyIsland.Armory.items.proxy.network.TongsCoolMessage;
import com.SkyIsland.Armory.items.tools.Tongs;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class CommonTongProxy  {
	
	
	
	public void updateTongs(Tongs inst, ItemStack tongs, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		;
	}
	
	protected static SimpleNetworkWrapper channel;
	
	private static int discriminator = 0;
	
	private static final String CHANNEL_NAME = "tongu_channel";
	
	public CommonTongProxy() {
		
		channel = NetworkRegistry.INSTANCE.newSimpleChannel(CHANNEL_NAME);
		
		//channel.registerMessage(RequestServerConfigMessage.Handler.class, RequestServerConfigMessage.class, discriminator++, Side.SERVER);
		//channel.registerMessage(ResponseServerConfigMessage.Handler.class, ResponseServerConfigMessage.class, discriminator++, Side.CLIENT);
		channel.registerMessage(TongsCoolMessage.Handler.class, TongsCoolMessage.class, discriminator++, Side.CLIENT);
	}
	
}
