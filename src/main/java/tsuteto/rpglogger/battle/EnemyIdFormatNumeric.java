package tsuteto.rpglogger.battle;

/**
 * Provides enemy id in numeric
 * 
 * @author Tsuteto
 * 
 */
public class EnemyIdFormatNumeric implements IEnemyIdFormat
{

    @Override
    public String format(int id)
    {
        return String.valueOf(id + 1);
    }

}
