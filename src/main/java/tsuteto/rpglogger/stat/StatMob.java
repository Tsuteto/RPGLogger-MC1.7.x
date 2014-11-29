package tsuteto.rpglogger.stat;

import tsuteto.rpglogger.logging.RlLogManager;
import tsuteto.rpglogger.param.ParamMob;

/**
 * Manages status of EntityMob
 *
 * @author Tsuteto
 *
 */
public class StatMob<E extends ParamMob> extends StatEntityLiving<E>
{

    public StatMob(E param)
    {
        super(param);
    }

    @Override
    public void logStat(RlLogManager logger, StatGame statGame)
    {
        super.logStat(logger, statGame);
    }
}
