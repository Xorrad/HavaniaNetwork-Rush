package net.havania.rush.listener.player;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class PlayerInventoryClick implements Listener {
	
	@EventHandler
	public void onClick(InventoryClickEvent e)
	{
		Player p = (Player) e.getWhoClicked();
		
		if(e.getCurrentItem() != null)
		{
			if(e.getCurrentItem().getType().equals(Material.LEATHER_BOOTS) || e.getCurrentItem().getType().equals(Material.LEATHER_HELMET) || e.getCurrentItem().getType().equals(Material.LEATHER_LEGGINGS) || e.getCurrentItem().getType().equals(Material.SKULL_ITEM))
			{
				e.setCancelled(true);
				return;
			}
			
			if(e.getInventory().getName() != null && e.getInventory().getName().equalsIgnoreCase("Ender Chest"))
			{
				if(!e.getCurrentItem().getType().equals(Material.DIAMOND))
				{
					e.setCancelled(true);
					return;
				}
			}
		}
		
		if(e.getCurrentItem() == null || e.getCurrentItem().getItemMeta() == null)
		{
			return;
		}
		
		if(e.getInventory().getName().equalsIgnoreCase("§6Liste des joueurs"))
		{
			Player t = Bukkit.getPlayer(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName().split(" ")[1]));
			
			p.closeInventory();
			p.teleport(t);
			p.sendMessage("Teleportation vers " + e.getCurrentItem().getItemMeta().getDisplayName());
		}
	}

}
