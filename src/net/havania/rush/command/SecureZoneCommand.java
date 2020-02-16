package net.havania.rush.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.havania.core.Core;
import net.havania.core.utils.Rank;
import net.havania.rush.Rush;
import net.havania.rush.game.GameManager;

public class SecureZoneCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(Core.getCore().getPlayerData((Player) sender).getRank().isUpper(Rank.ADMIN))
		{
			if(sender instanceof Player)
			{
				GameManager game = Rush.getGameManager();
				Player p = (Player) sender;
				
				if(game.secureZone)
				{
					game.secureZone = false;
					p.sendMessage("§aSecure Zone désactivé !");
				}
				else
				{
					game.secureZone = true;
					p.sendMessage("§aSecure Zone activé ! (cassez/posez des blocs)");
				}
			}
		}
		
		return false;
	}

}
