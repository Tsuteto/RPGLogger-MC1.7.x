package tsuteto.rpglogger.param;

import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Represents parameters of Ocelots
 *
 * @author Tsuteto
 *
 */
public class ParamOcelot extends ParamEntityTameable<EntityOcelot>
{
    public ParamOcelot(EntityOcelot entity, EntityPlayer player, ParamWorld world)
    {
        super(entity, player, world);
    }
}
