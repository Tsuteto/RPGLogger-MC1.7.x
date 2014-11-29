package tsuteto.rpglogger.param;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import tsuteto.rpglogger.logging.RlLogManager;
import tsuteto.rpglogger.stat.StatEntityAnimal;
import tsuteto.rpglogger.stat.StatGame;

/**
 * Represents parameters of EntityAnimal
 *
 * @author Tsuteto
 *
 * @param <E>
 */
public class ParamEntityAnimal<E extends EntityAnimal> extends ParamEntityAgeable<E>
{
    public boolean isInLove;

    public ParamEntityAnimal(E entity, EntityPlayer player, ParamWorld world)
    {
        super(entity, player, world);
        isInLove = entity.isInLove();
    }

    @Override
    protected void addStat(RlLogManager logger, StatGame statGame)
    {
        ParamEntityAnimal animal = this;
        if (animal.isTamed && animal.canBeSeen || animal.isFoundByPlayer)
        {
            // Add visible allies and other mobs NEARBY
            statGame.getMobEntities().put(animal.entityId, new StatEntityAnimal(animal));
        }
    }
}
