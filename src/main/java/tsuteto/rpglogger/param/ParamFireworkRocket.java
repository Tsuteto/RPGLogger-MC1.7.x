package tsuteto.rpglogger.param;

import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import tsuteto.rpglogger.logging.RlLogManager;
import tsuteto.rpglogger.stat.StatEntity;
import tsuteto.rpglogger.stat.StatGame;
import tsuteto.rpglogger.util.EntityNameUtil;
import tsuteto.rpglogger.util.Utilities;

public class ParamFireworkRocket extends ParamEntity<EntityFireworkRocket>
{

    public ParamFireworkRocket(EntityFireworkRocket entity, EntityPlayer player)
    {
        super(entity, player);
    }

    @Override
    protected void addStat(RlLogManager logger, StatGame statGame)
    {
        statGame.getEntitiesTracked().put(this.entityId, new StatEntity(this));
        logger.addMsgTranslate("fireworks.launch",
                Utilities.getMobMsgColor(statGame.getMobEntities().get(this.entityId)),
                EntityNameUtil.getItemName(new ItemStack(Items.fireworks)));
    }
}
