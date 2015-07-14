package tsuteto.rpglogger.settings.fml;

import com.google.common.collect.Maps;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.config.Configuration;
import tsuteto.rpglogger.Lang;
import tsuteto.rpglogger.RPGLoggerLoader;
import tsuteto.rpglogger.RpgLogger;
import tsuteto.rpglogger.battle.EnumEnemyIdType;
import tsuteto.rpglogger.logging.RlMsgTranslate;
import tsuteto.rpglogger.settings.EnumWindowPosition;
import tsuteto.rpglogger.settings.LogCategory;
import tsuteto.rpglogger.settings.LogType;
import tsuteto.rpglogger.settings.OptionsApi;
import tsuteto.rpglogger.util.Utilities;

import java.util.EnumMap;

/**
 * Handles RPG Logger settings for ForgeModLoader
 *
 * @author Tsuteto
 *
 */
public class OptionsUsingFml implements OptionsApi
{
    public static final String CAT_DISPLAY = "display";
    public static final String CAT_MESSAGE = "message";
    public static final String CAT_SYSTEM = "system";

    public Configuration conf;

    public int opacity = 80;
    public int lines = 15;
    public boolean isExportLogEnabled = true;
    public int scale = 0;
    public int position = 0;
    public int windowWidth = 200;
    public int allyPrefix = 0;
    public String language = "auto";
    public String enemyIdType = "A";
    public int memoryAlert = 90;
    public boolean updateCheck = true;

    public EnumMap<LogType, Boolean> logSwitchMap = Maps.newEnumMap(LogType.class);

    public OptionsUsingFml(Configuration conf)
    {
        this.conf = conf;
        this.syncConfig();
    }

