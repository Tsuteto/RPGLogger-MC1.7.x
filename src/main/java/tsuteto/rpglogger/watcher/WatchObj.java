package tsuteto.rpglogger.watcher;


public class WatchObj<T>
{
    T val = null;
    T prevVal = null;
    int ticksRemaining = 0;
    int ticksDelayed = 15;

    IWatchImpl<T> impl;

    public WatchObj(T val)
    {
        this.val = val;
        impl = new Simple();
    }

    public boolean checkVal(T newVal)
    {
        return impl.check(newVal);
    }

    public T getVal()
    {
        return val;
    }

    public void setVal(T val)
    {
        this.val = val;
    }

    public T getPrevVal()
    {
        return prevVal;
    }

    public void reset()
    {
        val = null;
    }

    public WatchObj setModeWait(int ticks)
    {
        ticksDelayed = ticks;
        impl = new Delay();
        return this;
    }

    public WatchObj setModeSettled(int ticks)
    {
        ticksDelayed = ticks;
        impl = new DelayForSettled();
        return this;
    }

    private class Simple implements IWatchImpl<T>
    {

        @Override
        public boolean check(T newVal)
        {
            if (newVal == null && val != null || newVal != null && !newVal.equals(val))
            {
                prevVal = val;
                val = newVal;
                return true;
            }
            return false;
        }
    }

    private class DelayForSettled implements IWatchImpl<T>
    {

        @Override
        public boolean check(T newVal)
        {
            if (newVal == null && val == null || newVal != null && newVal.equals(val))
            {
                if (ticksRemaining > -1)
                {
                    ticksRemaining--;
                }
                if (ticksRemaining == 0)
                {
                    return true;
                }
            }
            else
            {
                prevVal = val;
                val = newVal;
                ticksRemaining = ticksDelayed;
            }
            return false;
        }
    }

    private class Delay implements IWatchImpl<T>
    {

        @Override
        public boolean check(T newVal)
        {
            if (newVal == null && val == null || newVal != null && newVal.equals(val))
            {
                ticksRemaining = ticksDelayed;
            }
            else
            {
                if (ticksRemaining <= 0)
                {
                    prevVal = val;
                    val = newVal;
                    ticksRemaining = ticksDelayed;
                    return true;
                }
                ticksRemaining--;
            }
            return false;
        }

    }
}
