package tsuteto.rpglogger.param;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.village.Village;
import tsuteto.rpglogger.logging.RlLogManager;
import tsuteto.rpglogger.stat.StatGame;
import tsuteto.rpglogger.stat.StatVillager;
import cpw.mods.fml.common.registry.VillagerRegistry;

/**
 * Represents parameters of Villagers
 *
 * @author Tsuteto
 *
 */
public class ParamVillager extends ParamEntityAgeable<EntityVillager>
{
    public int profession;
    public boolean isPlaying;
    public boolean isMating;
    public boolean isVillageMember;

    public ParamVillager(EntityVillager entity, EntityPlayer player, ParamWorld world, Village village)
    {
        super(entity, player, world);
        profession = entity.getProfession();
        isPlaying = entity.isPlaying();
        isMating = entity.isMating();
        isVillageMember = village != null;

        String professionName = this.getVillagerProfessionName(entity);
        if (professionName != null)
        {
            entityName = professionName;
        }
    }

    /**
     * Returns the villager's profession name
     */
    private String getVillagerProfessionName(EntityVillager villager)
    {
        switch (profession)
        {
        case 0:
            return "Villager.farmer";
        case 1:
            return "Villager.librarian";
        case 2:
            return "Villager.priest";
        case 3:
            return "Villager.smith";
        case 4:
            return "Villager.butcher";
        default:
            ResourceLocation skin = VillagerRegistry.getVillagerSkin(villager.getProfession(), null);
            if (skin != null)
            {
                String fileName = skin.getResourcePath();
                int idxStart = fileName.lastIndexOf("/");
                int idxEnd = fileName.lastIndexOf(".");
                String name = fileName.substring(idxStart == -1 ? 0 : idxStart + 1, idxEnd == -1 ? fileName.length() : idxEnd);
                return "Villager." + name;
            }
            else
            {
                return "Villager";
            }
        }
    }

    @Override
    protected void addStat(RlLogManager logger, StatGame statGame)
    {
        if (this.isTamed && this.canBeSeen || this.isFoundByPlayer)
        {
            // Add ALL visible allies and other mobs NEARBY
            statGame.getMobEntities().put(this.entityId, new StatVillager(this));
        }
    }
}
