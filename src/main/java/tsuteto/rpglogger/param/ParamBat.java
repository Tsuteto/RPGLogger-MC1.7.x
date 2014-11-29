package tsuteto.rpglogger.param;

import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.player.EntityPlayer;
import tsuteto.rpglogger.logging.RlLogManager;
import tsuteto.rpglogger.stat.StatBat;
import tsuteto.rpglogger.stat.StatGame;

/**
 * Represents parameters of Bat
 *
 * @author Tsuteto
 * @param <E>
 *
 * @param <E>
 */
public class ParamBat extends ParamEntityLiving<EntityBat>
{
    public boolean isHanging;

    public ParamBat(EntityBat entity, EntityPlayer player, ParamWorld world)
    {
        super(entity, player, world);
        isHanging = entity.getIsBatHanging();
    }

    @Override
    protected void addStat(RlLogManager logger, StatGame statGame)
    {
        if (this.isFoundByPlayer)
        {
            // Add mobs NEARBY
            statGame.getMobEntities().put(this.entityId, new StatBat(this));
        }
    }
}
