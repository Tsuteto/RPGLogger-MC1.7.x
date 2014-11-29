package tsuteto.rpglogger.accessor;

import java.lang.reflect.Method;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import tsuteto.rpglogger.util.ReflectionHelper;

/**
 * Provides access to protected variables/methods in EntityLivingBase
 * @author Tsuteto
 *
 */
public class EntityLivingAccessor
{
    private static Method applyArmorCalculations;
    private static Method applyPotionDamageCalculations;

    private EntityLivingBase entityLivingBase;

    static
    {
        applyArmorCalculations = ReflectionHelper.findMethod(
                EntityLivingBase.class,
                new Class[]{DamageSource.class, float.class},
                "applyArmorCalculations", "func_70655_b");

        applyPotionDamageCalculations = ReflectionHelper.findMethod(
                EntityLivingBase.class,
                new Class[]{DamageSource.class, float.class},
                "applyPotionDamageCalculations", "func_70672_c");
    }

    public EntityLivingAccessor(EntityLivingBase EntityLivingBase)
    {
        this.entityLivingBase = EntityLivingBase;
    }

    public float applyArmorCalculations(DamageSource par1DamageSource, float par2) {
        return (Float)ReflectionHelper.invoke(applyArmorCalculations, this.entityLivingBase, par1DamageSource, par2);
    }

    public float applyPotionDamageCalculations(DamageSource par1DamageSource, float par2)
    {
        return (Float)ReflectionHelper.invoke(applyPotionDamageCalculations, this.entityLivingBase, par1DamageSource, par2);
    }
}
