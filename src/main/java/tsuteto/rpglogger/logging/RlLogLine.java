package tsuteto.rpglogger.logging;

import java.awt.Color;

/**
 * Represents a line of log
 * 
 * @author Tsuteto
 * 
 */
public class RlLogLine
{
    public String message;
    public Color color;
    public int updateCounter = 0;

    public static final Color DEFAULT_COLOR = Color.white;

    public RlLogLine(String s)
    {
        this(s, DEFAULT_COLOR);
    }

    public RlLogLine(String s, Color color)
    {
        this.message = s;
        this.color = color;
    }
}
