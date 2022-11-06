package by.errortest.decorations.client.gui;

import by.errortest.decorations.ClientProxy;
import by.errortest.decorations.DecorModel;
import by.errortest.decorations.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiButtonModelSelect extends GuiButton {
    private DecorModel decorModel;
    public GuiButtonModelSelect(int buttonId, int x, int y, DecorModel decorModel) {
        super(buttonId, x, y, 75, 15, decorModel.name);
        this.decorModel = decorModel;
    }

    public DecorModel getDecorObject() {
        return this.decorModel;
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

        if (this.visible) {
            GL11.glPushMatrix();
            int j = 14737632;
            int l = Integer.MIN_VALUE;
            if (hovered) {
                j = 16777120;
                l = Integer.MAX_VALUE;
            }

            if (decorModel == ClientProxy.selectedModel) {
                l += 16777120 / 4;
            }

            StringBuilder strBuilder = new StringBuilder(displayString);

            if (strBuilder.length() > 17)
                strBuilder.replace(17, strBuilder.length(), "...");

            drawRect(x, y, x + 75, y + 15, l);
            drawString(mc.fontRenderer, String.valueOf(strBuilder), x + 5, y + 3, j);
            GL11.glPopMatrix();
        }
    }

}
