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
    boolean isExportLogEnabled();
    int getAllyPrefix();
    Lang getLanguage();
    EnumEnemyIdType getEnemyIdType();
    int getMemoryAlertThreshold();
    boolean getUpdateCheck();
}
