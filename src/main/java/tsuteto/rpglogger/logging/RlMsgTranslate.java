package tsuteto.rpglogger.logging;

import com.google.common.base.Strings;
import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.util.StatCollector;
import org.apache.commons.lang3.StringUtils;
import tsuteto.rpglogger.Lang;
import tsuteto.rpglogger.RpgLogger;

import java.io.*;
import java.util.InvalidPropertiesFormatException;
import java.util.Locale;
import java.util.Properties;

/**
 * Translates key to message in a specified language
 *
 * @author Tsuteto
 *
 */
public class RlMsgTranslate
{
    private static RlMsgTranslate instance;

    private Properties rlTranslateTable;
    private Properties customTranslateTable = null;
    private File customLangFile;
    private long customLangLastModified = -1;
    public boolean islangFileUpdated = false;
    public boolean isCustomFileLoaded = false;
    public Lang lang = null;

    private static final String rlLangFileDir = "/assets/rpglogger/rlang";
    private static final String customLangFileDir = "RPGLogger/lang";

    private RlMsgTranslate(Lang lang)
    {
        loadLangFile(lang);
    }

    public static void createInstance(Lang lang)
    {
        instance = new RlMsgTranslate(lang);
    }

    public static RlMsgTranslate getInstance()
    {
        return instance;
    }

    public void setLanguage(Lang lang)
    {
        loadLangFile(lang);
    }

    public void loadLangFile(Lang lang)
    {
        Minecraft mc = FMLClientHandler.instance().getClient();

        boolean isLangChanged = this.lang != lang;

        this.lang = lang;
        islangFileUpdated = false;

        // Set a new lang file path
        String customLangFileName = lang.getCustomFileName();
        customLangFile = new File(Minecraft.getMinecraft().mcDataDir, customLangFileDir + "/" + customLangFileName);

        if (!customLangFile.getParentFile().exists())
        {
            // Create a directory
            if (!customLangFile.getParentFile().mkdirs())
            {
                RpgLogger.errorLog("Couldn't make a directory for custom lang files");
            }
        }

        // Check modification of custom lang file
        if (!isCustomFileLoaded || isLangChanged)
        {
            customLangLastModified = customLangFile.lastModified();
        }
        else if (customLangLastModified == customLangFile.lastModified())
        {
            return;
        }
        else
        {
            customLangLastModified = customLangFile.lastModified();
        }

        if (isLangChanged)
        {
            rlTranslateTable = new Properties();
            try
            {
                rlTranslateTable.load(new InputStreamReader(RlMsgTranslate.class.getResourceAsStream(rlLangFileDir + "/" + lang.getFileName()), "UTF-8"));
            }
            catch (Exception e)
            {
                RpgLogger.errorLog(e, "Failed to read RPG Logger lang file");
            }
        }

        customTranslateTable = new Properties();
        isCustomFileLoaded = false;

        if (!customLangFile.exists())
        {
            saveCustomLangFile();
            customLangLastModified = customLangFile.lastModified();
        }

        // Change locale to English temporarily
        Locale originalLocale = Locale.getDefault();
        Locale.setDefault(Locale.ENGLISH);

        InputStreamReader reader = null;

        try
        {
            // Read as old format first
            try
            {
                FileInputStream fis = new FileInputStream(customLangFile);
                customTranslateTable.loadFromXML(fis);
                isCustomFileLoaded = true;
            }
            catch (InvalidPropertiesFormatException e)
            {
                if (!"Content is not allowed in prolog.".equals(e.getCause().getMessage()))
                {
                    RpgLogger.errorLog(e, "Failed to read custom lang file due to invalid format");
                    return;
                }
            }
            catch (IOException e)
            {
                RpgLogger.errorLog(e, "Failed to read custom lang file");
                return;
            }

            if (!isCustomFileLoaded)
            {
                // Read as version 3 format
                try
                {
                    reader = new InputStreamReader(new FileInputStream(customLangFile), "UTF-8");
                    customTranslateTable.load(reader);
                    isCustomFileLoaded = true;
                }
                catch (IOException e)
                {
                    RpgLogger.errorLog(e, "Failed to read custom lang file");
                }
                finally
                {
                    try
                    {
                        if (reader!= null) reader.close();
                    }
                    catch (IOException ignored)
                    {}
                }
            }

            if (isCustomFileLoaded)
            {
                islangFileUpdated = true;
                RpgLogger.infoLog("Loaded custom lang file: " + customLangFileName);
            }
        }
        finally
        {
            // Restore the locale
            Locale.setDefault(originalLocale);
        }
    }

