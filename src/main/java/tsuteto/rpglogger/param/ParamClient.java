package tsuteto.rpglogger.param;

import net.minecraft.client.Minecraft;

public class ParamClient
{
    public Class currentScreen;

    public ParamClient(Minecraft mc)
    {
        this.currentScreen = mc.currentScreen == null ? null : mc.currentScreen.getClass();
    }
}
