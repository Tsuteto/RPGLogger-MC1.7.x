package tsuteto.rpglogger.battle;

/**
 * Provides no format for enemy id
 * 
 * @author Tsuteto
 * 
 */
public class EnemyIdFormatBlank implements IEnemyIdFormat
{

    @Override
    public String format(int id)
    {
        return null;
    }

}
