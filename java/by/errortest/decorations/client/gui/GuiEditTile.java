package by.errortest.decorations.client.gui;

import by.errortest.decorations.ClientProxy;
import by.errortest.decorations.Main;
import by.errortest.decorations.network.TileServerMessagePacket;
import by.errortest.decorations.tile.TileDecoration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import java.io.IOException;

public class GuiEditTile extends GuiScreen {
    private int[] rotate;
    private double[] translate;
    public TileDecoration tile;
    public TransformMode rotationMode = TransformMode.X, translationMode = TransformMode.X;
    private ResourceLocation tools = new ResourceLocation(Main.MODID, "textures/gui/setup_tools.png");
    private int scrollX = 90, scrollY = 16, scrollWidth = 5, scrollHeight, scrollFactor, lineSize, id, currentScroll;
    private double scale = 1.0;
    private boolean collide;

    public GuiEditTile(TileDecoration tile) {
        this.tile = tile;
        this.rotate = new int[]{tile.rotationX, tile.rotationY, tile.rotationZ};
        this.translate = new double[]{tile.translateX, tile.translateY, tile.translateZ};
        if (tile.scale != 0)
            this.scale = tile.scale;
        this.id = tile.modelId;
        this.collide = tile.collide;

    }


    @Override
    public void initGui() {
        scrollFactor = 0;
        currentScroll = 0;

        buttonList.add(new GuiButtonCheckBox(ClientProxy.models.size() + 1, 53, 100, tile.collide, "Коллизия: "));

        int o = 0;
        for (int i = 0; i < ClientProxy.models.size(); i++) {
            buttonList.add(new GuiButtonModelSelect(i, width - 85, (i * 16) + 17, ClientProxy.models.get(i)));
            o += 16;
        }
        lineSize = o;

    }


    @Override
    public void drawScreen(int var1, int var2, float f) {
        int col = 14737632;

        drawTools(col);

        drawScroll(mc, var1, var2, (int) f);

        GL11.glPushMatrix();
        drawString(fontRenderer, "Ось перемещения: "  + translationMode +  " | [Z] для переключения", 30, 30, col);
        drawString(mc.fontRenderer, "Ось поворота: "  + rotationMode + " | [X] для переключения", 30, 40, col);
        drawString(mc.fontRenderer, "Смещение: "  + "x: " + (float)translate[0] + " y: " + (float)translate[1] + " z: " + (float)translate[2], 30, 50, col);
        drawString(mc.fontRenderer, "Поворот: "  + "x: " + rotate[0] + " y: " + rotate[1] + " z: " + rotate[2], 30, 60, col);
        drawString(mc.fontRenderer, "Размер: "  + (float)scale, 30, 70, col);
        drawString(mc.fontRenderer, "[TAB] для отмены трансформаций", 30, 80, col);
        GL11.glPopMatrix();


        for (int k = 0; k < buttonList.size(); ++k) {
            if (!(buttonList.get(k) instanceof GuiButtonModelSelect))
            buttonList.get(k).drawButton(mc, var1, var2, f);
        }


    }

    private void drawTools(int col) {
        GL11.glPushMatrix();
        mc.renderEngine.bindTexture(tools);
        GL11.glTranslated(width/2 - 100, height - 70, 0);
        GL11.glColor3d(0, 1, 0);
        drawTexturedModalRect(0, 0, 0, 0, 65, 32);
        GL11.glColor3d(1, 1, 1);
        GL11.glColor3d(0, 0, 1);
        drawTexturedModalRect(70, 0, 0, 32, 70, 33);
        GL11.glColor3d(1, 1, 1);
        GL11.glColor3d(1, 0, 0);
        drawTexturedModalRect(140, 0, 32, 65, 32, 36);
        drawTexturedModalRect(170, 0, 0, 65, 31, 36);
        GL11.glColor3d(1, 1, 1);
        drawString(mc.fontRenderer, "[1]", 11, 33, col);
        drawString(mc.fontRenderer, "[2]", 41, 33, col);
        drawString(mc.fontRenderer, "[3]", 81, 33, col);
        drawString(mc.fontRenderer, "[4]", 112, 33, col);
        drawString(mc.fontRenderer, "[5]", 152, 33, col);
        drawString(mc.fontRenderer, "[6]", 183, 33, col);
        GL11.glPopMatrix();
    }

