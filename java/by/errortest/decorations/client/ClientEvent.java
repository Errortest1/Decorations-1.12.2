package by.errortest.decorations.client;

import by.errortest.decorations.ClientProxy;
import by.errortest.decorations.Main;
import by.errortest.decorations.blocks.BlockDecoration;
import by.errortest.decorations.client.gui.GuiModelSelect;
import by.errortest.decorations.network.CreateBlockServerMessagePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class ClientEvent {
    private Minecraft mc = Minecraft.getMinecraft();

    private BlockPos blockPos;
    boolean setBlockMode;

    @SubscribeEvent
    public void onPress(InputEvent.KeyInputEvent e) {
        if (ClientProxy.SET_BLOCK_MODE.isKeyDown()) {
            if (!setBlockMode)
                setBlockMode = true;
            else
                setBlockMode = false;
        }

        if (ClientProxy.OPEN_SELECT_GUI.isKeyDown())
            FMLClientHandler.instance().showGuiScreen(new GuiModelSelect());

        if (Keyboard.isKeyDown(Keyboard.KEY_TAB) && setBlockMode && mc.inGameHasFocus && mc.player.isCreative()) {
            if (blockPos != null) {
                if (!blockPos.add(0, 1, 0).equals(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)) && !blockPos.add(0, 1, 0).equals(new BlockPos(mc.player.posX, mc.player.posY + 1, mc.player.posZ)))
                    Main.NETWORK.sendToServer(new CreateBlockServerMessagePacket(blockPos));
                else
                    mc.player.sendMessage(new TextComponentString("Вы не можете поставить блок в себе!"));
            }
            else
                if (mc.world.isRemote)
                mc.player.sendMessage(new TextComponentString("Наведитесь на блок, на который хотите установить декоративный блок"));
        }
    }

    @SubscribeEvent
    public void renderTick(RenderWorldLastEvent event) {
        if (setBlockMode)
            renderModel(event);
    }

    @SubscribeEvent
    public void onOverlayRender(RenderGameOverlayEvent.Post e) {
        if (e.getType() == RenderGameOverlayEvent.ElementType.TEXT && setBlockMode) {
            mc.fontRenderer.drawString("Нажмите [TAB] для установки блока", e.getResolution().getScaledWidth() / 2 - 65, e.getResolution().getScaledHeight() - 50, 14737632);
        }
    }

    private void renderModel(RenderWorldLastEvent event) {
        RayTraceResult mop = Minecraft.getMinecraft().objectMouseOver;
        if (mop != null) {
            if (mop.typeOfHit == RayTraceResult.Type.BLOCK) {
                    BlockPos blockPos = mop.getBlockPos();
                    double x =
                            (blockPos.getX()
                                    - Minecraft.getMinecraft().getRenderManager().viewerPosX);
                    double y =
                            (blockPos.getY()
                                    - Minecraft.getMinecraft().getRenderManager().viewerPosY);
                    double z =
                            (blockPos.getZ()
                                    - Minecraft.getMinecraft().getRenderManager().viewerPosZ);
                    GL11.glPushMatrix();
                    if (ClientProxy.selectedModel != null) {
                        GL11.glTranslated(x + 0.5, y + 1, z + 0.5);
                        mc.renderEngine.bindTexture(ClientProxy.selectedModel.getModelTexture());
                        ClientProxy.selectedModel.getModel().renderAll();
                    } else {
                        GL11.glDisable(GL11.GL_LIGHTING);
                        GL11.glEnable(GL11.GL_BLEND);
                        GL11.glDisable(GL11.GL_TEXTURE_2D);
                        GlStateManager.depthMask(false);
                        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                        RenderGlobal.renderFilledBox(new AxisAlignedBB(x, y + 1.0, z, x + 1.0, y + 2.0, z + 1.0), 0, 0.4f, 1, 0.3F);
                        GlStateManager.depthMask(true);
                        GL11.glEnable(GL11.GL_TEXTURE_2D);
                        GL11.glEnable(GL11.GL_LIGHTING);
                        GL11.glDisable(GL11.GL_BLEND);
                    }

                    GL11.glPopMatrix();

                    this.blockPos = blockPos;
            }
        }
    }


}