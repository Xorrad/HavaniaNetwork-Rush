package net.havania.rush.listener.player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockCanBuildEvent;

import net.havania.rush.Rush;
import net.havania.rush.game.GameManager;
import net.havania.rush.game.GameState;

public class PlayerCanPlace implements Listener {
	
	@EventHandler
	public void onPlace(BlockCanBuildEvent e)
	{
		GameManager game = Rush.getGameManager();
		
		if(game.getGameState() == GameState.STARTED)
		{
			e.setBuildable(true);
		}
	}
}
