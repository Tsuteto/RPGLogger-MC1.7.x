package tsuteto.rpglogger.stat;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.potion.Potion;
import tsuteto.rpglogger.ModEntityAccessor;
import tsuteto.rpglogger.RpgLogger;
import tsuteto.rpglogger.logging.RlLogManager;
import tsuteto.rpglogger.logging.RlMsgTranslate;
import tsuteto.rpglogger.param.ParamEntity;
import tsuteto.rpglogger.param.ParamEntityLivingBase;
import tsuteto.rpglogger.param.ParamMob;
import tsuteto.rpglogger.param.ParamPlayer;
import tsuteto.rpglogger.util.EntityNameUtil;
import tsuteto.rpglogger.util.Utilities;
import tsuteto.rpglogger.watcher.WatchBool;
import tsuteto.rpglogger.watcher.WatchFloat;
import tsuteto.rpglogger.watcher.WatchObj;

/**
 * Manages status of EntityLivingBase
 *
 * @author Tsuteto
 *
 * @param <E>
 */
public class StatEntityLivingBase<E extends ParamEntityLivingBase> extends StatEntity<E>
{
    protected WatchBool statFound;
    public WatchFloat statLife;
    public WatchBool statInWater;
    public WatchBool[] statPotion = new WatchBool[Potion.potionTypes.length];
    public WatchObj<Integer> statRidingEntity;
    public Color msgColor;

    public WatchObj<String> statOwner = null;
    public WatchBool statTamed = null;

    // For mod entities
    public ModEntityAccessor modAccessor = null;
    public Map<String, WatchBool> statBools = null;

    public StatEntityLivingBase(E param)
    {
        super(param);
        statFound = new WatchBool(true).setDelay(120);
        statLife = new WatchFloat(param.entityHealth).setModeDiff(10);
        statInWater = new WatchBool(param.isInsideWater);
        for (Potion potion : Potion.potionTypes)
        {
            if (potion == null)
            {
                continue;
            }
            statPotion[potion.id] = new WatchBool(param.isPotionActive[potion.id]);
        }

        statRidingEntity = new WatchObj<Integer>(param.ridingEntity);

        if (param.modAccessor != null)
        {
            modAccessor = param.modAccessor;

            statBools = new HashMap<String, WatchBool>();
            String[] statList = modAccessor.getStatList();
            for (String statName : statList)
            {
                statBools.put(statName, new WatchBool((Boolean) param.modBoolParams.get(statName)));
            }

            if (param.isTameable)
            {
                statOwner = new WatchObj<String>(param.owner);
                statTamed = new WatchBool(param.isTamed);
            }
        }
        else
        {
            if (param.isTameable)
            {
                statTamed = new WatchBool(param.isTamed);
            }
        }
    }

    public boolean checkLostSightOfEntity()
    {
        return statFound.checkVal(param.canBeSeen);
    }

    public boolean chkStatLife()
    {
        return statLife.checkVal(param.entityHealth);
    }

    public boolean chkStatInWater()
    {
        return statInWater.checkVal(param.isInsideWater);
    }

    public boolean chkStatPotion(Potion potion)
    {
        return statPotion[potion.id].checkVal(param.isPotionActive[potion.id]);
    }

    public boolean chkStatRidingEntity()
    {
        return statRidingEntity.checkVal(param.ridingEntity);
    }

    public boolean chkStatOwner()
    {
        if (statOwner == null)
        {
            return false;
        }
        return statOwner.checkVal(param.owner);
    }

    public boolean chkStatTamed()
    {
        if (statTamed == null)
        {
            return false;
        }
        return statTamed.checkVal(param.isTamed);
    }

    public boolean isTameable()
    {
        return param.isTameable;
    }

    public boolean isTamed()
    {
        return param.isTamed;
    }

    public Map<String, WatchBool> chkBoolStat()
    {
        if (statBools == null)
        {
            return null;
        }

        Map changes = new HashMap<String, WatchBool>();
        for (Entry<String, WatchBool> entry : statBools.entrySet())
        {
            WatchBool watcher = entry.getValue();
            if (watcher.checkVal((Boolean) param.modBoolParams.get(entry.getKey())))
            {
                changes.put(entry.getKey(), watcher);
            }
        }
        return changes;
    }

