package tsuteto.rpglogger.eventhandler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import tsuteto.rpglogger.RpgLogger;

public class CraftingHandler
{
    private RpgLogger rpgLogger;

    public CraftingHandler(RpgLogger rpgLogger)
    {
        this.rpgLogger = rpgLogger;
    }

    @SubscribeEvent
    public void onCrafting(PlayerEvent.ItemCraftedEvent event)
    {
        if (rpgLogger != null)
        {
            rpgLogger.takenFromCrafting(event.player, event.crafting);
        }
    }

    @SubscribeEvent
    public void onSmelting(PlayerEvent.ItemSmeltedEvent event)
    {
        if (rpgLogger != null)
        {
            rpgLogger.takenFromFurnace(event.player, event.smelting);
        }
    }

}
