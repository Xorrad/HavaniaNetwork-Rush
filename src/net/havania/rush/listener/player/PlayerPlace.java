package net.havania.rush.listener.player;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import net.havania.core.Core;
import net.havania.rush.Rush;
import net.havania.rush.game.GameManager;
import net.havania.rush.game.GameState;
import net.havania.rush.utils.SavedBlock;

public class PlayerPlace implements Listener {
	
	@EventHandler
	public void onPlace(BlockPlaceEvent e)
	{
		GameManager game = Rush.getGameManager();
		Player p = e.getPlayer();
		
		if(game.getGameState() == GameState.WAITING || game.getGameState() == GameState.ENDING)
		{
			if(!e.getPlayer().getGameMode().equals(GameMode.CREATIVE))
			{
				e.setCancelled(true);
				e.getPlayer().updateInventory();
				return;
			}
			
			if(game.secureZone)
			{
				p.sendMessage("§aLe bloc a été ajouté avec succès !");
				game.addProtectedZone(e.getBlockPlaced());
			}
		}
		else
		{
			if(p.getGameMode().equals(GameMode.CREATIVE))
			{
				e.setCancelled(false);
				return;
			}
			
			if(game.specs.contains(p))
			{
				e.setCancelled(true);
				return;
			}
			
			if(game.isProtectedZone(e.getBlockPlaced()))
			{
				p.sendMessage("§cVous ne pouvez pas construire ici !");
				e.setCancelled(true);
				return;
			}
			
			Location loc = e.getBlockPlaced().getLocation().clone();
			Location spawn = Core.getCore().getSpawn().clone();
			loc.setY(0);
			spawn.setY(0);
			
			if(loc.distance(spawn) > game.maxDistanceToSpawn)
			{
				p.sendMessage("§cVous ne pouvez pas construire plus loin !");
				e.setCancelled(true);
				return;
			}
			
			if(e.getBlockPlaced().getLocation().getY() >= 101)
			{
				p.sendMessage("§cVous ne pouvez pas construire plus haut !");
				e.setCancelled(true);
				return;
			}
			
			if(e.getBlockPlaced().getLocation().getY() <= 63)
			{
				p.sendMessage("§cVous ne pouvez pas construire plus bas !");
				e.setCancelled(true);
				return;
			}
			
			if(!game.isSavedBed(e.getBlock()))
			{
				game.getSavedBlocks().add(new SavedBlock(e.getBlockReplacedState()));
			}
		}
	}
}
