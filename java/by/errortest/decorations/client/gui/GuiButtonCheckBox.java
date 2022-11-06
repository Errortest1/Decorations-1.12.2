package by.errortest.decorations.client.gui;

import by.errortest.decorations.Main;
import by.errortest.decorations.tile.TileDecoration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class GuiButtonCheckBox extends GuiButton {

    public boolean selected;
    private ResourceLocation texture = new ResourceLocation(Main.MODID, "textures/gui/setup_tools.png");
    public GuiButtonCheckBox(int buttonId, int x, int y, boolean bool, String buttonText) {
        super(buttonId, x + 20, y, 12, 12, buttonText);
        this.selected = bool;
    }

    public void onSelected() {
        if (selected)
            selected = false;
        else
            selected = true;
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
        if (this.visible) {
            GL11.glPushMatrix();
            mc.renderEngine.bindTexture(texture);
            if (hovered)
                GL11.glColor3d(0.5, 0.5, 0.5);

                drawTexturedModalRect(x, y, 73, 0, 20, 30);
                if (selected)
                    drawTexturedModalRect(x, y + 1, 65, 0, 7, 30);

            GL11.glPopMatrix();
            drawString(mc.fontRenderer, displayString, x - width - 30, y, 14737632);

        }
    }
}
