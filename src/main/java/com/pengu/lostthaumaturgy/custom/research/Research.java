package com.pengu.lostthaumaturgy.custom.research;

import java.security.SecureRandom;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;

public class Research
{
	protected final SecureRandom random = new SecureRandom();
	public float failChance;
	public final String uid;
	protected int color;
	
	public Research(String uid, float failChance)
	{
		this.uid = uid;
		this.failChance = failChance;
		color = uid.hashCode();
	}
	
	public String sucessToString()
	{
		if(failChance <= 10)
			return "Easy";
		if(failChance <= 30)
			return "Moderate";
		if(failChance <= 50)
			return "Hard";
		if(failChance <= 75)
			return "Tricky";
		if(failChance <= 99)
			return "Tortuous";
		if(failChance <= 100)
			return "Almost Impossible";
		return "Impossible";
	}
	
	public int getColor()
	{
		return color;
	}
	
	public Research setColor(int color)
    {
	    this.color = color;
	    return this;
    }
	
	public final boolean isCompleted(EntityPlayer player)
	{
		return ResearchSystem.isResearchCompleted(player, this);
	}
	
	public String getTitle()
	{
		return I18n.translateToLocal("research." + uid + ".title");
	}
	
	public String getDesc()
	{
		return I18n.translateToLocal("research." + uid + ".desc");
	}
	
	/**
	 * Used for special researches like Brain-in-a-jar to only be researchable
	 * via Zombie Brains
	 */
	public boolean canObtainFrom(ItemStack baseStack, ItemStack boostStack1, ItemStack boostStack2, EntityPlayer initiator)
	{
		return true;
	}
}