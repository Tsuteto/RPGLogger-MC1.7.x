package tsuteto.rpglogger.battle;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.entity.boss.EntityDragon;
import tsuteto.rpglogger.param.ParamEntity;
import tsuteto.rpglogger.param.ParamEntityLiving;

/**
 * Produces battle situation with enemies
 *
 * @author Tsuteto
 *
 */
public class EnemyMobManager
{
    public Map<Class, MobTypeSlot> enemies = new ConcurrentHashMap<Class, MobTypeSlot>();
    public IEnemyIdFormat enemyIdFormat = null;
    public int enemyCount = 0;

    public void encounter(ParamEntity param)
    {
        if (!enemies.containsKey(param.entityClass))
        {
            enemies.put(param.entityClass, new MobTypeSlot());
        }

        MobTypeSlot typeslot = enemies.get(param.entityClass);
        typeslot.put(param, this.isUniqueMob(param));
        enemyCount++;
    }

    public String getEnemyId(ParamEntity param)
    {
        // Normal mobs
        MobTypeSlot typeslot = enemies.get(param.entityClass);
        return typeslot != null ? typeslot.getId(param) : null;
    }

    public void remove(ParamEntity param)
    {
        MobTypeSlot typeslot = enemies.get(param.entityClass);
        if (typeslot == null)
        {
            return;
        }
        if (typeslot.remove(param))
        {
            enemyCount--;
        }
    }

    public void clear()
    {
        enemies.clear();
        enemyCount = 0;
    }

    public void setEnemyIdType(EnumEnemyIdType type)
    {
        switch (type)
        {
        case A:
            enemyIdFormat = new EnemyIdFormatAlphabet();
            return;
        case N:
            enemyIdFormat = new EnemyIdFormatNumeric();
            return;
        default:
            enemyIdFormat = new EnemyIdFormatBlank();
        }
    }

    public boolean isEnemyCleared()
    {
        return enemyCount == 0;
    }

    private boolean isUniqueMob(ParamEntity param)
    {
        if (EntityDragon.class.isAssignableFrom(param.entityClass))
        {
            return true;
        }

        if (param instanceof ParamEntityLiving && ((ParamEntityLiving)param).hasCustomName)
        {
            return true;
        }

        return false;
    }

    private class MobTypeSlot
    {
        public Map<Integer, String> mobList = new HashMap<Integer, String>();
        public int counter = 0;

        public void put(ParamEntity param, boolean isUnique)
        {
            if (isUnique)
            {
                mobList.put(param.entityId, null);
            }
            else
            {
                String enemyId = enemyIdFormat.format(counter);
                mobList.put(param.entityId, enemyId);
                counter++;
            }
        }

        public String getId(ParamEntity param)
        {
            return mobList.get(param.entityId);
        }

        public boolean remove(ParamEntity param)
        {
            boolean isKeyFound = mobList.containsKey(param.entityId);
            mobList.remove(param.entityId);
            return isKeyFound;
        }

        public int count()
        {
            return mobList.size();
        }
    }
}
