package tsuteto.rpglogger.util;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import tsuteto.rpglogger.Lang;
import tsuteto.rpglogger.RpgLogger;
import tsuteto.rpglogger.logging.RlMsgTranslate;
import tsuteto.rpglogger.param.ParamPlayer;
import tsuteto.rpglogger.stat.StatEntityLivingBase;

import com.google.common.base.Strings;

/**
 * Utilities
 *
 * @author Tsuteto
 *
 */
public class Utilities
{
    /**
     * Returns biome name
     */
    public static String getBiomeName(int biomeId, int dimension, boolean inUnderground, float py)
    {
        RlMsgTranslate msgTrans = RlMsgTranslate.getInstance();

        if (dimension == -1 && BiomeGenBase.hell.biomeID == biomeId)
        {
            return msgTrans.getMsg("dimension.Nether");
        }
        else if (dimension == 1 && BiomeGenBase.sky.biomeID == biomeId)
        {
            return msgTrans.getMsg("dimension.The End");
        }
        else if (dimension == 0 && inUnderground)
        {
            return msgTrans.getMsg("underground");
        }
        else
        {
            String key = BiomeGenBase.getBiome(biomeId).biomeName;
            boolean isMutated = key.endsWith(" M");
            String fullkey = "biome." + (isMutated ? key.substring(0, key.length() - 2) : key);
            if (msgTrans.isKeyAvailable(fullkey))
            {
                return msgTrans.getMsg("biome", msgTrans.getMsg(fullkey) + (isMutated ? msgTrans.getMsg("biome.mutated") : ""));
            }
            else
            {
                String biomeName = Utilities.capitalize(key);

                boolean isKeyAdded = msgTrans.addNAKeyToCustomLang(fullkey, biomeName);
                if (isKeyAdded)
                {
                    msgTrans.saveCustomLangFile();
                }

                return msgTrans.getMsg("biome", biomeName);
            }
        }
    }

    public static String getDimensionName(int dimId, String dimNameMc)
    {
        RlMsgTranslate msgTrans = RlMsgTranslate.getInstance();

        String dimNameRl = "dimension." + dimId;
        String dimLocalName;
        boolean isKeyAdded = false;

        if (Strings.isNullOrEmpty(dimNameMc) || msgTrans.isKeyAvailable(dimNameRl))
        {
            isKeyAdded = msgTrans.addNAKeyToCustomLang(dimNameRl, msgTrans.getMsgOrDefault("dimension.default", Utilities.toOrdinalNumber(dimId)));
            dimLocalName = msgTrans.getMsg(dimNameRl);
        }
        else
        {
            String key = "dimension." + dimNameMc;
            if (!msgTrans.isKeyAvailable(key))
            {
                isKeyAdded = msgTrans.addNAKeyToCustomLang(key, "");
            }
            dimLocalName = msgTrans.getMsgOrDefault(key, "");
            if (dimLocalName.isEmpty())
            {
                dimLocalName = dimNameMc;
            }
        }
        if (isKeyAdded) msgTrans.saveCustomLangFile();

        return dimLocalName;
    }

    /**
     * Returns entity type id from entity object, or return null if the entity
     * is the player
     */
    public static Integer getEntityType(Entity entity)
    {
        if (entity instanceof EntityPlayer)
        {
            return null;
        }
        return EntityList.getEntityID(entity);
    }

    /**
     * Whether the mob is hostile
     */
    public static boolean chkHostileMob(Entity entity)
    {
        return entity instanceof EntityLiving && (entity instanceof IMob);
    }

    /**
     * Returns which snow or rain falls here
     */
    public static String getFalls(float temperature)
    {
        RlMsgTranslate msgTrans = RlMsgTranslate.getInstance();
        return temperature >= 0 && temperature <= 0.15F ? msgTrans.getMsg("snow") : msgTrans.getMsg("rain");
    }

    public static Color getMobMsgColor(StatEntityLivingBase stat)
    {
        if (stat != null && stat.isTamed())
        {
            return Color.pink;
        }
        else
        {
            return Color.white;
        }
    }

