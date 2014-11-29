package tsuteto.rpglogger.param;

import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import tsuteto.rpglogger.logging.RlLogManager;
import tsuteto.rpglogger.stat.StatGame;
import tsuteto.rpglogger.stat.StatHorse;

public class ParamHorse extends ParamEntityAnimal<EntityHorse>
{
    //public int field_110278_bp;
    //public int field_110279_bq;
    //public boolean isJumping;
    public boolean hasChest;
    public int armorType;
    //public boolean isEating;
    //public boolean neighing;
    //public boolean bred;
    //public boolean hasReproduced;
    public int temperPts;
    //public double jumpStrength;
    //public int variant;
    //public int func_110265_bP;
    //public float jumpPower;

    public ParamHorse(EntityHorse entity, EntityPlayer player, ParamWorld world)
    {
        super(entity, player, world);

        if (!entity.hasCustomNameTag())
        {
            int i = entity.getHorseType();

            switch (i)
            {
                case 0:
                default:
                    entityName = "horse";
                    break;
                case 1:
                    entityName = "donkey";
                    break;
                case 2:
                    entityName = "mule";
                    break;
                case 3:
                    entityName = "zombiehorse";
                    break;
                case 4:
                    entityName = "skeletonhorse";
                    break;
            }
        }

        this.isTameable = true;
        this.isTamed = entity.isTame();
        this.owner = entity.func_152119_ch();

        //this.field_110278_bp = entity.field_110278_bp; // ?
        //this.field_110279_bq = entity.field_110279_bq; // unused
        //this.type = entity.func_110265_bP(); // Type
        //this.variant = entity.func_110202_bQ(); // Variant
        //this.isJumping = entity.func_110246_bZ(); // Jump
        this.hasChest = entity.isChested(); // ChestedHorse
        this.armorType = entity.func_110241_cb();
        //this.isEating = entity.func_110204_cc(); // eating
        //this.neighing = entity.func_110209_cd(); // neighing motion
        //this.bred = entity.func_110205_ce(); // Bred (Unused)
        //this.hasReproduced = entity.func_110243_cf(); // hasReproduced (Unused)
        this.temperPts = entity.getTemper(); // Temper
        //this.jumpStrength = entity.func_110215_cj(); // Jump strength
        //this.jumpPower = entity.field_110277_bt; // jump power factor
    }

    @Override
    protected void addStat(RlLogManager logger, StatGame statGame)
    {
        if (this.isFoundByPlayer)
        {
            // Add mobs NEARBY
            statGame.getMobEntities().put(this.entityId, new StatHorse(this));
        }
    }
}
