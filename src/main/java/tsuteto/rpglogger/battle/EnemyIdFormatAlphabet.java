package tsuteto.rpglogger.battle;

/**
 * Provide enemy id in alphabet
 * 
 * @author Tsuteto
 * 
 */
public class EnemyIdFormatAlphabet implements IEnemyIdFormat
{

    @Override
    public String format(int id)
    {
        int id2 = id;
        int i = 1;
        int l = 26;
        int m = 26;
        while ((id2 /= m) > 0)
        {
            i++;
            m = 27;
        }
        char[] chars = new char[i];

        id2 = id;
        l = 1;
        m = 26;
        for (int j = i - 1; j >= 0; j--)
        {
            int k = j == 0 && i != 1 ? id2 / l - 1 : (id2 / l) % m;
            chars[j] = (char) (0x41 + k);
            l *= 26;
            m = 27;
        }
        return new String(chars);
    }

}
