package tsuteto.rpglogger;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.relauncher.Side;
import net.minecraftforge.common.config.Configuration;
import tsuteto.rpglogger.command.CommandTemperature;
import tsuteto.rpglogger.util.UpdateNotification;

/**
 * Loader Class of the RPG Logger
 *
 * @author Tsuteto
 *
 */
@Mod(modid = RPGLoggerLoader.modid, name = "RPG Logger", version = "3.9.0-MC1.7.2",
        acceptedMinecraftVersions = "[1.7.2,1.8)",
        guiFactory = "tsuteto.rpglogger.settings.fml.RlGuiFactory")
public class RPGLoggerLoader
{
    public static final String modid = "RPGLogger";

    public static boolean enabled = true;

    @Mod.Metadata
    private ModMetadata metadada;

    private final RpgLogger rpgLogger;
    public static UpdateNotification updateChecker = null;

    public RPGLoggerLoader()
    {
        RpgLogger.init();
        this.rpgLogger = RpgLogger.getInstance();
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        ModInfo.load(metadada);

        Configuration conf = new Configuration(event.getSuggestedConfigurationFile());
        this.rpgLogger.loadFmlSettings(conf);

        if (rpgLogger.settings.getUpdateCheck())
        {
            updateChecker = new UpdateNotification();
            updateChecker.checkUpdate();
        }
    }

    @Mod.EventHandler
    public void load(FMLInitializationEvent event)
    {
        if (enabled)
        {
            this.rpgLogger.load();
        }
    }

    @Mod.EventHandler
    public void modsLoaded(FMLPostInitializationEvent event)
    {
        if (enabled)
        {
            rpgLogger.prepare();
        }
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
        // Notify if update is available
        if (updateChecker != null && event.getSide() == Side.SERVER)
        {
            updateChecker.notifyUpdate(event.getServer(), event.getSide());
        }

        event.registerServerCommand(new CommandTemperature());
    }

    @Mod.EventHandler
    public void serverStopping(FMLServerStoppingEvent event)
    {
        rpgLogger.onPlayerLogoutWorld(FMLClientHandler.instance().getClientPlayerEntity());
    }
}