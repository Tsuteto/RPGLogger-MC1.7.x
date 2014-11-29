package tsuteto.rpglogger;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.config.Configuration;

/**
 * Loader Class of the RPG Logger
 *
 * @author Tsuteto
 *
 */
@Mod(modid = RPGLoggerLoader.modid, name = "RPG Logger", version = "3.7.2-MC1.6.2",
        guiFactory = "tsuteto.rpglogger.settings.fml.RlGuiFactory")
public class RPGLoggerLoader
{
    public static final String modid = "RPGLogger";

    public static boolean enabled = true;

    @Mod.Metadata
    private ModMetadata metadada;

    private final RpgLogger rpgLogger;

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
}
