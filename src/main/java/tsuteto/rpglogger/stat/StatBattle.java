package tsuteto.rpglogger.stat;

import tsuteto.rpglogger.battle.EnemyMobManager;
import tsuteto.rpglogger.settings.RpgLoggerSettings;
import tsuteto.rpglogger.watcher.WatchBool;

/**
 * Manages status on battle
 * 
 * @author Tsuteto
 * 
 */
public class StatBattle
{
    public EnemyMobManager enemyMobManager;

    public WatchBool statInBattle;

    public int killCount = 0;
    public boolean isAllyJoined = false;
    public boolean isPlayerJoined = false;

    public StatBattle(RpgLoggerSettings settings)
    {
        enemyMobManager = new EnemyMobManager();
        enemyMobManager.setEnemyIdType(settings.getEnemyIdType());

        statInBattle = new WatchBool(!enemyMobManager.isEnemyCleared());
    }

    public boolean chkStatInBattle()
    {
        return statInBattle.checkVal(!enemyMobManager.isEnemyCleared());
    }

    public boolean isInBattle()
    {
        return !enemyMobManager.isEnemyCleared();
    }

    public void reset()
    {
        killCount = 0;
        isAllyJoined = false;
        isPlayerJoined = false;
        enemyMobManager.clear();
    }
}
