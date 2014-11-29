package tsuteto.rpglogger.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import cpw.mods.fml.relauncher.ReflectionHelper.UnableToAccessFieldException;
import cpw.mods.fml.relauncher.ReflectionHelper.UnableToFindFieldException;
import cpw.mods.fml.relauncher.ReflectionHelper.UnableToFindMethodException;

/**
 * Methods for accessing to protected variables/methods in a class
 *
 * @author Tsuteto
 *
 */
public class ReflectionHelper
{

    public static Field findField(Class clazz, String... fieldNames)
    {
        Exception failed = null;
        for (String fieldName : fieldNames)
        {
            try
            {
                Field f = clazz.getDeclaredField(fieldName);
                f.setAccessible(true);
                return f;
            }
            catch (Exception e)
            {
                failed = e;
            }
        }
        throw new UnableToFindFieldException(fieldNames, failed);
    }

    public static Method findMethod(Class clazz, Class[] args, String... methodNames)
    {
        Exception failed = null;
        for (String methodName : methodNames)
        {
            try
            {
                Method m = clazz.getDeclaredMethod(methodName, args);
                m.setAccessible(true);
                return m;
            }
            catch (Exception e)
            {
                failed = e;
            }
        }
        throw new UnableToFindMethodException(methodNames, failed);
    }

    public static Object invoke(Method m, Object instance, Object... args)
    {
        try
        {
            return m.invoke(instance, args);
        }
        catch (Exception e)
        {
            throw new UnableToAccessMethodException(new String[] { m.getName() }, e);
        }
    }

    public static Object get(Field f, Object instance)
    {
        try
        {
            return f.get(instance);
        }
        catch (Exception e)
        {
            throw new UnableToAccessFieldException(new String[] { f.getName() }, e);
        }
    }

    public static void set(Field f, Object instance, Object value)
    {
        try
        {
            f.set(instance, value);
        }
        catch (Exception e)
        {
            throw new UnableToAccessFieldException(new String[] { f.getName() }, e);
        }
    }

    public static class UnableToAccessMethodException extends RuntimeException
    {

        private String[] methodNameList;

        public UnableToAccessMethodException(String[] methodNames, Exception e)
        {
            super(e);
            this.methodNameList = methodNames;
        }
    }
}
