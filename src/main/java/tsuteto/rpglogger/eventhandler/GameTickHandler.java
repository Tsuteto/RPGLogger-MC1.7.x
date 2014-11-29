package tsuteto.rpglogger.eventhandler;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiSleepMP;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import tsuteto.rpglogger.RpgLogger;

@SideOnly(Side.CLIENT)
public class GameTickHandler
{
    private RpgLogger rpgLogger;

    public GameTickHandler(RpgLogger rpgLogger)
    {
        this.rpgLogger = rpgLogger;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void tickEnd(TickEvent.ClientTickEvent event)
    {
        if (event.type == TickEvent.Type.CLIENT && event.phase == TickEvent.Phase.END)
        {
            this.onTickInGame(FMLClientHandler.instance().getClient());
        }
    }

    public boolean onTickInGame(Minecraft mc)
    {
        if (!mc.isIntegratedServerRunning() || mc.theWorld == null) return true;

        if (rpgLogger.setupTick > 0)
        {
            rpgLogger.setupTick--;
        }
        else
        {
            // Logging in sync with the game tick
            rpgLogger.onTick(mc);
            rpgLogger.windowRenderer.updateTick();
        }
        return true;
    }

    @SubscribeEvent
    public void renderGameScreen(RenderGameOverlayEvent.Text event)
    {
        Minecraft mc = FMLClientHandler.instance().getClient();
        // Render the log window when in-game or some screen
        if (rpgLogger.isWindowEnabled)
        {
            rpgLogger.windowRenderer.renderGameOverlay(mc);
        }

    }
}
