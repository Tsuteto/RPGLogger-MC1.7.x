package tsuteto.rpglogger;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

import org.lwjgl.opengl.GL11;

import tsuteto.rpglogger.logging.RlLogLine;
import tsuteto.rpglogger.logging.RlLogManager;
import tsuteto.rpglogger.settings.EnumWindowPosition;
import tsuteto.rpglogger.settings.RpgLoggerSettings;


/**
 * Renders the RPG logger window
 *
 * @author Tsuteto
 *
 */
public class LogWindowRenderer
{
    public static final int LOG_FIELD_WIDTH = 200;

    public void renderGameOverlay(Minecraft mc)
    {
        RpgLoggerSettings settings = RpgLogger.getInstance().settings;

        if (mc.gameSettings.showDebugInfo || settings.getLines() == 0)
        {
            return;
        }

        double opacity = settings.getOpacity();
        int logSize = settings.getLines();
        int scale = settings.getScale();
        EnumWindowPosition position = settings.getPosition();
        ScaledResolution scaledresolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        int guiScale = scaledresolution.getScaleFactor();
        float logScaleFactor;
        if (settings.getScale() != 0 && settings.getScale() < guiScale)
        {
            logScaleFactor = (float) guiScale / (float) settings.getScale();
        }
        else
        {
            logScaleFactor = 1;
        }
        int guiW = scaledresolution.getScaledWidth();
        int guiH = scaledresolution.getScaledHeight();
        FontRenderer fontrenderer = mc.fontRenderer;
        Gui gui = new Gui();
        List<RlLogLine> logMsgList = RpgLogger.getInstance().logger.logMsgList;
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glScalef(1.0F / logScaleFactor, 1.0F / logScaleFactor, 1.0F);
        // GL11.glTranslatef(0.0F, 0.0F, 0.0F);
        for (int k2 = 0; k2 < logMsgList.size() && k2 < logSize; k2++)
        {
            RlLogLine logLine = logMsgList.get(k2);
            double d = logLine.updateCounter / 200D;
            d = 1.0D - d;
            d *= 10D;
            if (d < 0.2D)
            {
                d = 0.0D;
            }
            if (d > opacity)
            {
                d = opacity;
            }
            d *= d;
            int k3 = (int) (255D * d);
            if (k3 > 0)
            {
                String s1 = logLine.message;
                int y;
                if (position == EnumWindowPosition.TOP_LEFT)
                {
                    y = 2;
                }
                else if (position == EnumWindowPosition.TOP_CENTER)
                {
                    y = (int) (guiW * logScaleFactor / 2 - LOG_FIELD_WIDTH / 2);
                }
                else
                {
                    y = (int) (guiW * logScaleFactor - LOG_FIELD_WIDTH - 2);
                }
                int x = 2 + k2 * 9;
                gui.drawRect(y, x - 1, y + LOG_FIELD_WIDTH, x + 8, k3 / 2 << 24);
                GL11.glEnable(3042 /* GL_BLEND */);
                fontrenderer.drawString(s1, y, x, logLine.color.getRGB() + (k3 << 24));
            }
        }
        // GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glPopMatrix();
    }

    public void updateTick()
    {

        RlLogManager logMgr = RpgLogger.getInstance().logger;
        for (int i = 0; i < logMgr.logMsgList.size(); i++)
        {
            logMgr.logMsgList.get(i).updateCounter++;
        }
        logMgr.onTick();
    }

}
