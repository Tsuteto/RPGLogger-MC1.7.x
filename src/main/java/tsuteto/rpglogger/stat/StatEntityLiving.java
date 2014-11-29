package tsuteto.rpglogger.stat;

import tsuteto.rpglogger.logging.RlLogManager;
import tsuteto.rpglogger.param.ParamEntityLiving;
import tsuteto.rpglogger.util.EntityNameUtil;
import tsuteto.rpglogger.watcher.WatchBool;

public class StatEntityLiving<E extends ParamEntityLiving> extends StatEntityLivingBase<E>
{
    public WatchBool statIsTied;

    public StatEntityLiving(E param)
    {
        super(param);
        statIsTied = new WatchBool(param.isLeashed);
    }

    @Override
    public void logStat(RlLogManager logger, StatGame statGame)
    {
        super.logStat(logger, statGame);

        if (this.statIsTied.checkVal(param.isLeashed))
        {
            String key = statIsTied.getVal() ? "mob.released" : "mob.tied";
            logger.addMsgTranslate(key, msgColor, EntityNameUtil.getMobName(param));
        }
    }
}
