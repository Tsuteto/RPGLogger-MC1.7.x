package tsuteto.rpglogger.settings;

import java.util.ArrayList;
import java.util.List;

public enum LogType
{
    player_gainedXp,
    player_lostXp,
    player_increasedLvl,
    player_gotInWater,
    player_gotOutOfWater,
    player_healed,
    player_filledBelly,
    player_fellAsleep,
    player_wokeUp,
    player_gaveDmg,
    player_gaveDmgBy,
    player_tookDmg,
    player_tookDmgBy,
    player_tookDmgByMob,
    player_defeated,
    player_itemPickup,
    player_sumItems,
    player_crafted,
    player_smelted,
    player_fell,
    player_threw,
    player_bow,
    player_backToSurface,
    player_goingUnderground,
    player_walked,
    player_foundMob,
    player_lostMob,
    player_respawn,
    player_emptyHand,
    player_tookItem,
    player_rode,
    player_getOff,
    player_finishedBattle,
    player_achievement,
    player_potion,
    player_death,
    player_idle,

    party_owned_single,
    party_owned_multiple,

    item_flew,
    fireworks_launch,

    wolf_shaking,
    wolf_angry,

    bat_flewOff,
    witch_aggressive,

    mob_gotTamed,
    mob_healed,
    mob_threw,
    mob_gaveDmg,
    mob_gaveDmgBy,
    mob_tookDmg,
    mob_tookDmgBy,
    mob_tookDmgByMob,
    mob_defeated,
    mob_spawned,
    mob_growUp,
    mob_backToChild,
    mob_inLove,
    mob_rode,
    mob_getOff,
    mob_tied,
    mob_released,
    mob_equippedChest,
    mob_tookOffChest,
    mob_equipped,
    mob_tookOffArmor,
    mob_temper_inc,
    mob_temper_dec,
    mob_potion,
    mob_death,

    villager_isMating,
    villager_startPlaying,

    entity_destroyed,

    env_enterWorld,
    env_leaveWorld,
    env_rainBegan,
    env_rainLetUp,
    env_brightened,
    env_darkened,
    env_morning,
    env_noon,
    env_night,
    env_midnight,
    env_dayBegan,
    env_thunderBegan,
    env_thunderStopped,
    env_enterBiome,
    env_traveledDimension,
    env_traveledBack;

    private static final List<String> LIST = new ArrayList<String>()
    {
        {
            for (LogType type : LogType.values())
            {
                add(type.name());
            }
        }
    };

    public static LogType parse(String s)
    {
        return LIST.contains(s) ? LogType.valueOf(s) : null;
    }
}