    /**
     * Returns temperature in a specified place
     */
    public static float getTemperature(World world, int px, int py, int pz)
    {
        try
        {
            return world.getBiomeGenForCoords(px, pz).getFloatTemperature(px, py, pz);
        }
        catch (Exception e)
        {
            RpgLogger.infoLog("Failed to get temperature");
            return -1;
        }
    }

    /**
     * Calculates temperature in degree Celsius from game parameter
     */
    public static float calcTemperatureCelsius(float gameVal)
    {
        return gameVal * 55F - 15F;
    }

    /**
     * Returns the chance of rain here
     */
    public static float getRainfall(World world, int px, int pz)
    {
        try
        {
            WorldChunkManager wcm = world.getWorldChunkManager();
            return wcm.getRainfall(null, px, pz, 1, 1)[0];
        }
        catch (Exception e)
        {
            RpgLogger.infoLog("Failed to get rainfall");
            return -1;
        }
    }

    /**
     * Returns chance of rain in percentage from game parameter
     */
    public static float calcRainFallPercentage(float gameVal)
    {
        return gameVal * 80F + 20F;
    }

    public static Vec3 getLook(EntityLivingBase entity)
    {
        float f1;
        float f2;
        float f3;
        float f4;

        f1 = MathHelper.cos(-entity.rotationYaw * 0.017453292F - (float)Math.PI);
        f2 = MathHelper.sin(-entity.rotationYaw * 0.017453292F - (float)Math.PI);
        f3 = -MathHelper.cos(-entity.rotationPitch * 0.017453292F);
        f4 = MathHelper.sin(-entity.rotationPitch * 0.017453292F);
        return Vec3.createVectorHelper((f2 * f3), f4, (f1 * f3));
    }

    /**
     * Converts the Number to ordinal number
     */
    public static String toOrdinalNumber(long i)
    {
        RlMsgTranslate msgTrans = RpgLogger.getInstance().msgTrans;
        if (i % 100 / 10 != 1)
        {
            if (i % 10 == 1)
            {
                return i + msgTrans.getMsg("ordinal.st");
            }
            if (i % 10 == 2)
            {
                return i + msgTrans.getMsg("ordinal.nd");
            }
            if (i % 10 == 3)
            {
                return i + msgTrans.getMsg("ordinal.rd");
            }
        }
        return i + msgTrans.getMsg("ordinal.th");
    }

    /**
     * Formats the distance to m/km
     */
    public static String formatDistance(float meters)
    {
        RlMsgTranslate msgTrans = RpgLogger.getInstance().msgTrans;
        String formatted;
        if (meters > 1000)
        {
            formatted = (int) (meters / 10F) / 100F + msgTrans.getMsg("km");
        }
        else
        {
            formatted = (int) (meters * 10F) / 10F + msgTrans.getMsg("m");
        }
        return formatted;
    }

    /**
     * Chooses noun form
     */
    public static String chooseNounForm(int num, String singular, String plural)
    {
        if (num == 1 || num == 0)
        {
            return singular;
        }
        else
        {
            return plural;
        }
    }

    public static String capitalize(String s)
    {
        if (Strings.isNullOrEmpty(s))
        {
            return s;
        }
        return s.substring(0, 1).toUpperCase() + (s.length() >= 2 ? s.substring(1) : "");
    }

    /**
     * Calculates the allocated memory to the game
     */
    public static int calcUsingMemory()
    {
        Runtime rt = Runtime.getRuntime();
        return (int) ((double) (rt.totalMemory() - rt.freeMemory()) / rt.totalMemory() * 100);
    }

    /**
     * Chooses language, jp or en
     */
    public static Lang chooseLanguage(Lang lang)
    {
        if (lang == Lang.auto)
        {
            String gamelang = Minecraft.getMinecraft().gameSettings.language;
            if (gamelang.equals(Lang.ja_JP.toString()))
            {
                return Lang.ja_JP;
            }
            else
            {
                return Lang.en_US;
            }
        }
        else
        {
            return lang;
        }
    }

}
