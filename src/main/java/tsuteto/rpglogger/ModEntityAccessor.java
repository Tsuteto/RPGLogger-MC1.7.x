package tsuteto.rpglogger;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Accesses to tame methods and getter/setter methods in entities added by
 * various MODs
 *
 * @author Tsuteto
 *
 */
public class ModEntityAccessor
{
    public static Map<Class, ModEntityAccessor> entityClasses = new HashMap<Class, ModEntityAccessor>();
    private Method getOwner;
    private Method isTamed;
    private boolean isTamable;
    private Map<String, Method> boolMethods = new HashMap<String, Method>();

    private ModEntityAccessor()
    {
    }

    public static ModEntityAccessor registerEntityClass(Class entityClass)
    {
        if (entityClass == null)
        {
            return null;
        }

        if (entityClasses.containsKey(entityClass))
        {
            return null;
        }

        ModEntityAccessor accessor = new ModEntityAccessor();
        Method[] entityClsMtd = entityClass.getDeclaredMethods();
        Method getOwner = null;
        Method isTamed = null;
        boolean isTamable = false;

        for (Method m : entityClsMtd)
        {
            String name = m.getName();
            if (m.getParameterTypes().length == 0)
            {
                if (name.matches("^get(.*Owner|.+Master)$") && m.getReturnType().equals(String.class))
                {
                    try
                    {
                        m.setAccessible(true);
                        getOwner = m;
                        isTamable = true;
                    }
                    catch (SecurityException e)
                    {}
                }
                if (name.matches("^is.*Tamed$") && m.getReturnType().equals(boolean.class))
                {
                    try
                    {
                        m.setAccessible(true);
                        isTamed = m;
                        isTamable = true;
                    }
                    catch (SecurityException e)
                    {}
                }
                else if (name.matches("^is[A-Z].+") && m.getReturnType().equals(boolean.class))
                {
                    try
                    {
                        accessor.boolMethods.put(name.substring(2, 3).toLowerCase() + name.substring(3), m);
                    }
                    catch (SecurityException e)
                    {}
                }
            }
            // System.out.println(m.getReturnType().toString() + " " + entityClass.getName() + "#" + name);
        }
        accessor.getOwner = getOwner;
        accessor.isTamed = isTamed;
        accessor.isTamable = isTamable;

        entityClasses.put(entityClass, accessor);
        // System.out.println((accessor.getOwner != null ? "Tamed method " + accessor.getOwner.getName() : "None of tamed method") + " in the class " + entityClass.getName());
        return accessor;
    }

    public boolean isTamable()
    {
        return isTamable;
    }

    public static ModEntityAccessor getAccessor(Class entityClass)
    {
        return entityClasses.get(entityClass);
    }

    public String getOwner(EntityLivingBase entity)
    {
        if (getOwner == null)
        {
            return null;
        }
        try
        {
            return (String) getOwner.invoke(entity);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public Boolean isTamed(EntityLivingBase entity, EntityPlayer player)
    {
        if (getOwner == null && isTamed == null)
        {
            return false;
        }
        try
        {
            if (getOwner != null)
            {
                return player.getCommandSenderName().equalsIgnoreCase(getOwner.invoke(entity).toString());
            }
            else
            {
                return (Boolean) isTamed.invoke(entity);
            }
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public Boolean isStat(EntityLivingBase entity, String statName)
    {
        try
        {
            return (Boolean) boolMethods.get(statName).invoke(entity);
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public String[] getStatList()
    {
        return boolMethods.keySet().toArray(new String[0]);
    }
}