    @Override
    public void logStat(RlLogManager logger, StatGame statGame)
    {
        RlMsgTranslate msgTrans = RlMsgTranslate.getInstance();
        ParamPlayer player = statGame.statPlayer.param;

        msgColor = Utilities.getMobMsgColor(this);

        // Mob spawned
        if (!statGame.isWorldChanged() && param.ticksExisted == 1 && !(param instanceof ParamMob)
                && param.getDistanceToEntity(player) < 16)
        {
            logger.addMsgTranslate("mob.spawned",
                    new Color(128, 255, 196),
                    EntityNameUtil.getMobName(param));
        }

        for (Potion potion : Potion.potionTypes)
        {
            if (potion == null)
            {
                continue;
            }
            if (this.chkStatPotion(potion))
            {
                String potionName = potion.getName();
                if (this.statPotion[potion.id].getVal())
                {
                    logger.addMsgTranslate("mob." + potionName + ".got",
                            msgColor, EntityNameUtil.getMobName(param));
                }
                else
                {
                    String key = "mob." + potionName + ".gone";
                    if (msgTrans.isKeyAvailable(key))
                    {
                        logger.addMsgTranslate(key, msgColor, EntityNameUtil.getMobName(param));
                    }
                }
            }
        }

        if (this.chkStatLife())
        {
            float lifeDelta = this.statLife.getSum();
            if (lifeDelta > 0)
            {
                logger.addMsgTranslate("mob.healed", msgColor,
                        EntityNameUtil.getMobName(param),
                        lifeDelta,
                        Utilities.chooseNounForm((int)lifeDelta, msgTrans.getMsg("point"), msgTrans.getMsg("points")));
            }
        }

        if (this.chkStatRidingEntity())
        {
            String key;
            int paramRiding;
            if (param.ridingEntity != null)
            {
                paramRiding = param.ridingEntity;
                key = "mob.rode";
            }
            else
            {
                paramRiding = this.statRidingEntity.getPrevVal();
                key = "mob.getOff";
            }
            ParamEntity param = RpgLogger.getInstance().getParamByEntityId(paramRiding);
            logger.addMsgTranslate(key, msgColor, EntityNameUtil.getMobName(param), EntityNameUtil.getMobName(param));
        }

        if (statGame.statPlayer.isBowing() && param.isEyeContactWithPlayer(player))
        {
            logger.addMsgTranslate("player.bow", Color.yellow, EntityNameUtil.getMobName(param));
        }

        Map<String, WatchBool> boolStatMap = this.chkBoolStat();

        if (boolStatMap != null)
        {
            Iterator<Entry<String, WatchBool>> itrBoolStat = boolStatMap.entrySet().iterator();
            while (itrBoolStat.hasNext())
            {
                Entry<String, WatchBool> watcher = itrBoolStat.next();
                String key = param.entityName + "." + watcher.getKey() + "." + Boolean.toString(watcher.getValue().getVal());
                if (msgTrans.isKeyAvailable(key))
                {
                    logger.addMsgTranslate(key, msgColor, EntityNameUtil.getMobName(param));
                }
                else
                {
                    // System.out.println("[RPG Logger] Caught an unregistered key: " + key);
                }
            }
        }

        // Tameable Entities
        if (this.isTameable())
        {
            if (this.chkStatTamed())
            {
                logger.addMsgTranslate("mob.gotTamed",
                        Color.pink, new Object[] { EntityNameUtil.getDisplayEntityNameWithoutOwner(param) });
            }
            this.chkStatOwner();

            if (this.statTamed.getVal())
            {
                // Tamed tameable mobs
                if (this instanceof StatWolf)
                {
                    StatWolf wolf = (StatWolf) this;
                    if (wolf.chkStatShaking() && !wolf.statShaking.getVal())
                    {
                        logger.addMsgTranslate("wolf.shaking",
                                Color.pink, EntityNameUtil.getMobName(param));
                    }
                }
            }
            else
            {
                // Wild tameable mobs
                if (this.checkLostSightOfEntity())
                {
                    this.canRemove = true;
                }
            }
        }
    }
}
