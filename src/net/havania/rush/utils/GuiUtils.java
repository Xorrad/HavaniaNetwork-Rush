package net.havania.rush.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import net.havania.core.utils.SpeedItem;
import net.havania.rush.Rush;
import net.havania.rush.game.Team;

public class GuiUtils {
	
	public static void openPlayerListGui(Player p)
	{
		Inventory inv = Bukkit.createInventory(null, 6*9, "§6Liste des joueurs");
		
		for(Team team : Rush.getGameManager().getTeams())
		{
			for(Player pls : team.getPlayers())
			{
				SpeedItem head = new SpeedItem(Material.SKULL_ITEM, team.color + pls.getName());
				head.setDurability((short)3);
                /*SkullMeta sm = (SkullMeta) head.getItemMeta();
                sm.setOwner(pls.getName());
                head.setItemMeta(sm);*/
                inv.addItem(head);
			}
		}
		
		p.openInventory(inv);
	}

}
