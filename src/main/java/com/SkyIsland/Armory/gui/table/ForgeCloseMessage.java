package com.SkyIsland.Armory.gui.table;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Client gui has closed. Close remote.
 * @author Skyler
 *
 */
public class ForgeCloseMessage implements IMessage {

	public static class Handler implements IMessageHandler<ForgeCloseMessage, IMessage> {

		@Override
		public IMessage onMessage(ForgeCloseMessage message, MessageContext ctx) {
			System.out.println("received close message");
			TableGui.closeRemoteGui(message.guiId);
			
			return null;
		}
		
	}
	
	protected int guiId;
	
	public ForgeCloseMessage(int guiId) {
		this.guiId = guiId;
	}
	
	public ForgeCloseMessage() {
		;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		guiId = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(guiId);
	}

}
