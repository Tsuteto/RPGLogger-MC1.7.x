package tsuteto.rpglogger.stat;

import tsuteto.rpglogger.RpgLogger;
import tsuteto.rpglogger.logging.RlLogManager;
import tsuteto.rpglogger.logging.RlMsgTranslate;
import tsuteto.rpglogger.param.ParamVillager;
import tsuteto.rpglogger.util.EntityNameUtil;
import tsuteto.rpglogger.util.Utilities;
import tsuteto.rpglogger.watcher.WatchBool;

/**
 * Manages status of Villagers
 *
 * @author Tsuteto
 *
 */
public class StatVillager extends StatEntityAgeable<ParamVillager>
{
    public WatchBool statPlaying;
    public WatchBool statMating;

    public StatVillager(ParamVillager param)
    {
        super(param);
        statPlaying = new WatchBool(param.isPlaying);
        statMating = new WatchBool(param.isMating);

        if (param.profession >= 5)
        {
            RlMsgTranslate msgTrans = RpgLogger.getInstance().msgTrans;
            boolean isKeyAdded = msgTrans.addNAKeyToCustomLang(
                    "entity." + param.entityName + ".name",
                    Utilities.capitalize(param.entityName.replace("Villager.", "")));
            if (isKeyAdded) msgTrans.saveCustomLangFile();
        }
    }

    @Override
    public void logStat(RlLogManager logger, StatGame statGame)
    {
        super.logStat(logger, statGame);

        if (this.chkStatPlaying())
        {
            logger.addMsgTranslate("villager.startPlaying",
                    msgColor, EntityNameUtil.getMobName(param));
        }
        if (this.chkStatMating())
        {
            logger.addMsgTranslate("villager.isMating", msgColor, EntityNameUtil.getMobName(param));
        }
    }

    public boolean chkStatPlaying()
    {
        return statPlaying.checkVal(param.isPlaying);
    }

    public boolean chkStatMating()
    {
        return statMating.checkVal(param.isMating);
    }
}
