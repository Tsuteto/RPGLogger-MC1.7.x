package tsuteto.rpglogger.settings.fml;

import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.IConfigElement;
import net.minecraft.client.resources.I18n;

public class CycleValueTranslateEntry extends GuiConfigEntries.ButtonEntry
{
    protected final int beforeIndex;
    protected final int defaultIndex;
    protected int       currentIndex;

    public CycleValueTranslateEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement<String> configElement)
    {
        super(owningScreen, owningEntryList, configElement);
        beforeIndex = getIndex(configElement.get().toString());
        defaultIndex = getIndex(configElement.getDefault().toString());
        currentIndex = beforeIndex;
        this.btnValue.enabled = enabled();
        updateValueButtonText();
    }


    private int getIndex(String s)
    {
        for (int i = 0; i < configElement.getValidValues().length; i++)
            if (configElement.getValidValues()[i].equalsIgnoreCase(s))
            {
                return i;
            }

        return 0;
    }

    @Override
    public void updateValueButtonText()
    {
        this.btnValue.displayString = I18n.format(this.configElement.getLanguageKey() + "." + configElement.getValidValues()[currentIndex]);
    }

    @Override
    public void valueButtonPressed(int slotIndex)
    {
        if (enabled())
        {
            if (++this.currentIndex >= configElement.getValidValues().length)
                this.currentIndex = 0;

            updateValueButtonText();
        }
    }

    @Override
    public boolean isDefault()
    {
        return currentIndex == defaultIndex;
    }

    @Override
    public void setToDefault()
    {
        if (enabled())
        {
            currentIndex = defaultIndex;
            updateValueButtonText();
        }
    }

    @Override
    public boolean isChanged()
    {
        return currentIndex != beforeIndex;
    }

    @Override
    public void undoChanges()
    {
        if (enabled())
        {
            currentIndex = beforeIndex;
            updateValueButtonText();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean saveConfigElement()
    {
        if (enabled() && isChanged())
        {
            configElement.set(configElement.getValidValues()[currentIndex]);
            return configElement.requiresMcRestart();
        }
        return false;
    }

    @Override
    public String getCurrentValue()
    {
        return configElement.getValidValues()[currentIndex];
    }

    @Override
    public String[] getCurrentValues()
    {
        return new String[] { getCurrentValue() };
    }
}
