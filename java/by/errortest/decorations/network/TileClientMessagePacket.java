package by.errortest.decorations.network;

import by.errortest.decorations.tile.TileDecoration;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileClientMessagePacket implements IMessage {

    int x, y, z, rotX, rotY, rotZ;
    private double trX, trY, trZ, scale;
    private int id;
    private boolean collide;

    public TileClientMessagePacket() {
    }
    public TileClientMessagePacket(int rotateX, int rotateY, int rotateZ, double translationX, double translationY,
    double translationZ, double scale, boolean collide, int tpX, int tpY, int tpZ, int id) {
        this.trX = translationX;
        this.trY = translationY;
        this.trZ = translationZ;
        this.rotX = rotateX;
        this.rotY = rotateY;
        this.rotZ = rotateZ;
        this.x = tpX;
        this.y = tpY;
        this.z = tpZ;
        this.scale = scale;
        this.id = id;
        this.collide = collide;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        scale = buf.readDouble();
        trX = buf.readDouble();
        trY = buf.readDouble();
        trZ = buf.readDouble();
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
        rotX = buf.readInt();
        rotY = buf.readInt();
        rotZ = buf.readInt();
        id = buf.readInt();
        collide = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeDouble(scale);
        buf.writeDouble(trX);
        buf.writeDouble(trY);
        buf.writeDouble(trZ);
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeInt(rotX);
        buf.writeInt(rotY);
        buf.writeInt(rotZ);
        buf.writeInt(id);
        buf.writeBoolean(collide);
    }

    public static class Handler implements IMessageHandler<TileClientMessagePacket, IMessage> {


        @SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(TileClientMessagePacket message, MessageContext ctx) {
            TileDecoration tile = (TileDecoration) Minecraft.getMinecraft().player.world.getTileEntity(new BlockPos(message.x, message.y, message.z));
            tile.scale = message.scale;
            tile.rotationX = message.rotX;
            tile.rotationY = message.rotY;
            tile.rotationZ = message.rotZ;
            tile.translateX = message.trX;
            tile.translateY = message.trY;
            tile.translateZ = message.trZ;
            tile.modelId = message.id;
            tile.collide = message.collide;
            return null;
        }
    }
}

