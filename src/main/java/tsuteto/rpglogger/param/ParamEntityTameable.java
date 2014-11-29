package tsuteto.rpglogger.param;

import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Represents parameters of EntityTamable
 *
 * @author Tsuteto
 */
public class ParamEntityTameable<E extends EntityTameable> extends ParamEntityAnimal<E>
{
    public boolean isSitting;

    public ParamEntityTameable(E entity, EntityPlayer player, ParamWorld world)
    {
        super(entity, player, world);
        if (entity.getOwner() != null)
        {
            owner = entity.getOwner().getCommandSenderName();
        }
        isTamed = entity.isTamed();
        isTameable = true;
        isSitting = entity.isSitting();
    }
}
