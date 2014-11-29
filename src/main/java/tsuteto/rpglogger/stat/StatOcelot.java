package tsuteto.rpglogger.stat;

import tsuteto.rpglogger.logging.RlLogManager;
import tsuteto.rpglogger.param.ParamOcelot;
import tsuteto.rpglogger.watcher.WatchBool;
import tsuteto.rpglogger.watcher.WatchObj;

/**
 * Manages status of EntityOcelot
 * 
 * @author Tsuteto
 * 
 */
public class StatOcelot extends StatEntityTameable<ParamOcelot>
{
    public StatOcelot(ParamOcelot param)
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
}
