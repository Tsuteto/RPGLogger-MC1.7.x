package tsuteto.rpglogger.stat;

import java.awt.Color;

import net.minecraft.util.Vec3;
import tsuteto.rpglogger.logging.RlLogManager;
import tsuteto.rpglogger.param.ParamEntityThrowable;
import tsuteto.rpglogger.util.EntityNameUtil;
import tsuteto.rpglogger.watcher.WatchBool;

/**
 * Manages status of EntityThrowable
 *
 * @author Tsuteto
 *
 */
public class StatEntityThrowable extends StatEntity<ParamEntityThrowable>
{
    private double throwerPointX;
    private double throwerPointY;
    private double throwerPointZ;

    public StatEntityThrowable(ParamEntityThrowable param)
    {
        super(param);
        statAlive = new WatchBool(param.isAlive);

        if (param.thrower != null)
        {
            throwerPointX = param.thrower.posX;
            throwerPointY = param.thrower.posY;
            throwerPointZ = param.thrower.posZ;
        }
        else
        {
            throwerPointX = param.posX;
            throwerPointY = param.posY;
            throwerPointZ = param.posZ;
        }
    }

    @Override
    public void logStat(RlLogManager logger, StatGame statGame)
    {
        super.logStat(logger, statGame);

        float dist = ((int) (this.getFlyingDistance() * 100)) / 100F;
        if (dist >= 10)
        {
            logger.addMsgTranslate("item.flew",
                    Color.white, EntityNameUtil.getThrownEntityName(param), dist);
        }
    }

    public String getItemName()
    {
        return param.thrownItemName;
    }

    public double getFlyingDistance()
    {
        Vec3 pointLanded = Vec3.createVectorHelper(param.posX, param.posY, param.posZ);
        Vec3 pointThrown = Vec3.createVectorHelper(throwerPointX, throwerPointY, throwerPointZ);
        return pointLanded.distanceTo(pointThrown);
    }
}
