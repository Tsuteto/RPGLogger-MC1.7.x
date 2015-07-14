package tsuteto.rpglogger.settings;

import tsuteto.rpglogger.Lang;
import tsuteto.rpglogger.battle.EnumEnemyIdType;

/**
 * Handles RPG Logger settings using API
 * 
 * @author Tsuteto
 * 
 */
public interface OptionsApi
{
    String OPTION_NAME = "RpgLogger";

    float getOpacity();
    int getLines();
    int getScale();
    EnumWindowPosition getPosition();
    int getWindowWidth();
    boolean isExportLogEnabled();
    int getAllyPrefix();
    Lang getLanguage();
    EnumEnemyIdType getEnemyIdType();
    boolean isLogEnabled(LogType logType);
    int getMemoryAlertThreshold();
    boolean getUpdateCheck();
}
