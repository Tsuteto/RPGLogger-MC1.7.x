package tsuteto.rpglogger.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import tsuteto.rpglogger.RpgLogger;
import tsuteto.rpglogger.logging.RlMsgTranslate;
import tsuteto.rpglogger.param.ParamEntity;
import tsuteto.rpglogger.param.ParamEntityAgeable;
import tsuteto.rpglogger.param.ParamEntityLiving;
import tsuteto.rpglogger.param.ParamEntityThrowable;
import tsuteto.rpglogger.param.ParamMob;
import tsuteto.rpglogger.param.ParamVillager;
import tsuteto.rpglogger.settings.RpgLoggerSettings;
import tsuteto.rpglogger.stat.StatEntityLivingBase;
import tsuteto.rpglogger.stat.StatGame;

/**
 * Utility For obtaining name of entity
 *
 * @author Tsuteto
 *
 */
public class EntityNameUtil
{
    /**
     * Returns the mob name for display
     */
    public static String getMobName(ParamEntity param)
    {
        return getMobName(param, true);
    }

    /**
     * Returns the mob name with its prefix
     */
    private static String getMobName(ParamEntity param, boolean forDisplay)
    {
        RpgLogger rpgLogger = RpgLogger.getInstance();
        StatGame statGame = rpgLogger.statGame;
        RlMsgTranslate msgTrans = RlMsgTranslate.getInstance();

        if (param == null) return msgTrans.getMsg("something");

        String name = getSimpleMobName(param);

        if (forDisplay)
        {
            // Ally
            StatEntityLivingBase stat = statGame.getMobEntities().get(param.entityId);
            if (stat instanceof StatEntityLivingBase && stat.isTamed())
            {
                name = msgTrans.getMsg("mob.allyPrefix." + rpgLogger.settings.getAllyPrefix(), name);
            }

            // Child
            if (param instanceof ParamEntityAgeable)
            {
                ParamEntityAgeable ageable = (ParamEntityAgeable) param;
                if (ageable.isChild)
                {
                    name = msgTrans.getMsg("mob.child", new Object[]{name});
                }
            }

            // Wandering villager
            if (param instanceof ParamVillager)
            {
                ParamVillager villager = (ParamVillager) param;
                if (!villager.isVillageMember)
                {
                    name = msgTrans.getMsg("villager.wandering", new Object[]{name});
                }
            }

            // Hostile mob ID
            if (param instanceof ParamMob)
            {
                String enemyId = statGame.statBattle.enemyMobManager.getEnemyId(param);
                if (enemyId != null)
                {
                    name = msgTrans.getMsg("mob.identifiedName", new Object[]{name, enemyId});
                }
            }
        }

        return name;
    }

    public static String getSimpleMobName(ParamEntity param)
    {
        RpgLogger rpgLogger = RpgLogger.getInstance();
        RlMsgTranslate msgTrans = RlMsgTranslate.getInstance();

        if (param instanceof ParamEntityLiving)
        {
            ParamEntityLiving paramLiving = (ParamEntityLiving)param;
            if (paramLiving.hasCustomName)
            {
                return msgTrans.getMsg("mob.uniqueName", paramLiving.entityName);
            }
        }

        String name = msgTrans.translateNamedKey("entity." + param.entityName);

        if (name != null)
        {
            return name;
        }

        String className = getEntityNameFromClass(param);
        name = msgTrans.translateNamedKey("entity." + className);

        if (name != null)
        {
            return name;
        }

        return getPhysicalEntityName(param.entityName, className);
    }

    /**
     * Returns the mob name for display without its owner
     */
    public static String getDisplayEntityNameWithoutOwner(ParamEntity param)
    {
        return getSimpleMobName(param);
    }

    /**
     * Returns the name of the thrown entity
     */
    public static String getThrownEntityName(ParamEntityThrowable param)
    {
        RlMsgTranslate msgTrans = RlMsgTranslate.getInstance();
        String name;
        String key;

        if (param.thrownItemName != null)
        {
            name = msgTrans.translateNamedKey(param.thrownItemName);
        }
        else
        {
            name = msgTrans.translateNamedKey("entity." + param.entityName);
        }

        if (name != null && name.length() != 0)
        {
            return name;
        }

        // Items registered with class name
        String className = getEntityNameFromClass(param);
        name = msgTrans.translateNamedKey("entity." + className);

        if (name != null && name.length() != 0)
        {
            return name;
        }

        return getPhysicalEntityName(param.entityName, className);
    }

