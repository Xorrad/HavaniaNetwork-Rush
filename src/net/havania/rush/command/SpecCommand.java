package net.havania.rush.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.havania.core.Core;
import net.havania.core.utils.Rank;
import net.havania.rush.Rush;

public class SpecCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(Core.getCore().getPlayerData((Player) sender).getRank().isUpper(Rank.ADMIN))
		{
			Rush.getGameManager().addSpec((Player) sender);
		}
		
		//InvUtils.giveGameItem((Player) sender);
		
		return false;
	}

}
