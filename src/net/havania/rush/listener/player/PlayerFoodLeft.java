package net.havania.rush.listener.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import net.havania.rush.Rush;
import net.havania.rush.game.GameManager;
import net.havania.rush.game.GameState;

public class PlayerFoodLeft implements Listener {
	
	@EventHandler
	public void onChange(FoodLevelChangeEvent e)
	{
		if(e.getEntity() instanceof Player)
		{
			Player p = (Player) e.getEntity();
			GameManager game = Rush.getGameManager();
			
			if(game.getGameState() == GameState.WAITING || game.getGameState() == GameState.ENDING)
			{
				e.setCancelled(true);
				return;
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
	}
}
