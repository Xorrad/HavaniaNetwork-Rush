package net.havania.rush.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.havania.rush.Rush;
import net.havania.rush.game.GameManager;
import net.havania.rush.game.Team;

public class TeamCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender.hasPermission("havania.admin"))
		{
			if(sender instanceof Player)
			{
				GameManager game = Rush.getGameManager();
				Player p = (Player) sender;
				
				if(args.length > 1)
				{
					Team team = game.getTeam(args[0]);
					if(team != null)
					{
						if(args[1].equalsIgnoreCase("spawn"))
						{
							String world = p.getWorld().getName();
							Double x = p.getLocation().getX();
							Double y = p.getLocation().getY();
							Double z = p.getLocation().getZ();
							Float yaw = p.getLocation().getYaw();
							Float pitch = p.getLocation().getPitch();
								
							game.getMain().getConfig().set("teams." + team.id + ".spawn", world + "," + x + "," + y + "," + z + "," + yaw + "," + pitch);
							game.getMain().saveConfig();
							team.setSpawn(p.getLocation());
							p.sendMessage("§aLe spawn de l'équipe " + team.getTag() + " §aa été changé avec succès !");
						}
						else if(args[1].equalsIgnoreCase("bed"))
						{
							p.sendMessage("§aCassez un bloc pour choisir le lit de l'équipe: " + team.getTag());
							game.placingBed.put(p, team);
						}
						else if(args[1].equalsIgnoreCase("resources"))
						{
							if(args.length > 2)
							{
								if(isInt(args[2]))
								{
									Integer number = Integer.parseInt(args[2]);
									p.sendMessage("§aCassez un bloc pour choisir le spawn des ressources de l'équipe: " + team.getTag());
									game.placingResourcesSpawn.put(p, team);
									game.placingResourcesSpawnNumber.put(p, number);
								}
								else
								{
									p.sendMessage("§cL'argument 3 n'est pas un nombre !");
								}
							}
							else
							{
								p.sendMessage("§cSyntax: /team <team_name> <resources> <number>");
							}
						}
					}
					else 
					{
						p.sendMessage("§cEquipe invalide !");
					}
				}
				else
				{
					p.sendMessage("§cSyntax: /team <team_name> <spawn,bed,resources>");
				}
			}
		}
		
		return false;
	}
	
	public boolean isInt(String s) {
	    try {
	        Integer.parseInt(s);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}

}
