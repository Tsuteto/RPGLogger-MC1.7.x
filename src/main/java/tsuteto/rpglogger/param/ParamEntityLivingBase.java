package tsuteto.rpglogger.param;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import tsuteto.rpglogger.ModEntityAccessor;
import tsuteto.rpglogger.logging.RlLogManager;
import tsuteto.rpglogger.stat.StatEntityLivingBase;
import tsuteto.rpglogger.stat.StatGame;
import tsuteto.rpglogger.util.Utilities;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents parameters of EntityLivingBase
 *
 * @author Tsuteto
 */
public class ParamEntityLivingBase<E extends EntityLivingBase> extends ParamEntity<E>
{
    public float entityHealth;
    public float rotationYaw;
    public float rotationYawHead;
    public float rotationPitch;
    public float eyeHeight;
    public float height;
    public int ticksExisted;

    public Integer ridingEntity = null;

    public boolean isInsideWater;
    public boolean isFoundByPlayer;
    public boolean[] isPotionActive = new boolean[Potion.potionTypes.length];

    public boolean isTameable = false;
    public String owner = null;
    public boolean isTamed = false;

    // For mod entities
    public ModEntityAccessor modAccessor = null;
    public Map modBoolParams = new HashMap();

    public ParamEntityLivingBase(E entity, EntityPlayer player, ParamWorld world)
    {
        super(entity, player);
        entityHealth = entity.getHealth();
        rotationYaw = entity.rotationYaw;
        rotationYawHead = entity.rotationYawHead;
        rotationPitch = entity.rotationPitch;
        eyeHeight = entity.getEyeHeight();
        height = entity.height;
        ticksExisted = entity.ticksExisted;

        if (entity.ridingEntity != null)
        {
            ridingEntity = entity.ridingEntity.getEntityId();
        }

        isInsideWater = entity.isInsideOfMaterial(Material.water);
        isFoundByPlayer = (player != null) ? chkFoundByPlayer(player, lightValue) : false;

        for (Potion potion : Potion.potionTypes)
        {
            if (potion == null)
            {
                continue;
            }
            isPotionActive[potion.id] = entity.isPotionActive(potion);
        }

        modAccessor = ModEntityAccessor.getAccessor(entity.getClass());

        if (modAccessor != null)
        {
            String[] statList = modAccessor.getStatList();
            for (String statName : statList)
            {
                modBoolParams.put(statName, modAccessor.isStat(entity, statName));
            }

            isTameable = modAccessor.isTamable();

            if (isTameable)
            {
                owner = modAccessor.getOwner(entity);
                isTamed = modAccessor.isTamed(entity, player);
            }
        }
    }

    public ParamEntityLivingBase(E entity)
    {
        super(entity);
        entityHealth = entity.getHealth();
        rotationYawHead = entity.rotationYawHead;
        rotationPitch = entity.rotationPitch;
        eyeHeight = entity.getEyeHeight();
    }

    @Override
    public void setStat(RlLogManager logger, StatGame statGame)
    {
        if (statGame.getMobEntities().containsKey(this.entityId))
        {
            statGame.getMobEntities().get(this.entityId).updateParam(this);
        }
        else
        {
            this.addStat(logger, statGame);
        }
    }

    @Override
    protected void addStat(RlLogManager logger, StatGame statGame)
    {
        if (this.isTamed && this.canBeSeen || this.isFoundByPlayer)
        {
            // Add ALL ally mobs and other mobs NEARBY
            statGame.getMobEntities().put(this.entityId, new StatEntityLivingBase(this));
        }
    }

    /**
     * Whether the mob found by player
     */
    public boolean chkFoundByPlayer(EntityPlayer player, int lightValue)
    {
        double searchRadius;
        if (player.dimension != 1)
        {
            searchRadius = Math.min(15, Math.pow(2.5, Math.sqrt(lightValue)) - 1);
        }
        else
        {
            searchRadius = 10;
        }
        if (getDistance(player.posX, player.posY, player.posZ) > searchRadius) {
            return false;
        }
        return isMobWithinSight(player);
    }

