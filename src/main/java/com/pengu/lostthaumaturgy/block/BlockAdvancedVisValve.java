package com.pengu.lostthaumaturgy.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.mrdimka.hammercore.HammerCore;
import com.mrdimka.hammercore.api.ITileBlock;
import com.mrdimka.hammercore.common.utils.WorldUtil;
import com.pengu.lostthaumaturgy.custom.aura.AuraTicker;
import com.pengu.lostthaumaturgy.tile.TileAdvancedVisValve;
import com.pengu.lostthaumaturgy.tile.TileVisValve;

public class BlockAdvancedVisValve extends BlockContainer implements ITileEntityProvider, ITileBlock<TileAdvancedVisValve>
{
	public BlockAdvancedVisValve()
	{
		super(Material.WOOD);
		setSoundType(SoundType.WOOD);
		setUnlocalizedName("advanced_vis_valve");
		setHardness(.2F);
		setResistance(4F);
	}
	
	@Override
	public Class<TileAdvancedVisValve> getTileClass()
	{
		return TileAdvancedVisValve.class;
	}
	
	@Override
	public TileAdvancedVisValve createNewTileEntity(World worldIn, int meta)
	{
		return new TileAdvancedVisValve();
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
	{
		return true;
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}
	
	@Override
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer)
	{
		return false;
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		AuraTicker.spillTaint(worldIn, pos);
		super.breakBlock(worldIn, pos, state);
	}
	
	public static final AxisAlignedBB VALVE_AABB = new AxisAlignedBB(3.5 / 16, 3.5 / 16, 3.5 / 16, 12.5 / 16, 12.5 / 16, 12.5 / 16);
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return VALVE_AABB;
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		TileAdvancedVisValve tile = WorldUtil.cast(worldIn.getTileEntity(pos), TileAdvancedVisValve.class);
		if(tile != null)
		{
			if(!worldIn.isRemote)
				HammerCore.audioProxy.playSoundAt(worldIn, "block.lever.click", pos, 1F, tile.setting == 2 ? .4F : .6F, SoundCategory.PLAYERS);
			tile.prevSetting = tile.setting;
			tile.setting = (tile.setting + 1) % 3;
			tile.sync(); //sync to others
			return true;
		}
		return false;
	}
}