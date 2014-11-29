package tsuteto.rpglogger;

import java.util.Comparator;

import net.minecraft.entity.Entity;

/**
 * Provides a way to sort entity
 *
 * @author Tsuteto
 *
 */
public class EntityListComparator implements Comparator<Entity>
{
    private Entity center;

    public EntityListComparator(Entity center)
    {
        this.center = center;
    }

    @Override
    public int compare(Entity e1, Entity e2)
    {
        if (e1 == null && e2 == null)
        {
            return 0;
        }
        if (e1 == null)
        {
            return 1;
        }
        if (e2 == null)
        {
            return -1;
        }
        Float d1 = e1.getDistanceToEntity(center);
        Float d2 = e2.getDistanceToEntity(center);
        return d1.compareTo(d2);
    }
}
