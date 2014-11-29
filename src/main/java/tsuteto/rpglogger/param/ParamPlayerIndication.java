package tsuteto.rpglogger.param;

import net.minecraft.entity.player.EntityPlayer;

/**
 * Just indicates the player
 * 
 * @author Tsuteto
 * 
 */
public class ParamPlayerIndication extends ParamEntityLivingBase<EntityPlayer> implements IParamPlayer
{

    public ParamPlayerIndication(EntityPlayer player)
    {
        super(player);
    }

}
