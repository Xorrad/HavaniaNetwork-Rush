package net.havania.rush.listener.player;

import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import net.havania.core.Core;
import net.havania.rush.Rush;
import net.havania.rush.game.GameManager;
import net.havania.rush.game.Team;
import net.havania.rush.utils.ScoreboardUtils;

public class PlayerKill implements Listener {
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onDeath(PlayerDeathEvent e)
	{
		GameManager game = Rush.getGameManager();
		Player p = e.getEntity();
		Team team = game.getPlayerTeam(p);
		
		Iterator<ItemStack> it = e.getDrops().iterator();
		while(it.hasNext())
		{
			ItemStack i = it.next();
			if(i.getType().equals(Material.LEATHER_BOOTS) || i.getType().equals(Material.LEATHER_HELMET) || i.getType().equals(Material.LEATHER_LEGGINGS))
			{
				it.remove();
			}
		}
		
		e.setDroppedExp(0);
		e.setDeathMessage(null);
		
		Core.getCore().getPlayerData(p).addRushDeaths(1);
		
		if(p.getKiller() != null)
		{
			Player killer = p.getKiller();
			Team killerTeam = game.getPlayerTeam(killer);
			game.kills.put(killer, game.kills.get(killer)+1);
			Bukkit.broadcastMessage("§6[§eRush§6]§r " + team.getTag() + " " + p.getName() + " §ra été tué par " + killerTeam.getTag() + " " + killer.getName() + "§r.");
			killer.sendMessage("[§bi§r] §eKill(s): §c" + game.kills.get(killer));
			
			Core.getCore().getPlayerData(killer).addRushKills(1);
			
			if(!team.hasBed())
			{
				Bukkit.broadcastMessage("§6[§eRush]§r " + team.getTag() + " " + p.getName() + " §ra été éléminé.");
				p.setDisplayName(p.getName());
				game.addSpec(p);
				team.removePlayer(p);
				
				for(Player pls : Bukkit.getOnlinePlayers())
				{
					ScoreboardUtils.setoGameScoreboard(pls);
				}
				
				if(team.getPlayers().size() == 0)
				{
					p.teleport(Core.getCore().getSpawn());
					game.addSpec(p);
					Bukkit.broadcastMessage("§e§m----------------§6[§eRush§6]§e§m----------------");
					Bukkit.broadcastMessage("L'équipe " + team.getTag() + " §ra été éradiquée !");
					Bukkit.broadcastMessage("§e§m--------------------------------------");
				}
				
				if(game.isEnd())
				{
					game.endGame();
				}
			}
		}
		else
		{
			if(game.hits.get(p) == null || System.currentTimeMillis() - game.hits.get(p).getTimestamp() > 10000L)
			{
				System.out.println(p.getLocation().toString());
				
				Bukkit.broadcastMessage("§6[§eRush§6]§r " + team.getTag() + " " + p.getName() + " §rest mort.");
				
				if(!team.hasBed())
				{
					Bukkit.broadcastMessage("§6[§eRush§6]§r " + team.getTag() + " " + p.getName() + " §ra été éléminé.");
					p.setDisplayName(p.getName());
					game.addSpec(p);
					team.removePlayer(p);
					
					for(Player pls : Bukkit.getOnlinePlayers())
					{
						ScoreboardUtils.setoGameScoreboard(pls);
					}
					
					if(team.getPlayers().size() == 0)
					{
						p.teleport(Core.getCore().getSpawn());
						game.addSpec(p);
						Bukkit.broadcastMessage("§e§m----------------§6[§eRush§6]§e§m----------------");
						Bukkit.broadcastMessage("L'équipe " + team.getTag() + " §ra été éradiquée !");
						Bukkit.broadcastMessage("§e§m--------------------------------------");
					}
					
					if(game.isEnd())
					{
						game.endGame();
					}
				}
			}
			else
			{
				Player killer = game.hits.get(p).getAttacker();
				Team killerTeam = game.getPlayerTeam(killer);
				game.kills.put(killer, game.kills.get(killer)+1);
				Bukkit.broadcastMessage("§6[§eRush]§r " + team.getTag() + " " + p.getName() + " §ra été poussé dans le vide par " + killerTeam.getTag() + " " + killer.getName() + "§r.");
				killer.sendMessage("[§bi§r] §eKill(s): §c" + game.kills.get(killer));
				
				if(!team.hasBed())
				{
					Bukkit.broadcastMessage("§6[§eRush]§r " + team.getTag() + " " + p.getName() + " §ra été éléminé.");
					p.setDisplayName(p.getName());
					game.addSpec(p);
					team.removePlayer(p);
					
					for(Player pls : Bukkit.getOnlinePlayers())
					{
						ScoreboardUtils.setoGameScoreboard(pls);
					}
					
					if(team.getPlayers().size() == 0)
					{
						p.teleport(Core.getCore().getSpawn());
						game.addSpec(p);
						Bukkit.broadcastMessage("§e§m----------------§6[§eRush§6]§e§m----------------");
						Bukkit.broadcastMessage("L'équipe " + team.getTag() + " §ra été éradiquée !");
						Bukkit.broadcastMessage("§e§m--------------------------------------");
					}
					
					if(game.isEnd())
					{
						game.endGame();
					}
				}
			}
		}
	}

}
