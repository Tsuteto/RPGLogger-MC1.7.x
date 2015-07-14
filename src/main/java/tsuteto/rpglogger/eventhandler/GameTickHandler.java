package tsuteto.rpglogger.eventhandler;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import tsuteto.rpglogger.RpgLogger;

public class GameTickHandler
{
    private RpgLogger rpgLogger;

    public GameTickHandler(RpgLogger rpgLogger)
    {
        this.rpgLogger = rpgLogger;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void tickEnd(TickEvent.ServerTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END)
        {
            rpgLogger.onServerTick();
        }
    }

    @SubscribeEvent
    public void tickEnd(TickEvent.ClientTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END)
        {
            rpgLogger.onClientTick();
        }
    }
    @SubscribeEvent
    public void renderGameScreen(RenderGameOverlayEvent.Text event)
    {
        Minecraft mc = FMLClientHandler.instance().getClient();
        if (rpgLogger.isWindowEnabled || this.rpgLogger.logWindowGui.isWindowOpen())
        {
            rpgLogger.logWindowGui.draw();
        }

    }
}
