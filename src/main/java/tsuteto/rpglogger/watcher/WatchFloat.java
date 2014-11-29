package tsuteto.rpglogger.watcher;


public class WatchFloat
{
    float val = 0;
    float diff = 0;
    int ticksDelayed = 15;
    int ticksRemaining = -1;

    IWatchImpl<Float> impl;

    public WatchFloat(float val)
    {
        this.val = val;
        impl = new Simple();
    }

    public boolean checkVal(float newVal)
    {
        return impl.check(newVal);
    }

    public float getSum()
    {
        float output = diff;
        diff = 0;
        return output;
    }

    public float getVal()
    {
        return val;
    }

    public void setVal(float val)
    {
        this.val = val;
    }

    public WatchFloat setModeDiff(int ticks)
    {
        ticksDelayed = ticks;
        impl = new Diff();
        return this;
    }

    public WatchFloat setModeSettled(int ticks)
    {
        ticksDelayed = ticks;
        impl = new DelayForSettled();
        return this;
    }

    public WatchFloat setModeDelay(int ticks)
    {
        ticksDelayed = ticks;
        impl = new Delay();
        return this;
    }

    private class Simple implements IWatchImpl<Float>
    {

        @Override
        public boolean check(Float newVal)
        {
            if (val != newVal)
            {
                val = newVal;
                return true;
            }
            return false;
        }
    }

    private class Diff implements IWatchImpl<Float>
    {

        @Override
        public boolean check(Float newVal)
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
                diff += newVal - val;
                ticksRemaining = ticksDelayed;
                val = newVal;
            }
            return false;
        }
    }

    private class DelayForSettled implements IWatchImpl<Float>
    {

        @Override
        public boolean check(Float newVal)
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

    private class Delay implements IWatchImpl<Float>
    {

        @Override
        public boolean check(Float newVal)
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
