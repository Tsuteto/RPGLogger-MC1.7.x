package tsuteto.rpglogger.param;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;

public class ParamEntityLiving<E extends EntityLiving> extends ParamEntityLivingBase<E>
{
    public boolean hasCustomName;
    public boolean isLeashed;

    public ParamEntityLiving(E entity, EntityPlayer player, ParamWorld world)
    {
        super(entity, player, world);

        this.hasCustomName = entity.hasCustomNameTag();
        if (hasCustomName)
        {
            this.entityName = entity.getCommandSenderName();
        }
        this.isLeashed = entity.getLeashed();
    }

}
