package net.havania.rush.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.havania.core.Core;
import net.havania.core.utils.Rank;
import net.havania.rush.listener.entity.EntitySpawn;

public class CenterVillagerCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender instanceof Player)
		{
			Player p = (Player) sender;
			
			if(Core.getCore().getPlayerData((Player) sender).getRank().isUpper(Rank.ADMIN))
			{
				if(EntitySpawn.center)
				{
					EntitySpawn.center = false;
					p.sendMessage("§aPnj du centre désactivé !");
				}
				else
				{
					EntitySpawn.center = true;
					p.sendMessage("§aPnj du centre activé !");
				}
			}
		}
		
		return false;
	}

}
