package com.SkyIsland.Armory.listeners;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.lwjgl.input.Keyboard;

import com.SkyIsland.Armory.chat.ChatFormat;
import com.SkyIsland.Armory.config.ModConfig;
import com.SkyIsland.Armory.items.weapons.Weapon;
import com.SkyIsland.Armory.mechanics.ArmorUtils;
import com.SkyIsland.Armory.mechanics.DamageType;
import com.SkyIsland.Armory.mechanics.WeaponUtils;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.StatCollector;
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
		if (stack != null) {
			if (stack.getItem() instanceof ItemArmor) {
		
			
				Map<DamageType, Float> protectionMap = ArmorUtils.getValues(stack);
	
				List<String> hiddenList = new LinkedList<String>();
				for (DamageType type : DamageType.values())
				if (ModConfig.config.getShowZeros() || protectionMap.get(type) != 0.0f)
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
			} else if (stack.getItem() instanceof Weapon 
					|| stack.getItem() instanceof ItemSword
					|| stack.getAttributeModifiers().containsKey(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName())) {
				//weapon, from what it looks
				
				//remove attack damage line if it exists
				if (!event.toolTip.isEmpty()) {
				Iterator<String> it = event.toolTip.iterator();
				while (it.hasNext()) {
					String line = it.next();
					if (line.contains(
							StatCollector.translateToLocal("attribute.name." + (String) SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName())
							)) {
						it.remove();
						break;
					}
				}
				}
				
				float damageBoost = getDamageModifier(stack);
				
				Map<DamageType, Float> damageMap = WeaponUtils.getValues(stack, damageBoost);
				
				List<String> hiddenList = new LinkedList<String>();
				for (DamageType type : DamageType.values())
				if (type.isVisible())
				if (ModConfig.config.getShowZeros() || damageMap.get(type) != 0.0f) {
					if (type.alwaysShow())
						event.toolTip.add(formatDamage(type, damageMap.get(type)));
					else
						hiddenList.add(formatDamage(type, damageMap.get(type)));
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
	}
	
	private static float getDamageModifier(ItemStack stack) {
		if (stack.getItem() instanceof ItemSword)
			return ((ItemSword) stack.getItem()).getDamageVsEntity() + 4.0f; //4.0 constant from vanilla mechanics.
		
		float total = 0.0f;
		Collection<AttributeModifier> modifiers = stack.getAttributeModifiers().get(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName());
		for (AttributeModifier mod : modifiers)
		if (mod.getOperation() == 0)
			total += mod.getAmount();
		
		return total;
	}
	
	private static String formatProtection(DamageType type, float prot) {
		return ChatFormat.ARMOR.wrap(
				type.toString() + ": " + String.format("%.2f", prot));
		//instead of prot, maybe do actual reduction? (based on client config?)
		//TODO
	}
	
	private static String formatDamage(DamageType type, float damage) {
		return ChatFormat.DAMAGE.wrap(
				type.toString() + ": " + String.format("%.2f", damage));
		//instead of prot, maybe do actual reduction? (based on client config?)
		//TODO
	}
}
