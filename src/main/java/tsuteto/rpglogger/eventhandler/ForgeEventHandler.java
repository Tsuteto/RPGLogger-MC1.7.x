package tsuteto.rpglogger.eventhandler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AchievementEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import tsuteto.rpglogger.RPGLoggerLoader;
import tsuteto.rpglogger.RpgLogger;
import tsuteto.rpglogger.accessor.EntityLivingAccessor;

/**
 * Handles game events with Forge
 *
 * @author Tsuteto
 *
 */
public class ForgeEventHandler
{
    private RpgLogger rpgLogger;

    public ForgeEventHandler(RpgLogger rpgLogger)
    {
        this.rpgLogger = rpgLogger;
    }

    @SubscribeEvent
    public void onEntityLivingBaseHurt(LivingHurtEvent event)
    {
        if (!RPGLoggerLoader.enabled || !rpgLogger.loaded)
        {
            return;
        }

        EntityLivingBase entity = event.entityLiving;
        DamageSource damagesource = event.source;
        float amount = event.ammount;

        EntityLivingAccessor bridgedEntity = new EntityLivingAccessor(entity);
        amount = bridgedEntity.applyArmorCalculations(damagesource, amount);
        amount = bridgedEntity.applyPotionDamageCalculations(damagesource, amount);

        rpgLogger.onAttack(entity, damagesource, amount);
    }

    @SubscribeEvent
    public void onEntityLivingBaseDeath(LivingDeathEvent event)
    {
        if (RPGLoggerLoader.enabled && rpgLogger.loaded)
        {
            rpgLogger.onDeath(event.entityLiving, event.source);
        }
    }

    @SubscribeEvent
    public void onEntityLivingBaseFall(LivingFallEvent event)
    {
        if (RPGLoggerLoader.enabled && rpgLogger.loaded)
        {
            rpgLogger.onFall(event.entityLiving, event.distance);
        }
    }

    @SubscribeEvent
    public void onAchievement(AchievementEvent event)
    {
        if (RPGLoggerLoader.enabled && rpgLogger.loaded)
        {
            rpgLogger.onAchievement(event.entityPlayer, event.achievement);
        }
    }
}
