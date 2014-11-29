package tsuteto.rpglogger.accessor;

import java.lang.reflect.Field;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.village.Village;
import tsuteto.rpglogger.util.ReflectionHelper;

/**
 * Provides access to protected variables/methods in EntityVillager
 * @author Tsuteto
 *
 */
public class EntityVillagerAccessor
{
    private static Field villageObj;

    public EntityVillager entity;

    static
    {
        villageObj = ReflectionHelper.findField(EntityVillager.class, "villageObj", "field_70954_d");
    }

    public EntityVillagerAccessor(EntityVillager entity)
    {
        this.entity = entity;
    }

    public Village getVillage()
    {
        return (Village)ReflectionHelper.get(villageObj, entity);
    }
}