    public void saveCustomLangFile()
    {
        OutputStreamWriter writer = null;
        try
        {
            writer = new OutputStreamWriter(new FileOutputStream(customLangFile), "UTF-8");
            customTranslateTable.store(writer, "RPG Logger - Custom lang file");
            customLangLastModified = customLangFile.lastModified();
            // RpgLogger.systemLog("Saved custom lang File: " + customLangFile.getName());
        }
        catch (IOException e)
        {
            RpgLogger.errorLog(e, "Failed to save custom lang file");
        }
        finally
        {
            try
            {
                if (writer != null)
                {
                    writer.close();
                }
            }
            catch (IOException ignored) {}
        }
    }

    public String getMsg(String s)
    {
        return getMsgOrDefault(s, s);
    }

    public String getMsg(String s, Object... aobj)
    {
        String s1 = getMsg(s);
        return String.format(s1, aobj);
    }

    public String getMsgOrDefault(String key, String defaultValue)
    {
        String msg;
        msg = customTranslateTable.getProperty(key);
        if (StringUtils.isNotEmpty(msg)) // Take one that is not blank
        {
            return msg;
        }
        msg = rlTranslateTable.getProperty(key);
        if (msg != null)
        {
            return msg;
        }
        msg = StatCollector.translateToLocal(key); // spitted back the key string if no message found
        if (!msg.equals(key))
        {
            return msg;
        }
        return defaultValue;
    }

    public String translateNamedKey(String s)
    {
        String name = customTranslateNamedKey(s);
        // RpgLogger.systemLog("Custom: " + name);
        if (name != null)
        {
            return name;
        }
        name = rlTranslateNamedKey(s);
        // RpgLogger.systemLog("RL translate: " + name);
        if (name != null)
        {
            return name;
        }
        name = StatCollector.translateToLocal(s + ".name"); // spitted back the key string if no message found
        //RpgLogger.systemLog("Internal translate: " + name);
        if (!name.equals(s + ".name"))
        {
            return name;
        }
        return null;
    }

    public String rlTranslateNamedKey(String s)
    {
        String name = rlTranslateTable.getProperty(s + ".name");
        if (name != null && name.length() != 0)
        {
            return name;
        }
        return null;
    }

    public String customTranslateNamedKey(String s)
    {
        String name = customTranslateTable.getProperty(s + ".name");
        if (name != null && name.length() != 0)
        {
            return name;
        }
        return null;
    }

    public boolean isKeyAvailable(String s)
    {
        if (!StatCollector.translateToLocal(s).equals(s))
        {
            return true;
        }
        if (rlTranslateTable.containsKey(s))
        {
            return true;
        }
        String prop = customTranslateTable.getProperty(s);
        if (prop != null && prop.length() > 0)
        {
            return true;
        }
        return false;
    }

    public boolean addNAKeyToCustomTable(String key)
    {
        return addNAKeyToCustomLang(key, "");
    }

    public boolean addNAKeyToCustomLang(String key, String value)
    {
        if (!customTranslateTable.containsKey(key))
        {
            customTranslateTable.setProperty(key, value);
            RpgLogger.infoLog("Added key: " + key);
            return true;
        }
        return false;
    }
}
