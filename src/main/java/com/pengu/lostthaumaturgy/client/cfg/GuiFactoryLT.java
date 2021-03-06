package com.pengu.lostthaumaturgy.client.cfg;

import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

import com.pengu.lostthaumaturgy.LostThaumaturgy;
import com.pengu.lostthaumaturgy.core.Info;

public class GuiFactoryLT implements IModGuiFactory
{
	@Override
	public void initialize(Minecraft minecraftInstance)
	{
		LostThaumaturgy.LOG.info("Created " + Info.MOD_NAME + " Gui Config Factory!");
	}
	
	@Override
	public GuiScreen createConfigGui(GuiScreen parentScreen)
	{
		return new GuiConfigsLT(parentScreen);
	}
	
	@Override
	public boolean hasConfigGui()
	{
		return true;
	}
	
	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories()
	{
		return null;
	}
}