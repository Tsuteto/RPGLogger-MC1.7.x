//package tsuteto.rpglogger.settings.moapi;
//
//import moapi.ModOption;
//import moapi.ModOptionBoolean;
//
//public class RlOptionBoolean extends ModOptionBoolean
//{
//    private DisplayStringFormatter formatter = null;
//
//    public RlOptionBoolean(String id, String name, boolean value)
//    {
//        super(id, name, value);
//    }
//
//    @Override
//    public String getDisplayString(boolean scope)
//    {
//        boolean shouldDisplayGlobal = !scope && useGlobalValue();
//
//        if (formatter != null)
//        {
//            return formatter.manipulate(this, getStringValue(getValue(scope)), shouldDisplayGlobal);
//        }
//        else
//        {
//            if (shouldDisplayGlobal) {
//                return getName() + ": GLOBAL";
//            }
//
//            return getName() + ": " + getStringValue(getValue(scope));
//        }
//    }
//
//    public RlOptionBoolean setFormatter(DisplayStringFormatter formatter)
//    {
//        this.formatter = formatter;
//        return this;
//    }
//}
