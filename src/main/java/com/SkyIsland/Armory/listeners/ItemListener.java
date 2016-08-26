package com.SkyIsland.Armory.listeners;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.lwjgl.input.Keyboard;

import com.SkyIsland.Armory.mechanics.ArmorUtils;
import com.SkyIsland.Armory.mechanics.DamageType;

import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ItemListener {

	public ItemListener() {
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	
	@SubscribeEvent
	public void onItemTooltip(ItemTooltipEvent event){
		ItemStack stack = event.itemStack;
		if (stack != null && stack.getItem() instanceof ItemArmor) {
			
			Map<DamageType, Float> protectionMap = ArmorUtils.getValues(stack);

			List<String> hiddenList = new LinkedList<String>();
			for (DamageType type : DamageType.values())
			if (type.isVisible()) {
				if (type.alwaysShow())
					event.toolTip.add(formatProtection(type, protectionMap.get(type)));
				else
					hiddenList.add(formatProtection(type, protectionMap.get(type)));
			}

			if (!hiddenList.isEmpty()) {
				if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
					for (String item : hiddenList)
						event.toolTip.add(item);
				} else {
					event.toolTip.add("[Hold Shift]");
				}
			}
		}
	}
	
	private static String formatProtection(DamageType type, float prot) {
		return type.toString() + ": "
				+ String.format("%.2f", prot);
		//instead of prot, maybe do actual reduction? (based on client config?)
		//TODO
	}
}
