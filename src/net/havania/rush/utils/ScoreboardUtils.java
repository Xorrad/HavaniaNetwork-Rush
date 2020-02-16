package net.havania.rush.utils;

import org.bukkit.entity.Player;

import net.havania.core.Core;
import net.havania.core.utils.PlayerData;
import net.havania.core.utils.scoreboard.CustomScoreboard;
import net.havania.rush.Rush;
import net.havania.rush.game.GameManager;
import net.havania.rush.game.Team;

public class ScoreboardUtils {
	
	public static void setToLobbyScoreboard(Player p)
	{
		GameManager gameManager = Rush.getGameManager();
		PlayerData pd = Core.getCore().getPlayerData(p);
		
		if(pd != null)
		{
			if(pd.scoreboard != null)
			{
				pd.scoreboard.remove(4);
				pd.scoreboard.remove(5);
				pd.scoreboard.add("Joueurs: " + (gameManager.players.size() == gameManager.teams.size()*4 ? "§a" : "§c") + ""+gameManager.players.size()+"/"+(gameManager.teams.size()*4)+"", 5);
				pd.scoreboard.add("Temps: §7" + (gameManager.gameTask.getTimer() == -1 ? "En attente..." : gameManager.gameTask.getTimer() + "s"), 4);
				pd.scoreboard.rebuild();
			}
			else
			{
				pd.scoreboard = new CustomScoreboard("§aHavania§7[Rush]");
				pd.scoreboard.add("§m-------------------", 7);
				pd.scoreboard.add("§e", 6);
				pd.scoreboard.add("Joueurs: " + (gameManager.players.size() == gameManager.teams.size()*4 ? "§a" : "§c") + ""+gameManager.players.size()+"/"+(gameManager.teams.size()*4)+"", 5);
				pd.scoreboard.add("Temps: §7" + (gameManager.gameTask.getTimer() == -1 ? "En attente..." : gameManager.gameTask.getTimer() + "s"), 4);
				pd.scoreboard.add("§e", 3);
				pd.scoreboard.add("§6mc.havania.net", 2);
				pd.scoreboard.add("§m-------------------", 1);
				
				pd.scoreboard.build();
			}
			
			pd.scoreboard.send(p);
		}
	}

	public static void setoGameScoreboard(Player p) 
	{
		GameManager gameManager = Rush.getGameManager();
		PlayerData pd = Core.getCore().getPlayerData(p);
		Team team = gameManager.getPlayerTeam(p);
		
		if(pd != null)
		{
			pd.scoreboard = new CustomScoreboard("§aHavania§7[Rush]");
			
			pd.scoreboard.add("§m-------------------");
			
			if(team != null) //Player isn't a spectator
			{
				if(!team.hasBed())
				{
					pd.scoreboard.add("§4/!\\ Lit détruit !");
					pd.scoreboard.blankLine();
				}
			}
			
			for(Team t : gameManager.getTeams())
			{
				/*if(t.getPlayers().size() == 0)
				{
					pd.scoreboard.add(t.getColor() + "§m" + t.getName() + "§f: " + t.getPlayers().size() + " §7" + (t.hasBed() ? "✔" : "✘"));
				}
				else
				{
					pd.scoreboard.add(t.getTag() + "§f: " + t.getPlayers().size() + " §7" + (t.hasBed() ? "✔" : "✘"));
				}*/
				
				pd.scoreboard.add(t.getTag() + "§f: " + t.getPlayers().size() + " §7" + (t.hasBed() ? "✔" : "✘"));
			}
			
			if(team != null) //Player isn't a spectator
			{
				pd.scoreboard.blankLine();
				pd.scoreboard.add("Tu es dans l'équipe " + team.getTag());
			}
			
			pd.scoreboard.add("§m-------------------");
			
			pd.scoreboard.build();
			pd.scoreboard.send(p);
		}
	}

}
