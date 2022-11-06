package by.errortest.decorations.network;

import by.errortest.decorations.tile.TileDecoration;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class TileServerMessagePacket implements IMessage {

    private int x, y, z, rotX, rotY, rotZ;
    private double trX, trY, trZ, scale;
    private int id;
    private boolean collide;


    public TileServerMessagePacket() {
    }
    public TileServerMessagePacket(int rotateX, int rotateY, int rotateZ, double translationX, double translationY,
                                   double translationZ, double scale, boolean collide, int x, int y, int z, int id) {
        this.trX = translationX;
        this.trY = translationY;
        this.trZ = translationZ;
        this.rotX = rotateX;
        this.rotY = rotateY;
        this.rotZ = rotateZ;
        this.x = x;
        this.y = y;
        this.z = z;
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
        rotX = buf.readInt();
        rotY = buf.readInt();
        rotZ = buf.readInt();
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
        id = buf.readInt();
        collide = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeDouble(scale);
        buf.writeDouble(trX);
        buf.writeDouble(trY);
        buf.writeDouble(trZ);
        buf.writeInt(rotX);
        buf.writeInt(rotY);
        buf.writeInt(rotZ);
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeInt(id);
        buf.writeBoolean(collide);
    }

    public static class Handler implements IMessageHandler<TileServerMessagePacket, IMessage> {

        @Override
        public IMessage onMessage(TileServerMessagePacket message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            World w = player.world;
            TileDecoration tile = (TileDecoration) w.getTileEntity(new BlockPos(message.x, message.y, message.z));
            tile.scale = message.scale;
            tile.rotationX = message.rotX;
            tile.rotationY = message.rotY;
            tile.rotationZ = message.rotZ;
            tile.translateX = message.trX;
            tile.translateY = message.trY;
            tile.translateZ = message.trZ;
            tile.changed = true;
            tile.modelId = message.id;
            tile.collide = message.collide;
            return null;
        }
    }
}

