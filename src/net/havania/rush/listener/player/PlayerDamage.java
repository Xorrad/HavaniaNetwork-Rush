package net.havania.rush.listener.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import net.havania.rush.Rush;
import net.havania.rush.game.GameState;

public class PlayerDamage implements Listener {
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onDamage(EntityDamageEvent e)
	{
		if(e.getEntity() instanceof Player)
		{
			if(e.getCause().equals(DamageCause.FIRE) || e.getCause().equals(DamageCause.FIRE_TICK))
			{
				e.setCancelled(true);
				return;
			}
			
			if(Rush.getGameManager().getGameState() == GameState.WAITING || Rush.getGameManager().getGameState() == GameState.ENDING)
			{
				e.setCancelled(true);
			}
			else
			{
				if(Rush.getGameManager().specs.contains(e.getEntity()))
				{
					e.setCancelled(true);
					return;
				}
				
				if(Rush.getGameManager().respawn.containsKey(e.getEntity()))
				{
					e.setCancelled(true);
					return;
				}
				
				if(e.getCause().equals(DamageCause.VOID))
				{
					//((Player)e.getEntity()).setHealth(0.0D);
					e.setDamage(50);
				}
				
				/*if(((Damageable)((Player)e.getEntity())).getHealth() <= 0)
				{
					((Player)e.getEntity()).setVelocity(new Vector(0, 0, 0));
					((Player)e.getEntity()).spigot().respawn();
				}*/
			}
		}
	}

}
