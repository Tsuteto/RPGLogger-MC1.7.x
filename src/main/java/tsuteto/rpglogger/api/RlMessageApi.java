package tsuteto.rpglogger.api;

import java.awt.Color;

/**
 * A part of the RPG Logger API, allows you to add your message to the logger.
 *
 * @author Tsuteto
 *
 */
public class RlMessageApi
{
    /**
     * Constructor, only used internally
     */
    protected RlMessageApi()
    {
    }

    /**
     * Add a message to the RPG Logger.
     * @param key - an string to specify a message
     * @param color - an integer represents an RGB color such as 0xABCDEF
     * @param params - a parameter list to be included to the message
     */
    public void addMsgTranslate(String key, int color, Object... params)
    {
        addMsgTranslate(key, new Color(color), params);
    }

    /**
     * Add a message to the RPG Logger.
     * @param key - a string to specify a message
     * @param color - a string represents a color such as red, green
     * @param params - a parameter list to be included to the message
     */
    public void addMsgTranslate(String key, String color, Object... params)
    {
        addMsgTranslate(key, Color.decode(color), params);
    }

    /**
     * Add a message to the RPG Logger.
     * @param key - a string to specify a message
     * @param color - a AWT's Color object
     * @param params - a parameter list to be included to the message
     */
    public void addMsgTranslate(String key, Color color, Object... params)
    {
        if (RpgLoggerApi.isEnabled())
        {
            tsuteto.rpglogger.RpgLogger rpgLogger;
            rpgLogger = tsuteto.rpglogger.RpgLogger.getInstance();

            if (rpgLogger.loaded)
            {
                rpgLogger.logger.addMsgTranslate(key, color, params);
            }
        }
    }
}
