package net.havania.rush.listener.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import net.havania.core.Core;
import net.havania.core.utils.PlayerData;
import net.havania.core.utils.Rank;
import net.havania.rush.Rush;
import net.havania.rush.game.GameManager;
import net.havania.rush.game.GameState;
import net.havania.rush.game.Team;

public class PlayerChat implements Listener {
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e)
	{
		Player p = e.getPlayer();
		GameManager game = Rush.getGameManager();
		PlayerData pd = Core.getCore().getPlayerData(p);
		Team team = game.getPlayerTeam(p);
		String message = e.getMessage();
		char[] arr = message.toCharArray();
		
		if(game.getGameState() == GameState.WAITING)
		{
			String rank = pd.getRank().equals(Rank.PLAYER) ? "" : "[" + pd.getRank().getTag() + "§r]";
			e.setFormat("§7[§6"+pd.getRushPoint()+"§7]§r" + rank + (team == null ? "" : " " + team.getTag()) + " " + p.getName() + "§r: " + message);
		}
		else
		{
			if(game.specs.contains(p))
			{
				/*for(Player pls : game.specs)
				{
					String rank = pd.getRank().equals(Rank.PLAYER) ? "" : "[" + pd.getRank().getTag() + "§r]";
					pls.sendMessage("(§7Spec§r)" + rank + " " + p.getName() + "§r: " + message);
				}
				e.setCancelled(true);
				return;*/
				String rank = pd.getRank().equals(Rank.PLAYER) ? "" : "[" + pd.getRank().getTag() + "§r]";
				e.setMessage("(§7Spec§r)" + rank + " " + p.getName() + "§r: " + message);
				return;
			}
			
			if(arr[0] == '@')
			{
				StringBuilder builder = new StringBuilder(message); 
				builder.deleteCharAt(0);
				String rank = pd.getRank().equals(Rank.PLAYER) ? "" : "[" + pd.getRank().getTag() + "§r]";
				e.setFormat("§7[§6" + pd.getRushPoint() + "§7]§r" + rank + " " + team.getTag() + " " + p.getName() + "§r: " + "§b@§r" + builder);
			}
			else
			{
				e.setCancelled(true);
				p.sendMessage("[§bi§r] Pour envoyer un message global, débute par un \"@\".");
				
				for(Player pls : team.getPlayers())
				{
					String rank = pd.getRank().equals(Rank.PLAYER) ? "" : "[" + pd.getRank().getTag() + "§r]";
					pls.sendMessage("[Privé " + team.getTag() + "§r]§7[§6" + pd.getRushPoint() + "§7]§r" + rank + " " + p.getName() + "§r: " + message);
				}
			}
		}
	}

}
