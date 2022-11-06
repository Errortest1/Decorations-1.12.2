package by.errortest.decor.blocks;

import by.errortest.decor.Main;
import by.errortest.decor.tiles.TileDecor;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockDecor extends Block implements ITileEntityProvider, IDecor {

    private final String modelPath;
    private final String texturePath;
    private final boolean collide;
    public BlockDecor(String name, String modelPath, String texturePath, boolean collide) {
        super(Material.iron);
        this.modelPath = modelPath;
        this.texturePath = texturePath;
        this.collide = collide;
        setResistance(99999f);
        setCreativeTab(Main.DECOR_TAB);
        setBlockTextureName("decor-mod:decor_icon");
        setBlockName(name);
        LanguageRegistry.addName(this, name);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_) {
        if (!collide)
            return null;
        else
            return super.getCollisionBoundingBoxFromPool(p_149668_1_, p_149668_2_, p_149668_3_, p_149668_4_);
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileDecor();
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack is) {
        int l = MathHelper.floor_double((double)(entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        world.setBlockMetadataWithNotify(x, y, z, l, 2);
    }


    public int getRenderType() {
        return -1;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public String getModelPath() {
        return this.modelPath;
    }

    @Override
    public String getTexturePath() {
        return texturePath;
    }
}