package by.errortest.decorations.blocks;

import by.errortest.decorations.client.gui.GuiEditTile;
import by.errortest.decorations.Main;
import by.errortest.decorations.tile.TileDecoration;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;

import javax.annotation.Nullable;

public class BlockDecoration extends Block implements ITileEntityProvider {

    public static final PropertyDirection FACING = PropertyDirection.create("facing");

    public BlockDecoration(String name) {
        super(Material.IRON);
        setRegistryName(Main.MODID, name);
        setUnlocalizedName(name);
        setBlockUnbreakable();

        setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    public EnumBlockRenderType getRenderType(IBlockState p_getRenderType_1_) {return EnumBlockRenderType.INVISIBLE;}

    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        TileDecoration tile = (TileDecoration) worldIn.getTileEntity(pos);
        if (tile != null) {
            if (!tile.collide)
                return NULL_AABB;
            else
                return FULL_BLOCK_AABB;
        } else
            return FULL_BLOCK_AABB;
    }

    public boolean isFullCube(IBlockState p_isFullCube_1_) {
        return false;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos position, IBlockState blockState, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileDecoration tile = (TileDecoration) world.getTileEntity(position);
        if (world.isRemote && Minecraft.getMinecraft().player.isCreative()) {
            FMLClientHandler.instance().showGuiScreen(new GuiEditTile(tile));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        world.setBlockState(pos, state.withProperty(FACING, getFacingFromEntity(pos, placer)), 2);
    }

    public static EnumFacing getFacingFromEntity(BlockPos clickedBlock, EntityLivingBase entity) {
        return EnumFacing.getFacingFromVector(
                (float) (entity.posX - clickedBlock.getX()),
                (float) (entity.posY - clickedBlock.getY()),
                (float) (entity.posZ - clickedBlock.getZ()));
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.getFront(meta & 7));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    public boolean hasTileEntity(IBlockState blockState) {

        return true;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileDecoration();
    }

    @Override
    public TileDecoration createTileEntity(World world, IBlockState blockState) {

        return new TileDecoration();
    }
}
