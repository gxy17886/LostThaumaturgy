package com.pengu.lostthaumaturgy.core.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.client.utils.RenderUtil;
import com.pengu.lostthaumaturgy.core.Info;

public class UtilsFX
{
	private static Map<String, ResourceLocation> textures = new HashMap<>();
	
	public static void bindTexture(String tex)
	{
		bindTexture(Info.MOD_ID, tex);
	}
	
	public static void bindTexture(String dom, String tex)
	{
		if(textures.containsKey(dom + ":" + tex))
		{
			Minecraft.getMinecraft().getTextureManager().bindTexture(textures.get(dom + ":" + tex));
			return;
		}
		ResourceLocation value = new ResourceLocation(dom, tex);
		textures.put(dom + ":" + tex, value);
		Minecraft.getMinecraft().getTextureManager().bindTexture(value);
	}
	
	public static void drawTexturedQuadFull(int par1, int par2, double zLevel)
	{
		Tessellator var9 = Tessellator.getInstance();
		BufferBuilder b = var9.getBuffer();
		b.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		b.pos((double) (par1 + 0), (double) (par2 + 16), zLevel).tex(0.0, 1.0).endVertex();
		b.pos((double) (par1 + 16), (double) (par2 + 16), zLevel).tex(1.0, 1.0).endVertex();
		b.pos((double) (par1 + 16), (double) (par2 + 0), zLevel).tex(1.0, 0.0).endVertex();
		b.pos((double) (par1 + 0), (double) (par2 + 0), zLevel).tex(0.0, 0.0).endVertex();
		var9.draw();
	}
	
	public static void drawCustomTooltip(GuiScreen gui, RenderItem itemRenderer, FontRenderer fr, List<String> var4, int par2, int par3, int subTipColor)
	{
		GL11.glDisable((int) 32826);
		GL11.glDisable((int) 2929);
		GlStateManager.enableBlend();
		
		if(!var4.isEmpty())
		{
			int var5 = 0;
			
			for(String var7 : var4)
				var5 = Math.max(var5, fr.getStringWidth(var7));
			
			int var15 = par2 + 12;
			int var16 = par3 - 12;
			
			int var9 = 8;
			if(var4.size() > 1)
				var9 += 2 + (var4.size() - 1) * 10;
			
			itemRenderer.zLevel = 300.0f;
			int var10 = -267386864;
			
			RenderUtil.drawGradientRect(var15 - 3, var16 - 4, var5 + 3, 1, var10, var10);
			RenderUtil.drawGradientRect(var15 - 3, var16 + var9 + 3, var5 + 3, 1, var10, var10);
			RenderUtil.drawGradientRect(var15 - 3, var16 - 3, var5 + 3, var9 + 6, var10, var10);
			RenderUtil.drawGradientRect(var15 - 4, var16 - 3, 1, var9 + 6, var10, var10);
			RenderUtil.drawGradientRect(var15 + var5, var16 - 3, 1, var9 + 6, var10, var10);
			
			int var11 = 1347420415;
			int var12 = (var11 & 16711422) >> 1 | var11 & -16777216;
			
			RenderUtil.drawGradientRect(var15 - 3, var16 - 2, 1, var9 + 5, var11, var12);
			RenderUtil.drawGradientRect(var15 + var5 - 1, var16 - 3 + 1, 1, var9 + 5, var11, var12);
			RenderUtil.drawGradientRect(var15 - 3, var16 - 3, var5 + 3, 1, var11, var11);
			RenderUtil.drawGradientRect(var15 - 2, var16 + var9 + 2, var5 + 1, 1, var12, var12);
			
			for(int var13 = 0; var13 < var4.size(); ++var13)
			{
				String var14 = (String) var4.get(var13);
				var14 = var13 == 0 ? "\u00a7" + Integer.toHexString(subTipColor) + var14 : "\u00a77" + var14;
				fr.drawStringWithShadow(var14, var15, var16, -1);
				if(var13 == 0)
					var16 += 2;
				var16 += 10;
			}
		}
		
		itemRenderer.zLevel = 0.0f;
		GL11.glEnable((int) 2929);
		GlStateManager.enableBlend();
	}
}