package tsuteto.rpglogger.settings.fml;

import com.google.common.collect.Lists;
import cpw.mods.fml.client.config.DummyConfigElement.DummyCategoryElement;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import tsuteto.rpglogger.RPGLoggerLoader;
import tsuteto.rpglogger.RpgLogger;
import tsuteto.rpglogger.settings.LogCategory;

import java.util.List;

public class RlGuiConfig extends GuiConfig
{
    public RlGuiConfig(GuiScreen parent)
    {
        super(parent, getConfigElements(),
                RPGLoggerLoader.modid, false, false, StatCollector.translateToLocal("rpglogger.options.title"));
    }

    private static List<IConfigElement> getConfigElements()
    {
        List<IConfigElement> elements = Lists.newArrayList();
        List<IConfigElement> messageEnabledList = Lists.newArrayList();

        elements.add(new ConfigElement(getConf().getCategory(OptionsUsingFml.CAT_DISPLAY)));
        elements.add(new ConfigElement(getConf().getCategory(OptionsUsingFml.CAT_MESSAGE)));

        for (LogCategory cat : LogCategory.values())
        {
            messageEnabledList.add(new ConfigElement(getConf().getCategory(cat.getFullName())));
        }
        elements.add(new DummyCategoryElement("msgSwitch", "rpglogger.options.category.msgSwitch", messageEnabledList));


        elements.add(new ConfigElement(getConf().getCategory(OptionsUsingFml.CAT_SYSTEM)));

        return elements;
    }

    private static Configuration getConf()
    {
        return RpgLogger.getInstance().settings.getFmlSettings().conf;
    }
}
