package tsuteto.rpglogger.stat;

import tsuteto.rpglogger.logging.RlLogManager;
import tsuteto.rpglogger.param.ParamEntity;
import tsuteto.rpglogger.watcher.WatchBool;

/**
 * Manages status of Entity
 * 
 * @author Tsuteto
 * 
 * @param <E>
 */
public class StatEntity<E extends ParamEntity>
{
    protected E param;
    protected WatchBool statAlive;
    
    public boolean canRemove = false;

    public StatEntity(E param)
    {
        this.param = param;
        statAlive = new WatchBool(param.isAlive);
    }

    public void updateParam(E param)
    {
        this.param = param;
    }

    public boolean checkDeath()
    {
        return statAlive.checkVal(param.isAlive);
    }

    public E getParam()
    {
        return this.param;
    }

    public void logStat(RlLogManager logger, StatGame statGame)
    {
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof ParamEntity))
        {
            return false;
        }
        return param.entityId == ((ParamEntity) o).entityId;
    }

    @Override
    public int hashCode()
    {
        return param.entityId;
    }
}
