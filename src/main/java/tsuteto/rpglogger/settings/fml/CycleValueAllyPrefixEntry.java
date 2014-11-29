package tsuteto.rpglogger.settings.fml;

import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.IConfigElement;
import tsuteto.rpglogger.RpgLogger;
import tsuteto.rpglogger.logging.RlMsgTranslate;

public class CycleValueAllyPrefixEntry extends CycleValueTranslateEntry
{
    public CycleValueAllyPrefixEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement<String> configElement)
    {
        super(owningScreen, owningEntryList, configElement);
    }


    @Override
    public void updateValueButtonText()
    {
        RlMsgTranslate msgTrans = RpgLogger.getInstance().msgTrans;
        this.btnValue.displayString = msgTrans.getMsg(
                "mob.allyPrefix." + configElement.getValidValues()[currentIndex],
                msgTrans.getMsg("options.allyPrefix.namePart"));
    }
}
