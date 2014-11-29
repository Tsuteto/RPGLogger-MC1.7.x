package tsuteto.rpglogger.param;

import java.awt.Color;

import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import tsuteto.rpglogger.logging.RlLogManager;
import tsuteto.rpglogger.stat.StatEntity;
import tsuteto.rpglogger.stat.StatGame;
import tsuteto.rpglogger.util.EntityNameUtil;

/**
 * Represents parameters of Eye of Ender
 *
 * @author Tsuteto
 *
 */
public class ParamEnderEye extends ParamEntity<EntityEnderEye>
{

    public ParamEnderEye(EntityEnderEye entity, EntityPlayer player)
    {
        super(entity, player);
    }

    @Override
    protected void addStat(RlLogManager logger, StatGame statGame)
    {
        StatEntity stat = new StatEntity(this);
        statGame.getEntitiesTracked().put(this.entityId, stat);
        logger.addMsgTranslate("player.threw", Color.yellow,
                EntityNameUtil.getItemName(new ItemStack(Items.ender_eye)));
    }
}
