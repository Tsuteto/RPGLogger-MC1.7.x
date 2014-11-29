package tsuteto.rpglogger;

import java.awt.Color;
import java.util.Iterator;
import java.util.Map.Entry;

import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import net.minecraft.world.biome.BiomeGenBase;
import tsuteto.rpglogger.logging.RlLogManager;
import tsuteto.rpglogger.logging.RlMsgTranslate;
import tsuteto.rpglogger.param.ParamEntity;
import tsuteto.rpglogger.param.ParamEntityLivingBase;
import tsuteto.rpglogger.param.ParamMob;
import tsuteto.rpglogger.param.ParamPlayer;
import tsuteto.rpglogger.param.ParamWorld;
import tsuteto.rpglogger.settings.RpgLoggerSettings;
import tsuteto.rpglogger.stat.StatBattle;
import tsuteto.rpglogger.stat.StatEntity;
import tsuteto.rpglogger.stat.StatEntityLivingBase;
import tsuteto.rpglogger.stat.StatGame;
import tsuteto.rpglogger.stat.StatPlayer;
import tsuteto.rpglogger.util.EntityNameUtil;
import tsuteto.rpglogger.util.Utilities;

/**
 * RPG Logger Logging Task
 *
 * @author Tsuteto
 *
 */
public class LoggerTask extends Thread
{
    public RpgLogger rpgLogger;

    public LoggerTask(RpgLogger rpgLogger)
    {
        this.rpgLogger = rpgLogger;
    }

    /**
     * Run thread
     */
    @Override
    public void run()
    {
        while (true)
        {
            ready();
            //long start = System.nanoTime();
            task();
            //RpgLogger.systemLog("Thread time: " + (((System.nanoTime() - start) / 1000) / 1000D) + "ms");
        }
    }

    /**
     * Ready for call
     */
    public synchronized void ready()
    {
        try
        {
            wait();
        }
        catch (InterruptedException e) {}
    }

    /**
     * Kicks the task
     */
    public synchronized void kick()
    {
        notifyAll();
    }

