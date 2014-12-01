package tsuteto.rpglogger.param;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import tsuteto.rpglogger.RpgLogger;
import tsuteto.rpglogger.logging.RlLogManager;
import tsuteto.rpglogger.stat.StatGame;

/**
 * Represents parameters of Entity
 *
 * @author Tsuteto
 *
 * @param <E>
 */
public class ParamEntity<E extends Entity>
{
    public int entityId;
    public String entityName;
    public Class entityClass;

    public double posX;
    public double posY;
    public double posZ;

    public boolean isAlive;
    public int lightValue;
    public boolean canBeSeen;
    public AxisAlignedBB boundingBox;

    public ParamEntity(E entity, EntityPlayer player)
    {
        // RpgLogger rl = RpgLogger.getInstance();
        entityId = entity.getEntityId();
        entityName = EntityList.getEntityString(entity);
        entityClass = entity.getClass();
        posX = entity.posX;
        posY = entity.posY;
        posZ = entity.posZ;

        isAlive = entity.isEntityAlive();
        lightValue = entity.worldObj.getBlockLightValue((int)posX, (int)posY, (int)posZ);
        boundingBox = entity.boundingBox;

        if (player != null)
        {
            try
            {
                canBeSeen = player.canEntityBeSeen(entity);
            }
            catch (Exception e)
            {
                RpgLogger.errorLog("Failed to get canBeSeen for " + entity.toString());
                canBeSeen = false;
            }
        }
        else
        {
            canBeSeen = false;
        }
    }

    public ParamEntity(E entity)
    {
        this(entity, null);
    }

    public void setStat(RlLogManager logger, StatGame statGame)
    {
        if (statGame.getEntitiesTracked().containsKey(this.entityId))
        {
            statGame.getEntitiesTracked().get(this.entityId).updateParam(this);
        }
        else
        {
            this.addStat(logger, statGame);
        }
    }

    protected void addStat(RlLogManager logger, StatGame statGame)
    {
    }

    public float getDistanceToEntity(ParamEntity par1Entity)
    {
        float f = (float) (posX - par1Entity.posX);
        float f1 = (float) (posY - par1Entity.posY);
        float f2 = (float) (posZ - par1Entity.posZ);
        return MathHelper.sqrt_float(f * f + f1 * f1 + f2 * f2);
    }

    @Override
    public boolean equals(Object o)
    {
        if (o instanceof ParamEntity)
        {
            return entityId == ((ParamEntity) o).entityId;
        }
        else if (o instanceof Entity)
        {
            return entityId == ((Entity) o).getEntityId();
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return entityId;
    }

}
