package by.errortest.decor.client.render;

import by.errortest.decor.Main;
import by.errortest.decor.blocks.IDecor;
import by.errortest.decor.tiles.TileDecor;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.client.model.obj.WavefrontObject;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;

public class RenderDecor extends TileEntitySpecialRenderer {
    public static final HashMap<String, IModelCustom> CACHE = new HashMap<>();
    private IModelCustom model;
    private String modelPath;
    private String texturePath;

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float f) {
        render((TileDecor) tile, x, y, z);
    }

    private void render(TileDecor tile, double x, double y, double z) {
        if (tile.getBlockType() instanceof IDecor) {
            modelPath = ((IDecor) tile.getBlockType()).getModelPath();
            texturePath = ((IDecor) tile.getBlockType()).getTexturePath();
        }
        if (!CACHE.containsKey(modelPath)) {
            model = new ModelWrapperDisplayList((WavefrontObject) AdvancedModelLoader.loadModel(new ResourceLocation
                    (Main.MODID, modelPath)));
            CACHE.put(modelPath, model);
        } else
            model = CACHE.get(modelPath);
        if (model != null) {
            bindTexture(new ResourceLocation
                    (Main.MODID, texturePath));
            GL11.glPushMatrix();
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glTranslated(x + 0.5d, y, z + 0.5d);
            this.setupRotation(tile);
            model.renderAll();
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();
        }
    }

    private void setupRotation(TileDecor tile) {
        if (tile != null) {
            switch(tile.getBlockMetadata()) {
                case 0:GL11.glRotatef(180F + 180F, 0.0F, 1.0F, 0.0F);break;
                case 1:GL11.glRotatef(90F + 180F, 0.0F, 1.0F, 0.0F);break;
                case 2:GL11.glRotatef(0F + 180F, 0.0F, 1.0F, 0.0F);break;
                case 3:GL11.glRotatef(180F + 90F + 180F, 0.0F, 1.0F, 0.0F);break;
            }
        }
    }
}