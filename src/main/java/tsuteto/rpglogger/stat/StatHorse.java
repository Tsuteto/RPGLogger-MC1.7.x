package tsuteto.rpglogger.stat;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.StatCollector;
import tsuteto.rpglogger.logging.RlLogManager;
import tsuteto.rpglogger.param.ParamHorse;
import tsuteto.rpglogger.util.EntityNameUtil;
import tsuteto.rpglogger.watcher.WatchBool;
import tsuteto.rpglogger.watcher.WatchInt;

public class StatHorse extends StatEntityAnimal<ParamHorse>
{
    public static Item[] armorList = new Item[]{null, Items.iron_horse_armor, Items.golden_horse_armor, Items.diamond_horse_armor};

    public WatchBool statHasChest;
    public WatchInt statArmorType;
    public WatchInt statTemper;

    public StatHorse(ParamHorse param)
    {
        super(param);
        statHasChest = new WatchBool(param.hasChest);
        statArmorType = new WatchInt(param.armorType);
        statTemper = new WatchInt(param.temperPts).setModeDiff(1);
    }

    @Override
    public void logStat(RlLogManager logger, StatGame statGame)
    {
        super.logStat(logger, statGame);

        if (this.statHasChest.checkVal(param.hasChest) && statHasChest.getVal())
        {
            String key = statHasChest.getVal() ? "mob.equippedChest" : "mob.tookOffChest";
            logger.addMsgTranslate(key, msgColor, EntityNameUtil.getMobName(param));
        }

        if (this.statArmorType.checkVal(param.armorType))
        {
            if (statArmorType.getVal() != 0)
            {
                String armorName = StatCollector.translateToLocal(armorList[param.armorType].getUnlocalizedName() + ".name");
                logger.addMsgTranslate("mob.equipped", msgColor, EntityNameUtil.getMobName(param), armorName);
            }
            else
            {
                logger.addMsgTranslate("mob.tookOffArmor", msgColor, EntityNameUtil.getMobName(param));
            }
        }
        if (this.statTemper.checkVal(param.temperPts))
        {
            if (statTemper.getDiff() >= 0)
            {
                logger.addMsgTranslate("mob.temper.inc", msgColor, EntityNameUtil.getMobName(param), param.temperPts);
            }
            else
            {
                logger.addMsgTranslate("mob.temper.dec", msgColor, EntityNameUtil.getMobName(param), param.temperPts);
            }
        }
    }
}
