package tsuteto.rpglogger.settings;

public enum LogCategory
{
    player,
    mob,
    env,
    misc;

    public String getFullName()
    {
        return "msg-switch." + name();
    }
}
