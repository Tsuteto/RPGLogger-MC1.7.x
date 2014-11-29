package tsuteto.rpglogger.param;

import java.awt.Color;

import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.player.EntityPlayer;
import tsuteto.rpglogger.logging.RlLogManager;
import tsuteto.rpglogger.stat.StatBattle;
import tsuteto.rpglogger.stat.StatGame;
import tsuteto.rpglogger.stat.StatMob;
import tsuteto.rpglogger.stat.StatWitch;
import tsuteto.rpglogger.util.EntityNameUtil;

/**
 * Represents parameters of a mob
 *
 * @author Tsuteto
 *
 */
public class ParamMob<E extends EntityLiving> extends ParamEntityLiving<E>
{

    public ParamMob(E entity, EntityPlayer player, ParamWorld world)
    {
        super(entity, player, world);
    }

    @Override
    protected void addStat(RlLogManager logger, StatGame statGame)
    {
        ParamMob paramMob = this;
        if (paramMob.isFoundByPlayer
                || (EntityFlying.class.isAssignableFrom(paramMob.entityClass)
                        || EntityDragon.class.isAssignableFrom(paramMob.entityClass)))
        {
            // ENCOUNTER
            // Add only hostile mobs NEARBY
            // System.out.println((statGame.mobEntities.size() + 1) + " + (" + param.entityId + ")" +
            // rpgLogger.getMobName(param) + String.format("%.2fm", param.getDistanceToEntity(player)));
            if (paramMob instanceof ParamWitch)
            {
                statGame.getMobEntities().put(paramMob.entityId, new StatWitch((ParamWitch) paramMob));
            }
            else
            {
                statGame.getMobEntities().put(paramMob.entityId, new StatMob(paramMob));
            }

            StatBattle statBattle = statGame.statBattle;
            statBattle.enemyMobManager.encounter(paramMob);
            logger.addMsgTranslate("player.foundMob", Color.orange, EntityNameUtil.getMobName(paramMob));
        }
    }

}
