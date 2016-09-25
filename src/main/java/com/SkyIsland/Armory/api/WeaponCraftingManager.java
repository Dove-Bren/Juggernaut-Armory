package com.SkyIsland.Armory.api;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.SkyIsland.Armory.items.weapons.components.WeaponComponent;

public class WeaponCraftingManager {

	public static class WeaponRecipe {
		
		private List<WeaponComponent> requiredParts;
		private IWeaponTemplate template;
		
		/**
		 * Creates a weapon recipe on the given list of components.
		 * @param requiredComponents
		 * @param template
		 */
		public WeaponRecipe(List<WeaponComponent> requiredComponents,
				IWeaponTemplate template) {
			this.requiredParts = requiredComponents;
			this.template = template;
		}
		
		/**
		 * Checks to see if the given parts match (so far) this recipe.
		 * If checking for full match, will only return true if the entire
		 * recipe is matched and there is no extra. If false, just checks
		 * to see if what we have so far (parts) is inline with this recipe.
		 * @param parts The current parts.
		 * @param full whether to see if it's an exact match (parts == recipe components)
		 * or if the given parts are just headed in the direction of this recipe (blade,hilt > blade,hilt,pommel)
		 * @return
		 */
		public boolean match(List<WeaponComponent> parts, boolean full) {
			if (parts.isEmpty())
				return !full;
			
			Iterator<WeaponComponent> itIn = parts.iterator(),
			itReq = requiredParts.iterator();
			
			while (itIn.hasNext()) {
				if (!itReq.hasNext()) //ran out of required materials, but still have some in input
					return false;
				
				if (!itIn.next().equals(itReq.next()))
					return false;
			}
			
			if (itReq.hasNext()) {
				//still have some in the required list
				return (!full);
			}
			
			return true; //perfect match. works on full too
		}
		
		public IWeaponTemplate getTemplate() {
			return template;
		}
		
	}
	
	public static WeaponCraftingManager instance;
	
	private List<WeaponRecipe> recipes;
	
	public static void init() {
		instance = new WeaponCraftingManager();
	}
	
	public WeaponCraftingManager() {
		recipes = new LinkedList<WeaponRecipe>();
	}
	
	public void registerRecipe(WeaponRecipe recipe) {
		recipes.add(recipe);
	}
	
	/**
	 * Checks to see if the given list of parts matches (potentially in full)
	 * any currently registered recipes.
	 * @param parts The list of parts to check
	 * @param full Whether the parts must match a recipe in full. If false,
	 * will match a recipe that begins with the list of parts.
	 * @return
	 */
	public boolean isValid(List<WeaponComponent> parts, boolean full) {
		if (recipes.isEmpty())
			return false;
		
		for (WeaponRecipe recipe : recipes)
		if (recipe.match(parts, full))
			return true;
		
		return false;
	}
	
	/**
	 * Returns the first recipe that matches (exactly) the given parts. If
	 * no recipe is found to match exactly, returns null.
	 * @param parts
	 * @return The first recipe for which <em>isValid(parts, true)</em>
	 * returns true.
	 */
	public WeaponRecipe getMatch(List<WeaponComponent> parts) {
		if (recipes.isEmpty())
			return null;
		
		for (WeaponRecipe recipe : recipes)
		if (recipe.match(parts, true))
			return recipe;
		
		return null;
	}
	
}
