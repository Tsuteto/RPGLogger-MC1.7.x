package tsuteto.rpglogger.logging;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import tsuteto.rpglogger.LogWindowRenderer;
import tsuteto.rpglogger.RpgLogger;
import tsuteto.rpglogger.util.Utilities;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Gateway of logging message
 *
 * @author Tsuteto
 *
 */
public class RlLogManager
{
    public List<RlLogLine> logMsgList = new ArrayList<RlLogLine>();
    private int timeSinceLastLog = 0;
    private FontRenderer fontRenderer;
    private RpgLogger rpgLogger;

    public RlLogManager(RpgLogger rpgLogger)
    {
        Minecraft mc = FMLClientHandler.instance().getClient();
        this.fontRenderer = mc.fontRenderer;
        this.rpgLogger = rpgLogger;
    }

    public void clearLogList()
    {
        logMsgList.clear();
    }

    public void log(String s)
    {
        log(s, RlLogLine.DEFAULT_COLOR);
    }

    public void log(String s, Color color)
    {
        Minecraft mc = FMLClientHandler.instance().getClient();
        log(s, color, mc.thePlayer);
    }

    public synchronized void log(String s, Color color, EntityPlayer player)
    {
        s = Utilities.capitalize(s);
        if (rpgLogger.settings.isEnableExportLog())
        {
            rpgLogger.logFileWriter.writeLog(s, player);
        }
        addToList(s, color);
        timeSinceLastLog = 0;
    }

    private void addToList(String s, Color color)
    {
        int i;
        for (; fontRenderer.getStringWidth(s) > LogWindowRenderer.LOG_FIELD_WIDTH; s = s.substring(0, i))
        {
            for (i = 1; i < s.length()
                    && fontRenderer.getStringWidth(s.substring(0, i + 1)) <= LogWindowRenderer.LOG_FIELD_WIDTH; i++)
            {}
            addToList(s.substring(i), color);
        }

        logMsgList.add(0, new RlLogLine(s, color));
        if (logMsgList.size() > 50)
        {
            logMsgList.remove(logMsgList.size() - 1);
        }
    }

    public void addMsgTranslate(String s, Color color)
    {
        String s1 = rpgLogger.msgTrans.getMsg(s);
        log(s1, color);
    }

    public void addMsgTranslate(String s, Color color, Object... aobj)
    {
        String s1 = rpgLogger.msgTrans.getMsg(s, aobj);
        log(s1, color);
    }

    public void addMsgTranslate(EntityPlayer player, String s, Color color, Object... aobj)
    {
        String s1 = rpgLogger.msgTrans.getMsg(s, aobj);
        log(s1, color, player);
    }

    public void onTick()
    {
        timeSinceLastLog++;
    }

    public int getTimeSinceLastLog()
    {
        return timeSinceLastLog;
    }
}
