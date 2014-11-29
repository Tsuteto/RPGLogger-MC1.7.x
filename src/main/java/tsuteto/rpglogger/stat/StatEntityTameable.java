package tsuteto.rpglogger.stat;

import tsuteto.rpglogger.logging.RlLogManager;
import tsuteto.rpglogger.param.ParamEntityTameable;
import tsuteto.rpglogger.watcher.WatchBool;
import tsuteto.rpglogger.watcher.WatchObj;

/**
 * Manages status of EntityTamable
 * 
 * @author Tsuteto
 * 
 * @param <E>
 */
public abstract class StatEntityTameable<E extends ParamEntityTameable> extends StatEntityAnimal<E>
{
    public WatchBool statSitting;

    public StatEntityTameable(E param)
    {
        super(param);
        statOwner = new WatchObj<String>(param.owner);
        statTamed = new WatchBool(param.isTamed);
        statSitting = new WatchBool(param.isSitting);
    }
    
    @Override
    public void logStat(RlLogManager logger, StatGame statGame)
    {
        super.logStat(logger, statGame);
    }

    @Override
    public boolean chkStatOwner()
    {
        return statOwner.checkVal(param.owner);
    }

    @Override
    public boolean chkStatTamed()
    {
        return statTamed.checkVal(param.isTamed);
    }
    
    @Override
    public boolean isTameable()
    {
        return true;
    }

    @Override
    public boolean isTamed()
    {
        return param.isTamed;
    }

    public boolean chkStatSitting()
    {
        return statSitting.checkVal(param.isSitting);
    }
}
