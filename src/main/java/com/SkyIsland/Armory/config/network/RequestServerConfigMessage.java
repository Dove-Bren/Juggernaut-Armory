package com.SkyIsland.Armory.config.network;

import com.SkyIsland.Armory.Armory;
import com.SkyIsland.Armory.config.ModConfig;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Message requesting server's config for syncronization
 * @author Skyler
 *
 */
public class RequestServerConfigMessage implements IMessage {

	public static class Handler implements IMessageHandler<RequestServerConfigMessage, ResponseServerConfigMessage> {

		@Override
		public ResponseServerConfigMessage onMessage(RequestServerConfigMessage message, MessageContext ctx) {
			Armory.logger.info("Recieved config request from client");
			return new ResponseServerConfigMessage(ModConfig.config);
		}
		
	}
	
	public RequestServerConfigMessage() {
		;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		;
	}

}
