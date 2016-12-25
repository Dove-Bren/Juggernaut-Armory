# Juggernaut's Armory - Jarmory

Jaggernaut's Armory is a Forge Mod that introduced a much heavier style of damage, armor, and weapons to Minecraft. Armor progression in Vanilla Minecraft is very limited -- a maximum of 20 armor points with a capped damage reduction value. In 1.8 mechanics, this equates to a flat 80% damage reduction, in steps of 4%. While protection enchantments provide further modification and variation, the progression is very linear. In addition, there are no checks and balances to armor (like drawbacks for using diamond against iron or gold). Becaues of this, the player simply wears diamond armor as soon as they can, and have no need to ever consider again what they're wearing. Juggernaut's Armory (_Jarmory_) seeks to remedy this in two ways; split damage into multiple types, and provide much closer customization of armor. 

1. [Damage Types]
2. [Customization]
    1. [Creating Components]
4. [Support For Vanilla]
5. [Interfacing with JArmory (APIs)]
6. [Configuration (Server)]
7. [Configuration (Client)]

### Damage Types
JARMORY splits damage and protection into different damage types. By default, these are the classic _Slashing_, _Crushing_, and _Piercing_ damage. In addition, _Magic_ and _Other_ damages are also defined to directly correspond to the respective damage types from Vanilla. By doing so, damage and protection amounts are allowed to very in more than a single dimension, which allows developers much more freedom when deciding the details of armors and weapons. This added variation enables developers to hand-tune where exactly a piece of armor excels, and where it doesn't. In this way, armor and weapons become better balanced, and nonlinearity introduced to armor and weapon progression. Both armor and weapons use the split damage types.

Each damage type corresponds to different types of weapons and actions. These are:

 * Slashing* - Typical bladed damage. Swords are a good example of where slashing damage comes from. Slashing damage can also be found on axes (to a smaller degree).
 * Crushing* - Damage from bash attacks or explosions. Punching with fists (including monster bashes to a player) are crushing damage. Shovels do pure crushing damage.
 * Piercing* - Attacks that pierce (or stab) do piercing damage. The easiest example is arrows -- whether shot with a bow or stabbed at by hand.
 * Magic - Damage by potions and special damage events. Can also be used by magic mods.
 * Other - Damage that doesn't fit into another category. In other words, _special_ damage, like void damage or suffocation.
  
 > \* - the damage type is one of the 3 major types. This means that default vanilla handling (see section below) automatically places damage into these categories._

Armor protection values and weapon damage values can be viewed like they normally can: simply mouse-over any piece of equipment. The damage values are in the tooltips. Protection/damage values for each type are displayed as per the client's config.

### Customization of Armor (and Weapons)
In Vanilla Minecraft, there are only four equipment slots. Jarmory expands on the customizability of the player by expanding on these, by breaking each of these four equipment pieces into a number of components. Each component acts as a modular addition to the piece of equipment itself, combining its strength with it's fellow components to make a single piece of equipment. The components that make up each piece of armor varies per piece -- from left and right vambraces on the chestplate to the Sabatons on your boots. Each component can be made up of different materials, and not all pieces are required to have a functional piece of armor. 

Total armor value for each piece of armor is a simple sum of the protection values of all of it's components. There are two major considerations when determining the armor values of a component: the material it's made out of, and what piece it is. Some pieces provide more protection that others. Take, for example, the breastplate in the torso armor piece. The breastplate provides a large chunk of the overall protection, with each pauldron and vambrace producing smaller amounts. Each piece varies in how large it's contribution to the overall protection is on a per-damage-type basis. Some pieces are very important for protecting against slashing, but not piercing or crushing. Different materials excel and suffer against different damage types as well. As a result, a piece of armor made entirely of the same material might not be as effective as a piece made of several different parts carefully selected for each piece.

##### Creating Components
Components are created by hand and individually by the player. Each piece requires, in broad terms:
1. Aquiring metals to construct components out of
2. Smelting the metals down in the forge
3. Taking the molten metal and shaping it on an anvil and cutting table
4. Cooling the metal in a trough to finalize the product

Jarmory adds several blocks and mechanics to allow the player to perform all of the above. Forge and Brazier blocks are added to construct a Forge construct. This takes metals and melts them down. Tongs are added to allow the player to move the molten metal while it's still hot. A smith's anvil and cutting table allow the player to hammer out and shape metal to the desired shape. Troughs store coolants that quickly cool the shaped metal to produce the component.

Armor components are then taken to an armor mannequin (similar to an armor stand) where pieces are added, removed, and swapped from equipment meshes. Weapon components are brought together and assembled on a Weapon Rig. 

### Support for Vanilla
Because all the new damage types are not native to Vanilla, Vanilla weapons and armor are not set up to use them. To combat this, vanilla items, weapons, and armors are assigned default values based on the type of item they are. For example, swords do their vanilla damage amount, but purely in slashing. Axes have the same attack power they had before, but split between slashing and crushing.

Vanilla armor is affected in a similar way. However, armor is a little harder to break down systematically based solely on the armor type. Armor is separated by applying a linear transformation of the default protection type into each main category. This value is a server-side configured value, but defaults to 70%. This means that a piece of armor that has 10 protection will instead have 7 protection in slashing, piercing, and crushing.

Finally, armor and weapons that are added by other mods that do not used the defined APIs to provide custom values (see below) are treated the same as Vanilla items.

