package tsuteto.rpglogger;

import cpw.mods.fml.common.ModMetadata;

public class ModInfo
{
    public static void load(ModMetadata meta)
    {
        meta.modId = RPGLoggerLoader.modid;
        meta.name = "RPG Logger";
        meta.version = "3.7";
        meta.url = "http://forum.minecraftuser.jp/viewtopic.php?f=13&t=1014";
        meta.credits = "Mob name tag featured by yuyu/haoling's Nameplate Mod";
        meta.authorList.add("Tsuteto");
        meta.description = "RPG Logger shows RPG-like log on the game screen, logging battles, events, actions, and various things on you, your co. and mobs.\n\nForm your party, go for adventure!";
        meta.updateUrl = "https://dl.dropboxusercontent.com/u/14577828/mcmod/update/rpglogger.json";
        meta.logoFile = "";

        meta.autogenerated = false;
    }
}
