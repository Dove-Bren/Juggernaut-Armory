package com.SkyIsland.Armory.items.proxy.network;

import java.util.UUID;

import com.SkyIsland.Armory.items.ToolItems;
import com.SkyIsland.Armory.items.ToolItems.Tools;
import com.SkyIsland.Armory.items.tools.Tongs;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Lets a client know that the metal in their tongs has cooled
 * @author Skyler
 *
 */
public class TongsCoolMessage implements IMessage {

	public static class Handler implements IMessageHandler<TongsCoolMessage, IMessage> {

		@Override
		public IMessage onMessage(TongsCoolMessage message, MessageContext ctx) {
			System.out.println("Got tongs cool message!");
			System.out.println("uuid: " + message.ownerID);
			System.out.println("id: " + message.id);
			
			if (Minecraft.getMinecraft().thePlayer.getUniqueID()
					.equals(message.ownerID)) {
				
				Tongs inst = (Tongs) ToolItems.getItem(Tools.TONGS);
				
				for (ItemStack stack : Minecraft.getMinecraft().thePlayer.getInventory()) {
					if (stack != null && stack.getItem() instanceof Tongs) {
						if (inst.getID(stack) == message.id) {
							inst.setHeldItem(stack, null);
							return null;
						}
					}
				}
			}
			
			return null;
		}
		
	}
	
	protected UUID ownerID;
	
	protected int id;
	
	public TongsCoolMessage() {
		ownerID = null;
		id = -1;
	}
	
	public TongsCoolMessage(UUID ownerId, int tongId) {
		this();
		
		this.ownerID = ownerId;
		this.id = tongId;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound nbt = ByteBufUtils.readTag(buf);
		
		ownerID = UUID.fromString(nbt.getString("uuid"));
		id = nbt.getInteger("id");
	}

	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound nbt = new NBTTagCompound();
		
		nbt.setString("uuid", ownerID.toString());
		nbt.setInteger("id", id);
		
		ByteBufUtils.writeTag(buf, nbt);
	}

}
