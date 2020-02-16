package net.havania.rush.listener.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.util.Vector;

import net.havania.core.Core;
import net.havania.core.utils.SpeedItem;
import net.havania.rush.Rush;
import net.havania.rush.game.GameManager;
import net.havania.rush.game.Team;
import net.havania.rush.utils.SavedBlock;
import net.havania.rush.utils.ScoreboardUtils;

public class TNTExplode implements Listener {
	
	public static HashMap<Player, Long> TNT;
	public static HashMap<Player, ArrayList<Block>> TNTIgnited;
	
	static 
	{
		TNT = new HashMap<>();
		TNTIgnited = new HashMap<>();
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onExplode(EntityExplodeEvent e)
	{
		GameManager game = Rush.getGameManager();
		if(e.getEntity() instanceof TNTPrimed)
		{
			TNTPrimed tnt = (TNTPrimed) e.getEntity();
			Player p = null;
			
			if(tnt.getSource() != null)
			{
				p = (Player) tnt.getSource();
			}
			else
			{
				for(Player pls : TNTIgnited.keySet())
				{
					Iterator<Block> it = TNTIgnited.get(pls).iterator();
					while(it.hasNext())
					{
						Block b = it.next();
						
						//tnt.get
						if(tnt.getLocation().distance(b.getLocation()) <= 10)
						{
							p = pls;
							it.remove();
							break;
						}
					}
				}
			}
			
			if(p != null)
			{
			
				Team team = game.getPlayerTeam(p);
				
				boolean teamPassed = false;
				
				Iterator<Block> it = e.blockList().iterator();
				while(it.hasNext())
				{
					Block b = it.next();
					if(b.getType().equals(Material.BED_BLOCK))
					{
						for(Team t : game.getTeams())
						{
							if(!teamPassed)
							{
								if(!t.getPlayers().contains(p))
								{
									if(t.getBed().distance(b.getLocation()) <= 1)
									{
										if(!t.getName().equalsIgnoreCase(team.getName()))
										{
											if(t.hasBed())
											{
												World world = b.getWorld();
												Block block2;
										        block2 = world.getBlockAt(b.getX()-1, b.getY(), b.getZ());
										        if (!block2.getType().equals(Material.BED_BLOCK))
										        {
										        	block2 = world.getBlockAt(b.getX()+1, b.getY(), b.getZ());
										        	if (!block2.getType().equals(Material.BED_BLOCK))
											        {
										        		block2 = world.getBlockAt(b.getX(), b.getY(), b.getZ()-1);
										        		if (!block2.getType().equals(Material.BED_BLOCK))
												        {
										        			block2 = world.getBlockAt(b.getX(), b.getY(), b.getZ()+1);
												        }
											        }
										        }
										        
										        game.getSavedBlocks().add(new SavedBlock(b.getState()));
										        game.getSavedBlocks().add(new SavedBlock(block2.getState()));
										        
										        b.setType(Material.AIR);
										        block2.setType(Material.AIR);
												
												game.bed.put(p, game.bed.get(p)+1);
												
												Bukkit.broadcastMessage("§e[§6Rush§e]§r BOOM ! ");
												p.sendMessage("[§bi§r] §eLit(s) Détruit(s): §c" + game.bed.get(p));
												Bukkit.broadcastMessage("§6[§eRush§6]§r Le lit de l'équipe " + t.getTag() + "§r à été détruit par l'équipe " + team.getTag() + "§r !");
												Bukkit.broadcastMessage("§e[§6Rush§e]§r Des objets supplémentaires sont apparus au spawn de l'équipe " + team.getTag() + " !");
												
												Core.getCore().getPlayerData(p).addRushBeds(1);
												
												
												for(int i=1; i<=team.getMaxPlayer(); i++)
												{
													Location l = team.getResourcesSpawn(i).clone().add(0, 1, 0);
									    			Item diamond = l.getWorld().dropItem(l, new SpeedItem(Material.DIAMOND, "§9Diamant"));
									    			diamond.setVelocity(new Vector(0, 0.25, 0));
									    			
									    			Item gold = l.getWorld().dropItem(l, new SpeedItem(Material.GOLD_INGOT));
									    			gold.setVelocity(new Vector(0, 0.25, 0));
									    			
									    			Item bronze = l.getWorld().dropItem(l, new SpeedItem(Material.DIAMOND, "§9Diamant"));
									    			bronze.setVelocity(new Vector(0, 0.25, 0));
									    			
									    			Item tnti = l.getWorld().dropItem(l, new SpeedItem(Material.TNT, 2));
									    			tnti.setVelocity(new Vector(0, 0.25, 0));
												}
												
												/*for(Chunk c : Bukkit.getWorlds().get(0).getLoadedChunks())
											    {
											    	for(BlockState bs : c.getTileEntities())
											    	{
											    		if(bs.getType().equals(Material.ENDER_CHEST))
											    		{
												    		if(b.getLocation().distance(t.getSpawn()) <= 10)
												    		{
												    			Location l = bs.getBlock().getLocation().clone().add(0.5, 1, 0.5);
												    			Item diamond = bs.getBlock().getWorld().dropItem(l, new SpeedItem(Material.DIAMOND, "§9Diamant"));
												    			diamond.setVelocity(new Vector(0, 0.25, 0));
												    			
												    			Item gold = bs.getBlock().getWorld().dropItem(l, new SpeedItem(Material.GOLD_INGOT));
												    			gold.setVelocity(new Vector(0, 0.25, 0));
												    			
												    			Item bronze = bs.getBlock().getWorld().dropItem(l, new SpeedItem(Material.DIAMOND, "§9Diamant"));
												    			bronze.setVelocity(new Vector(0, 0.25, 0));
												    			
												    			Item tnti = bs.getBlock().getWorld().dropItem(l, new SpeedItem(Material.TNT, 2));
												    			tnti.setVelocity(new Vector(0, 0.25, 0));
												    		}
												    	}
											    	}
											    }*/
												
												t.setHasBed(false);
												teamPassed = true;
												
												for(Player pls : Bukkit.getOnlinePlayers())
												{
													ScoreboardUtils.setoGameScoreboard(pls);
												}
											}
										}
										else
										{
											it.remove();
											p.sendMessage("§cVous ne pouvez pas détruire votre propre lit !");
										}
									}
								}
								else
								{
									it.remove();
								}
							}
						}
					}
					else if(!b.getType().equals(Material.ENDER_STONE) && !b.getType().equals(Material.SANDSTONE) && !b.getType().equals(Material.TNT))
					{
						it.remove();
					}
					else
					{
						game.removeSavedBlock(new SavedBlock(b.getState()));
					}
				}
			}
			else
			{
				
				boolean teamPassed = false;
				Iterator<Block> it = e.blockList().iterator();
				while(it.hasNext())
				{
					Block b = it.next();
					if(b.getType().equals(Material.BED_BLOCK))
					{
						for(Team t : game.getTeams())
						{
							if(!teamPassed)
							{
								if(t.getBed().distance(b.getLocation()) <= 1)
								{
									if(t.hasBed())
									{
										World world = b.getWorld();
										Block block2;
								        block2 = world.getBlockAt(b.getX()-1, b.getY(), b.getZ());
								        if (!block2.getType().equals(Material.BED_BLOCK))
								        {
								        	block2 = world.getBlockAt(b.getX()+1, b.getY(), b.getZ());
								        	if (!block2.getType().equals(Material.BED_BLOCK))
									        {
								        		block2 = world.getBlockAt(b.getX(), b.getY(), b.getZ()-1);
								        		if (!block2.getType().equals(Material.BED_BLOCK))
										        {
								        			block2 = world.getBlockAt(b.getX(), b.getY(), b.getZ()+1);
										        }
									        }
								        }
								        
								        game.getSavedBlocks().add(new SavedBlock(b.getState()));
								        game.getSavedBlocks().add(new SavedBlock(block2.getState()));
								        
								        b.setType(Material.AIR);
								        block2.setType(Material.AIR);
										
										Bukkit.broadcastMessage("§e[§6Rush§e]§r BOOM ! ");
										Bukkit.broadcastMessage("§6[§eRush§6]§r Le lit de l'équipe " + t.getTag() + "§r à été détruit !");
										
										t.setHasBed(false);
										teamPassed = true;
										
										for(Player pls : Bukkit.getOnlinePlayers())
										{
											ScoreboardUtils.setoGameScoreboard(pls);
										}
									}
								}
							}
						}
					}
					else if(!b.getType().equals(Material.ENDER_STONE) && !b.getType().equals(Material.SANDSTONE) && !b.getType().equals(Material.TNT))
					{
						it.remove();
					}
					else
					{
						game.removeSavedBlock(new SavedBlock(b.getState()));
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onRedstone(BlockRedstoneEvent e)
	{
		if(e.getBlock().getType().equals(Material.TNT))
		{
			for(Player pls : TNT.keySet())
			{
				Long diff = TNT.get(pls) - System.currentTimeMillis();
				
				System.out.println(diff);
				if(diff <= 10L)
				{
					if(TNTIgnited.containsKey(pls))
					{
						TNTIgnited.get(pls).add(e.getBlock());
					}
					else
					{
						ArrayList<Block> list = new ArrayList<>();
						list.add(e.getBlock());
						TNTIgnited.put(pls, list);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void PlayerOpenChest(InventoryOpenEvent e)
	{
		Player p = (Player) e.getPlayer();
		
		if(e.getInventory().getHolder() instanceof Chest)
		{
			TNT.put(p, System.currentTimeMillis());
		}
	}
}
