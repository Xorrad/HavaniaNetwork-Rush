package net.havania.rush.listener.player;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import net.havania.rush.Rush;
import net.havania.rush.game.GameManager;

public class PlayerMove implements Listener {
	
	@EventHandler
    public void onMove(PlayerMoveEvent e) {
		GameManager game = Rush.getGameManager();
		if(game.respawn.containsKey(e.getPlayer()))
		{
			e.getPlayer().setVelocity(new Vector().zero());
			Location l = e.getFrom();
			l.setYaw(e.getTo().getYaw());
			l.setPitch(e.getTo().getPitch());
			e.setTo(l);
		}
		else if(game.specs.contains(e.getPlayer()))
		{
			Player closestPlayer = null;
			for(Player pls : game.getPlayers())
			{
				if(closestPlayer == null)
				{
					closestPlayer = pls;
				}
				else
				{
					if(closestPlayer.getLocation().distance(e.getTo()) < pls.getLocation().distance(e.getTo()))
					{
						closestPlayer = pls;
					}
				}
			}
			
			if(closestPlayer != null)
			{
				if(closestPlayer.getLocation().distance(e.getTo()) >= 50)
				{
					e.getPlayer().teleport(closestPlayer);
					e.getPlayer().sendMessage("§cN'allez pas trop loin !");
				}
			}
		}
	}
}
