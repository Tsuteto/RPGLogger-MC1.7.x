package tsuteto.rpglogger.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import tsuteto.rpglogger.util.Utilities;

public class CommandTemperature extends CommandBase
{
    @Override
    public String getCommandName()
    {
        return "temp";
    }

    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_)
    {
        return "command.rpglogger.temp.usage";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args)
    {
        EntityPlayer player = getCommandSenderAsPlayer(sender);
        int tileX = MathHelper.floor_double(player.posX);
        int tileY = MathHelper.floor_double(player.posY);
        int tileZ = MathHelper.floor_double(player.posZ);
        float gameVal = sender.getEntityWorld().getBiomeGenForCoords(tileX, tileZ).getFloatTemperature(tileX, tileY, tileZ);
        double celsius = Utilities.calcTemperatureCelsius(gameVal);

        sender.addChatMessage(new ChatComponentText(String.format("Temperature here is %.1f celsius degrees, from game value %.3f", celsius, gameVal)));
    }
}
