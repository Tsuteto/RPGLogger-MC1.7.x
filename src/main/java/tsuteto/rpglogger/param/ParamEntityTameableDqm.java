package tsuteto.rpglogger.param;

import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Represents parameters of EntityTamable for DQM
 *
 * @author Tsuteto
 *
 * @param <E>
 */
public class ParamEntityTameableDqm<E extends EntityTameable> extends ParamEntityTameable<E>
{
    public boolean isSitting;

    public ParamEntityTameableDqm(E entity, EntityPlayer player, ParamWorld world)
    {
        super(entity, player, world);
        isTamed = true;
    }
}
