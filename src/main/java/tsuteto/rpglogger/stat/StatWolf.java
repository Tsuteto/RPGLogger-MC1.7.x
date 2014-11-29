package tsuteto.rpglogger.stat;

import java.awt.Color;

import tsuteto.rpglogger.logging.RlLogManager;
import tsuteto.rpglogger.param.ParamWolf;
import tsuteto.rpglogger.util.EntityNameUtil;
import tsuteto.rpglogger.watcher.WatchBool;
import tsuteto.rpglogger.watcher.WatchObj;

/**
 * Manages status of Wolves
 *
 * @author Tsuteto
 *
 */
public class StatWolf extends StatEntityTameable<ParamWolf>
{
    public WatchBool statShaking;
    public WatchBool statSitting;
    public WatchBool statAngry;

    public StatWolf(ParamWolf param)
    {
        super(param);
        statOwner = new WatchObj<String>(param.owner);
        statTamed = new WatchBool(param.isTamed);
        statShaking = new WatchBool(param.isShaking);
        statSitting = new WatchBool(param.isSitting);
        statAngry = new WatchBool(param.isAngry);
    }

    @Override
    public void logStat(RlLogManager logger, StatGame statGame)
    {
        super.logStat(logger, statGame);

        if (this.chkStatAngry() && this.statAngry.getVal())
        {
            logger.addMsgTranslate("wolf.angry", Color.orange,
                    EntityNameUtil.getMobName(param));
        }
    }

    public boolean chkStatShaking()
    {
        return statShaking.checkVal(param.isShaking);
    }

    public boolean chkStatAngry()
    {
        return statAngry.checkVal(param.isAngry);
    }
}
