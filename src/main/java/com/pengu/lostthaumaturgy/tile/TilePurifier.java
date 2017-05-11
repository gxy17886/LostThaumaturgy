package com.pengu.lostthaumaturgy.tile;

import java.text.DecimalFormat;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;

import com.pengu.lostthaumaturgy.block.BlockPurifier;

public class TilePurifier extends TileConduit
{
	public Axis orientation;
	private DecimalFormat format = new DecimalFormat("#0.0");
	
	@Override
	public void tick()
	{
		IBlockState state = world.getBlockState(pos);
		if(state.getBlock() instanceof BlockPurifier) orientation = state.getValue(BlockPurifier.PROPERTY_AXIS);
		
		if(world.isRemote)
			return;
		calculateSuction();
		if(taintSuction < 5)
			setTaintSuction(5);
		if(getSuction(null) > 0)
			equalizeWithNeighbours();
		
		if(prevdisplayPure != displayPure || prevdisplayTaint != displayTaint)
		{
			prevdisplayPure = displayPure;
			prevdisplayTaint = displayTaint;
			sync();
		}
		
		if(displayPure != pureVis || displayTaint != taintedVis)
		{
			displayPure = pureVis;
			displayTaint = taintedVis;
		}
		
		if(displayTaint + displayPure < .1F)
		{
			displayTaint = 0F;
			displayPure = 0F;
		}
		
		if(taintedVis > 0.01F)
		{
			taintedVis -= 0.01F;
			sync();
		}
	}
	
	@Override
	public boolean getConnectable(EnumFacing face)
	{
		return face.getAxis() == orientation;
	}
	
	public void addTooltipToGoggles(List<String> tooltip)
	{
		tooltip.add("Purifying " + format.format(taintedVis) + " T");
	}
}