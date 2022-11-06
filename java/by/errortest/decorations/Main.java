package by.errortest.decorations;

import by.errortest.decorations.blocks.BlockDecoration;
import by.errortest.decorations.tile.TileDecoration;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;


@Mod(
        modid = "decormod",
        name = "Decorations Mod",
        acceptedMinecraftVersions = "[1.12]",
        version = "0.1"
)
public class Main {

    @SidedProxy(
            serverSide = "by.errortest.decorations.CommonProxy",
            clientSide = "by.errortest.decorations.ClientProxy"
    )
    public static CommonProxy proxy;
    public static final String MODID = "decormod";
    public static final Block BLOCK = new BlockDecoration("block_decor_mod_block");
    public static final SimpleNetworkWrapper NETWORK = new SimpleNetworkWrapper(Main.MODID);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ForgeRegistries.BLOCKS.register(BLOCK);
        GameRegistry.registerTileEntity(TileDecoration.class, BLOCK.getRegistryName().toString());

        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        proxy.serverStarting(event);
    }

}
