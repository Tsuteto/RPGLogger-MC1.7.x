package tsuteto.rpglogger.eventhandler;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import tsuteto.rpglogger.RpgLogger;

public class PlayerTracker
{
    private RpgLogger rpgLogger;

    public PlayerTracker(RpgLogger rpgLogger)
    {
        this.rpgLogger = rpgLogger;
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event)
    {
        if (rpgLogger == null) return;
        rpgLogger.onPlayerLoginWorld(event.player);
    }

    @SubscribeEvent
    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event)
    {
        if (rpgLogger == null) return;
        rpgLogger.releaseLogger();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event)
    {
        if (rpgLogger == null) return;
        rpgLogger.onPlayerTraveledDimension(event.player);
    }
}
