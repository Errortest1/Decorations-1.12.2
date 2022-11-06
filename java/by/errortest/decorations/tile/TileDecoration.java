package by.errortest.decorations.tile;

import by.errortest.decorations.network.TileClientMessagePacket;
import by.errortest.decorations.Main;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class TileDecoration extends TileEntityFurnace {
    public int rotationX, rotationY, rotationZ;
    public double translateX, translateY, translateZ, scale;
    public boolean changed = false, collide = true;
    public int modelId = 0;

    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        this.safeRead(tag);
    }

    public void update() {
        if (changed) {
            if (!world.isRemote) {
                Main.NETWORK.sendToAll(new TileClientMessagePacket(rotationX, rotationY, rotationZ, translateX, translateY, translateZ, scale, collide, getPos().getX(), getPos().getY(), getPos().getZ(), modelId));
                changed = false;
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public net.minecraft.util.math.AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    public void safeRead(NBTTagCompound tag) {
        this.collide = tag.getBoolean("collide");
        this.modelId = tag.getInteger("modelId");
        this.scale = tag.getDouble("scale");
        this.translateX = tag.getDouble("scale");
        this.translateX = tag.getDouble("translateX");
        this.translateY = tag.getDouble("translateY");
        this.translateZ = tag.getDouble("translateZ");
        this.rotationX = tag.getInteger("rotationX");
        this.rotationY = tag.getInteger("rotationY");
        this.rotationZ = tag.getInteger("rotationZ");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        this.safeWrite(tag);
        return tag;
    }

    public void safeWrite(NBTTagCompound tag) {
        tag.setInteger("modelId", modelId);
        tag.setDouble("scale", scale);
        tag.setDouble("translateX", translateX);
        tag.setDouble("translateY", translateY);
        tag.setDouble("translateZ", translateZ);
        tag.setInteger("rotationX", rotationX);
        tag.setInteger("rotationY", rotationY);
        tag.setInteger("rotationZ", rotationZ);
        tag.setBoolean("collide", collide);
    }

    @Override
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket() {

        return new SPacketUpdateTileEntity(this.pos, 3, this.getUpdateTag());
    }

    @Override
    public NBTTagCompound getUpdateTag() {

        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public void onDataPacket(NetworkManager networkManager, SPacketUpdateTileEntity packet) {

        super.onDataPacket(networkManager, packet);

        this.handleUpdateTag(packet.getNbtCompound());
    }
}
