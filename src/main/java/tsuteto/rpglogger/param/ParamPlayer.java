package tsuteto.rpglogger.param;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

/**
 * Represents parameters of the player
 *
 * @author Tsuteto
 *
 */
public class ParamPlayer extends ParamEntityLivingBase<EntityPlayer> implements IParamPlayer
{
    public int dimension;
    public String dimensionName;
    public int expTotal;
    public int expLevel;
    public float exp;
    public int xpBarCap;
    public boolean isSleeping;
    public boolean isSneaking;
    public float distanceWalkedModified;
    public int foodLevel;
    public ItemStack equippedItem;
    public boolean onGround;
    public boolean isCollidedHorizontally;
    public boolean hasAlly = false;

    public ParamPlayer(EntityPlayer player, ParamWorld world)
    {
        super(player, player, world);

        entityName = "player";

        dimension = player.dimension;
        dimensionName = player.worldObj.provider.getDimensionName();

        expTotal = player.experienceTotal;
        expLevel = player.experienceLevel;
        exp = player.experience;
        xpBarCap = player.xpBarCap();

        isSleeping = player.isPlayerSleeping();
        isSneaking = player.isSneaking();
        distanceWalkedModified = player.distanceWalkedModified;
        foodLevel = player.getFoodStats().getFoodLevel();
        equippedItem = player.getCurrentEquippedItem();
        onGround = player.onGround;
        isCollidedHorizontally = player.isCollidedHorizontally;
    }

    /**
     * returns true if the entity provided in the argument can be seen. (Raytrace)
     */
    public boolean canEntityBeSeen(World worldObj, Entity par1Entity)
    {
        return worldObj.rayTraceBlocks(Vec3.createVectorHelper(this.posX, this.posY + this.eyeHeight, this.posZ), Vec3.createVectorHelper(par1Entity.posX, par1Entity.posY + par1Entity.getEyeHeight(), par1Entity.posZ)) == null;
    }
}
