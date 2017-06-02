package com.pengu.lostthaumaturgy.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.mrdimka.hammercore.HammerCore;
import com.mrdimka.hammercore.common.items.MultiVariantItem;
import com.mrdimka.hammercore.common.utils.WorldUtil;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.api.tiles.IConnection;

public class ItemAuraDetector extends MultiVariantItem
{
	public static int type;
	
	public ItemAuraDetector()
	{
		super("aura_detector", "vis_detector", "taint_detector", "thaumometer");
		insertPrefix(LTInfo.MOD_ID + ":");
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		IConnection conn = WorldUtil.cast(worldIn.getTileEntity(pos), IConnection.class);
		if(conn != null)
		{
			if(!worldIn.isRemote)
				HammerCore.audioProxy.playSoundAt(worldIn, "entity.experience_orb.pickup", pos, .5F, 1.6F, SoundCategory.PLAYERS);
			player.swingArm(hand);
		}
		return EnumActionResult.FAIL;
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		if(type == -1)
		{
			int dmg = stack.getItemDamage();
			type = dmg;
		}else if(type != 2 && (stack.getItemDamage() == 2 || stack.getItemDamage() + type == 1))
			type = 2;
	}
}