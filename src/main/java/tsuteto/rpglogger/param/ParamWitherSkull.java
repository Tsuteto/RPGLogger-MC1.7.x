package tsuteto.rpglogger.param;

import java.awt.Color;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityWitherSkull;
import tsuteto.rpglogger.logging.RlLogManager;
import tsuteto.rpglogger.logging.RlMsgTranslate;
import tsuteto.rpglogger.stat.StatEntity;
import tsuteto.rpglogger.stat.StatGame;
import tsuteto.rpglogger.util.EntityNameUtil;
import tsuteto.rpglogger.util.Utilities;

public class ParamWitherSkull extends ParamEntity<EntityWitherSkull>
{
    private ParamEntity shootingEntity;

    public ParamWitherSkull(EntityWitherSkull entity, EntityPlayer player, ParamWorld world)
    {
        super(entity, player);
        if (entity.shootingEntity != null)
        {
            this.shootingEntity = ParamConverter.convertEntityToParam(entity.shootingEntity, player, world);
        }
    }

    @Override
    protected void addStat(RlLogManager logger, StatGame statGame)
    {
        statGame.getEntitiesTracked().put(this.entityId, new StatEntity(this));

        if (this.shootingEntity != null)
        {
            logger.addMsgTranslate("witherSkull.thrown",
                    Utilities.getMobMsgColor(statGame.getMobEntities().get(this.shootingEntity.entityId)),
                    EntityNameUtil.getMobName(this.shootingEntity));
        }
        else
        {
            logger.addMsgTranslate("witherSkull.thrown", Color.white,
                    RlMsgTranslate.getInstance().getMsg("something"));
        }
    }
}
