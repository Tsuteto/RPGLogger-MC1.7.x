package tsuteto.rpglogger.param;

import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import tsuteto.rpglogger.logging.RlLogManager;
import tsuteto.rpglogger.stat.StatGame;

/**
 * Represents parameters of Ender Crystals
 * 
 * @author Tsuteto
 * 
 */
public class ParamEnderCrystal extends ParamEntity<EntityEnderCrystal>
{

    public ParamEnderCrystal(EntityEnderCrystal entity, EntityPlayer player)
    {
        super(entity, player);
    }
    
    protected void addStat(RlLogManager logger, StatGame statGame)
    {
    }
}
