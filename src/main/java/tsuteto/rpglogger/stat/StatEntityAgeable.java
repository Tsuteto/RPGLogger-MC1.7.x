package tsuteto.rpglogger.stat;

import tsuteto.rpglogger.logging.RlLogManager;
import tsuteto.rpglogger.param.ParamEntityAgeable;
import tsuteto.rpglogger.util.EntityNameUtil;
import tsuteto.rpglogger.watcher.WatchBool;

/**
 * Manages status of EntityAgeable
 *
 * @author Tsuteto
 *
 * @param <E>
 */
public class StatEntityAgeable<E extends ParamEntityAgeable> extends StatEntityLiving<E>
{
    public WatchBool statChild;

    public StatEntityAgeable(E param)
    {
        super(param);
        statChild = new WatchBool(param.isChild);
    }

    @Override
    public void logStat(RlLogManager logger, StatGame statGame)
    {
        super.logStat(logger, statGame);

        if (this.chkStatChild())
        {
            if (this.statChild.getVal())
            {
                logger.addMsgTranslate("mob.backToChild",
                        msgColor, EntityNameUtil.getMobName(param));
            }
            else
            {
                logger.addMsgTranslate("mob.growUp",
                        msgColor, EntityNameUtil.getMobName(param));
            }
        }
    }

    public boolean chkStatChild()
    {
        return statChild.checkVal(param.isChild);
    }
}
