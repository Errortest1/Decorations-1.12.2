package by.errortest.decorations;

import by.errortest.decorations.network.CreateBlockServerMessagePacket;
import by.errortest.decorations.network.TileClientMessagePacket;
import by.errortest.decorations.network.TileServerMessagePacket;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        Main.NETWORK.registerMessage(new TileServerMessagePacket.Handler(), TileServerMessagePacket.class, 0, Side.SERVER);
        Main.NETWORK.registerMessage(new TileClientMessagePacket.Handler(), TileClientMessagePacket.class, 1, Side.CLIENT);
        Main.NETWORK.registerMessage(new CreateBlockServerMessagePacket.Handler(), CreateBlockServerMessagePacket.class, 2, Side.SERVER);
    }

    public void init(FMLInitializationEvent event) {

    }

    public void postInit(FMLPostInitializationEvent event) {

    }

    public void serverStarting(FMLServerStartingEvent event) {

    }

}
