package tsuteto.rpglogger.logging;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import tsuteto.rpglogger.RpgLogger;
import tsuteto.rpglogger.settings.LogType;
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
    private int updateTick = 0;
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
    }

    private void addToList(String s, Color color)
    {
        RlLogLine newLog = new RlLogLine(s, color, updateTick);

        RpgLogger.getInstance().logWindowGui.addToList(newLog);

        logMsgList.add(0, newLog);
        if (logMsgList.size() > 500)
        {
            logMsgList.remove(logMsgList.size() - 1);
        }
    }

    public void addMsgTranslate(String s, Color color, Object... aobj)
    {
        this.addMsgTranslate((EntityPlayer)null, s, color, aobj);
    }

    public void addMsgTranslate(EntityPlayer player, String s, Color color, Object... aobj)
    {
        String key = s.replace('.', '_');
        this.addMsgTranslate(LogType.parse(key), player, s, color, aobj);
    }

    public void addMsgTranslate(LogType logType, String s, Color color, Object... aobj)
    {
        this.addMsgTranslate(logType, null, s, color, aobj);
    }

    public void addMsgTranslate(LogType logType, EntityPlayer player, String s, Color color, Object... aobj)
    {
        if (logType == null || rpgLogger.settings.isLogEnabled(logType))
        {
            String s1 = rpgLogger.msgTrans.getMsg(s, aobj);
            if (player != null)
            {
                log(s1, color, player);
            }
            else
            {
                log(s1, color);
            }
        }
    }

    public int getUpdateTick()
    {
        return this.updateTick;
    }

    public void onTick()
    {
        updateTick++;
    }
}
