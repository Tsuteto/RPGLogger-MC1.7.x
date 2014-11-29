package tsuteto.rpglogger.stat;

import tsuteto.rpglogger.logging.RlLogManager;
import tsuteto.rpglogger.param.ParamWitch;
import tsuteto.rpglogger.util.EntityNameUtil;
import tsuteto.rpglogger.watcher.WatchBool;


public class StatWitch extends StatMob<ParamWitch>
{
    public WatchBool statAggressive;

    public StatWitch(ParamWitch param)
    {
        super(param);
        this.statAggressive = new WatchBool(param.isAggressive);
    }

    @Override
    public void logStat(RlLogManager logger, StatGame statGame)
    {
        super.logStat(logger, statGame);

        if (this.chkStatAggressive())
        {
            if (this.statAggressive.getVal())
            {
                logger.addMsgTranslate("witch.aggressive", msgColor, EntityNameUtil.getMobName(param));
            }
        }
    }

    public boolean chkStatAggressive()
    {
        return statAggressive.checkVal(param.isAggressive);
    }
}
