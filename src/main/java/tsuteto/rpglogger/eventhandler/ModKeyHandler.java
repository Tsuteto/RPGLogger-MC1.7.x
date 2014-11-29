package tsuteto.rpglogger.eventhandler;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import net.minecraft.client.Minecraft;
import tsuteto.rpglogger.RpgLogger;

public class ModKeyHandler
{
    private RpgLogger rpgLogger;

    public ModKeyHandler(RpgLogger rpgLogger)
    {
        this.rpgLogger = rpgLogger;
    }

    @SubscribeEvent
    public void onKeyDown(InputEvent.KeyInputEvent event)
    {
        Minecraft mc = FMLClientHandler.instance().getClient();

        if (mc.currentScreen == null && mc.thePlayer != null)
        {
            if (rpgLogger == null) return;
            rpgLogger.keyboardEvent(mc);
        }
    }
}
