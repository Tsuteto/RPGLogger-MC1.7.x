package tsuteto.rpglogger.param;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.entity.projectile.EntityWitherSkull;
import tsuteto.rpglogger.EntityListComparator;
import tsuteto.rpglogger.RpgLogger;
import tsuteto.rpglogger.accessor.EntityVillagerAccessor;
import tsuteto.rpglogger.util.Utilities;

public class ParamConverter
{
//    private static ExecutorService executor = Executors.newFixedThreadPool(5);
//    private static AtomicInteger numThreads = new AtomicInteger();
//
//    private CountDownLatch latch;
//
//    public List<ParamEntity> convertAllEntity(Entity[] worldEntityList, final EntityPlayer player, final ParamWorld world)
//    {
//        boolean isCrowd = false;
//        // System.out.println("worldEntityList: " + worldEntityList.size());
//
//        // Turn the flag on to add some constraints when there are more than 256
//        // entities in the game
//        if (worldEntityList.length > 512)
//        {
//            isCrowd = true;
//            // Sort the entity list by distance from the player
//            Arrays.sort(worldEntityList, new EntityListComparator(player));
//        }
//
//        final List<ParamEntity> paramList = new CopyOnWriteArrayList<ParamEntity>();
//        latch = new CountDownLatch(worldEntityList.length);
//        numThreads.set(0);
//
//        for (final Entity entity : worldEntityList)
//        {
//            // Why is the null mixed in as of 1.3.2?
//            if (entity == null)
//            {
//                latch.countDown();
//                continue;
//            }
//
//            // Cut off entities further than 64. If isCrowd is true, finish the loop because the list is sorted.
//            if (entity.getDistanceToEntity(player) > 64)
//            {
//                if (isCrowd)
//                {
//                    Thread.currentThread().interrupt();
//                    latch = new CountDownLatch(numThreads.get());
//                    break;
//                }
//                else
//                {
//                    latch.countDown();
//                    continue;
//                }
//            }
//
//            numThreads.incrementAndGet();
//            executor.execute(new Runnable() {
//
//                @Override
//                public void run()
//                {
//                    if (paramList.size() <= 256)
//                    {
//                        ParamEntity param = convertEntityToParam(entity, player, world);
//                        if (param != null)
//                        {
//                            paramList.add(param);
//                        }
//                    }
//                    numThreads.decrementAndGet();
//                    latch.countDown();
//                }
//
//            });
//        }
//
//        try
//        {
//            latch.await();
//        }
//        catch (InterruptedException e) {
//            try
//            {
//                latch.await(1, TimeUnit.MILLISECONDS);
//            }
//            catch (InterruptedException e1) {}
//        }
//
//        return paramList;
//    }

    public static List<ParamEntity> convertAllEntities(List<Entity> worldEntityList, final EntityPlayer player, final ParamWorld world)
    {
        List paramList = new ArrayList<ParamEntity>();

        for (final Entity entity : worldEntityList)
        {
            // Why is the null mixed in as of 1.3.2?
            if (entity == null)
            {
                continue;
            }

            if (paramList.size() <= 256)
            {
                ParamEntity param = convertEntityToParam(entity, player, world);
                if (param != null)
                {
                    paramList.add(param);
                }
            }
        }

        return paramList;
    }

    public static List<ParamEntity> convertAllEntities(Entity[] worldEntityList, final EntityPlayer player, final ParamWorld world)
    {
        boolean isCrowd = false;
        // System.out.println("worldEntityList: " + worldEntityList.size());

        // Turn the flag on to add some constraints when there are more than 256
        // entities in the game
        if (worldEntityList.length > 512)
        {
            isCrowd = true;
            // Sort the entity list by distance from the player
            Arrays.sort(worldEntityList, new EntityListComparator(player));
        }

        List paramList = new ArrayList<ParamEntity>();

        for (final Entity entity : worldEntityList)
        {
            // Why is the null mixed in as of 1.3.2?
            if (entity == null)
            {
                continue;
            }

            // Cut off entities further than 64. If isCrowd is true, finish the loop because the list is sorted.
            if (entity.getDistanceToEntity(player) > 64)
            {
                if (isCrowd)
                {
                    break;
                }
                else
                {
                    continue;
                }
            }

            if (paramList.size() <= 256)
            {
                ParamEntity param = convertEntityToParam(entity, player, world);
                if (param != null)
                {
                    paramList.add(param);
                }
            }
        }

        return paramList;
    }

    /**
     * Converts the entity into a param object for logging
     *
     * @param entity
     * @param player
     * @param world
     * @return
     */
    public static ParamEntity convertEntityToParam(Entity entity, EntityPlayer player, ParamWorld world)
    {
        if (entity == null) return null;

        if (entity instanceof EntityWitch)
        {
            return new ParamWitch((EntityWitch) entity, player, world);
        }
        else if (Utilities.chkHostileMob(entity))
        {
            return new ParamMob((EntityLiving) entity, player, world);
        }
        else if (entity instanceof EntityHorse)
        {
            return new ParamHorse((EntityHorse) entity, player, world);
        }
        else if (entity instanceof EntityWolf)
        {
            return new ParamWolf((EntityWolf) entity, player, world);
        }
        else if (entity instanceof EntityOcelot)
        {
            return new ParamOcelot((EntityOcelot) entity, player, world);
        }
        else if (entity instanceof EntityVillager)
        {
            EntityVillagerAccessor bridgedEntity = new EntityVillagerAccessor((EntityVillager) entity);
            return new ParamVillager((EntityVillager) entity, player, world, bridgedEntity.getVillage());
        }
        else if (entity instanceof EntityBat)
        {
            return new ParamBat((EntityBat) entity, player, world);
        }
        else if (entity instanceof EntityPlayer)
        {
            return new ParamPlayerIndication((EntityPlayer) entity);
        }
        else if (RpgLogger.dqmEntityTameable != null && RpgLogger.dqmEntityTameable.isAssignableFrom(entity.getClass()))
        {
            return new ParamEntityTameableDqm((EntityTameable)entity, player, world);
        }
        else if (entity instanceof EntityTameable)
        {
            return new ParamEntityTameable((EntityTameable) entity, player, world);
        }
        else if (entity instanceof EntityAnimal)
        {
            return new ParamEntityAnimal((EntityAnimal) entity, player, world);
        }
        else if (entity instanceof EntityLiving)
        {
            return new ParamEntityLiving((EntityLiving) entity, player, world);
        }
        else if (entity instanceof EntityLivingBase)
        {
            return new ParamEntityLivingBase((EntityLivingBase) entity, player, world);
        }
        else if (entity instanceof EntityThrowable)
        {
            return new ParamEntityThrowable((EntityThrowable) entity, player, world);
            // } else if (entity instanceof EntityEnderCrystal) {
            // return new ParamEnderCrystal((EntityEnderCrystal)entity, player);
        }
        else if (entity instanceof EntityFireworkRocket)
        {
            return new ParamFireworkRocket((EntityFireworkRocket) entity, player);
        }
        else if (entity instanceof EntityWitherSkull)
        {
            return new ParamWitherSkull((EntityWitherSkull) entity, player, world);
        }
        else if (entity instanceof EntityEnderEye)
        {
            return new ParamEnderEye((EntityEnderEye) entity, player);
        }

        // EntityItem, EntityXPOrb, EntityFallingSand etc
        return null;
    }
}
