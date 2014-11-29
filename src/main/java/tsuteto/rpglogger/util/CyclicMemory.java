package tsuteto.rpglogger.util;

/**
 * Behaves cyclic memory; each time it gets new one, it drops the oldest one
 *
 * @author Tsuteto
 *
 */
public class CyclicMemory
{
    public byte[] cache;
    public int ptr;

    public CyclicMemory(int size)
    {
        cache = new byte[size];
        ptr = 0;
    }

    public void add(int val)
    {
        cache[ptr++] = (byte)val;
        if (ptr == cache.length)
        {
            ptr = 0;
        }
    }

    public int sum()
    {
        int sum = 0;
        for (int val : cache)
        {
            sum += val;
        }
        return sum;
    }

    public int average()
    {
        return sum() / cache.length;
    }
}
