package com.SkyIsland.Armory.items.weapons.components;

import java.util.Map;

import com.SkyIsland.Armory.api.ForgeManager;
import com.SkyIsland.Armory.api.ForgeManager.ForgeRecipe;
import com.SkyIsland.Armory.api.WeaponComponentRecipe;
import com.SkyIsland.Armory.mechanics.DamageType;

/**
 * Nice easy enum to create our weapon components.
 * This enum is just a convenience. Run-time additions can be added by
 * doing what the constructor does here; create a new
 * {@link WeaponComponent}. Boom.
 * @author Skyler
 *
 */
public enum EnumWeaponComponents {
	HANDLE_SHORT("Short Handle", "handle_short", 0.2f, ForgeRecipe.drawMap(new String[]{
			"          ",
			"          ",
			"          ",
			"          ",
			"          ",
			"          ",
			"     ..   ",
			"    ...   ",
			"    ..    ",
			"   ..     "
		})),
	HANDLE_MEDIUM("Medium Handle", "handle_medium", 0.3f, ForgeRecipe.drawMap(new String[]{
			"          ",
			"          ",
			"    ..    ",
			"    ..    ",
			"    ..    ",
			"     ..   ",
			"     ...  ",
			"      ..  ",
			"          ",
			"          "
		})),
	HANDLE_LARGE("Large Handle", "handle_large", 0.4f, ForgeRecipe.drawMap(new String[]{
			"          ",
			"  ......  ",
			"   ....   ",
			"    ..    ",
			"   ....   ",
			"   ....   ",
			"   ....   ",
			"   ....   ",
			"  ......  ",
			"  ......  "
		})),
	GUARD_SMALL("Small Guard", "guard_small", 0.15f, ForgeRecipe.drawMap(new String[]{
			"          ",
			"    .     ",
			"   ..     ",
			"   .      ",
			"  .. ..   ",
			"     .    ",
			"    ..    ",
			"    .     ",
			"          ",
			"          "
		})),
	GUARD_LARGE("Large Guard", "guard_large", 0.3f, ForgeRecipe.drawMap(new String[]{
			"          ",
			"     ..   ",
			"     ...  ",
			"       .. ",
			"       .. ",
			"      ..  ",
			"      ..  ",
			"   ....   ",
			"    ..    ",
			"          "
		})),
	GUARD_WHOLE("Whole Guard", "guard_whole", 0.25f, ForgeRecipe.drawMap(new String[]{
			"          ",
			"          ",
			"    ..    ",
			"   .....  ",
			"  ..  ... ",
			" ..    .. ",
			"  ..      ",
			"  ..      ",
			"   .      ",
			"          "
		})),
	POMMEL_TAIL("Tail Pommel", "pommel_tail", 0.0f, ForgeRecipe.drawMap(new String[]{
			"   .      ",
			"  ..      ",
			"    ...   ",
			"    . .   ",
			"    ...   ",
			"          ",
			"          ",
			"          ",
			"          ",
			"          "
		})),
	POMMEL_MID("Mid Pommel", "pommel_mid", 0.0f, ForgeRecipe.drawMap(new String[]{
			"          ",
			"          ",
			"       .  ",
			"    ...   ",
			"    . .   ",
			"    ...   ",
			"   .      ",
			"          ",
			"          ",
			"          "
		})),
	BLADE_SHORT("Short Blade", "blade_short", 0.4f, 0.6f, 0.4f, 0.0f, ForgeRecipe.drawMap(new String[]{
			"          ",
			"          ",
			"    .     ",
			"    ..    ",
			"    ..    ",
			"    ..    ",
			"     .    ",
			"          ",
			"          ",
			"          "
		})),
	BLADE_MEDIUM("Medium Blade", "blade_medium", 0.45f, 0.9f, 0.1f, 0.0f, ForgeRecipe.drawMap(new String[]{
			"          ",
			"   ...    ",
			"   ...    ",
			"   ...    ",
			"   ...    ",
			"    ...   ",
			"    ...   ",
			"    ...   ",
			"          ",
			"          "
		})),
	BLADE_LARGE("Large Bladed", "blade_large", 0.35f, 0.8f, 0.0f, 0.2f, ForgeRecipe.drawMap(new String[]{
			"     ...  ",
			"   .....  ",
			"   .....  ",
			"   .....  ",
			"   .....  ",
			"   .....  ",
			"   .....  ",
			"  ....    ",
			"  ...     ",
			" ...      "
		})),
	BLADE_CURVED("Curved Blade", "blade_curved", 0.55f, 1.0f, 0.0f, 0.0f, ForgeRecipe.drawMap(new String[]{
			"       ...",
			"     ...  ",
			"    ...   ",
			"   ...    ",
			"   ...    ",
			"    ...   ",
			"     ..   ",
			"      ..  ",
			"       .. ",
			"          "
		})),
	AXE_SINGLE("Single-Headed Axe", "axe_single", 0.3f, 0.6f, 0.0f, 0.4f, ForgeRecipe.drawMap(new String[]{
			"          ",
			"          ",
			"   ....   ",
			"    ....  ",
			"    ....  ",
			"     .... ",
			"    ....  ",
			"   ....   ",
			"          ",
			"          "
		})),
	AXE_DOUBLE("Double-Headed Axe", "axe_double", 0.25f, 0.7f, 0.0f, 0.3f, ForgeRecipe.drawMap(new String[]{
			"          ",
			"   ....   ",
			"  ...     ",
			" ..       ",
			"....  ....",
			"     ...  ",
			"    ..    ",
			"   ....   ",
			"          ",
			"          "
		}));
	
	private WeaponComponent component;
		
	private EnumWeaponComponents(String title, String suffix, float durabilityRate, boolean[][] metalMap) {
		component = new WeaponComponent(suffix, durabilityRate);
		registerWithMap(title, metalMap);
	}
	
	private EnumWeaponComponents(String title, String suffix, Map<DamageType, Float> map, float durabilityRate, boolean[][] metalMap) {
		component = new WeaponComponent(suffix, map, durabilityRate);
		registerWithMap(title, metalMap);
	}
	
	private EnumWeaponComponents(String title, String suffix, float durabilityRate, float slash, float pierce, float crush, boolean[][] metalMap) {
		Map<DamageType, Float> map = DamageType.freshMap();
		
		map.put(DamageType.SLASH, slash);
		map.put(DamageType.PIERCE, pierce);
		map.put(DamageType.CRUSH, crush);
		
		component = new WeaponComponent(suffix, map, durabilityRate);
		registerWithMap(title, metalMap);
	}
	
	private void registerWithMap(String title, boolean[][] metalMap) {
		WeaponComponentRecipe recipe = new WeaponComponentRecipe(this.component);
		ForgeManager.instance().registerForgeRecipe(new ForgeRecipe(
				title, -5, metalMap, recipe
				));
		//TODO get rid of static -5 difficulty.
	}
	
	public WeaponComponent getComponent() {
		return component;
	}
}
