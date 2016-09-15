package com.SkyIsland.Armory.mechanics;

import com.SkyIsland.Armory.config.ModConfig;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class ExtendedSmith implements IExtendedEntityProperties {
	
	private static final String PROP_KEY = "SmithLevel";

	private static final String LEVEL_KEY = "SmithLevel";
	
	private static final String PROGRESS_KEY = "SmithProgress";
	
	private int level;
	
	private float progress;
	
	private ExtendedSmith(EntityLivingBase entity) {
		level = 0;
		progress = 0f;
	}
	
	@Override
	public void saveNBTData(NBTTagCompound compound) {

		compound.setInteger(LEVEL_KEY, level);
		compound.setFloat(PROGRESS_KEY, progress);

	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		level = compound.getInteger(LEVEL_KEY);
		progress = compound.getFloat(PROGRESS_KEY);
	}

	@Override
	public void init(Entity entity, World world) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	* Used to register these extended properties for the entity during EntityConstructing event
	* This method is for convenience only; it will make your code look nicer
	* !!Taken from http://www.minecraftforum.net/forums/mapping-and-modding/mapping-and-modding-tutorials/1571567-forge-1-6-4-1-8-eventhandler-and
	*/
	public static final void register(EntityLivingBase entity)
	{
		entity.registerExtendedProperties(ExtendedSmith.PROP_KEY, new ExtendedSmith(entity));
	}
	
	/**
	* Returns ExtendedArmor properties for entity.
	* If the entity doesn't have defined armor, a default is created with 0's for
	* protection values
	* @see #ExtendedArmor(EntityLivingBase)
	*/
	public static final ExtendedSmith get(EntityLivingBase entity, boolean create)
	{
		IExtendedEntityProperties prop = entity.getExtendedProperties(PROP_KEY);
		if (prop == null && create)
			prop = new ExtendedSmith(entity);
		return (ExtendedSmith) (prop); 
	}
	
	/**
	 * Returns number of whole levels achieved.
	 * @return
	 */
	public int getLevel() {
		return level;
	}
	
	/**
	 * Returns current progress to next level
	 * @return
	 */
	public float getProgress() {
		return progress;
	}
	
	/**
	 * Straight adds the given float to the stored progress amount, and performs
	 * and update. In other words, addAbsoluteProgress(.2) adds 20% progress
	 * to the next level regardless of the current level.
	 * @param absoluteProgress
	 */
	public void addAbsoluteProgress(float absoluteProgress) {
		this.progress += absoluteProgress;
		update();
	}
	
	/**
	 * Adds some progress ot the next level dependent on the player's level. The
	 * provided relativeProgress is the progress given to a level 0 player. For
	 * example, is .2 is passed in, a lvl 0 player will get 20% progress. On the
	 * other hand, a lvl 5 player will get much less.
	 * @param relativeProgress
	 */
	public void addProgress(float relativeProgress) {
		addAbsoluteProgress(getProgressForLevel(relativeProgress, level));
	}
	
	private void update() {
		if (progress >= 1.0f) {
			float rel = progress - 1;
			float rate = (float) Math.pow(ModConfig.config.getSmithXpRate(), level);
			float absolute = rel * rate;
			level++;
			
			float updateRate = rate * ModConfig.config.getSmithXpRate();
			progress = absolute / updateRate;
			
			update();
		}
	}
	
	/**
	 * Calc and rets how much progress the input progress (scaled to a lvl 0) is
	 * worth to a lvl <em>lvl</em>. For example, to calculate how much progress
	 * a lvl 5 would get from the equivalent of a lvl 0 getting 20%, we'd
	 * pass    progress = .2    lvl = 5
	 * @param progress
	 * @param lvl
	 * @return
	 */
	private float getProgressForLevel(float progress, int lvl) {
		float rate = (float) Math.pow(ModConfig.config.getSmithXpRate(), lvl);
		return progress / rate;
	}
}
