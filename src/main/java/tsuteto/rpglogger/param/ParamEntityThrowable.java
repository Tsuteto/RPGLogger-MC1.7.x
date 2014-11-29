package tsuteto.rpglogger.param;

import java.awt.Color;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import tsuteto.rpglogger.logging.RlLogManager;
import tsuteto.rpglogger.logging.RlMsgTranslate;
import tsuteto.rpglogger.stat.StatEntityThrowable;
import tsuteto.rpglogger.stat.StatGame;
import tsuteto.rpglogger.util.EntityNameUtil;
import tsuteto.rpglogger.util.Utilities;

/**
 * Represents parameters of EntityThrowable
 *
 * @author Tsuteto
 *
 */
public class ParamEntityThrowable extends ParamEntity<EntityThrowable>
{
    public String thrownItemName = null;
    public ParamEntity thrower;

    public ParamEntityThrowable(EntityThrowable entity, EntityPlayer player, ParamWorld world)
    {
        super(entity, player);

        EntityLivingBase thrower = entity.getThrower();
        if (thrower != null)
        {
            this.thrower = ParamConverter.convertEntityToParam(thrower, player, world);
        }

        String entityString = EntityList.getEntityString(entity);
        thrownItemName = "entity." + entityString + ".name";
    }

    @Override
    protected void addStat(RlLogManager logger, StatGame statGame)
    {
        StatEntityThrowable stat = new StatEntityThrowable(this);
        statGame.getEntitiesTracked().put(this.entityId, stat);
        if (this.thrower != null)
        {
            if (EntityPlayer.class.isAssignableFrom(this.thrower.entityClass))
            {
                logger.addMsgTranslate("player.threw",
                        Color.yellow, EntityNameUtil.getThrownEntityName(stat.getParam()));
            }
            else if (statGame.getMobEntities().containsKey(this.thrower.entityId))
            {
                logger.addMsgTranslate("mob.threw",
                        Utilities.getMobMsgColor(statGame.getMobEntities().get(this.entityId)),
                            EntityNameUtil.getMobName(this.thrower),
                            EntityNameUtil.getThrownEntityName(stat.getParam()));
            }
        }
        else
        {
            RlMsgTranslate msgTrans = RlMsgTranslate.getInstance();
            logger.addMsgTranslate("mob.threw",
                    Utilities.getMobMsgColor(statGame.getMobEntities().get(this.entityId)),
                        msgTrans.getMsg("something"),
                        EntityNameUtil.getThrownEntityName(stat.getParam()));
        }
    }
}
