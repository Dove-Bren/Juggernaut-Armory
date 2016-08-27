package com.SkyIsland.Armory.config.network;

import com.SkyIsland.Armory.config.ModConfig;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Message requesting server's config for syncronization
 * @author Skyler
 *
 */
public class ResponseServerConfigMessage implements IMessage {

	public static class Handler implements IMessageHandler<ResponseServerConfigMessage, IMessage> {

		@Override
		public IMessage onMessage(ResponseServerConfigMessage message, MessageContext ctx) {
			//have tag, now read it into local config
			System.out.println(message.tag);
			
			for (ModConfig.Key key : ModConfig.Key.values())
			if (key.isServerBound()) {
				//load up value from nbt tag
				ModConfig.config.updateLocal(key, key.valueFromNBT(message.tag));
			}
			
			return null;
		}
		
	}
	
	protected NBTTagCompound tag;
	
	
	public ResponseServerConfigMessage() {
		tag = new NBTTagCompound();
	}
	
	public ResponseServerConfigMessage(ModConfig config) {
		this();
		
		//pull out server values from base, send them over
		for (ModConfig.Key key : ModConfig.Key.values())
		if (key.isServerBound()) {
			key.saveToNBT(config, tag);
		}
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		tag = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, tag);
	}

}
