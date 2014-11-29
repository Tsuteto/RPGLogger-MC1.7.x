package tsuteto.rpglogger;

/**
 * Defines language for message
 *
 * @author Tsuteto
 *
 */
public enum Lang
{
    auto(null), en_US("en_US"), ja_JP("ja_JP"), ja_kana_JP("ja-kana_JP");

    String langFile;

    Lang(String langFile)
    {
        this.langFile = langFile;
    }

    public String getFileName()
    {
        return langFile + ".rlang";
    }
    public String getCustomFileName()
    {
        return langFile + ".lang";
    }
}

