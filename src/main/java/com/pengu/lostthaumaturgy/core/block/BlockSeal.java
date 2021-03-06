package com.pengu.lostthaumaturgy.core.block;

import java.util.Arrays;
import java.util.List;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.api.ITileBlock;
import com.pengu.hammercore.common.EnumRotation;
import com.pengu.hammercore.common.utils.WorldUtil;
import com.pengu.hammercore.net.HCNetwork;
import com.pengu.hammercore.utils.WorldLocation;
import com.pengu.lostthaumaturgy.api.seal.ItemSealSymbol;
import com.pengu.lostthaumaturgy.api.seal.SealInstance;
import com.pengu.lostthaumaturgy.core.Info;
import com.pengu.lostthaumaturgy.core.block.def.BlockRendered;
import com.pengu.lostthaumaturgy.core.tile.TileSeal;
import com.pengu.lostthaumaturgy.init.ItemsLT;

public class BlockSeal extends BlockRendered implements ITileEntityProvider, ITileBlock<TileSeal>
{
	public BlockSeal()
	{
		super(Material.ROCK);
		setUnlocalizedName("seal");
		setHarvestLevel("pickaxe", -1);
		setHardness(0);
		setResistance(0);
	}
	
	public static final AxisAlignedBB[] SEAL_BOUNDS = { new AxisAlignedBB(5 / 16D, 0, 5 / 16D, 11 / 16D, 1 / 16D, 11 / 16D), new AxisAlignedBB(5 / 16D, 14 / 16D, 5 / 16D, 11 / 16D, 1, 11 / 16D), new AxisAlignedBB(5 / 16D, 5 / 16D, 0, 11 / 16D, 11 / 16D, 1 / 16D), new AxisAlignedBB(5 / 16D, 5 / 16D, 14 / 16D, 11 / 16D, 11 / 16D, 1), new AxisAlignedBB(0, 5 / 16D, 5 / 16D, 1 / 16D, 11 / 16D, 11 / 16D), new AxisAlignedBB(14 / 16D, 5 / 16D, 5 / 16D, 1, 11 / 16D, 11 / 16D) };
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
	{
		return getDefaultState().withProperty(EnumRotation.EFACING, facing);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return SEAL_BOUNDS[state.getValue(EnumRotation.EFACING).getOpposite().ordinal()];
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, EnumRotation.EFACING);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(EnumRotation.EFACING).ordinal();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(EnumRotation.EFACING, EnumFacing.VALUES[meta % 6]);
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
	public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player)
	{
		return true;
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileSeal();
	}
	
	@Override
	public Class<TileSeal> getTileClass()
	{
		return TileSeal.class;
	}
	
	@Override
	public String getParticleSprite(World world, BlockPos pos)
	{
		return Info.MOD_ID + ":blocks/seal_base";
	}
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
	{
		return getDrop(new WorldLocation(world, pos));
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		return Arrays.asList();
	}
	
	public ItemStack getDrop(WorldLocation loc)
	{
		TileSeal seal = loc.getTileOfType(TileSeal.class);
		if(seal != null)
			return seal.stack.get().copy();
		return ItemStack.EMPTY;
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		TileSeal seal = WorldUtil.cast(worldIn.getTileEntity(pos), TileSeal.class);
		if(seal == null)
			worldIn.setTileEntity(pos, seal = new TileSeal());
		if(seal != null)
		{
			ItemStack stack2 = stack.copy();
			stack2.setCount(1);
			seal.stack.set(stack2);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced)
	{
		if(advanced.isAdvanced())
		{
			String col = "#FFFFFF";
			if(stack.hasTagCompound())
			{
				int[] rgb = stack.getTagCompound().getIntArray("RGB");
				if(rgb.length >= 3)
					col = "#" + Integer.toHexString((rgb[0] << 16) | (rgb[1] << 8) | rgb[2]);
			}
			tooltip.add(I18n.translateToLocal(getUnlocalizedName() + ".desc").replace("$col", col.toUpperCase()));
		}
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		TileSeal seal = WorldUtil.cast(worldIn.getTileEntity(pos), TileSeal.class);
		if(seal != null && !worldIn.isRemote)
		{
			ItemSealSymbol symbol = WorldUtil.cast(playerIn.getHeldItem(hand).getItem(), ItemSealSymbol.class);
			boolean isSymbol = symbol != null;
			
			if(isSymbol && seal.getSymbol(2) == null)
			{
				if(seal.getSymbol(0) == null)
					seal.setSymbol(0, symbol);
				else if(seal.getSymbol(1) == null)
					seal.setSymbol(1, symbol);
				else
					seal.setSymbol(2, symbol);
				playerIn.getHeldItem(hand).shrink(1);
				HammerCore.audioProxy.playSoundAt(worldIn, Info.MOD_ID + ":rune_set", pos, .5F, 1F, SoundCategory.BLOCKS);
				HCNetwork.swingArm(playerIn, hand);
				return false;
			} else if(playerIn.getHeldItem(hand).getItem() == ItemsLT.WAND_REVERSAL && seal.getSymbol(0) != null)
			{
				for(int i = 2; i >= 0; --i)
					if(seal.getSymbol(i) != null)
						if(seal.getSymbol(i) != null)
						{
							ItemStack stack = new ItemStack(seal.getSymbol(i));
							seal.setSymbol(i, null);
							
							if(worldIn.rand.nextInt(100) > 10)
								WorldUtil.spawnItemStack(worldIn, pos, stack);
							else
								HammerCore.audioProxy.playSoundAt(worldIn, Info.MOD_ID + ":fizz", pos, .5F, 1F, SoundCategory.BLOCKS);
							
							HammerCore.audioProxy.playSoundAt(worldIn, Info.MOD_ID + ":zap", pos, .5F, 1F, SoundCategory.BLOCKS);
							HCNetwork.swingArm(playerIn, hand);
							
							return false;
						}
			}
			SealInstance i = seal.instance;
			if(i != null && i.onSealActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ))
				HCNetwork.swingArm(playerIn, hand);
		}
		return false;
	}
	
	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
	{
		TileSeal tile = WorldUtil.cast(worldIn.getTileEntity(pos), TileSeal.class);
		if(tile != null && tile.instance != null)
			tile.instance.onEntityCollidedWithSeal(worldIn, pos, state, entityIn);
		super.onEntityCollidedWithBlock(worldIn, pos, state, entityIn);
	}
}