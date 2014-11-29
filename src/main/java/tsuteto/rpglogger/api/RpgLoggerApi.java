package tsuteto.rpglogger.api;

import cpw.mods.fml.common.Loader;

/**
 * <p>RPG Logger API Class</p>
 *
 * This is WIP, not tested, but maybe it can work.<br>
 * If you want to try it:<br>
 * - First of all, put the entire source of RPG Logger into your MCP.<br>
 * - To register an original message, use ModLoader.addLocalization().<br>
 * - After recompiling, contain the .class files in the package 'net.minecraft.src.rpglogger.api' (keeping the directory) into your mod.<br>
 * - And don't forget to be sure to run successfully without the RPG Logger if you release your mod.<br>
 * <br>
 * Here's an example:<br>
 * <pre>
 * ModLoader.addLocalization("mod.sayHello", "Hello, this is %s!");
 *
 * boolean isRpgLoggerEnabled = RpgLoggerApi.isEnabled();
 * if (isRpgLoggerEnabled)
 * {
 *     RlMessageApi messageApi = RpgLoggerApi.getMessageApi();
 *     messageApi.addMsgTranslate("mod.sayHello", 0xFFFFFF, "my mod");
 * }
 * </pre>
 * -> This will show "Hello, this is my mod!" on the message window.
 *
 * @author Tsuteto
 *
 */
public class RpgLoggerApi
{
    /** True when the RPG Logger mod is installed */
    private static boolean isRpgLoggerEnabled;

    /** Message API */
    private static RlMessageApi messageApi;

    /**
     * Lets your mod know whether the RPG Logger is installed.
     * This will work as of @PostInit or BaseMod.ModsLoaded().
     * @return true when the RPG Logger is installed
     */
    public static boolean isEnabled() {
        return Loader.isModLoaded("RPGLogger");
    }

    /**
     * Returns this API's revision number that increases when its interface is modified. It's similar to a version.
     * Recommended to use for checking compatibility in your mod.
     * @return revision number
     */
    public static int getRevision()
    {
        return 2;
    }

    /**
     * Returns a singleton instance of the Message API.
     * @return a Message API instance
     */
    public static RlMessageApi getMessageApi() {
        if (messageApi == null)
        {
            messageApi = new RlMessageApi();
        }
        return messageApi;
    }

}
