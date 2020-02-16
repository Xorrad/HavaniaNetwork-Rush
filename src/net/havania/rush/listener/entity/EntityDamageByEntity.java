package net.havania.rush.listener.entity;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.havania.rush.Rush;
import net.havania.rush.game.Team;
import net.havania.rush.utils.CustomHit;

public class EntityDamageByEntity implements Listener {
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e)
	{
		if(e.getDamager() instanceof Player)
		{
			Player damager = (Player) e.getDamager();
			
			if(Rush.getGameManager().specs.contains(damager))
			{
				e.setCancelled(true);
				return;
			}
			
			if(Rush.getGameManager().respawn.containsKey(damager))
			{
				e.setCancelled(true);
				return;
			}
			
			if(e.getEntity() instanceof Villager)
			{
				if(!damager.getGameMode().equals(GameMode.CREATIVE))
				{
					e.setCancelled(true);
					return;
				}
			}
			
			if(e.getEntity() instanceof Player)
			{
				Team damagerTeam = Rush.getGameManager().getPlayerTeam(damager);
				Team targetTeam = Rush.getGameManager().getPlayerTeam((Player) e.getEntity());
				
				if(damagerTeam != null && targetTeam != null)
				{
					if(damagerTeam.getName().equalsIgnoreCase(targetTeam.getName()))
					{
						e.setCancelled(true);
						return;
					}
					else
					{
						Rush.getGameManager().hits.put((Player) e.getEntity(), new CustomHit((Player) e.getEntity(), damager));
					}
				}
			}
		}
		else
		{
			if(e.getEntity() instanceof Villager)
			{
				e.setCancelled(true);
				return;
			}
			
			if(e.getDamager() instanceof TNTPrimed)
			{
				Player damager = (Player) ((TNTPrimed) e.getDamager()).getSource();
				
				if(e.getEntity() instanceof Player)
				{
					Player entity = (Player) e.getEntity();
					Team damagerTeam = Rush.getGameManager().getPlayerTeam(damager);
					Team targetTeam = Rush.getGameManager().getPlayerTeam((Player) e.getEntity());
					
					if(damagerTeam != null && targetTeam != null)
					{
						if(damagerTeam.getName().equalsIgnoreCase(targetTeam.getName()))
						{
							e.setCancelled(true);
							return;
						}
						else
						{
							Rush.getGameManager().hits.put((Player) e.getEntity(), new CustomHit((Player) e.getEntity(), damager));
							
							if(entity.getInventory().getChestplate() != null)
							{
								e.setDamage(1.5);
							}
						}
					}
				}
			}
		}
	}

}