    /**
     * Returns the name of the item
     */
    public static String getItemName(ItemStack itemstack)
    {
        RlMsgTranslate msgTrans = RlMsgTranslate.getInstance();

        Item item = itemstack.getItem();
        String itemName = item.getUnlocalizedName(itemstack);

        String name = null;

        // Common
        name = msgTrans.translateNamedKey(itemName);

        if (name != null && name.length() != 0)
        {
            return name;
        }

        // For some blocks by Forge MOD
        name = item.getItemStackDisplayName(itemstack);

        if (name != null && name.length() != 0)
        {
            return name;
        }

        // Classify into block or item
        String prefix = (item instanceof ItemBlock) ? "tile" : "item";

        // Items registered with class name
        String className = getItemNameFromClass(item);
        name = msgTrans.translateNamedKey(prefix + "." + className);

        if (name != null && name.length() != 0)
        {
            return name;
        }

        return getPhysicalItemName(itemName, className, prefix);
    }

    /**
     * Returns physical name for unnamed entity
     */
    private static String getPhysicalEntityName(String entityName, String className)
    {
        RlMsgTranslate msgTrans = RlMsgTranslate.getInstance();
        RpgLoggerSettings settings = RpgLogger.getInstance().settings;

        String name;

        // Internal name
        name = entityName;

        if (name != null && name.length() != 0)
        {
            String namePart = name.replaceFirst(".*\\.", "");
            String tempName = Utilities.capitalize(namePart);
            boolean isAdded = msgTrans.addNAKeyToCustomLang("entity." + name + ".name", tempName);
            if (isAdded)
            {
                msgTrans.saveCustomLangFile();
            }
            return tempName;
        }

        // Class name at last
        name = className;
        String tempName = Utilities.capitalize(name);
        boolean isAdded = msgTrans.addNAKeyToCustomLang("entity." + name + ".name", tempName);
        if (isAdded)
        {
            msgTrans.saveCustomLangFile();
        }
        return tempName;
    }

    /**
     * Returns physical name for unnamed item
     */
    private static String getPhysicalItemName(String entityName, String className, String prefix)
    {
        RlMsgTranslate msgTrans = RlMsgTranslate.getInstance();

        String name;

        // Internal name
        name = entityName;

        if (name != null && name.length() != 0)
        {
            String tempName = Utilities.capitalize(name.replaceFirst(prefix + "\\.", ""));
            boolean isAdded = msgTrans.addNAKeyToCustomLang(name + ".name", tempName);
            if (isAdded)
            {
                msgTrans.saveCustomLangFile();
            }
            return tempName;
        }

        // Class name at last
        name = className;
        String tempName = Utilities.capitalize(name);
        boolean isAdded = msgTrans.addNAKeyToCustomLang(prefix + "." + name + ".name", tempName);
        if (isAdded)
        {
            msgTrans.saveCustomLangFile();
        }
        return tempName;
    }

    /**
     * Returns entity name from class name
     */
    public static String getEntityNameFromClass(ParamEntity param)
    {
        String className = param.entityClass.getSimpleName();
        if (className.matches("^Entity.+"))
        {
            return className.replaceFirst("Entity", "");
        }
        else
        {
            return className;
        }
    }

    /**
     * Returns item name from class name
     */
    public static String getItemNameFromClass(Item item)
    {
        String name = null;
        if (item instanceof ItemBlock)
        {
            String className = ((ItemBlock)item).field_150939_a.getClass().getSimpleName();
            if (className.matches("^Block.+"))
            {
                return className.replaceFirst("Block", "");
            }
            else
            {
                return className;
            }
        }
        else
        {
            String className = item.getClass().getSimpleName();
            if (className.matches("^Item.+"))
            {
                return className.replaceFirst("Item", "");
            }
            else
            {
                return className;
            }
        }
    }
}