    /**
     * Whether the mob within sight of the player
     */
    public boolean isMobWithinSight(EntityPlayer player)
    {
        Vec3 vec3d = Utilities.getLook(player).normalize();
        Vec3 vec3d1 = Vec3.createVectorHelper(
                this.posX - player.posX,
                (this.boundingBox.minY + (this.height / 2.0F)) - player.boundingBox.minY,
                this.posZ - player.posZ);
        vec3d1 = vec3d1.normalize();
        double d1 = vec3d.dotProduct(vec3d1);
        if (d1 > 0.5D)
        {
            return canBeSeen;
        }
        else
        {
            return false;
        }
    }

    public Vec3 getHeadLook()
    {
        float f = MathHelper.cos(-rotationYawHead * 0.01745329F - (float) Math.PI);
        float f2 = MathHelper.sin(-rotationYawHead * 0.01745329F - (float) Math.PI);
        float f4 = -MathHelper.cos(-rotationPitch * 0.01745329F);
        float f6 = MathHelper.sin(-rotationPitch * 0.01745329F);
        return Vec3.createVectorHelper(f2 * f4, f6, f * f4);
    }

    public Vec3 getLook()
    {
        float f1;
        float f2;
        float f3;
        float f4;

        f1 = MathHelper.cos(-this.rotationYaw * 0.017453292F - (float)Math.PI);
        f2 = MathHelper.sin(-this.rotationYaw * 0.017453292F - (float)Math.PI);
        f3 = -MathHelper.cos(-this.rotationPitch * 0.017453292F);
        f4 = MathHelper.sin(-this.rotationPitch * 0.017453292F);
        return Vec3.createVectorHelper((f2 * f3), f4, (f1 * f3));
    }

    /**
     * Returns true if the player makes eye contact with the mob
     */
    public boolean isEyeContactWithPlayer(ParamPlayer player)
    {
        //System.out.printf("%s: ", this.entityName);

        double distance = getDistanceToEntity(player);
        //System.out.printf("d=%.2f ", distance);
        if (distance > 10)
        {
            //System.out.println("");
            return false;
        }

        Vec3 playerEyeLine = player.getHeadLook().normalize();
        Vec3 distanceToEntity = Vec3.createVectorHelper(posX - player.posX, (boundingBox.minY + eyeHeight)
                - (player.posY + player.eyeHeight), posZ - player.posZ);
        double lengthVec1 = distanceToEntity.lengthVector();

        distanceToEntity = distanceToEntity.normalize();

        double d1 = playerEyeLine.dotProduct(distanceToEntity);

        //System.out.printf("eye1=%.3f ", d1 - (1.0D - 0.025D / lengthVec1));
        if (d1 > 1.0D - 0.25D / lengthVec1)
        {
            Vec3 entityEyeLine = getHeadLook().normalize();
            Vec3 distanceToPlayer = Vec3.createVectorHelper(player.posX - posX,
                    (player.boundingBox.minY + player.eyeHeight) - (posY + eyeHeight), player.posZ - posZ);
            distanceToPlayer = distanceToPlayer.normalize();

            double lengthVec2 = distanceToPlayer.lengthVector();
            double d2 = entityEyeLine.dotProduct(distanceToPlayer);

            //System.out.printf("eye2=%.3f%n", d2 - (1.0D - 0.025D / lengthVec2));
            return d2 > 1.0D - 0.25D / lengthVec2;

        }
        else
        {
            //System.out.println("");
            return false;
        }
    }

    /**
     * Gets the distance to the position. Args: x, y, z
     */
    public double getDistance(double par1, double par3, double par5)
    {
        double var7 = this.posX - par1;
        double var9 = this.posY - par3;
        double var11 = this.posZ - par5;
        return MathHelper.sqrt_double(var7 * var7 + var9 * var9 + var11 * var11);
    }
}
