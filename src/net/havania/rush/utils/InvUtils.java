package net.havania.rush.utils;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import net.havania.core.utils.SpeedItem;
import net.havania.rush.Rush;
import net.havania.rush.game.Team;

public class InvUtils {
	
	public static void giveLobbyItem(Player p)
	{
		p.getInventory().clear();
		p.getInventory().setHelmet(new ItemStack(Material.AIR));
		p.getInventory().setChestplate(new ItemStack(Material.AIR));
		p.getInventory().setLeggings(new ItemStack(Material.AIR));
		p.getInventory().setBoots(new ItemStack(Material.AIR));
		for(Team team : Rush.getGameManager().getTeams())
		{
			SpeedItem item = new SpeedItem(Material.WOOL, team.getTag());
			item.setDurability(team.getWoolData());
			p.getInventory().addItem(item);
		}
		p.getInventory().addItem(new SpeedItem(Material.BEDROCK, "Aléatoire"));
		p.updateInventory();
	}
	
	public static void giveGameItem(Player p)
	{
		p.getInventory().clear();
		Team t = Rush.getGameManager().getPlayerTeam(p);
		
		ItemStack helmet = setColor(new ItemStack(Material.LEATHER_HELMET), t.getColor());
		helmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta hi = helmet.getItemMeta();
		hi.spigot().setUnbreakable(true);
		helmet.setItemMeta(hi);
		ItemStack leggings = setColor(new ItemStack(Material.LEATHER_LEGGINGS), t.getColor());
		leggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta li = leggings.getItemMeta();
		li.spigot().setUnbreakable(true);
		leggings.setItemMeta(li);
		ItemStack boots = setColor(new ItemStack(Material.LEATHER_BOOTS), t.getColor());
		boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta bi = boots.getItemMeta();
		bi.spigot().setUnbreakable(true);
		boots.setItemMeta(bi);
		
		p.getInventory().setHelmet(helmet);
		p.getInventory().setChestplate(new ItemStack(Material.AIR));
		p.getInventory().setLeggings(leggings);
		p.getInventory().setBoots(boots);
		
		p.updateInventory();
	}
	
	public static void giveSpecItem(Player p)
	{
		p.getInventory().clear();
		p.getInventory().addItem(new SpeedItem(Material.COMPASS, "Liste des joueurs"));
		p.updateInventory();
	}
	
    public static ItemStack setColor(ItemStack item, String color){
        LeatherArmorMeta lam = (LeatherArmorMeta)item.getItemMeta();
        
        if(color.equalsIgnoreCase("§b"))
        {
        	lam.setColor(Color.BLUE);
        }
        else if(color.equalsIgnoreCase("§0"))
        {
        	lam.setColor(Color.BLACK);
        }
        else if(color.equalsIgnoreCase("§9"))
        {
        	lam.setColor(Color.BLUE);
        }
        else if(color.equalsIgnoreCase("§7"))
        {
        	lam.setColor(Color.GRAY);
        }
        else if(color.equalsIgnoreCase("§a"))
        {
        	lam.setColor(Color.GREEN);
        }
        else if(color.equalsIgnoreCase("§c"))
        {
        	lam.setColor(Color.RED);
        }
        else if(color.equalsIgnoreCase("§d"))
        {
        	lam.setColor(Color.FUCHSIA);
        }
        else if(color.equalsIgnoreCase("§5"))
        {
        	lam.setColor(Color.PURPLE);
        }
        else if(color.equalsIgnoreCase("§e"))
        {
        	lam.setColor(Color.YELLOW);
        }
        else if(color.equalsIgnoreCase("§6"))
        {
        	lam.setColor(Color.ORANGE);
        }
        else if(color.equalsIgnoreCase("§f"))
        {
        	lam.setColor(Color.WHITE);
        }
        
        item.setItemMeta(lam);
        return item;
    }

}
