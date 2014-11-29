//package tsuteto.rpglogger.settings.moapi;
//
//import moapi.ModOptionMapped;
//
//public class RlOptionMapped extends ModOptionMapped
//{
//    private DisplayStringFormatter formatter = null;
//
//    public RlOptionMapped(String name, String[] labels, int[] keys)
//    {
//        super(name, labels, keys);
//    }
//
//    public RlOptionMapped(String id, String name, String[] labels, int[] keys)
//    {
//        super(id, name, labels, keys);
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
//    public RlOptionMapped setFormatter(DisplayStringFormatter formatter)
//    {
//        this.formatter = formatter;
//        return this;
//    }
//}
