package net.havania.rush.listener.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import net.havania.rush.Rush;
import net.havania.rush.game.GameManager;
import net.havania.rush.game.GameState;
import net.havania.rush.utils.ScoreboardUtils;

public class PlayerQuit implements Listener {
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onQuit(PlayerQuitEvent e)
	{
		Player p = e.getPlayer();
		GameManager game = Rush.getGameManager();
		
		e.setQuitMessage(null);
		game.removePlayer(p);
		
		if(game.getGameState() == GameState.STARTED)
		{
			for(Player pls : Bukkit.getOnlinePlayers())
			{
				ScoreboardUtils.setoGameScoreboard(pls);
			}
			
			if(game.isEnd())
			{
				game.endGame();
			}
		}
		else if(game.getGameState() == GameState.WAITING)
		{
			for(Player pls : Bukkit.getOnlinePlayers())
			{
				ScoreboardUtils.setToLobbyScoreboard(pls);
			}
		}
		
	}

}
