package tsuteto.rpglogger.watcher;


public class WatchInt
{
    int val = 0;
    int diff = 0;
    int ticksDelayed = 15;
    int ticksRemaining = -1;

    IWatchImpl<Integer> impl;

    public WatchInt(int val)
    {
        this.val = val;
        impl = new Simple();
    }

    public boolean checkVal(int newVal)
    {
        return impl.check(newVal);
    }

    public int getDiff()
    {
        int output = diff;
        diff = 0;
        return output;
    }

    public int getVal()
    {
        return val;
    }

    public void setVal(int val)
    {
        this.val = val;
    }

    public WatchInt setModeDiff(int ticks)
    {
        ticksDelayed = ticks;
        impl = new Diff();
        return this;
    }

    public WatchInt setModeSettled(int ticks)
    {
        ticksDelayed = ticks;
        impl = new DelayForSettled();
        return this;
    }

    public WatchInt setModeDelay(int ticks)
    {
        ticksDelayed = ticks;
        impl = new Delay();
        return this;
    }

    private class Simple implements IWatchImpl<Integer>
    {

        @Override
        public boolean check(Integer newVal)
        {
            if (val != newVal)
            {
                val = newVal;
                return true;
            }
            return false;
        }
    }

    private class Diff implements IWatchImpl<Integer>
    {

        @Override
        public boolean check(Integer newVal)
        {
            if (val == newVal)
            {
                if (ticksRemaining > -1)
                {
                    ticksRemaining--;
                }
                if (ticksRemaining == 0 && diff != 0)
                {
                    return true;
                }
            }
            else
            {
                diff += newVal - val;
                ticksRemaining = ticksDelayed;
                val = newVal;
            }
            return false;
        }
    }

    private class DelayForSettled implements IWatchImpl<Integer>
    {

        @Override
        public boolean check(Integer newVal)
        {
            if (val == newVal)
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
                val = newVal;
                ticksRemaining = ticksDelayed;
            }
            return false;
        }
    }

    private class Delay implements IWatchImpl<Integer>
    {

        @Override
        public boolean check(Integer newVal)
        {
            if (newVal.equals(val))
            {
                ticksRemaining = ticksDelayed;
            }
            else
            {
                if (ticksRemaining <= 0)
                {
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
