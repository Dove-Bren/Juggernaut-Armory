package com.SkyIsland.Armory.chat;

import net.minecraft.util.EnumChatFormatting;

/**
 * Stores mod chat formatting codes
 * @author Skyler
 *
 */
public enum ChatFormat {
	
	ARMOR(EnumChatFormatting.BLUE.toString()),
	DAMAGE(EnumChatFormatting.BLUE.toString()),
	DAMAGE_TYPE(EnumChatFormatting.RESET.toString()),
	COMPONENTS(EnumChatFormatting.DARK_GRAY.toString()),
	LUSTER(EnumChatFormatting.GOLD.toString()),
	HEAT_BAD(EnumChatFormatting.RED.toString()),
	HEAT_GOOD(EnumChatFormatting.GREEN.toString());
	
	private String prefix;
	
	private ChatFormat(String prefix) {
		this.prefix = prefix;
	}
	
	@Override
	public String toString() {
		return prefix;
	}
	
	/**
	 * Wraps the given string in formatting codes to paint it with the
	 * proper style, and insert a reset afterwards
	 * @param inside
	 * @return
	 */
	public String wrap(String inside) {
		return toString() + inside + EnumChatFormatting.RESET;
	}
	
}