    public void syncConfig()
    {
        this.opacity = conf.get(CAT_DISPLAY, "opacity", opacity, "Percentage of opacity for the log window.")
                .setMinValue(10).setMaxValue(100)
                .setLanguageKey("rpglogger.options.opacity")
                .setConfigEntryClass(GuiConfigEntries.NumberSliderEntry.class)
                .getInt();

        this.lines = conf.get(CAT_DISPLAY, "lines", lines, "Number of lines in the log window.")
                .setMinValue(0).setMaxValue(50)
                .setLanguageKey("rpglogger.options.lines")
                .setConfigEntryClass(GuiConfigEntries.NumberSliderEntry.class)
                .getInt();

        this.scale = conf.get(CAT_DISPLAY, "scale", scale, "Log window's scale, text size. 0=adjust to the game settings, 1=small, 2=normal and 3=large.")
                .setValidValues(new String[]{"0", "1", "2", "3"})
                .setLanguageKey("rpglogger.options.scale")
                .setConfigEntryClass(CycleValueTranslateEntry.class)
                .getInt();

        this.position = conf.get(CAT_DISPLAY, "position", position, "Log window's position. 0=top-right, 1=top-center and 2=top-left.")
                .setValidValues(new String[]{"0", "1", "2"})
                .setLanguageKey("rpglogger.options.position")
                .setConfigEntryClass(CycleValueTranslateEntry.class)
                .getInt();

        this.windowWidth = conf.get(CAT_DISPLAY, "width", this.windowWidth, "Log window's width.")
                .setMinValue(100).setMaxValue(320)
                .setLanguageKey("rpglogger.options.width")
                .setConfigEntryClass(GuiConfigEntries.NumberSliderEntry.class)
                .getInt();

        this.language = conf.get(CAT_MESSAGE, "language", language, "Language in logger messages. auto=follow the game settings, ja_JP=Japanese, ja_kana_JP=Japanese kana, en_US=English.")
                .setValidValues(new String[]{"auto", "ja_JP", "ja_kana_JP", "en_US"})
                .setLanguageKey("rpglogger.options.language")
                .setConfigEntryClass(CycleValueTranslateEntry.class)
                .getString();

        this.allyPrefix = conf.get(CAT_MESSAGE, "allyPrefix", position, "Prefix type of your ally. 0-10")
                .setValidValues(new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"})
                .setLanguageKey("rpglogger.options.allyPrefix")
                .setConfigEntryClass(CycleValueAllyPrefixEntry.class)
                .getInt();

        this.enemyIdType = conf.get(CAT_MESSAGE, "enemyIdType", enemyIdType, "Type of enemy identification. A=A B C..., N=1 2 3..., off=none")
                .setValidValues(new String[]{"A", "N", "off"})
                .setLanguageKey("rpglogger.options.enemyIdType")
                .setConfigEntryClass(CycleValueTranslateEntry.class)
                .getString();

        this.memoryAlert = conf.get(CAT_MESSAGE, "memoryAlert", memoryAlert, "Threshold percentage of usage memory for alert message. -1=Never")
                .setMinValue(-1).setMaxValue(100)
                .setLanguageKey("rpglogger.options.memoryAlert")
                .setConfigEntryClass(GuiConfigEntries.NumberSliderEntry.class)
                .getInt();

        this.isExportLogEnabled = conf.get(CAT_SYSTEM, "enableExportLog", isExportLogEnabled, "Toggle exporting log to file. 'true' or 'false'.")
                .setLanguageKey("rpglogger.options.exportLog")
                .getBoolean(isExportLogEnabled);

        this.updateCheck = conf.get(CAT_SYSTEM, "updateCheck", updateCheck, "Check this mod is up to date or not.")
                .setRequiresMcRestart(true)
                .setLanguageKey("rpglogger.options.updateCheck")
                .getBoolean(updateCheck);

        for (LogType logType : LogType.values())
        {
            String[] items = logType.name().split("_");
            LogCategory cat;
            try
            {
                cat = LogCategory.valueOf(items[0]);
            }
            catch (IllegalArgumentException e)
            {
                cat = LogCategory.misc;
            }
            String langKey = logType.name().replace('_', '.');

            boolean isEnabled = conf.get(cat.getFullName(), logType.name(), true)
                    .setLanguageKey("rpglogger.options." + langKey)
                    .getBoolean(true);
            this.logSwitchMap.put(logType, isEnabled);
        }

        conf.getCategory(CAT_DISPLAY).setLanguageKey("rpglogger.options.category.display");
        conf.getCategory(CAT_MESSAGE).setLanguageKey("rpglogger.options.category.message");
        conf.getCategory(CAT_SYSTEM).setLanguageKey("rpglogger.options.category.system");

        for (LogCategory cat : LogCategory.values())
        {
            conf.getCategory(cat.getFullName()).setLanguageKey("rpglogger.options.category." + cat.name());
        }

        if (conf.hasChanged())
        {
            conf.save();
        }
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs)
    {
        if(eventArgs.modID.equals(RPGLoggerLoader.modid))
        {
            this.syncConfig();

            RlMsgTranslate msgTrans = RpgLogger.getInstance().msgTrans;
            Lang newLang = RpgLogger.getInstance().settings.getLanguage();
            if (msgTrans.lang != newLang)
            {
                msgTrans.setLanguage(Utilities.chooseLanguage(newLang));
            }
        }
    }

    @Override
    public float getOpacity()
    {
        return this.opacity / 100F;
    }

    @Override
    public int getLines()
    {
        return this.lines;
    }

    @Override
    public boolean isExportLogEnabled()
    {
        return this.isExportLogEnabled;
    }

    @Override
    public int getScale()
    {
        return this.scale;
    }

    @Override
    public EnumWindowPosition getPosition()
    {
        return EnumWindowPosition.values()[this.position];
    }

    @Override
    public int getWindowWidth()
    {
        return this.windowWidth;
    }

    @Override
    public int getAllyPrefix()
    {
        return this.allyPrefix;
    }

    @Override
    public Lang getLanguage()
    {
        return Lang.valueOf(this.language);
    }

    @Override
    public EnumEnemyIdType getEnemyIdType()
    {
        return EnumEnemyIdType.valueOf(this.enemyIdType);
    }

    @Override
    public boolean isLogEnabled(LogType logType)
    {
        if (this.logSwitchMap.containsKey(logType))
        {
            return this.logSwitchMap.get(logType);
        }
        else
        {
            return true;
        }
    }

    public int getMemoryAlertThreshold()
    {
        return this.memoryAlert;
    }

    @Override
    public boolean getUpdateCheck()
    {
        return this.updateCheck;
    }
}
