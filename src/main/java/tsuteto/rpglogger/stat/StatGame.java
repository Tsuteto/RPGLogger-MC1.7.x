package tsuteto.rpglogger.stat;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.client.gui.GuiOptions;
import tsuteto.rpglogger.param.ParamPlayer;
import tsuteto.rpglogger.param.ParamWorld;
import tsuteto.rpglogger.settings.RpgLoggerSettings;
import tsuteto.rpglogger.util.CyclicMemory;
import tsuteto.rpglogger.watcher.WatchBool;
import tsuteto.rpglogger.watcher.WatchInt;

/**
 * Manages status of the game
 *
 * @author Tsuteto
 *
 */
public class StatGame
{
    public StatPlayer statPlayer;
    public StatBattle statBattle;
    public WatchBool statDaytime;
    public WatchInt statSceneOfDay;
    public WatchBool statRaining;
    public WatchBool statThundering;
    public WatchInt statBiome;
    public WatchInt statDimension;
    public WatchInt statWorldChange;
    public WatchBool statInMenu;

    public ParamWorld paramWorld;

    public CyclicMemory memoryUsings;
    public WatchBool statMemoryAlert;

    private Map<Integer, StatEntityLivingBase> mobEntities;
    private Map<Integer, StatEntity> entitiesTracked;

    public StatGame(ParamPlayer player, ParamWorld world, RpgLoggerSettings settings)
    {
        statPlayer = new StatPlayer(player, world);
        statBattle = new StatBattle(settings);
        statDaytime = new WatchBool(world.isDaytime);
        statRaining = new WatchBool(world.isRaining);
        statThundering = new WatchBool(world.isThundering);
        statBiome = new WatchInt(world.biomeID).setModeDelay(60);
        statDimension = new WatchInt(player.dimension);
        statSceneOfDay = new WatchInt((int) (world.worldTime % 24000 / 6000));

        if (settings.getMemoryAlertThreshold() != -1)
        {
            memoryUsings = new CyclicMemory(60);
            statMemoryAlert = new WatchBool(memoryUsings.average() > settings.getMemoryAlertThreshold()).setDelay(100);
        }
        else
        {
            memoryUsings = null;
            statMemoryAlert = null;
        }

        statWorldChange = new WatchInt(0);
        statInMenu = new WatchBool(world.currentScreen == GuiOptions.class);

        mobEntities = new ConcurrentHashMap<Integer, StatEntityLivingBase>();
        entitiesTracked = new ConcurrentHashMap<Integer, StatEntity>();

        paramWorld = world;
    }

    public boolean isWorldChanged()
    {
        return this.statWorldChange.checkVal(paramWorld.worldObjHash);
    }

    public Map<Integer, StatEntityLivingBase> getMobEntities()
    {
        return this.mobEntities;
    }

    public Map<Integer, StatEntity> getEntitiesTracked()
    {
        return entitiesTracked;
    }
}
