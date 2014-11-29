package tsuteto.rpglogger.util;

import net.minecraft.util.MathHelper;

/**
 *
 * @author Tsuteto
 *
 */
public class SimpleMilestone
{
    private float base;
    private float val;
    private float min;
    private float milestonePassed = 0F;

    public SimpleMilestone(float val, float min, float base)
    {
        this.val = val;
        this.min = min;
        this.base = base;
    }

    public boolean check(float newVal)
    {
        boolean flag = false;
        if (newVal - min >= 0 && val - min < 0)
        {
            flag = true;
            milestonePassed = min;
        }
        int newValMP = MathHelper.floor_float(newVal / base);
        if (newValMP != 0 && newValMP != MathHelper.floor_float(val / base))
        {
            flag = true;
            milestonePassed = MathHelper.floor_float(newVal / base) * base;
        }
        val = newVal;
        return flag;
    }

    public float getMilestonePassed()
    {
        return milestonePassed;
    }

}
