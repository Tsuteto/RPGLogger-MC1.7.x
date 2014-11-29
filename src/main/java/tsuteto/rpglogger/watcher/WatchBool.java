package tsuteto.rpglogger.watcher;

/**
 * Watches a boolean parameter
 * @author Tsuteto
 *
 */
public class WatchBool
{
    boolean val;
    int waitRemain = 0;
    int waitTicks = 15;

    private IWatchImpl<Boolean> impl;

    public WatchBool(boolean val)
    {
        this.val = val;
        impl = new Simple();
    }

    public boolean checkVal(Boolean newVal)
    {
        return impl.check(newVal);
    }

    public boolean getVal()
    {
        return val;
    }

    public void setVal(boolean val)
    {
        this.val = val;
    }

    public WatchBool setDelay(int ticks)
    {
        waitTicks = ticks;
        impl = new Delay();
        return this;
    }

    public WatchBool setLazy(int ticks)
    {
        waitTicks = ticks;
        impl = new Lazy();
        return this;
    }

    private class Simple implements IWatchImpl<Boolean>
    {

        @Override
        public boolean check(Boolean newVal)
        {
            if (val != newVal)
            {
                val = newVal;
                return true;
            }
            return false;
        }
    }

    private class Delay implements IWatchImpl<Boolean>
    {

        @Override
        public boolean check(Boolean newVal)
        {
            if (newVal.equals(val))
            {
                waitRemain = waitTicks;
            }
            else
            {
                if (waitRemain > -1)
                {
                    waitRemain--;
                }
                if (waitRemain == 0)
                {
                    val = newVal;
                    return true;
                }
            }
            return false;
        }
    }

    private class Lazy implements IWatchImpl<Boolean>
    {

        @Override
        public boolean check(Boolean newVal)
        {
            if (waitRemain > 0)
            {
                val = newVal;
                waitRemain--;
            }
            else if (!newVal.equals(val))
            {
                val = newVal;
                waitRemain = waitTicks;
                return true;
            }
            return false;
        }
    }
}
