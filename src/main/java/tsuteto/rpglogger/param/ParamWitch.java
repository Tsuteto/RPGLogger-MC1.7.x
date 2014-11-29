package tsuteto.rpglogger.param;

import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.player.EntityPlayer;

public class ParamWitch extends ParamMob<EntityWitch>
{
    public boolean isAggressive;

    public ParamWitch(EntityWitch entity, EntityPlayer player, ParamWorld world)
    {
        super(entity, player, world);
        this.isAggressive = entity.getAggressive();
    }
}
