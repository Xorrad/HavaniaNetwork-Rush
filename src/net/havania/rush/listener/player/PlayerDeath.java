package net.havania.rush.listener.player;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.CraftServer;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import net.havania.core.Core;
import net.havania.rush.Rush;
import net.havania.rush.game.GameManager;
import net.havania.rush.game.Team;
import net.havania.rush.utils.InvUtils;

public class PlayerDeath implements Listener {
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e)
	{
		GameManager game = Rush.getGameManager();
		Player p = e.getEntity();
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				
				((CraftServer)Bukkit.getServer()).getHandle().moveToWorld(((CraftPlayer)p).getHandle(), 0, false);
				
				if(game.players.contains(p))
				{
					Team team = game.getPlayerTeam(p);
					for(Team ts : game.teams)
		        	{
		        		if(ts.players.size() > 0 && !ts.players.contains(p))
		        		{
		        			for(Player pls : ts.getPlayers())
		        			{
		        				pls.hidePlayer(p);
		        			}
		        		}
		        	}
					
					game.respawn.put(p, 3);
					p.teleport(team.getSpawn());
					InvUtils.giveGameItem(p);
				}
				else
				{
					p.teleport(Core.getCore().getSpawn());
				}
				this.cancel();
			}
		}.runTaskLater(game.getMain(), 1L);
	}

}