### Interfacing with Jarmory
There are two major ways to interface with Jarmory: through configuration (both server-side and client-side. See below) and through the mod API's. For information on the configurations, see below.

There are a large number of APIs to allow mods to work very closely with Jarmory. Mods can register their weapons and armor with Jarmory for finely-tuned protection and damage values. As fluids are added, they can be registered to be used as coolants. Different types of fuels can be added. Metals and materials added by other mods can be set up to be usable in the creation of components. Entirely different weapon types can be added. The list goes on and on.

Much of the specifics of the interfaces can be found attached the actual API classes. In place of an exhaustive description of each API, here is a list of each API with a link to the defining class.
* [Weapon API] - Used to register weapons with specific damage values
* [Armor API] - Like the Weapon API, but with Armor
* [Metals & Materials] [MetMat API] - Register custom metals and materials for use in component crafting
* [Alloying API] - Specify how alloyed materials are made
* [Component API] - Add recipes for new components
* [ArmorPiece API]* - Create new pieces of armor, with their own set of components
* [WeaponCraft API] - Define new weapon types and the pieces that make them up
* [Fuel API] - Register fuels that can be used with the braziers (and optionally the forge)
* [Coolant API] - Define fluids that can be used as coolants and how well they work

> \* - The ArmorPiece API is not an official API. Developers can still create and define new armor pieces and their list of components, but do so in a less-defined manner

All of the above API's are expected to be used during the **init** phase. Doing so before will almost certainly cause *Null Pointer Exception*s, and doing so after is not gauranteed to catch your additions before creating caches. While some APIs (like the [Weapon API] are very tolerant to changes during execution, no support is available for making modifications later than the **init** phase.

### Configuration (Server)
Aside from mod developers, server administrators can also specify exactly how several different components of Jarmory works. Changes made in configuration do not add or specify new things, but dictate exactly how things should operate and what assumptions should be made. For example, serverside configuration dictates at what rate non-defined (vanilla or armor added by other mods that do not register with Jarmory) armor is converted into the respected armor categories. The default value is 70% (so armor with 10 protection ends up with 7 protection in _Slashing_, _Piercing_, and _Crushing_ damage types). This value, however, is set through configuration.

Jarmory automatically syncronizes all server-side configurations with clients as they connect. This works in single-player mode as well as when running a dedicated server. All properties that are server-bound are located in the dedicated _Server_ section of the config file.

As usual, running the mod for the first time creates all neccessary config files with default values. It's recommended you do this to generate the config file before making edits.

Descriptions for each config can be found in the generated config file. Please see that for a list of configurable options. (it's changing rapidly in development, so that list will be much more up-to-date than anything I could put here!)

### Configuration (Client)
Not all configuration is server-bound. Individual configurations are also available. These generally apply to visual preferences, like displaying armor values of 0.0 or hiding them. Like the server configurations, a list of available options can be found in the config file along with descriptions of what they do.

In-game modification of the config is available as well. Simply press [ESC] to go to the Minecraft menu, select Mod Options, and select Jarmory.

[Weapon Api]: <https://github.com/Dove-Bren/SkylanderArmory/blob/master/src/main/java/com/SkyIsland/Armory/api/WeaponManager.java> "Jarmory/WeaponManager.java"
[Armor API]: <https://github.com/Dove-Bren/SkylanderArmory/blob/master/src/main/java/com/SkyIsland/Armory/api/ArmorManager.java> "Jarmory/ArmorManager.java"
[MetMat API]: <https://github.com/Dove-Bren/SkylanderArmory/blob/master/src/main/java/com/SkyIsland/Armory/api/ForgeManager.java> "Jarmory/ForgeManager.java"
[Alloying API]: <https://github.com/Dove-Bren/SkylanderArmory/blob/master/src/main/java/com/SkyIsland/Armory/api/ForgeManager.java> "Jarmory/ForgeManager.java"
[Component API]: <https://github.com/Dove-Bren/SkylanderArmory/blob/master/src/main/java/com/SkyIsland/Armory/api/ForgeManager.java> "Jarmory/ForgeManager.java"
[ArmorPiece API]: <https://github.com/Dove-Bren/SkylanderArmory/blob/master/src/main/java/com/SkyIsland/Armory/api/ArmorpieceAPI.java> "Jarmory/ArmorpieceAPI.java"
[WeaponCraft API]: <https://github.com/Dove-Bren/SkylanderArmory/blob/master/src/main/java/com/SkyIsland/Armory/api/WeaponCraftingManager.java> "Jarmory/WeaponCraftingManager.java"
[Fuel API]: <https://github.com/Dove-Bren/SkylanderArmory/blob/master/src/main/java/com/SkyIsland/Armory/api/ForgeManager.java> "Jarmory/ForgeManager.java"
[Coolant API]: <https://github.com/Dove-Bren/SkylanderArmory/blob/master/src/main/java/com/SkyIsland/Armory/api/ForgeManager.java> "Jarmory/ForgeManager.java"

[Damage Types]: <#damage-types>
[Customization]: <#customization-of-armor-and-weapons>
[Creating Components]: <#creating-components>
[Support For Vanilla]: <#support-for-vanilla>
[Interfacing with JArmory (APIs)]: <#interfacing-with-jarmory>
[Configuration (Server)]: <#configuration-server>
[Configuration (Client)]: <#configuration-client>
