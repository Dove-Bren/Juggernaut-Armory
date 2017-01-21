package com.SkyIsland.Armory.api;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import com.SkyIsland.Armory.items.weapons.components.WeaponComponent;
import com.google.common.collect.Maps;

import net.minecraft.item.ItemStack;

public class WeaponRecipe {
	
	private Map<WeaponComponent, Restriction> partMap;
	private IWeaponTemplate template;
	
	public static enum Restriction {
		REQUIRED,
		OPTIONAL,
		DISALLOWED
	}
	
	/**
	 * Creates a weapon recipe on the given list of components.
	 * Optional components are required to be listed at the end of
	 * the provided list. The number of optional parts tells the recipe
	 * how many matches are required before being complete. In
	 * addition, optional 
	 * @param requiredComponents
	 * @param template
	 */
	public WeaponRecipe(Map<WeaponComponent, Restriction> requiredComponents,
			IWeaponTemplate template) {
		this.partMap = requiredComponents;
		this.template = template;
	}
	
	/**
	 * Checks to see if the given parts match (so far) this recipe.
	 * If checking for full match, will only return true if the entire
	 * recipe is matched and there is no extra. If false, just checks
	 * to see if what we have so far (parts) is inline with this recipe.<br />
	 * This method does not check for duplicates or make any distintion
	 * between a recipe with duplicates and one that doesn't.
	 * @param parts The current parts.
	 * @param full whether to see if it's an exact match (parts == recipe components)
	 * or if the given parts are just headed in the direction of this recipe (blade,hilt > blade,hilt,pommel)
	 * @return
	 */
	public boolean match(List<ItemStack> parts, boolean full) {
		if (parts.isEmpty())
			return !full;
		
		ListIterator<ItemStack> itIn = parts.listIterator();
		Map<WeaponComponent, Boolean> seenMap = new HashMap<WeaponComponent, Boolean>();
		WeaponComponent comp;
		ItemStack item;
				
		while (itIn.hasNext()) {
			item = itIn.next();
			if (item != null && item.getItem() instanceof WeaponComponent) {
				comp = (WeaponComponent) item.getItem();
				if (!partMap.containsKey(comp) || partMap.get(comp) == Restriction.DISALLOWED)
					return false;
				
				//else continue. 
				seenMap.put(comp, true);
				
			} else {
				return false; //invalid item
			}
		}
		
		if (full) {
			//check for no REQUIRE'd that didn't see their part
			for (Entry<WeaponComponent, Restriction> entry : partMap.entrySet()) {
				if (entry.getValue() == Restriction.REQUIRED) {
					Boolean b = seenMap.get(entry.getKey());
					if (b == null || !b)
						return false;
				}
			}
		}
		
		return true; //perfect match. works on full too
	}
	
	/**
	 * Checks to see if the parts in the given map of components match the parts
	 * required for this recipe.
	 * @param map the bit map to check against
	 * @param full whether to see if it's an exact match (parts == recipe components)
	 * or if the given parts are just headed in the direction of this recipe (blade,hilt > blade,hilt,pommel)
	 * @return
	 */
	public boolean match(Map<WeaponComponent, Boolean> map, boolean full) {
		Map<WeaponComponent, Boolean> scratch = Maps.newHashMap(map);
		for (Entry<WeaponComponent, Restriction> entry : partMap.entrySet()) {
			//step through our requirements, and mark scratch as false
			//after, check for any true (unchecked) entries. Fail on those
			
			if (scratch.containsKey(entry.getKey()) && scratch.get(entry.getKey())) {
				//has this key, and it's TRUE. check it works for us
				if (entry.getValue() == null || entry.getValue() == Restriction.DISALLOWED)
					return false; //auto fail. Not allowed here
				
				//our entry says required or optional. Their entry says it
				//exists. So, turn it to false and continue
				scratch.put(entry.getKey(), false);
			} else {
				//we have key, but they don't (or it's false)
				if (entry.getValue() == null || entry.getValue() == Restriction.OPTIONAL) {
					//it's empty on our side, or optional
					//so it's all good still
					continue;
				}
				
				//They are missing a key. Fail is set to full
				if (full)
					return false;
					
				//else continue
			}
		}
		
		//made it through the map. Now check for any trye keys in scratch
		//(means items we didn't visit)
		for (Boolean b : scratch.values())
		if (b)
			return false;
		
		return true;
	}
	
	public IWeaponTemplate getTemplate() {
		return template;
	}
	
	/**
	 * Creates a recipe on the given collection of components and the template.
	 * All items in the collection are considered REQUIRED.<br />
	 * To set optional components, construct the map yourself and use
	 * the {@link #WeaponRecipe(Map, IWeaponTemplate)} constructor.
	 * @param template
	 * @param components
	 * @return
	 */
	public static WeaponRecipe assembleFromParts(IWeaponTemplate template, Collection<WeaponComponent> components) {
		
		Map<WeaponComponent, Restriction> map = new HashMap<WeaponComponent, Restriction>();

		if (components != null && !components.isEmpty())
		for (WeaponComponent comp : components) {
			map.put(comp, Restriction.REQUIRED);
		}
		
		WeaponRecipe recipe = new WeaponRecipe(map , template);
		
		return recipe;
	}
	
}
