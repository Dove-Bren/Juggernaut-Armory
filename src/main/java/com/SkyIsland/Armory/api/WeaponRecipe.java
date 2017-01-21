package com.SkyIsland.Armory.api;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.SkyIsland.Armory.items.weapons.components.WeaponComponent;

import net.minecraft.item.ItemStack;

public class WeaponRecipe {
	
	public static class RequiredPiece {
		
		public static enum Result {
			REJECT,
			ACCEPT,
			PASS; //means an optional didn't match but said don't fail
		}
		
		protected WeaponComponent component;
		
		public RequiredPiece(WeaponComponent component) {
			this.component = component;
		}
		
		
		protected Result accepts(WeaponRecipe caller, ItemStack comp) {
			if (component.matchesComponent(comp)) {
				return Result.ACCEPT;
			}
			
			return Result.REJECT;
		}
		
	}
	
	public static class OptionalPiece extends RequiredPiece {

		public OptionalPiece(WeaponComponent component) {
			super(component);
		}
		
		@Override
		protected Result accepts(WeaponRecipe caller, ItemStack comp) {
			if (super.accepts(caller, comp) == Result.ACCEPT) {
				return Result.ACCEPT;
			}
			
			return Result.PASS;
		}
		
	}
	

	private List<RequiredPiece> requiredParts;
	private IWeaponTemplate template;
	
	/**
	 * Creates a weapon recipe on the given list of components.
	 * Optional components are required to be listed at the end of
	 * the provided list. The number of optional parts tells the recipe
	 * how many matches are required before being complete. In
	 * addition, optional 
	 * @param requiredComponents
	 * @param template
	 */
	public WeaponRecipe(List<RequiredPiece> requiredComponents,
			IWeaponTemplate template) {
		this.requiredParts = requiredComponents;
		this.template = template;
	}
	
	/**
	 * Checks to see if the given parts match (so far) this recipe.
	 * If checking for full match, will only return true if the entire
	 * recipe is matched and there is no extra. If false, just checks
	 * to see if what we have so far (parts) is inline with this recipe.<br />
	 * This method of matching includes Required and Optional parts.
	 * Regardless of if the parts or required or optional, they must
	 * be laid out in the right order. 
	 * @param parts The current parts.
	 * @param full whether to see if it's an exact match (parts == recipe components)
	 * or if the given parts are just headed in the direction of this recipe (blade,hilt > blade,hilt,pommel)
	 * @return
	 */
	public boolean match(List<ItemStack> parts, boolean full) {
		if (parts.isEmpty())
			return !full;
		
		ListIterator<ItemStack> itIn = parts.listIterator();
		Iterator<RequiredPiece> itReq = requiredParts.iterator();
		
		while (itIn.hasNext()) {
			if (!itReq.hasNext()) //ran out of required materials, but still have some in input
				return false;
			
			//if (!itIn.next().equals(itReq.next()))
			RequiredPiece.Result res = itReq.next().accepts(this, itIn.next());
			if (res == RequiredPiece.Result.REJECT)
				return false;
			if (res == RequiredPiece.Result.PASS)
				itIn.previous();
			
			//Pass and Accept move on
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
