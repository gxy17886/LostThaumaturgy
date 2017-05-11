package com.pengu.lostthaumaturgy.tile;

import net.minecraft.nbt.NBTTagCompound;

public class TileVisValve extends TileConduit
{
	public boolean open;
	private boolean prevPower;
	
	@Override
	public void tick()
	{
		if(world.isRemote)
			return;
		
		if(this.prevdisplayPure != this.displayPure || this.prevdisplayTaint != this.displayTaint)
		{
			sync();
			this.prevdisplayPure = this.displayPure;
			this.prevdisplayTaint = this.displayTaint;
		}
		
		this.calculateSuction();
		
		if(!this.open)
		{
			this.setSuction(0);
		}
		
		if(this.getSuction(null) > 0)
		{
			this.equalizeWithNeighbours();
		}
		
		if(displayPure != pureVis || displayTaint != taintedVis)
		{
			displayPure = pureVis;
			displayTaint = taintedVis;
		}
		
		if(this.displayTaint + this.displayPure < 0.1f)
		{
			this.displayTaint = 0.0f;
			this.displayPure = 0.0f;
		}
		
		if(this.gettingPower())
		{
			this.prevPower = true;
			this.open = false;
			sync();
		}
		
		if(!this.gettingPower() && this.prevPower)
		{
			this.open = true;
			this.prevPower = false;
			sync();
		}
	}
	
	protected boolean gettingPower()
	{
		return world.isBlockIndirectlyGettingPowered(pos) > 0;
	}
	
	@Override
	public void writeNBT(NBTTagCompound nbt)
	{
		super.writeNBT(nbt);
		nbt.setBoolean("Open", open);
		nbt.setBoolean("PrevPower", prevPower);
	}
	
	@Override
	public void readNBT(NBTTagCompound nbt)
	{
		super.readNBT(nbt);
		open = nbt.getBoolean("Open");
		prevPower = nbt.getBoolean("PrevPower");
	}
}