package tsuteto.rpglogger.param;

import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import tsuteto.rpglogger.logging.RlLogManager;
import tsuteto.rpglogger.stat.StatGame;
import tsuteto.rpglogger.stat.StatWolf;

/**
 * Represents parameters of Wolves
 *
 * @author Tsuteto
 *
 */
public class ParamWolf extends ParamEntityTameable<EntityWolf>
{
    public boolean isShaking;
    public boolean isAngry;

    public ParamWolf(EntityWolf entity, EntityPlayer player, ParamWorld world)
    {
        super(entity, player, world);
        isShaking = entity.getWolfShaking();
        isAngry = entity.isAngry();

    }

    @Override
    protected void addStat(RlLogManager logger, StatGame statGame)
    {
        ParamWolf wolf = this;
        {
            if (wolf.isTamed && wolf.canBeSeen || wolf.isFoundByPlayer)
            {
                // Add visible allies and wilds NEARBY
                statGame.getMobEntities().put(wolf.entityId, new StatWolf(wolf));
            }
        }
    }
}
