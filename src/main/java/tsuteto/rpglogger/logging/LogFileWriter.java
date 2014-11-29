package tsuteto.rpglogger.logging;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import tsuteto.rpglogger.RpgLogger;
import tsuteto.rpglogger.param.ParamWorld;

/**
 * Handles writing log to an external file
 *
 * @author Tsuteto
 *
 */
public final class LogFileWriter
{
    static final String DIR_NAME = "RPGLogger";
    static final String EXT_NAME = "log";
    static final String CHARSET = "UTF-8";
    private File logDir;
    private File logFile;
    private PrintWriter pw = null;
    private SimpleDateFormat dateFormatter;

    public LogFileWriter(String worldName)
    {
        logDir = new File(Minecraft.getMinecraft().mcDataDir, DIR_NAME);
        logFile = new File(logDir, worldName + "." + EXT_NAME);
        dateFormatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        if (!logDir.exists())
        {
            logDir.mkdir();
        }

        boolean autoFlash = true;
        boolean append = true;
        try
        {
            pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logFile, append),
                    CHARSET)), autoFlash);

        }
        catch (IOException e)
        {
            RpgLogger.errorLog(e, "Failed to open file: " + logFile.getAbsolutePath());
        }
    }

    public void writeLog(String msg, EntityPlayer player)
    {
        if (pw == null || msg == null || msg.length() == 0)
        {
            return;
        }
        String currtime = dateFormatter.format(new Date());
        String worldTime = getFormattedWorldTime(player.worldObj);

        msg = msg.replaceAll("\247.", "");

        pw.println(String.format("%s %s (%d,%d,%d) %s", currtime, worldTime, (int) player.posX, (int) player.posY,
                (int) player.posZ, msg));
    }

    public void showLogFile()
    {
        if (!Desktop.isDesktopSupported())
        {
            RpgLogger.errorLog("Failed to show log file. This operation is not supported by your system");
            return;
        }

        if (!logFile.exists())
        {
            RpgLogger.errorLog("Could not find log file: " + logFile.getAbsolutePath());
            return;
        }

        Desktop desktop = Desktop.getDesktop();
        try
        {
            desktop.open(logFile);
        }
        catch (IOException e)
        {
            RpgLogger.errorLog(e, "Failed to show log file: " + logFile.getAbsolutePath());
        }
    }

    private String getFormattedWorldTime(World world)
    {
        long virtualWorldTime = world.getWorldTime() + 6000;
        long days = virtualWorldTime / 24000 + 1;
        int hours = (int) (virtualWorldTime % 24000) / 1000;
        int minutes = (int) (60 * ((virtualWorldTime % 1000) / 1000F));
        return String.format("%dd %02d:%02d", days, hours, minutes);
    }

    public void closeLogfile()
    {
        try
        {
            if (pw != null)
            {
                pw.close();
            }
        }
        catch (Exception e)
        {}
    }

    @Override
    protected void finalize() throws Throwable
    {
        closeLogfile();
        super.finalize();
    }
}
