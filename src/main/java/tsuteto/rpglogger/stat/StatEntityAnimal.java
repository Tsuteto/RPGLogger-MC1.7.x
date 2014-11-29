package tsuteto.rpglogger.stat;

import tsuteto.rpglogger.logging.RlLogManager;
import tsuteto.rpglogger.param.ParamEntityAnimal;
import tsuteto.rpglogger.util.EntityNameUtil;
import tsuteto.rpglogger.watcher.WatchBool;

/**
 * Manages status of EntityAnimal
 *
 * @author Tsuteto
 *
 * @param <E>
 */
public class StatEntityAnimal<E extends ParamEntityAnimal> extends StatEntityAgeable<E>
{
    public WatchBool statInLove;

    public StatEntityAnimal(E param)
    {
        super(param);
        statInLove = new WatchBool(param.isInLove);
    }

    public boolean chkStatInLove()
    {
        return statInLove.checkVal(param.isInLove);
    }

    @Override
    public void logStat(RlLogManager logger, StatGame statGame)
    {
        super.logStat(logger, statGame);

        if (this.chkStatInLove())
        {
            if (this.statInLove.getVal())
            {
                logger.addMsgTranslate("mob.inLove", msgColor, EntityNameUtil.getMobName(param));
            }
        }

    }
}
