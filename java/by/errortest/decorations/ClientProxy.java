package by.errortest.decorations;


import by.errortest.decorations.tile.TileDecoration;
import by.errortest.decorations.client.ClientEvent;
import by.errortest.decorations.client.render.RenderDecor;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.lwjgl.input.Keyboard;

import java.util.HashMap;

public class ClientProxy extends CommonProxy {

    public static DecorModel selectedModel;
    private static final String CATEGORY = "Decorations";
    public static final KeyBinding SET_BLOCK_MODE = new KeyBinding("Режим установки блока", Keyboard.KEY_M, CATEGORY);
    public static final KeyBinding OPEN_SELECT_GUI = new KeyBinding("Выбор модели по умолчанию", Keyboard.KEY_N, CATEGORY);
    public static final HashMap<Integer, DecorModel> models = new HashMap<>();
    Config config = new Config();
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        OBJLoader.INSTANCE.addDomain(Main.MODID);
        MinecraftForge.EVENT_BUS.register(new ClientEvent());
        ClientRegistry.bindTileEntitySpecialRenderer(TileDecoration.class, new RenderDecor());
        ClientRegistry.registerKeyBinding(SET_BLOCK_MODE);
        ClientRegistry.registerKeyBinding(OPEN_SELECT_GUI);
        config.readConfig();
        super.preInit(event);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }


}
