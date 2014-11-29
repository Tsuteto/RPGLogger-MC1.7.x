package tsuteto.rpglogger.eventhandler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import tsuteto.rpglogger.RPGLoggerLoader;
import tsuteto.rpglogger.RpgLogger;

public class ItemPickupHandler
{
    private RpgLogger rpgLogger;
    private int stackSize = 0;

    public ItemPickupHandler(RpgLogger rpgLogger)
    {
        this.rpgLogger = rpgLogger;
    }

    @SubscribeEvent
    public void onForgeItemPickup(EntityItemPickupEvent event)
    {
        if (RPGLoggerLoader.enabled && rpgLogger.loaded && event.item.getEntityItem() != null)
        {
            // Forge sets an actual size to the stackSize, but doesn't check whether the player can actually pick it up.
            this.stackSize = event.item.getEntityItem().stackSize;
        }
        else
        {
            this.stackSize = 0;
        }
    }

    @SubscribeEvent
    public void onFmlItemPickup(PlayerEvent.ItemPickupEvent event)
    {
        if (this.stackSize != 0 && event.pickedUp.getEntityItem() != null)
        {
            // ML and FML always takes 0 stackSize of item stack.
            rpgLogger.onItemPickup(event.player, event.pickedUp.getEntityItem(), stackSize - event.pickedUp.getEntityItem().stackSize);
        }
    }
}
