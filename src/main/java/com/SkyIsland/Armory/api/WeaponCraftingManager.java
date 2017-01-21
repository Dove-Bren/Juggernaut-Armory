package com.SkyIsland.Armory.api;

import java.util.LinkedList;
import java.util.List;

import com.SkyIsland.Armory.items.weapons.components.WeaponComponent;

public class WeaponCraftingManager {
	
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
