package by.errortest.decorations.network;

import by.errortest.decorations.ClientProxy;
import by.errortest.decorations.DecorModel;
import by.errortest.decorations.Main;
import by.errortest.decorations.tile.TileDecoration;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CreateBlockServerMessagePacket implements IMessage {

    private int x, y, z;

    public CreateBlockServerMessagePacket() {

    }

    public CreateBlockServerMessagePacket(BlockPos blockPos) {
        this.x = blockPos.getX();
        this.y = blockPos.getY();
        this.z = blockPos.getZ();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
    }

    public static class Handler implements IMessageHandler<CreateBlockServerMessagePacket, IMessage> {

        @Override
        public IMessage onMessage(CreateBlockServerMessagePacket message, MessageContext ctx) {
            World world = ctx.getServerHandler().player.world;
            world.setBlockState(new BlockPos(message.x, message.y + 1, message.z), Main.BLOCK.getDefaultState());
            DecorModel model = ClientProxy.selectedModel;
            TileDecoration tile = (TileDecoration) world.getTileEntity(new BlockPos(message.x, message.y + 1, message.z));
            if (model != null)
            tile.modelId = model.id;

            return null;
        }
    }
}
