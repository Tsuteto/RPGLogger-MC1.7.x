package tsuteto.rpglogger.stat;

import java.util.HashMap;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.util.MathHelper;
import tsuteto.rpglogger.param.ParamPlayer;
import tsuteto.rpglogger.param.ParamWorld;
import tsuteto.rpglogger.util.SimpleMilestone;
import tsuteto.rpglogger.watcher.WatchBool;
import tsuteto.rpglogger.watcher.WatchInt;
import tsuteto.rpglogger.watcher.WatchObj;

/**
 * Manages status of the player
 *
 * @author Tsuteto
 *
 */
public class StatPlayer extends StatEntityLivingBase<ParamPlayer>
{
    public WatchInt statXp;
    public WatchInt statXpLevel;
    public HashMap itemsPickedUp = new HashMap();
    public WatchObj<Integer> statItemsPickedUp;
    public WatchInt statEquippedItemId;
    public WatchInt statEquippedItemDmg;
    public WatchInt statFood;
    public WatchBool statSleeping;
    public WatchBool statSneaking;
    public WatchInt statPlayerMoving;
    public WatchBool statUnderground;
    public SimpleMilestone msWalk;
    public int idleTime = 0;
    private boolean isBowing = false;

    public StatPlayer(ParamPlayer player, ParamWorld world)
    {
        super(player);

        int px = MathHelper.floor_double(player.posX);
        int py = MathHelper.floor_double(player.posY);
        int pz = MathHelper.floor_double(player.posZ);

        statXp = new WatchInt(player.expTotal).setModeDiff(15);
        statXpLevel = new WatchInt(player.expLevel);
        statItemsPickedUp = new WatchObj<Integer>(itemsPickedUp.hashCode()).setModeSettled(120);

        int itemId = player.equippedItem != null ? Item.getIdFromItem(player.equippedItem.getItem()) : 0;
        int itemDmg = player.equippedItem != null ? player.equippedItem.getItemDamage() : 0;
        statEquippedItemId = new WatchInt(itemId).setModeSettled(10);
        statEquippedItemDmg = new WatchInt(itemDmg).setModeSettled(10);

        statFood = new WatchInt(player.foodLevel).setModeDiff(1);
        statSleeping = new WatchBool(player.isSleeping);
        statSneaking = new WatchBool(player.isSneaking);
        statUnderground = new WatchBool(!isAroundSurface(world) && py < 64).setDelay(90);
        msWalk = new SimpleMilestone(player.distanceWalkedModified, 100F, 500F);
        statPlayerMoving = new WatchInt((int) (player.distanceWalkedModified * 100));
    }

    public boolean chkStatXp()
    {
        return statXp.checkVal(param.expTotal);
    }

    public boolean chkStatXpLevel()
    {
        return statXpLevel.checkVal(param.expLevel);
    }

    public boolean chkStatItemsPickedUp()
    {
        return statItemsPickedUp.checkVal(itemsPickedUp.hashCode());
    }

    public boolean chkStatEquippedItem()
    {
        int itemId = param.equippedItem != null ? Item.getIdFromItem(param.equippedItem.getItem()) : 0;
        int itemDmg = param.equippedItem != null ? param.equippedItem.getItemDamage() : 0;
        boolean hasSubtypes = param.equippedItem != null ? param.equippedItem.getItem().getHasSubtypes() : false;
        if (hasSubtypes)
        {
            boolean isItemIdChanged = statEquippedItemId.checkVal(itemId);
            boolean isItemDmgChanged = statEquippedItemDmg.checkVal(itemDmg);
            if (isItemIdChanged)
            {
                statEquippedItemDmg.setVal(itemDmg);
                return true;
            }
            if (isItemDmgChanged)
            {
                statEquippedItemId.setVal(itemId);
                return true;
            }
            return false;
        }
        else
        {
            statEquippedItemDmg.setVal(itemDmg);
            return statEquippedItemId.checkVal(itemId);
        }
    }

    public boolean chkStatFood()
    {
        return statFood.checkVal(param.foodLevel);
    }

    public boolean chkStatSleeping()
    {
        return statSleeping.checkVal(param.isSleeping);
    }

    public boolean chkStatUndergroundTrue(ParamWorld world)
    {
        return statUnderground.checkVal(!isAroundSurface(world) && param.posY < 64);
    }

    public boolean chkStatUndergroundFalse(ParamWorld world)
    {
        return statUnderground.checkVal(world.mapHeight > MathHelper.floor_double(param.posY));
    }

    public boolean chkMsWalk()
    {
        return msWalk.check(param.distanceWalkedModified);
    }

    public boolean chkStatPlayerMoving()
    {
        return statPlayerMoving.checkVal((int) (param.distanceWalkedModified * 100));
    }

    public boolean isAroundSurface(ParamWorld world)
    {
        return world.mapHeight - param.posY < 15;
    }

    public boolean inUnderground()
    {
        return statUnderground.getVal();
    }

    public void countIdleTime()
    {
        Minecraft mc = FMLClientHandler.instance().getClient();
        if (chkStatPlayerMoving() || param.equippedItem != null || !param.onGround || !param.isAlive
                || param.isCollidedHorizontally || param.isSneaking || param.isSleeping || mc.currentScreen != null)
        {
            idleTime = 0;
        }
        else
        {
            idleTime++;
        }
    }

    public boolean chkStatSneaking()
    {
        isBowing  = statSneaking.checkVal(param.isSneaking) && param.isSneaking;
        return isBowing;
    }

    public boolean isBowing()
    {
        return isBowing;
    }
}
