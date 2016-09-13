package com.SkyIsland.Armory.gui.table;

import com.SkyIsland.Armory.items.HeldMetal;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Client gui has clicked an icon for cutting/deleting. Passed client tests.
 * This message responds with a reset if an invalid move is detected
 * @author Skyler
 *
 */
public class ForgeCutMessage implements IMessage {

	public static class Handler implements IMessageHandler<ForgeCutMessage, IMessage> {

		@Override
		public IMessage onMessage(ForgeCutMessage message, MessageContext ctx) {
			if (!CuttingTableGui.doCut(message.guiId, message.cellX, message.cellY)) {
				//bad pound. send a reset
				boolean[][] map = TableGui.fetchMap(message.guiId);
				int[] ints;
				if (map != null) {
					ints = HeldMetal.map2Int(map);
				} else {
					ints = new int[4];
				}
				
				return new ForgeResetMessage(message.guiId,
						ints[0],
						ints[1],
						ints[2],
						ints[3]
						);
				
			}
			
			return null;
		}
		
	}
	
	protected int guiId;
	
	protected int cellX;
	
	protected int cellY;
	
	public ForgeCutMessage(int guiId, int x, int y) {
		this.guiId = guiId;
		this.cellX = x;
		this.cellY = y;
	}
	
	public ForgeCutMessage() {
		;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		guiId = buf.readInt();
		cellX = buf.readInt();
		cellY = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(guiId);
		buf.writeInt(cellX);
		buf.writeInt(cellY);
	}

}
