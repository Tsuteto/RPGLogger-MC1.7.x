package tsuteto.rpglogger.stat;

import com.google.common.collect.Maps;
import tsuteto.rpglogger.param.ParamPlayer;
import tsuteto.rpglogger.param.ParamWorld;
import tsuteto.rpglogger.settings.RpgLoggerSettings;
import tsuteto.rpglogger.util.CyclicMemory;
import tsuteto.rpglogger.watcher.WatchBool;
import tsuteto.rpglogger.watcher.WatchInt;

import java.util.Map;

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

    public ParamWorld paramWorld;

    public CyclicMemory memoryUsings;
    public WatchBool statMemoryAlert;

    private final Map<Integer, StatEntityLivingBase> mobEntities;
    private final Map<Integer, StatEntity> entitiesTracked;

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

        //mobEntities = new ConcurrentHashMap<Integer, StatEntityLivingBase>();
        mobEntities = Maps.newHashMap();
        //entitiesTracked = new ConcurrentHashMap<Integer, StatEntity>();
        entitiesTracked = Maps.newHashMap();

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
