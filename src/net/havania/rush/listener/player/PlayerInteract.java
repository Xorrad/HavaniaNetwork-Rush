package net.havania.rush.listener.player;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import net.havania.rush.Rush;
import net.havania.rush.game.GameManager;
import net.havania.rush.game.Team;
import net.havania.rush.utils.GuiUtils;

public class PlayerInteract implements Listener {
	
	@EventHandler
	public void onClick(PlayerInteractEvent e)
	{
		GameManager game = Rush.getGameManager();
		Player p = e.getPlayer();
		
		if(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
		{
			if(e.getClickedBlock() != null)
			{
				if(e.getItem() != null && e.getItem().getType() != null && e.getItem().getType().equals(Material.FLINT_AND_STEEL) && e.getBlockFace().equals(BlockFace.UP) && !e.getClickedBlock().getType().equals(Material.TNT))
				{
					e.setCancelled(true);
					return;
				}
				
				if(e.getClickedBlock().getType().equals(Material.BED_BLOCK))
				{
					e.setCancelled(true);
					return;
				}
				
				if(e.getClickedBlock().getType().equals(Material.COMPASS))
				{
					if(game.compassClosest.containsKey(p))
					{
						game.compassClosest.put(p, !game.compassClosest.get(p));
						if(game.compassClosest.get(p))
						{
							p.sendMessage("§aVous traquez désormait le joueur le plus proche de vous !");
						}
						else
						{
							p.sendMessage("§aVous traquez désormait le joueur le plus loin de vous !");
						}
					}
					else
					{
						game.compassClosest.put(p, false);
						p.sendMessage("§aVous traquez désormait le joueur le plus loin de vous !");
					}
				}
			}
			
			if(e.getItem() == null || e.getItem().getItemMeta() == null || e.getItem().getItemMeta().getDisplayName() == null)
			{
				return;
			}
			
			if(e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("Liste des joueurs")) //LIST OF PLAYERS
			{
				GuiUtils.openPlayerListGui(p);
			}
			else if(e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("Aléatoire")) //JOIN TEAM
			{
				Random ran = new Random();
				Integer i;
				
				do 
				{
					i = ran.nextInt(game.getTeams().size());
				}
				while(game.getTeams().get(i).isFull() || game.getTeams().get(i).getPlayers().contains(p));
				
				game.getTeams().get(i).addPlayer(p);
			}
			else //JOIN TEAM
			{
				for(Team team : game.getTeams())
				{
					if(team.getTag().equalsIgnoreCase(e.getItem().getItemMeta().getDisplayName()))
					{
						if(!team.getPlayers().contains(p))
						{
							if(!team.isFull())
							{
								team.addPlayer(p);
							}
							else
							{
								p.sendMessage("§cCette équipe est pleine !");
							}
						}
					}
				}
			}
		}
		else
		{
			if(game.specs.contains(p))
			{
				e.setCancelled(true);
				return;
			}
		}
	}
	
	@EventHandler
	public void onClickEntity(PlayerInteractEntityEvent e)
	{
		GameManager game = Rush.getGameManager();
		Player p = e.getPlayer();
		
		if(game.specs.contains(p))
		{
			e.setCancelled(true);
			return;
		}
	}

}
