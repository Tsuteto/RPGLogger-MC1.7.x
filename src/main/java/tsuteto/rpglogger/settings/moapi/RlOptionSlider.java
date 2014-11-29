//package tsuteto.rpglogger.settings.moapi;
//
//import moapi.ModOptionSlider;
//
//public class RlOptionSlider extends ModOptionSlider
//{
//    private DisplayStringFormatter formatter = null;
//
//    public RlOptionSlider(String id, String name, int low, int high)
//    {
//        super(id, name, low, high);
//    }
//
//    @Override
//    public String getDisplayString(boolean scope)
//    {
//        boolean shouldDisplayGlobal = !scope && useGlobalValue();
//
//        if (formatter != null)
//        {
//            return formatter.manipulate(this, getValue(scope).toString(), shouldDisplayGlobal);
//        }
//        else
//        {
//            if (shouldDisplayGlobal) {
//                return getName() + ": GLOBAL";
//            }
//
//            return getName() + ": " + getValue(scope).toString();
//        }
//    }
//
//    public RlOptionSlider setFormatter(DisplayStringFormatter formatter)
//    {
//        this.formatter = formatter;
//        return this;
//    }
//}
