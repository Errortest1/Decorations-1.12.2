package by.errortest.decorations.client.gui;

import by.errortest.decorations.ClientProxy;
import by.errortest.decorations.Main;
import by.errortest.decorations.network.TileServerMessagePacket;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.text.TextComponentString;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

public class GuiModelSelect extends GuiScreen {

    private int scrollFactor, lineSize, scrollX = 90, scrollY = 16, scrollWidth = 5, scrollHeight = 300, currentScroll;

    @Override
    public void initGui() {
        scrollFactor = 0;
        currentScroll = 0;
        int o = 0;
        for (int i = 0; i < ClientProxy.models.size(); i++) {
            buttonList.add(new GuiButtonModelSelect(i, width - 85, (i * 16) + 17, ClientProxy.models.get(i)));
            o += 16;
        }
        lineSize = o;
    }

    @Override
    public void drawScreen(int var1, int var2, float f) {
        drawString(mc.fontRenderer, "Выбранная модель будет установлена по умолчанию", width / 2 - 100, 0, 14737632);
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
                b.drawButton(mc, var1, var2, f);
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
            ClientProxy.selectedModel = ((GuiButtonModelSelect) button).getDecorObject();
            mc.player.sendMessage(new TextComponentString("Выбрано: " + ChatFormatting.GREEN + ClientProxy.selectedModel.name));
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
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

}
