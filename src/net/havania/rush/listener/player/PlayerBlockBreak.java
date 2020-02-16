package net.havania.rush.listener.player;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;

import net.havania.rush.Rush;
import net.havania.rush.game.GameManager;
import net.havania.rush.game.GameState;
import net.havania.rush.game.Team;
import net.havania.rush.utils.SavedBlock;

public class PlayerBlockBreak implements Listener {
	
	@EventHandler
	public void onBreak(BlockBreakEvent e)
	{
		Player p = e.getPlayer();
		GameManager game = Rush.getGameManager();
		
		if(game.placingBed.containsKey(p))
		{
			if(e.getBlock().getType().equals(Material.BED_BLOCK))
			{
				Team team = game.placingBed.get(p);
				String world = e.getBlock().getWorld().getName();
				Integer x = e.getBlock().getX();
				Integer y = e.getBlock().getY();
				Integer z = e.getBlock().getZ();
				
				game.getMain().getConfig().set("teams." + team.id + ".bed", world + "," + x + "," + y + "," + z);
				game.getMain().saveConfig();
				team.setBed(e.getBlock().getLocation());
				
				game.placingBed.remove(p);
				p.sendMessage("§aLe lit de l'équipe " + team.getTag() + " §aa été changé avec succès !");
				e.setCancelled(true);
				return;
			}
		}
		else if(game.placingResourcesSpawn.containsKey(p))
		{
			Team team = game.placingResourcesSpawn.get(p);
			Integer number = game.placingResourcesSpawnNumber.get(p);
			String world = e.getBlock().getWorld().getName();
			Integer x = e.getBlock().getX();
			Integer y = e.getBlock().getY();
			Integer z = e.getBlock().getZ();
			
			game.getMain().getConfig().set("teams." + team.id + ".resources_spawn." + number, world + "," + x + "," + y + "," + z);
			game.getMain().saveConfig();
			team.setResourcesSpawn(e.getBlock().getLocation(), number);
			
			game.placingResourcesSpawn.remove(p);
			game.placingResourcesSpawnNumber.remove(p);
			p.sendMessage("§aLe point de spawn des ressources n°§7" + number + "§a de l'équipe " + team.getTag() + " §aa été changé avec succès !");
			e.setCancelled(true);
			return;
		}
		
		if(game.getGameState() == GameState.WAITING || game.getGameState() == GameState.ENDING)
		{
			if(!e.getPlayer().getGameMode().equals(GameMode.CREATIVE))
			{
				e.setCancelled(true);
				return;
			}
			
			if(game.secureZone)
			{
				p.sendMessage("§aLe bloc a été retiré avec succès !");
				game.removeProtectedZone(e.getBlock());
			}
		}
		else
		{
			if(e.getPlayer().getGameMode().equals(GameMode.CREATIVE))
			{
				e.setCancelled(false);
				return;
			}
			
			if(game.specs.contains(p))
			{
				e.setCancelled(true);
				return;
			}
				
			if(!e.getBlock().getType().equals(Material.ENDER_STONE) && !e.getBlock().getType().equals(Material.SANDSTONE) && !e.getBlock().getType().equals(Material.TNT))
			{
				e.setCancelled(true);
				return;
			}
			else if(e.getBlock().getType().equals(Material.BED_BLOCK))
			{
				p.sendMessage("§cVous devez utiliser de la TNT pour détruire les lits !");
				e.setCancelled(true);
				return;
			}
			
			game.removeSavedBlock(new SavedBlock(e.getBlock().getState()));
		}
	}
	
	@EventHandler
	public void onPhysic(BlockPhysicsEvent e)
	{
		GameManager game = Rush.getGameManager();
		
		if(game.getGameState() == GameState.WAITING || game.getGameState() == GameState.ENDING)
		{
			e.setCancelled(true);
			return;
		}
	}

}
