package by.errortest.decorations.client.render;

import by.errortest.decorations.ClientProxy;
import by.errortest.decorations.Main;
import by.errortest.decorations.blocks.BlockDecoration;
import by.errortest.decorations.client.gui.GuiEditTile;
import by.errortest.decorations.tile.TileDecoration;
import by.errortest.decorations.DecorModel;
import com.sun.javafx.scene.traversal.Direction;
import io.netty.handler.codec.http2.Http2FrameLogger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import ru.d33.graphics.model.IModelCustom;

import java.awt.*;
import java.util.HashMap;

import static by.errortest.decorations.blocks.BlockDecoration.FACING;
import static java.lang.Math.*;
import static org.lwjgl.opengl.GL11.*;

@SideOnly(Side.CLIENT)
public class RenderDecor extends TileEntitySpecialRenderer {
    public static final HashMap<Integer, IModelCustom> CACHE = new HashMap<>();
    private IModelCustom model;
    private GuiEditTile gui;

    public void render(TileEntity tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        TileDecoration tileDecor = (TileDecoration) tile;
        DecorModel decorModel = ClientProxy.models.get(tileDecor.modelId);
            if (!CACHE.containsKey(tileDecor.modelId)) {
                model = decorModel.getModel();
                CACHE.put(tileDecor.modelId, model);
            } else {
                model = CACHE.get(tileDecor.modelId);
            }
            if (model != null) {
                bindTexture(decorModel.getModelTexture());
                GL11.glPushMatrix();
                GL11.glColor3d(1, 1, 1);
                glBlendFunc(GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glTranslated(x + 0.5, y, z + 0.5);
                GL11.glTranslated(((TileDecoration) tile).translateX, ((TileDecoration) tile).translateY, ((TileDecoration) tile).translateZ);
                GL11.glRotated(((TileDecoration) tile).rotationX, 1d, 0d, 0d);
                GL11.glRotated(((TileDecoration) tile).rotationY, 0d, 1, 0d);
                GL11.glRotated(((TileDecoration) tile).rotationZ, 0d, 0d, 1d);


                if (((TileDecoration) tile).scale != 0.0)
                    GL11.glScaled(((TileDecoration) tile).scale, ((TileDecoration) tile).scale, ((TileDecoration) tile).scale);
                model.renderAll();
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glPopMatrix();


                if (Minecraft.getMinecraft().currentScreen instanceof GuiEditTile) {
                    gui = (GuiEditTile) Minecraft.getMinecraft().currentScreen;
                    if (tile == gui.tile) {
                        renderLine(x, y, z, ((TileDecoration) tile).translateX, ((TileDecoration) tile).translateY, ((TileDecoration) tile).translateZ, gui.translationMode, (TileDecoration) tile);
                    }
                }
                renderCreativeBlock(x, y, z);
            }
    }

    void renderCreativeBlock(double x, double y, double z) {
        if (Minecraft.getMinecraft().player.isCreative()) {
            GL11.glPushMatrix();
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GlStateManager.depthMask(false);
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            RenderGlobal.renderFilledBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0), 0, 0.4f, 1, 0.3F);
            GlStateManager.depthMask(true);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();

        }
    }

    void renderLine(double tileX, double tileY, double tileZ, double modelX, double modelY, double modelZ, GuiEditTile.TransformMode mode, TileDecoration tile) {
        GL11.glPushMatrix();
        GL11.glLineWidth(2F);
        GL11.glDisable(GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBegin(GL_LINES);
            switch (mode) {
                case X:
                GL11.glColor3d(0, 1.0, 0);
                GL11.glVertex3d(tileX - 100 + tile.rotationX / 10, tileY + 0.5 + modelY, tileZ + 0.5 + modelZ); // x
                GL11.glVertex3d(tileX + 100 + tile.rotationX / 10, tileY + 0.5 + modelY, tileZ + 0.5 + modelZ); // x
                    break;
                case Y:
                GL11.glColor3d(0, 0, 1);
                GL11.glVertex3d(tileX + modelX + 0.5, tileY - 100, tileZ + 0.5 + modelZ); //y
                GL11.glVertex3d(tileX + modelX + 0.5, tileY + 100, tileZ + 0.5 + modelZ); //y
                    break;
                case Z:
                GL11.glColor3d(1, 0, 0);
                GL11.glVertex3d(tileX + modelX + 0.5, tileY + modelY + 0.5, tileZ - 100); // z
                GL11.glVertex3d(tileX + modelX + 0.5, tileY + modelY + 0.5, tileZ + 100); // z
                    break;
            }
                GL11.glEnd();
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glDisable(GL_LIGHTING);
                GL11.glPopMatrix();
        }
}
