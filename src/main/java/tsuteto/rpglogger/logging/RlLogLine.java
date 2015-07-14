package tsuteto.rpglogger.logging;

import java.awt.*;

/**
 * Represents a line of log
 * 
 * @author Tsuteto
 * 
 */
public class RlLogLine
{
    public static final Color DEFAULT_COLOR = Color.white;

    public String message;
    public Color color;
    private int createdTick;

    public RlLogLine(String s, int createdTick)
    {
        this(s, DEFAULT_COLOR, createdTick);
    }

    public RlLogLine(String s, Color color, int createdTick)
    {
        this.createdTick = createdTick;
        this.message = s;
        this.color = color;
    }

    public int getCreatedTick()
    {
        return createdTick;
    }

    public RlLogLine split(String newMsg)
    {
        return new RlLogLine(newMsg, this.color, this.createdTick);
    }
}