    private void drawScroll(Minecraft mc, int x, int y, int f) {
        float translateScroll = 0;
        translateScroll += currentScroll;
        scrollHeight = height / 2 + 50;

        ScaledResolution r = new ScaledResolution(mc);
        int s = r.getScaleFactor();
        int translatedY = r.getScaledHeight() - scrollY - scrollHeight;
        Gui.drawRect(width - scrollX, scrollY, width - scrollWidth, scrollHeight + 17, 0xff212121);
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((width - scrollX) * s, translatedY * s + 2, (width - scrollWidth) * s ,scrollHeight * s - 7);

        for (int k = 0; k < buttonList.size(); ++k) {
            if (buttonList.get(k) instanceof GuiButtonModelSelect) {
                GuiButtonModelSelect b = (GuiButtonModelSelect) buttonList.get(k);
                b.drawButton(mc, x, y, f);
                b.y += translateScroll;
                if (b.y > 312 || b.y < 5)
                    b.enabled = false;
                else
                    b.enabled = true;
            }
        }

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GL11.glPopMatrix();

        currentScroll = 0;
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button instanceof GuiButtonModelSelect) {
            for (int i = 0; i < ClientProxy.models.size(); i++) {
                if (button.id == i) {
                    id = ((GuiButtonModelSelect) button).getDecorObject().id;
                    Main.NETWORK.sendToServer(new TileServerMessagePacket(rotate[0], rotate[1], rotate[2], translate[0], translate[1], translate[2], scale, collide, tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), id));
                }
            }
        }
        if (button instanceof GuiButtonCheckBox) {
            ((GuiButtonCheckBox) button).onSelected();
            if (button.id == ClientProxy.models.size() + 1)
                collide = ((GuiButtonCheckBox) button).selected;
        }

    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int n;

        int dw = Mouse.getEventDWheel();
        if (dw != 0) {
            if (dw > 0) {
                dw = -1;
            } else {
                dw = 1;
            }

            if (lineSize > scrollHeight) {
                n = lineSize - scrollHeight;
                if (scrollFactor < n && dw > 0) {
                    currentScroll -= dw * 5f;
                    scrollFactor += dw * 5f;
                } else if (scrollFactor > 0 && dw < 0) {
                    currentScroll -= dw * 5f;
                    scrollFactor += dw * 5f;
                }
            }
        }
    }

    @Override
    public void onGuiClosed() {
        Main.NETWORK.sendToServer(new TileServerMessagePacket(rotate[0], rotate[1], rotate[2], translate[0], translate[1], translate[2], scale, collide, tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), id));
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        keyboardListener();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);

        if (Keyboard.isKeyDown(Keyboard.KEY_3)) {
            switch (rotationMode) {
                case Y:
                    if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
                        this.rotate[1] += 90;
                    break;
            }
        } else if (Keyboard.isKeyDown(Keyboard.KEY_4)) {
            switch (rotationMode) {
                case Y:
                    if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
                        this.rotate[1] -= 90;
                    break;
            }
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_Z)) {
            switch (translationMode) {
                case X:
                    translationMode = TransformMode.Y;
                    break;
                case Y:
                    translationMode = TransformMode.Z;
                    break;
                case Z:
                    translationMode = TransformMode.X;
                    break;
            }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_X)) {
            switch (rotationMode) {
                case X:
                    rotationMode = TransformMode.Y;
                    break;
                case Y:
                    rotationMode = TransformMode.Z;
                    break;
                case Z:
                    rotationMode = TransformMode.X;
                    break;
            }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_TAB))
            resetAll();

    }

    private void keyboardListener() {
        tile.rotationX = rotate[0];
        tile.rotationY = rotate[1];
        tile.rotationZ = rotate[2];
        tile.translateX = translate[0];
        tile.translateY = translate[1];
        tile.translateZ = translate[2];
        tile.scale = scale;

        if (Keyboard.isKeyDown(Keyboard.KEY_1)) {
            switch (translationMode) {
                case X:
                    this.translate[0] += 0.04;
                    break;
                case Y:
                    this.translate[1] += 0.04;
                    break;
                case Z:
                    this.translate[2] += 0.04;
                    break;
            }

        } else if (Keyboard.isKeyDown(Keyboard.KEY_2)) {
            switch (translationMode) {
                case X:
                    this.translate[0] -= 0.04;
                    break;
                case Y:
                    this.translate[1] -= 0.04;
                    break;
                case Z:
                    this.translate[2] -= 0.04;
                    break;
            }

        } else if (Keyboard.isKeyDown(Keyboard.KEY_3)) {
            switch (rotationMode) {
                case X:
                    this.rotate[0] += 2;
                    break;
                case Y:
                    if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
                        this.rotate[1] += 2;
                    break;
                case Z:
                    this.rotate[2] += 2;
                    break;
            }
        } else if (Keyboard.isKeyDown(Keyboard.KEY_4)) {
            switch (rotationMode) {
                case X:
                    this.rotate[0] -= 2;
                    break;
                case Y:
                    if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
                        this.rotate[1] -= 2;
                    break;
                case Z:
                    this.rotate[2] -= 2;
                    break;
            }
        } else if (Keyboard.isKeyDown(Keyboard.KEY_5)) {
            scale-=0.04;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_6)) {
            scale+=0.04;
        }
    }


    private void resetAll() {
        rotate[0] = 0;
        rotate[1] = 0;
        rotate[2] = 0;
        translate[0] = 0;
        translate[1] = 0;
        translate[2] = 0;
        scale = 1;
    }


    public enum TransformMode {
        X ("X"),
        Y ("Y"),
        Z ("Z");

        TransformMode(String title) {
        }
    }


}
