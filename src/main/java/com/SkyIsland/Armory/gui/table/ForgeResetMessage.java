package com.SkyIsland.Armory.gui.table;

import com.SkyIsland.Armory.items.HeldMetal;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Server is sending an override map to the client, usually in response to
 * an invalid client-side click
 * @author Skyler
 *
 */
public class ForgeResetMessage implements IMessage {

	public static class Handler implements IMessageHandler<ForgeResetMessage, IMessage> {

		@Override
		public IMessage onMessage(ForgeResetMessage message, MessageContext ctx) {
			System.out.println("received reset message");
			boolean[][] map = HeldMetal.int2Map(message.int1, message.int2, message.int3, message.int4);
			
			TableGui.resetMap(message.guiId, map);
			
			return null;
		}
		
	}
	
	protected int guiId;
	
	protected int int1;
	
	protected int int2;
	
	protected int int3;
	
	protected int int4;
	
	public ForgeResetMessage(int guiId, int int1, int int2, int int3, int int4) {
		this.guiId = guiId;
		this.int1 = int1;
		this.int2 = int2;
		this.int3 = int3;
		this.int4 = int4;
	}
	
	public ForgeResetMessage() {
		;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		guiId = buf.readInt();
		int1 = buf.readInt();
		int2 = buf.readInt();
		int3 = buf.readInt();
		int4 = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(guiId);
		buf.writeInt(int1);
		buf.writeInt(int2);
		buf.writeInt(int3);
		buf.writeInt(int4);
	}

}
