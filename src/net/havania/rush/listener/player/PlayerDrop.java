package net.havania.rush.listener.player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import net.havania.rush.Rush;
import net.havania.rush.game.GameState;

public class PlayerDrop implements Listener {
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e)
	{
		if(Rush.getGameManager().getGameState() == GameState.WAITING || Rush.getGameManager().getGameState() == GameState.ENDING)
		{
			e.setCancelled(true);
		}
		else
		{
			if(Rush.getGameManager().specs.contains(e.getPlayer()))
			{
				e.setCancelled(true);
				return;
			}
			
			if(Rush.getGameManager().respawn.containsKey(e.getPlayer()))
			{
				e.setCancelled(true);
				return;
			}
		}
	}

}
