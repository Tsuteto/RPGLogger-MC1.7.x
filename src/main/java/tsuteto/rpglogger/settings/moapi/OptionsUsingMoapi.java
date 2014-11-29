//package tsuteto.rpglogger.settings.moapi;
//
//import moapi.ModOption;
//import moapi.ModOptionCallback;
//import moapi.ModOptionMapped;
//import moapi.ModOptionSlider;
//import moapi.ModOptions;
//import moapi.ModOptionsAPI;
//import net.minecraft.util.MathHelper;
//import tsuteto.rpglogger.Lang;
//import tsuteto.rpglogger.RpgLogger;
//import tsuteto.rpglogger.battle.EnemyMobManager;
//import tsuteto.rpglogger.battle.EnumEnemyIdType;
//import tsuteto.rpglogger.logging.RlMsgTranslate;
//import tsuteto.rpglogger.settings.EnumWindowPosition;
//import tsuteto.rpglogger.settings.OptionsApi;
//import tsuteto.rpglogger.util.Utilities;
//
///**
// * Handles RPG Logger settings using Mod Options API
// *
// * @author Tsuteto
// *
// */
//public class OptionsUsingMoapi implements OptionsApi
//{
//    private ModOptions mainMenu;
//
//    public OptionsUsingMoapi()
//    {
//        mainMenu = new ModOptions(OPTION_NAME, "RPG Logger Options");
//        ModOptionsAPI.addMod(mainMenu);
//        buildGui();
//        mainMenu.loadValues();
//        mainMenu.saveValues();
//    }
//
//    public void buildGui()
//    {
//        mainMenu.setWide(false);
//
//        // Open Log File
//        RlOptionBoolean openLogFile = new RlOptionBoolean("openLogFile", "rpglogger.options.openLogFile", true);
//        openLogFile.setCallback(new CallbackOpenLogFile());
//        openLogFile.setGlobal(true);
//        openLogFile.setWide(true);
//
//        // Export Log to File
//        RlOptionBoolean exportLog = new RlOptionBoolean("exportLog", "rpglogger.options.exportLog", true);
//        exportLog.setLabels("options.on", "options.off");
//        exportLog.setGlobal(true);
//
//        // Ally Prefix
//        RlOptionMapped allyPrefix = new RlOptionMapped("allyPrefix", "rpglogger.options.allyPrefix", new String[0], new int[0]);
//        for (int i = 0; i < 11; i++)
//        {
//            allyPrefix.addValue("mob.allyPrefix." + i, i);
//        }
//        allyPrefix.setGlobal(true);
//
//        // Align
//        RlOptionMapped position = new RlOptionMapped("position", "rpglogger.options.position", new String[0], new int[0]);
//        position.addValue("rpglogger.options.position.topright", 0);
//        position.addValue("rpglogger.options.position.topcenter", 1);
//        position.addValue("rpglogger.options.position.topleft", 2);
//        position.setGlobal(true);
//
//        // Scale
//        RlOptionMapped scale = new RlOptionMapped("scale", "rpglogger.options.scale", new String[0], new int[0]);
//        scale.addValue("rpglogger.options.scale.adjusting", 0);
//        scale.addValue("rpglogger.options.scale.small", 1);
//        scale.addValue("rpglogger.options.scale.normal", 2);
//        scale.addValue("rpglogger.options.scale.large", 3);
//        scale.setGlobal(true);
//
//        // Lines
//        RlOptionSlider lines = new RlOptionSlider("lines", "rpglogger.options.lines", 0, 50);
//        lines.setGlobalValue(15);
//        lines.setGlobal(true);
//        // mainMenu.setWideOption(lines);
//
//        // Opacity
//        RlOptionSlider opacity = new RlOptionSlider("opacity", "rpglogger.options.opacity", 10, 100);
//        opacity.setGlobalValue(80);
//        opacity.setGlobal(true);
//        // mainMenu.setWideOption(opacity);
//
//        // Language
//        RlOptionMapped language = new RlOptionMapped("language", "rpglogger.options.language", new String[0], new int[0]);
//        for (Lang lang : Lang.values())
//        {
//            language.addValue("rpglogger.options.language." + lang.toString(), lang.ordinal());
//        }
//        language.setCallback(new CallbackLanguage());
//        language.setGlobal(true);
//        language.setWide(true);
//
//        // Enemy ID Type
//        RlOptionMapped enemyIdType = new RlOptionMapped("enemyIdType", "rpglogger.options.enemyIdType", new String[0], new int[0]);
//        for (EnumEnemyIdType type : EnumEnemyIdType.values())
//        {
//            enemyIdType.addValue("rpglogger.options.enemyIdType." + type.toString(), type.ordinal());
//        }
//        enemyIdType.setCallback(new CallbackEnemyIdType());
//        enemyIdType.setGlobal(true);
//
//        // Arrangement, bottom to top
//
//        mainMenu.addOption(opacity);
//        mainMenu.addOption(lines);
//        mainMenu.addOption(scale);
//        mainMenu.addOption(position);
//        mainMenu.addOption(allyPrefix);
//        mainMenu.addOption(enemyIdType);
//        mainMenu.addOption(exportLog);
//        mainMenu.addOption(language);
//        mainMenu.addOption(openLogFile);
//
//        // Set formatters
//        opacity.setFormatter(new OpacityFormatter());
//        language.setFormatter(new StandardOptionFormatter());
//        allyPrefix.setFormatter(new AllyPrefixFormatter());
//        scale.setFormatter(new StandardOptionFormatter());
//        position.setFormatter(new StandardOptionFormatter());
//        lines.setFormatter(new LinesFormatter());
//        exportLog.setFormatter(new StandardOptionFormatter());
//        openLogFile.setFormatter(new ActionButtonFormatter());
//        enemyIdType.setFormatter(new StandardOptionFormatter());
//    }
//
//    @Override
//    public float getOpacity()
//    {
//        return mainMenu.getSliderValue("opacity") / 100F;
//    }
//
//    @Override
//    public int getLines()
//    {
//        return MathHelper.floor_float(mainMenu.getSliderValue("lines"));
//    }
//
//    @Override
//    public boolean isEnableExportLog()
//    {
//        return mainMenu.getBooleanValue("exportLog");
//    }
//
//    @Override
//    public int getScale()
//    {
//        return mainMenu.getMappedValue("scale");
//    }
//
//    @Override
//    public EnumWindowPosition getPosition()
//    {
//        return EnumWindowPosition.values()[mainMenu.getMappedValue("position")];
//    }
//
//    @Override
//    public int getAllyPrefix()
//    {
//        return mainMenu.getMappedValue("allyPrefix");
//    }
//
//    @Override
//    public Lang getLanguage()
//    {
//        return Lang.values()[mainMenu.getMappedValue("language")];
//    }
//
//    @Override
//    public EnumEnemyIdType getEnemyIdType()
//    {
//        return EnumEnemyIdType.values()[mainMenu.getMappedValue("enemyIdType")];
//    }
//}
//
//class OpacityFormatter implements DisplayStringFormatter<Integer>
//{
//    @Override
//    public String manipulate(ModOption option, String value, boolean shouldDisplayGlobal)
//    {
//        RlMsgTranslate msgTrans = RpgLogger.getInstance().msgTrans;
//        String dispval;
//        if (shouldDisplayGlobal)
//        {
//            dispval = msgTrans.getMsg("options.GLOBAL");
//        }
//        else
//        {
//           dispval = String.format("%s%%", ((ModOptionSlider)option).getValue());
//        }
//        return String.format("%s: %s", msgTrans.getMsg(option.getName()), dispval);
//    }
//}
//
//class LinesFormatter implements DisplayStringFormatter<Integer>
//{
//    @Override
//    public String manipulate(ModOption option, String value, boolean shouldDisplayGlobal)
//    {
//        RlMsgTranslate msgTrans = RpgLogger.getInstance().msgTrans;
//        String dispval;
//        if (shouldDisplayGlobal)
//        {
//            dispval = msgTrans.getMsg("options.GLOBAL");
//        }
//        else
//        {
//            dispval = String.format("%d", ((ModOptionSlider)option).getValue());
//        }
//        return String.format("%s: %s", msgTrans.getMsg(option.getName()), dispval);
//    }
//}
//
//class AllyPrefixFormatter implements DisplayStringFormatter<String>
//{
//    @Override
//    public String manipulate(ModOption option, String value, boolean shouldDisplayGlobal)
//    {
//        RlMsgTranslate msgTrans = RpgLogger.getInstance().msgTrans;
//        String dispval;
//        if (shouldDisplayGlobal)
//        {
//            dispval = msgTrans.getMsg("options.GLOBAL");
//        }
//        else
//        {
//            dispval = msgTrans.getMsg(value, msgTrans.getMsg("options.allyPrefix.namePart"));
//        }
//        return String.format("%s: %s", msgTrans.getMsg(option.getName()), dispval);
//    }
//}
//
//class StandardOptionFormatter implements DisplayStringFormatter<String>
//{
//    @Override
//    public String manipulate(ModOption option, String value, boolean shouldDisplayGlobal)
//    {
//        RlMsgTranslate msgTrans = RpgLogger.getInstance().msgTrans;
//        String key = shouldDisplayGlobal ? "options.GLOBAL" : value;
//        return String.format("%s: %s", msgTrans.getMsg(option.getName()), msgTrans.getMsg(key));
//    }
//}
//
//class class implements DisplayStringFormatter<String>
//{
//    @Override
//    public String manipulate(ModOption option, String value, boolean shouldDisplayGlobal)
//    {
//        RlMsgTranslate msgTrans = RpgLogger.getInstance().msgTrans;
//        return msgTrans.getMsg(option.getName());
//    }
//}
//
//class CallbackLanguage extends ModOptionCallback
//{
//    @Override
//    public boolean onClick(ModOption option)
//    {
//        if (RpgLogger.hasInstance())
//        {
//            ModOptionMapped mappedOption = (ModOptionMapped) option;
//            Lang nextOption = Lang.values()[mappedOption.getNextValue(mappedOption.getValue())];
//
//            RlMsgTranslate msgTrans = RpgLogger.getInstance().msgTrans;
//            msgTrans.setLanguage(Utilities.chooseLanguage(nextOption));
//        }
//        return true;
//    }
//}
//
//class CallbackEnemyIdType extends ModOptionCallback
//{
//    @Override
//    public boolean onClick(ModOption option)
//    {
//        if (RpgLogger.hasInstance())
//        {
//            ModOptionMapped mappedOption = (ModOptionMapped) option;
//            EnumEnemyIdType nextOption = EnumEnemyIdType.values()[mappedOption.getNextValue(mappedOption.getValue())];
//
//            EnemyMobManager enemyManager = RpgLogger.getInstance().statGame.statBattle.enemyMobManager;
//            enemyManager.setEnemyIdType(nextOption);
//        }
//        return true;
//    }
//}
//
//class CallbackOpenLogFile extends ModOptionCallback
//{
//    @Override
//    public boolean onClick(ModOption option)
//    {
//        if (RpgLogger.getInstance().logFileWriter == null)
//        {
//            return true;
//        }
//        RpgLogger.getInstance().logFileWriter.showLogFile();
//        return true;
//    }
//}
