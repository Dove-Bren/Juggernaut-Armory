package com.SkyIsland.Armory.client.armor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * These are sneaky model renderers that defer to regular model renderer
 * behavior, but expect a little pre-render information. This allows the
 * texture to be changed before each piece is rendered, rather than
 * all at once.
 * @author Skyler
 *
 */
public class SmartModelRenderer extends ModelRenderer {

	private final ResourceLocation missing;
	private ResourceLocation cache;
	
	public SmartModelRenderer(ModelBase model, int texOffX, int texOffY, ResourceLocation missingTexture) {
		super(model, texOffX, texOffY);
		missing = missingTexture;
	}
	
	/**
	 * Stores the texture this renderer should use next time rendered.
	 * This value must be reset for each render pass if it's to be
	 * accurate.
	 * @param texture
	 */
	public void provideTexture(ResourceLocation texture) {
		this.cache = texture;
		
		if (texture == null) {
			System.out.println("passed null texture!");
			return;
		}
//		System.out.println("provided texture " + texture.getResourcePath());
//		if (Minecraft.getMinecraft().getRenderManager().renderEngine
//				.getTexture(cache) == null) {
//			this.cache = missing;
//			System.out.print("[X]");
//		}
		
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public void render(float p_78785_1_) {
		if (!this.showModel)
			return;
		
		//same as LayerAmorBiped's this.renderer.bindTexture
		if (cache != null) {
			try {
				Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(cache);
			} catch (ReportedException e) {
				cache = missing;
				Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(cache);
			}
		} 
		
//		p_78785_1_ = ModConfig.config.getTestValue(ModConfig.Key.ARMOR_SCALE);
//		System.out.println("  > show: " + this.showModel + " | scale: " + p_78785_1_);
		
		
		super.render(p_78785_1_);
	}
	

}