    /**
     * Main Logging Task
     */
    public void task()
    {
        long start = System.nanoTime();

        RpgLoggerSettings settings = rpgLogger.settings;
        RlMsgTranslate msgTrans = RlMsgTranslate.getInstance();
        RlLogManager logger = rpgLogger.logger;

        ParamPlayer player = rpgLogger.paramPlayer;
        ParamWorld world = rpgLogger.paramWorld;

        int px = MathHelper.floor_double(player.posX);
        int py = MathHelper.floor_double(player.posY);
        int pz = MathHelper.floor_double(player.posZ);
        // if (world.getWorldChunkManager() == null) return;

        StatGame statGame = rpgLogger.statGame;
        StatBattle statBattle = statGame.statBattle;

        // ----------------------------------------
        // Player
        // ----------------------------------------
        StatPlayer statPlayer;
        boolean isPlayerSneaking;
        {
            statPlayer = statGame.statPlayer;
            statPlayer.updateParam(player);

            /*
             * Player Status
             */

            // Respawn
            if (statPlayer.checkDeath())
            {
                if (statPlayer.getParam().isAlive)
                {
                    logger.addMsgTranslate("player.respawn", Color.yellow);
                    rpgLogger.loaded = false;
                    return;
                }
            }

            if (player.isAlive)
            {

                if (statPlayer.chkStatXp())
                {
                    int exDelta = statPlayer.statXp.getDiff();
                    if (exDelta > 0)
                    {
                        logger.addMsgTranslate(
                                "player.gainedXp",
                                Color.yellow,
                                exDelta,
                                Utilities.chooseNounForm(exDelta, msgTrans.getMsg("point"), msgTrans.getMsg("points")));
                    }
                    else
                    {
                        logger.addMsgTranslate(
                                "player.lostXp",
                                Color.yellow,
                                -exDelta,
                                Utilities.chooseNounForm(-exDelta, msgTrans.getMsg("point"), msgTrans.getMsg("points")));
                    }
                    if (statPlayer.chkStatXpLevel())
                    {
                        logger.addMsgTranslate("player.increasedLvl", Color.yellow,
                                statPlayer.statXpLevel.getVal());
                    }
                }

                if (statPlayer.chkStatInWater())
                {
                    if (statPlayer.statInWater.getVal())
                    {
                        logger.addMsgTranslate("player.gotInWater", Color.yellow);
                    }
                    else
                    {
                        logger.addMsgTranslate("player.gotOutOfWater", Color.yellow);
                    }
                }

                if (statPlayer.chkStatLife())
                {
                    float lifeDelta = statPlayer.statLife.getSum();
                    if (lifeDelta > 0)
                    {
                        logger.addMsgTranslate(
                                "player.healed",
                                Color.yellow,
                                lifeDelta, Utilities.chooseNounForm((int)lifeDelta, msgTrans.getMsg("point"), msgTrans.getMsg("points")));
                    }
                }

                if (statPlayer.chkStatSleeping())
                {
                    if (statPlayer.statSleeping.getVal())
                    {
                        logger.addMsgTranslate("player.fellAsleep", Color.yellow);
                    }
                    else
                    {
                        logger.addMsgTranslate("player.wokeUp", Color.yellow);
                    }
                }

                if (statPlayer.chkStatFood())
                {
                    int foodDelta = statPlayer.statFood.getDiff();
                    if (foodDelta > 0)
                    {
                        logger.addMsgTranslate(
                                "player.filledBelly",
                                Color.yellow,
                                foodDelta,
                                Utilities.chooseNounForm(foodDelta, msgTrans.getMsg("point"), msgTrans.getMsg("points")));
                    }
                }

                for (Potion potion : Potion.potionTypes)
                {
                    if (potion == null)
                    {
                        continue;
                    }
                    if (statPlayer.chkStatPotion(potion))
                    {
                        String potionName = potion.getName();
                        if (statPlayer.statPotion[potion.id].getVal())
                        {
                            logger.addMsgTranslate("player." + potionName + ".got", Color.yellow);
                        }
                        else
                        {
                            String key = "player." + potionName + ".gone";
                            if (msgTrans.isKeyAvailable(key))
                            {
                                logger.addMsgTranslate(key, Color.yellow);
                            }
                        }
                    }
                }

                if (statPlayer.chkStatRidingEntity())
                {
                    String ridingEntityName, key;
                    int ridingEntity;
                    if (player.ridingEntity != null)
                    {
                        ridingEntity = player.ridingEntity;
                        key = "player.rode";
                    }
                    else
                    {
                        ridingEntity = statPlayer.statRidingEntity.getPrevVal();
                        key = "player.getOff";
                    }
                    ParamEntity param = RpgLogger.getInstance().getParamByEntityId(ridingEntity);
                    logger.addMsgTranslate(key, Color.yellow, EntityNameUtil.getMobName(param));
                }

                /*
                 * Player Action
                 */

                // Going underground
                if (!statPlayer.statUnderground.getVal() && statPlayer.chkStatUndergroundTrue(world)
                        || statPlayer.statUnderground.getVal() && statPlayer.chkStatUndergroundFalse(world))
                {
                    if (statPlayer.statUnderground.getVal())
                    {
                        logger.addMsgTranslate("player.goingUnderground", Color.yellow);
                    }
                    else
                    {
                        logger.addMsgTranslate("player.backToSurface", Color.yellow);
                    }
                }

                // Walk milestone
                // not counting while riding on entities
                if (statPlayer.chkMsWalk())
                {
                    logger.addMsgTranslate("player.walked",
                            Color.white,
                            new Float(statPlayer.msWalk.getMilestonePassed() / 1000).toString());
                }

                // Obtaining items
                if (statPlayer.chkStatItemsPickedUp())
                {
                    Iterator itr = statPlayer.itemsPickedUp.keySet().iterator();
                    while (itr.hasNext())
                    {
                        String displayItemName = (String) itr.next();
                        Integer numOfItems = (Integer) statPlayer.itemsPickedUp.get(displayItemName);
                        if (numOfItems >= 5)
                        {
                            logger.addMsgTranslate("player.sumItems", Color.yellow, numOfItems, displayItemName);
                        }
                    }
                    statPlayer.itemsPickedUp.clear();
                    statPlayer.statItemsPickedUp.setVal(statPlayer.itemsPickedUp.hashCode());
                }

                // Take an item
                if (statPlayer.chkStatEquippedItem())
                {
                    ItemStack itemstack = statPlayer.getParam().equippedItem;
                    if (itemstack == null)
                    {
                        logger.addMsgTranslate("player.emptyHand", Color.yellow);
                    }
                    else
                    {
                        logger.addMsgTranslate("player.tookItem", Color.yellow, EntityNameUtil.getItemName(itemstack));
                    }
                }

                // Count idle time
                statPlayer.countIdleTime();

                statPlayer.chkStatSneaking();
            }
        }

        // ----------------------------------------
        // Collect params of entities
        // ----------------------------------------
        // System.out.println("entityParams: " + entityParams.size());
        synchronized (rpgLogger.entityParams)
        {
            Iterator itr = rpgLogger.entityParams.iterator();
            while (itr.hasNext())
            {
                ParamEntity param = (ParamEntity) itr.next();
                if (!param.isAlive)
                {
                    continue;
                }

                param.setStat(logger, statGame);

                if (param instanceof ParamEntityLivingBase && ((ParamEntityLivingBase) param).isTamed)
                {
                    player.hasAlly = true;
                }
            }
        }
        // System.out.println("mobEntities: " + statGame.mobEntities.size());

        // ----------------------------------------
        // EntityLivingBase status
        // ----------------------------------------
        {
            Iterator<Entry<Integer, StatEntityLivingBase>> itr = statGame.getMobEntities().entrySet().iterator();
            while (itr.hasNext())
            {
                Entry<Integer, StatEntityLivingBase> entry = itr.next();
                StatEntityLivingBase stat = entry.getValue();
                ParamEntityLivingBase param = (ParamEntityLivingBase) stat.getParam();

                synchronized (rpgLogger.entityParams)
                {
                    if (!rpgLogger.entityParams.contains(param)
                            && !EntityDragon.class.isAssignableFrom(param.entityClass))
                    {
                        statBattle.enemyMobManager.remove(param);
                        stat.canRemove = true;
                    }
                }

                // Log status of mobs
                stat.logStat(logger, statGame);

                // Player lost sight of mobs
                if (!stat.isTameable() && stat.checkLostSightOfEntity() && !EntityDragon.class.isAssignableFrom(param.entityClass))
                {
                    if (param instanceof ParamMob)
                    {
                        logger.addMsgTranslate("player.lostMob", Color.yellow, EntityNameUtil.getMobName(param));
                        statBattle.enemyMobManager.remove(param);
                    }
                    stat.canRemove = true;
                }
                else if (!stat.isTameable() && !param.isAlive)
                {
                    statBattle.enemyMobManager.remove(param);
                    stat.canRemove = true;
                }

                if (stat.canRemove)
                {
                    itr.remove();
                }
            }

        }

        // Battle finished
        if (statBattle.chkStatInBattle() && !statBattle.isInBattle())
        {
            logger.addMsgTranslate("player.finishedBattle", new Color(255, 128, 128));

            String party = null;
            if (statBattle.isAllyJoined && statBattle.isPlayerJoined)
            {
                party = msgTrans.getMsg("party.youAndCo");
            }
            else if (statBattle.isAllyJoined)
            {
                party = msgTrans.getMsg("party.co");
            }
            else if (statBattle.isPlayerJoined)
            {
                party = msgTrans.getMsg("party.you");
            }
            if (party != null && statBattle.killCount > 0)
            {
                String key = (statBattle.killCount == 1) ? "party.owned.single" : "party.owned.multiple";
                logger.addMsgTranslate(key, new Color(255, 128, 128), party, statBattle.killCount);
            }
            statBattle.reset();
        }

        // ----------------------------------------
        // Entity status
        // ----------------------------------------
        synchronized (rpgLogger.entityParams)
        {
            Iterator<Entry<Integer, StatEntity>> itr = statGame.getEntitiesTracked().entrySet().iterator();
            while (itr.hasNext())
            {
                Entry<Integer, StatEntity> entry = itr.next();
                StatEntity stat = entry.getValue();
                ParamEntity param = stat.getParam();
                if (!rpgLogger.entityParams.contains(param))
                {
                    stat.logStat(logger, statGame);

                    // else if (param instanceof ParamEnderCrystal && !param.isAlive) {
                    //     // Breaking EnderCrystal
                    //     logger.addMsgTranslate("entity.destroyed", new Object[]{rpgLogger.getEntityName(param)}, Color.yellow);
                    // }
                    itr.remove();
                }
            }
        }

        // --------------------------
        // Environment
        // --------------------------
        if (statGame.statRaining.checkVal(world.isRaining) && world.getBiome().canSpawnLightningBolt()
                && !statPlayer.inUnderground())
        {
            String falls = Utilities.getFalls(world.temperature);
            if (statGame.statRaining.getVal())
            {
                logger.addMsgTranslate("env.rainBegan", Color.green, falls);
            }
            else
            {
                logger.addMsgTranslate("env.rainLetUp", Color.green, falls);
            }
        }

        if (statGame.statDaytime.checkVal(world.isDaytime) && !statPlayer.inUnderground())
        {
            if (statGame.statDaytime.getVal())
            {
                logger.addMsgTranslate("env.brightened", Color.green);
            }
            else
            {
                logger.addMsgTranslate("env.darkened", Color.green);
            }
        }

        if (statGame.statSceneOfDay.checkVal((int) (world.worldTime % 24000 / 6000)) || world.worldTime == 1)
        {
            if (!statPlayer.inUnderground() && player.dimension != -1 && player.dimension != 1 && world.worldTime != 1)
            {
                switch (statGame.statSceneOfDay.getVal())
                {
                case 0:
                    logger.addMsgTranslate("env.morning", Color.green);
                    break;
                case 1:
                    logger.addMsgTranslate("env.noon", Color.green);
                    break;
                case 2:
                    logger.addMsgTranslate("env.night", Color.green);
                    break;
                case 3:
                    logger.addMsgTranslate("env.midnight", Color.green);
                    break;
                }
            }
            if (statGame.statSceneOfDay.getVal() == 0)
            {
                logger.addMsgTranslate("env.dayBegan",
                        Color.green, new Object[] { Utilities.toOrdinalNumber(world.worldTime / 24000 + 1) });
            }
        }

        if (statGame.statThundering.checkVal(world.isThundering) && world.isRaining
                && world.getBiome().canSpawnLightningBolt() && !statPlayer.inUnderground())
        {
            if (statGame.statThundering.getVal())
            {
                logger.addMsgTranslate("env.thunderBegan", Color.green);
            }
            else
            {
                logger.addMsgTranslate("env.thunderStopped", Color.green);
            }
        }

        if (statGame.statBiome.checkVal(world.biomeID) && !statPlayer.inUnderground())
        {
            int biomeId = statGame.statBiome.getVal();
            logger.addMsgTranslate("env.enterBiome",
                    Color.green,
                    Utilities.getBiomeName(biomeId, player.dimension, statPlayer.inUnderground(), py));
        }

        // --------------------------
        // Messages while idling
        // --------------------------
        int idleMsgPhase = (statPlayer.idleTime - 300) / 30;
        if (idleMsgPhase >= 0 && statPlayer.idleTime % 30 == 0)
        {
            switch (idleMsgPhase)
            {
            case 0:
                int biomeId = statGame.statBiome.getVal();
                if (player.dimension >= -1 && player.dimension <= 1)
                {
                    logger.addMsgTranslate("idle.biome", Color.white,
                            Utilities.getBiomeName(biomeId, player.dimension, statPlayer.inUnderground(), py));
                }
                else
                {
                    logger.addMsgTranslate(
                            "idle.dimAndBiome",
                            Color.white,
                            Utilities.getDimensionName(player.dimension, player.dimensionName),
                            Utilities.getBiomeName(biomeId, player.dimension, statPlayer.inUnderground(), py));
                }
                break;
            case 1:
                if (player.dimension == -1)
                {
                    // Nether
                }
                else if (player.dimension == 1)
                {
                    // The End
                }
                else if (statPlayer.inUnderground() && py < 64)
                {
                    logger.addMsgTranslate("idle.depth", Color.white, world.mapHeight - py);
                }
                else
                {
                    float dispTemp = Utilities.calcTemperatureCelsius(world.temperature);
                    logger.addMsgTranslate("idle.temp", Color.white, dispTemp);
                }
                break;
            case 2:
                if (player.dimension == -1 || player.dimension == 1 || (statPlayer.inUnderground() && py < 64))
                {
                    logger.addMsgTranslate("idle.tempNA", Color.white);
                }
                else
                {
                    float dispHumidity = Utilities.calcRainFallPercentage(world.rainfall);
                    logger.addMsgTranslate("idle.humidity", Color.white, dispHumidity);
                }
                break;
            case 3:
                if (!world.getBiome().canSpawnLightningBolt() && !world.getBiome().getEnableSnow())
                {
                    logger.addMsgTranslate("idle.neverFall", Color.white);
                }
                else
                {
                    String nextRain;
                    String falls = Utilities.getFalls(world.temperature);
                    if (world.isRaining)
                    {
                        int rainHour = world.rainTime / 1000 + 1;
                        logger.addMsgTranslate("idle.fallLetUp",
                                Color.white, falls, rainHour,
                                Utilities.chooseNounForm(rainHour, msgTrans.getMsg("hour"), msgTrans.getMsg("hours")));
                    }
                    else
                    {
                        long rainDay = (world.rainTime + (world.worldTime % 24000)) / 24000;
                        if (rainDay >= 2)
                        {
                            logger.addMsgTranslate("idle.nextFall", Color.white, falls, rainDay);
                        }
                        else if (rainDay == 1)
                        {
                            logger.addMsgTranslate("idle.rainTomorrow", Color.white, falls);
                        }
                        else if (rainDay == 0)
                        {
                            logger.addMsgTranslate("idle.rainToday", Color.white, falls);
                        }
                    }
                }
                break;
            case 4:
                logger.addMsgTranslate("idle.today",
                        Color.white, Utilities.toOrdinalNumber(world.worldTime / 24000 + 1));
                break;
            case 5:
                logger.addMsgTranslate("idle.walked",
                        Color.white, Utilities.formatDistance(player.distanceWalkedModified));
                break;
            case 6:
                logger.addMsgTranslate("idle.yourLvl", Color.white, player.expLevel);
                break;
            case 7:
                logger.addMsgTranslate("idle.totalEx", Color.white, player.expTotal);
                break;
            case 8:
                logger.addMsgTranslate("idle.exToGo", Color.white, player.xpBarCap - (int) player.exp);
                break;
            case 9:
                logger.addMsgTranslate("idle.conclusion", Color.white);
                break;
            }
        }

        // --------------------------
        // Memory Alert
        // --------------------------
        int memoryAlertThreshold = settings.getMemoryAlertThreshold();

        if (memoryAlertThreshold != -1)
        {
            int usingMemory = Utilities.calcUsingMemory();
            statGame.memoryUsings.add(usingMemory);

            if (statGame.statMemoryAlert.checkVal(statGame.memoryUsings.average() > memoryAlertThreshold))
            {
                if (statGame.statMemoryAlert.getVal())
                {
                    logger.addMsgTranslate("system.memory.alert", Color.red, memoryAlertThreshold);
                }
                else
                {
                    logger.addMsgTranslate("system.memory.alertEnd", Color.cyan);
                }
            }

            if (statGame.statMemoryAlert.getVal() && world.worldTime % 200 == 0)
            {
                logger.addMsgTranslate("system.memory.report", Color.red, usingMemory);
            }
        }

        //RpgLogger.systemLog(String.format("LoggerTask time: %.3fms", (System.nanoTime() - start) / 1000000.0D));
    }
}
