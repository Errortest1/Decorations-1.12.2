package by.errortest.decor;

import by.errortest.decor.client.config.DecorConfig;
import by.errortest.decor.tiles.TileDecor;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

import static by.errortest.decor.Main.MODID;

@Mod(
        modid = MODID,
        name = "Decor Mod",
        version = "0.0.1",
        acceptedMinecraftVersions = "[1.7.10]"
)
public class Main {
    @SidedProxy(
            clientSide = "by.errortest.decor.client.ClientProxy",
            serverSide = "by.errortest.decor.CommonProxy"
    )
    private static CommonProxy proxy;
    public static final String MODID = "decor-mod";
    public static final CreativeTabs DECOR_TAB = new CreativeTabs("Decor") {
        @Override
        public Item getTabIconItem() {
            return Items.arrow;
        }
    };

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        DecorConfig decorConfig = new DecorConfig();
        decorConfig.readConfig();
        GameRegistry.registerTileEntity(TileDecor.class, "by.errortest.tiles.TileDecor");

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

}
