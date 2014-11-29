package tsuteto.rpglogger.param;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Represents parameters of EntityAgeable
 *
 * @author Tsuteto
 *
 * @param <E>
 */
public class ParamEntityAgeable<E extends EntityAgeable> extends ParamEntityLiving<E>
{
    public boolean isChild;

    public ParamEntityAgeable(E entity, EntityPlayer player, ParamWorld world)
    {
        super(entity, player, world);
        isChild = entity.isChild();
    }
}
