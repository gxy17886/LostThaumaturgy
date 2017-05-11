package com.pengu.lostthaumaturgy.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.mrdimka.hammercore.api.ITileBlock;
import com.pengu.lostthaumaturgy.custom.aura.AuraTicker;
import com.pengu.lostthaumaturgy.tile.TileReinforcedVisTank;

public class BlockReinforcedVisTank extends BlockContainer implements ITileBlock<TileReinforcedVisTank>
{
	public BlockReinforcedVisTank()
    {
		super(Material.IRON);
		setUnlocalizedName("reinforced_vis_tank");
		setSoundType(SoundType.METAL);
		setHardness(1.5F);
		setResistance(4F);
    }
	
	public static final AxisAlignedBB TANK_AABB = new AxisAlignedBB(1 / 16D, 0, 1 / 16D, 15 / 16D, 1, 15 / 16D);
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
	    return TANK_AABB;
	}
	
	@Override
	public Class<TileReinforcedVisTank> getTileClass()
	{
	    return TileReinforcedVisTank.class;
	}
	
	@Override
	public TileReinforcedVisTank createNewTileEntity(World worldIn, int meta)
	{
	    return new TileReinforcedVisTank();
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
}