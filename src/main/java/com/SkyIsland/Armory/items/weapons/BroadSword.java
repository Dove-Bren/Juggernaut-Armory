package com.SkyIsland.Armory.items.weapons;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.SkyIsland.Armory.api.IWeaponTemplate;
import com.SkyIsland.Armory.api.WeaponCraftingManager;
import com.SkyIsland.Armory.api.WeaponCraftingManager.WeaponRecipe;
import com.SkyIsland.Armory.items.common.NestedSlotInventory;
import com.SkyIsland.Armory.items.weapons.components.EnumWeaponComponents;
import com.SkyIsland.Armory.items.weapons.components.WeaponComponent;
import com.SkyIsland.Armory.mechanics.DamageType;
import com.google.common.collect.Lists;

import net.minecraft.item.ItemStack;

public class BroadSword extends ASword {

	public static enum Slot {
		BLADE,
		GUARD,
		HANDLE,
		POMMEL;
	}
	
	public BroadSword(String unlocalizedName) {
		super(unlocalizedName);
		
		//IWeaponTemplate template = 
		
		final BroadSword sword = this;
		WeaponCraftingManager.instance.registerRecipe(
				new WeaponRecipe(
						Lists.newArrayList(EnumWeaponComponents.BLADE_MEDIUM.getComponent(),
								EnumWeaponComponents.GUARD_LARGE.getComponent(),
								EnumWeaponComponents.HANDLE_MEDIUM.getComponent(),
								EnumWeaponComponents.POMMEL_TAIL.getComponent()),
						new IWeaponTemplate() {
							public ItemStack construct(List<ItemStack> components) {
								return sword.construct(components.get(0), components.get(1), components.get(2), components.get(3));
							}
						}
				));
	}
	
	public ItemStack construct(ItemStack blade, ItemStack guard,
			ItemStack handle, ItemStack pommel) {
		//calculate damage map
		//Map<DamageType, Float> damageMap = DamageType.freshMap();
		Map<DamageType, Float> damageMap = ((WeaponComponent) blade.getItem())
				.getDamageValues(blade);
		
		//TODO
//		for (ItemStack stack : new ItemStack[]{blade, guard, handle, pommel}) {
//			Map<DamageType, Float> map = ((WeaponComponent) stack.getItem()).getDamageValues(blade);
//			for (DamageType key : map.keySet()) {
//				damageMap.put(key, damageMap.get(key) + map.get(key));
//			}
//		}
		
		//calculate durability
		
		float durability = 0.0f;
		for (ItemStack stack : new ItemStack[]{blade, guard, handle, pommel}) {
			durability += ((WeaponComponent) stack.getItem()).getFactoredDurability(stack);
		}
		
		//create itemstack
		ItemStack construct = Weapon.constructWeaponFrom(this, "Broadsword",
				damageMap, Math.round(durability));
		
		//nest items inside of the construct, with a makeWrapper call
		NestedSlotInventory<Slot> comps = makeWrapper(construct);
		comps.setInventorySlotContents(Slot.BLADE, blade);
		comps.setInventorySlotContents(Slot.GUARD, guard);
		comps.setInventorySlotContents(Slot.HANDLE, handle);
		comps.setInventorySlotContents(Slot.POMMEL, pommel);
		
		return construct;
	}
	
	public static NestedSlotInventory<Slot> makeWrapper(ItemStack base) {
		return new NestedSlotInventory<Slot>(Slot.class, base);
	}

	@Override
	public Collection<ItemStack> getWeaponComponents(ItemStack weapon) {
		NestedSlotInventory<Slot> components = makeWrapper(weapon);
		List<ItemStack> parts = new LinkedList<ItemStack>();
		
		for (Slot slot : Slot.values()) {
			ItemStack item = components.getStackInSlot(slot);
			if (item != null)
				parts.add(item);
		}
		
		return parts;
	}

}
