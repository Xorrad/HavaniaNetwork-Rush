package net.havania.rush.utils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.havania.core.utils.SpeedItem;
import net.havania.rush.game.GameManager;
import net.havania.rush.game.GameState;
import net.havania.rush.game.Team;

public class GameTask extends BukkitRunnable {
	
	public GameManager gameManager;
	public Integer timer;
	public Integer playerMin;
	
	public Integer bronzeCount;
	public Integer ironCount;
	public Integer goldCount;
	
	public GameTask(GameManager gameManager) {
		this.gameManager = gameManager;
		this.timer = -1;
		this.playerMin = gameManager.maxPlayer;
		
		this.bronzeCount = 1;
		this.ironCount = 15;
		this.goldCount = 60;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	@Override
	public void run() {
		if(gameManager.getGameState() == GameState.WAITING)
		{
			if(timer != -1)
			{
				if(Bukkit.getOnlinePlayers().length < playerMin)
				{
					timer = -1;
					Bukkit.broadcastMessage("§6[§eRush§6]§r §fIl n'y a pas assez de joueurs pour commencer la partie !");
					
					for(Player pls : gameManager.players)
					{
						ScoreboardUtils.setToLobbyScoreboard(pls);
					}
				}
				else
				{
					if(timer == 0) //GAME STARTS
					{
						timer = -1;
						Bukkit.broadcastMessage("§6[§eRush§6]§r §fLa partie commence !");
						for(Player pls : gameManager.players)
						{
							pls.playSound(pls.getLocation(), Sound.AMBIENCE_THUNDER, 10, 1);
						}
						gameManager.startGame();
					}
					else //WAITING FOR GAME STARTS
					{
						if(timer <= 5 || timer == 10 || timer == 15)
						{
							for(Player pls : gameManager.players)
							{
								pls.playSound(pls.getLocation(), Sound.LEVEL_UP, 10, 1);
							}
							Bukkit.broadcastMessage("§6[§eRush§6]§r §fDébut dans " + timer + " secondes.");
						}
						
						for(Player pls : gameManager.players)
						{
							ScoreboardUtils.setToLobbyScoreboard(pls);
						}
						
						timer--;
					}
				}
			}
			else
			{
				if(Bukkit.getOnlinePlayers().length >= playerMin)
				{
					if(Bukkit.getOnlinePlayers().length >= gameManager.getTeams().size()* gameManager.maxPlayer)
					{
						timer = 15;
					}
					else
					{
						timer = 90;
					}
				}
			}
		}
		else if(gameManager.getGameState() == GameState.STARTED)
		{
			for(Player pls : Bukkit.getOnlinePlayers())
			{
				if(gameManager.compassClosest.containsKey(pls))
				{
					if(gameManager.compassClosest.get(pls))
					{
						Player playerNear = getNearestPlayer(pls);
						if (playerNear != null) {
						    pls.setCompassTarget(playerNear.getLocation());
						}
					}
					else
					{
						Player playerFar = getFarestPlayer(pls);
						if (playerFar != null) {
						    pls.setCompassTarget(playerFar.getLocation());
						}
					}
				}
				else
				{
					Player playerNear = getNearestPlayer(pls);
					if (playerNear != null) {
					    pls.setCompassTarget(playerNear.getLocation());
					}
				}
			}
				
			Iterator it = gameManager.respawn.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry)it.next();
		        Team t = gameManager.getPlayerTeam((Player)pair.getKey());
		        
		        if((int) pair.getValue() == 0)
		        {
		        	
		        	for(Team ts : this.gameManager.teams)
		        	{
		        		if(ts.players.size() > 0 && !ts.players.contains(pair.getKey()))
		        		{
		        			for(Player pls : ts.getPlayers())
		        			{
		        				pls.showPlayer((Player) pair.getKey());
		        			}
		        		}
		        	}
		        	
		        	((Player)pair.getKey()).teleport(t.getSpawn());
		        	((Player)pair.getKey()).sendMessage("§b> RESPAWN !");
		        	it.remove();
		        }
		        else
		        {
		        	((Player)pair.getKey()).teleport(t.getSpawn());
		        	((Player)pair.getKey()).sendMessage("§b> Respawn dans " + pair.getValue() + " secondes.");
		        	gameManager.respawn.put(((Player) pair.getKey()), (int) pair.getValue()-1);
		        }
		    }
			
			for(Team team : gameManager.getTeams())
			{
				for(int i=1; i<=team.getMaxPlayer(); i++)
				{
					Location spawnLoc = team.getResourcesSpawn(i).clone().add(0, 1, 0);
					if(bronzeCount==0)
					{
						Item bronze = spawnLoc.getWorld().dropItem(spawnLoc, new SpeedItem(Material.CLAY_BRICK, "§6Bronze"));
						bronze.setVelocity(new Vector(0, 0.25, 0));
					}
					if(ironCount==0)
					{
						Item iron = spawnLoc.getWorld().dropItem(spawnLoc, new SpeedItem(Material.IRON_INGOT, "§fFer"));
						iron.setVelocity(new Vector(0, 0.25, 0));
					}
					if(goldCount==0)
					{
						Item gold = spawnLoc.getWorld().dropItem(spawnLoc, new SpeedItem(Material.GOLD_INGOT, "§eOr"));
						gold.setVelocity(new Vector(0, 0.25, 0));
					}
					if(gameManager.gameTime == 300)
					{
						Item diamond = spawnLoc.getWorld().dropItem(spawnLoc, new SpeedItem(Material.DIAMOND, "§bDiamant"));
						diamond.setVelocity(new Vector(0, 0.25, 0));
					}
				}
			}
			
			if(bronzeCount==0)
			{
				bronzeCount = 2;
			}
			if(ironCount==0)
			{
				ironCount = 15;
			}
			if(goldCount==0)
			{
				goldCount = 60;
			}
				
		    
		    /*System.out.println(bronzeCount);
		    System.out.println(ironCount);
		    System.out.println(goldCount);*/
		    
		   /* for(Chunk c : Bukkit.getWorlds().get(0).getLoadedChunks())
		    {
		    	for(BlockState b : c.getTileEntities())
		    	{
		    		if(b.getBlock().getType().equals(Material.ENDER_CHEST))
		    		{
		    			Location l = b.getBlock().getLocation().clone().add(0.5, 1, 0.5);
		    			
		    			if(bronzeCount == 0)
		    			{
		    				bronzeCount = 2;
			    			Item bronze = b.getBlock().getWorld().dropItem(l, new SpeedItem(Material.CLAY_BRICK, "§6Bronze"));
			    			bronze.setVelocity(new Vector(0, 0.25, 0));
		    			}
						if(ironCount==0)
						{
							ironCount = 15;
							Item iron = b.getBlock().getWorld().dropItem(l, new SpeedItem(Material.IRON_INGOT));
							iron.setVelocity(new Vector(0, 0.25, 0));
						}
						if(goldCount==0)
						{
							goldCount = 60;
							Item gold = b.getBlock().getWorld().dropItem(l, new SpeedItem(Material.GOLD_INGOT));
							gold.setVelocity(new Vector(0, 0.25, 0));
						}
						if(gameManager.gameTime == 600)
						{
							Item diamond = b.getBlock().getWorld().dropItem(l, new SpeedItem(Material.DIAMOND, "§9Diamant"));
							diamond.setVelocity(new Vector(0, 0.25, 0));
						}
		    		}
		    	}
		    }*/
			
		    bronzeCount--;
			ironCount--;
			goldCount--;
			
			gameManager.gameTime++;
		}
		else if(gameManager.getGameState() == GameState.ENDING)
		{
			if(timer != -1)
			{
				if(timer == 0) //RELOADING THE MAP
				{
					for(Player pls : Bukkit.getOnlinePlayers())
					{
						pls.kickPlayer("§cLe serveur redémarre !");
					}
					
					gameManager.setGameState(GameState.RELOADING);
					
					for(Team team : gameManager.getTeams())
					{
						for(int i=1; i<=team.getMaxPlayer(); i++)
						{
							Chest c = (Chest) team.getResourcesSpawn(i).getBlock();
							c.getInventory().clear();
							c.getBlockInventory().clear();
						}
					}
					
					reloadWorld();
					
					this.timer = -1;
					this.ironCount = 15; 
					this.goldCount = 60; 
					
					gameManager.setGameState(GameState.WAITING);
					//rollback(Bukkit.getWorlds().get(0).getName());
					//Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
				}
				
				timer--;
			}
			else
			{
				timer = 15;
			}
		}
	}
	
	public boolean isRunning()
	{
		if(Bukkit.getScheduler().isCurrentlyRunning(this.getTaskId()))
		{
			return true;
		}
		
		return false;
	}

	public void setTimer(Integer timer)
	{
		this.timer = timer;
	}
	
	public Integer getTimer()
	{
		return this.timer;
	}
	
    public static void unloadMap(String mapname){
    	
    	for(Chunk c : Bukkit.getServer().getWorld(mapname).getLoadedChunks())
    	{
    		c.unload(false);
    	}
    	
        if(Bukkit.getServer().unloadWorld(Bukkit.getServer().getWorld(mapname), false)){
        	System.out.println("Successfully unloaded " + mapname);
        }else{
            System.out.println("COULD NOT UNLOAD " + mapname);
        }
    }
    
    public static void loadMap(String mapname){
        Bukkit.getServer().createWorld(new WorldCreator(mapname));
    }
 
    public static void rollback(String mapname){
        unloadMap(mapname);
        loadMap(mapname);
    }
    
    @SuppressWarnings("deprecation")
	public void reloadWorld()
    {
    	for(SavedBlock b : gameManager.getSavedBlocks())
    	{
    		Block block = b.getLocation().getWorld().getBlockAt(b.getLocation());
            block.setType(b.getMaterial());
            block.setData(b.getData());
    	}
    	
    	List<Entity> list = Bukkit.getWorlds().get(0).getEntities();
    	Iterator<Entity> entities = list.iterator();
    	while (entities.hasNext()) {
    	    Entity entity = entities.next();
    	    if (entity instanceof Item) {
    	        entity.remove();
    	    }
    	}
    	
    	gameManager.savedBlocks.clear();
    }
    
    @SuppressWarnings("deprecation")
	public Player getNearestPlayer(Player player) {
	    double distNear = 0.0D;
	    Player playerNear = null;
	    for (Player player2 : Bukkit.getOnlinePlayers()) {
	        if (player == player2) { continue; }
	        if (player.getWorld() != player2.getWorld()) { continue; }
	     
	        Location location2 = player2.getLocation();
	        double dist = player.getLocation().distanceSquared(location2);
	        if (playerNear == null || dist < distNear) {
	            playerNear = player2;
	            distNear = dist;
	        }
	    }
	    return playerNear;
	}
    
    @SuppressWarnings("deprecation")
	public Player getFarestPlayer(Player player) {
	    double distNear = 0.0D;
	    Player playerNear = null;
	    for (Player player2 : Bukkit.getOnlinePlayers()) {
	        if (player == player2) { continue; }
	        if (player.getWorld() != player2.getWorld()) { continue; }
	     
	        Location location2 = player2.getLocation();
	        double dist = player.getLocation().distanceSquared(location2);
	        if (playerNear == null || dist > distNear) {
	            playerNear = player2;
	            distNear = dist;
	        }
	    }
	    return playerNear;
	}
}
