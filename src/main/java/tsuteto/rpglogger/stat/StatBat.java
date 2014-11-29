package tsuteto.rpglogger.stat;

import tsuteto.rpglogger.logging.RlLogManager;
import tsuteto.rpglogger.param.ParamBat;
import tsuteto.rpglogger.util.EntityNameUtil;
import tsuteto.rpglogger.watcher.WatchBool;

/**
 * Watches status of EntityOcelot
 *
 * @author Tsuteto
 *
 */
public class StatBat extends StatEntityLiving<ParamBat>
{
    public WatchBool statHanging;

    public StatBat(ParamBat param)
    {
        super(param);
        statHanging = new WatchBool(param.isHanging).setLazy(10);
    }

    @Override
    public void logStat(RlLogManager logger, StatGame statGame)
    {
        super.logStat(logger, statGame);

        if (this.chkStatHanging())
        {
            if (!this.statHanging.getVal())
            {
                logger.addMsgTranslate("bat.flewOff", msgColor, EntityNameUtil.getMobName(param));
            }
        }
    }

    public boolean chkStatHanging()
    {
        return statHanging.checkVal(param.isHanging);
    }
}
