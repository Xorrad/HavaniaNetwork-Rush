package net.havania.rush.listener.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.havania.rush.Rush;
import net.havania.rush.game.GameManager;
import net.havania.rush.game.GameState;

public class PlayerJoin implements Listener {
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onJoin(PlayerJoinEvent e)
	{
		Player p = e.getPlayer();
		GameManager game = Rush.getGameManager();
		
		if(game.getGameState() == GameState.WAITING)
		{
			game.addPlayer(p);
		}
		else
		{
			game.addSpec(p);
		}
	}

}
