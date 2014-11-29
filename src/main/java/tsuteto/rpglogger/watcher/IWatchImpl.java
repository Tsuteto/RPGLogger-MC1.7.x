package tsuteto.rpglogger.watcher;

/**
 * Handles parameters in a certain way
 * 
 * @author Tsuteto
 * 
 * @param <E>
 */
public interface IWatchImpl<E>
{
    boolean check(E newVal);
}
