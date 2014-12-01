package tsuteto.rpglogger.stat;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiOptions;
import tsuteto.rpglogger.watcher.WatchBool;

public class StatClient
{
    public WatchBool statInMenu;
    public WatchBool statMainMenu;

    public StatClient()
    {
        Minecraft mc = FMLClientHandler.instance().getClient();
        statInMenu = new WatchBool(mc.currentScreen != null && mc.currentScreen.getClass() == GuiOptions.class);
        statMainMenu = new WatchBool(mc.currentScreen != null && mc.currentScreen.getClass() == GuiMainMenu.class);
    }
}
