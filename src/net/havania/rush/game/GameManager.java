package net.havania.rush.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import net.havania.core.Core;
import net.havania.core.utils.Server.ServerStatus;
import net.havania.rush.Rush;
import net.havania.rush.utils.CustomHit;
import net.havania.rush.utils.GameTask;
import net.havania.rush.utils.InvUtils;
import net.havania.rush.utils.SavedBlock;
import net.havania.rush.utils.ScoreboardUtils;

public class GameManager {
	
	public Rush main;
	public GameState state;
	public int maxPlayer;
	public ArrayList<Team> teams;
	public GameTask gameTask;
	public Integer gameTime;
	public Integer maxDistanceToSpawn;
	public ArrayList<Player> players;
	public ArrayList<Player> specs;
	public ArrayList<Block> protectedZone;
	public HashMap<Player, Team> placingBed;
	public HashMap<Player, Team> placingResourcesSpawn;
	public HashMap<Player, Integer> placingResourcesSpawnNumber;
	
	public HashMap<Player, Integer> kills;
	public HashMap<Player, Integer> bed;
	public HashMap<Player, Integer> respawn;
	public HashMap<Player, CustomHit> hits;
	public HashMap<Player, Boolean> compassClosest;
	
	public ArrayList<SavedBlock> savedBlocks;
	
	public ScoreboardManager manager;
	public Scoreboard board;
	
	public boolean secureZone;
	
	public GameManager() {
		registerVariables();
		
		gameTask.runTaskTimer(main, 20, 20);
	}
	
	private void registerVariables()
	{
		main = Rush.getInstance();
		state = GameState.WAITING;
		maxPlayer = 0;
		teams = new ArrayList<>();
		gameTask = new GameTask(this);
		gameTime = 0;
		players = new ArrayList<>();
		specs = new ArrayList<>();
		protectedZone = new ArrayList<>();
		placingBed = new HashMap<>();
		placingResourcesSpawn = new HashMap<>();
		placingResourcesSpawnNumber = new HashMap<>();
		kills = new HashMap<>();
		bed = new HashMap<>();
		respawn = new HashMap<>();
		hits = new HashMap<>();
		compassClosest = new HashMap<>();
		savedBlocks = new ArrayList<>();
		manager = Bukkit.getScoreboardManager();
		board = manager.getNewScoreboard();
		secureZone = false;
		maxDistanceToSpawn = 0;
	}
	
	public ArrayList<SavedBlock> getSavedBlocks() {
		return savedBlocks;
	}

	public boolean isSavedBed(Block block)
	{
		Iterator<SavedBlock> it = savedBlocks.iterator();
		while(it.hasNext())
		{
			SavedBlock b = it.next();
			if(b.getMaterial().equals(Material.BED_BLOCK) && b.getLocation() == block.getLocation())
			{
				return true;
			}
		}
		
		return false;
	}
	
	public void removeSavedBlock(SavedBlock block)
	{
		Iterator<SavedBlock> it = savedBlocks.iterator();
		while(it.hasNext())
		{
			SavedBlock b = it.next();
			if(b.getLocation() == block.getLocation() && b.getMaterial() == block.getMaterial())
			{
				it.remove();
				return;
			}
		}
	}
	
	public boolean isProtectedZone(Block b)
	{
		if(this.protectedZone.contains(b))
		{
			return true;
		}
		
		return false;
	}
	
	public void addProtectedZone(Block b)
	{
		if(!isProtectedZone(b))
		{
			this.protectedZone.add(b);
			this.main.getConfig().set("protected-area", this.protectedZone);
			this.main.saveConfig();
		}
	}
	
	public void removeProtectedZone(Block b)
	{
		if(isProtectedZone(b))
		{
			this.protectedZone.remove(b);
			this.main.getConfig().set("protected-area", this.protectedZone);
			this.main.saveConfig();
		}
	}
	
	public Rush getMain()
	{
		return this.main;
	}
	
	public GameState getGameState()
	{
		return this.state;
	}

	public ArrayList<Team> getTeams()
	{
		return this.teams;
	}
	
	public void setGameTime(Integer time)
	{
		this.gameTime = time;
	}
	
	public Integer getGameTime()
	{
		return this.gameTime;
	}
	
	public ArrayList<Player> getPlayers()
	{
		return this.players;
	}
	
	public Team getTeam(String name)
	{
		for(Team t : teams)
		{
			if(t.getName().equalsIgnoreCase(name))
			{
				return t;
			}
		}
		
		return null;
	}
	
	public void setGameState(GameState state) {
		this.state =  state;
	}

	@SuppressWarnings("deprecation")
	public void addSpec(Player p)
	{
		p.getInventory().clear();
		
		ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
		SkullMeta meta = (SkullMeta) skull.getItemMeta();
		meta.setOwner(p.getName());
		skull.setItemMeta(meta);
		
		p.getInventory().setHelmet(skull);
		p.getInventory().setChestplate(new ItemStack(Material.AIR));
		p.getInventory().setLeggings(new ItemStack(Material.AIR));
		p.getInventory().setBoots(new ItemStack(Material.AIR));
		main.clearPotionEffects(p);
		p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 9999999, 0), true);
		p.setFoodLevel(20);
		p.setAllowFlight(true);
		InvUtils.giveSpecItem(p);
		this.players.remove(p);
		this.specs.add(p);
		for(Player pls : Bukkit.getOnlinePlayers())
		{
			pls.hidePlayer(p);
			if(specs.contains(pls))
			{
				p.hidePlayer(pls);
			}
		}
		
		for(Player specs : specs)
		{
			p.showPlayer(specs);
			specs.showPlayer(p);
		}
		
		p.getEnderChest().clear();
	}
	
	public void addPlayer(Player p)
	{
		p.getInventory().clear();
		p.getInventory().setHelmet(new ItemStack(Material.AIR));
		p.getInventory().setChestplate(new ItemStack(Material.AIR));
		p.getInventory().setLeggings(new ItemStack(Material.AIR));
		p.getInventory().setBoots(new ItemStack(Material.AIR));
		main.clearPotionEffects(p);
		p.setFoodLevel(20);
		p.setHealth(20.0D);
		InvUtils.giveLobbyItem(p);
		players.add(p);
		p.setGameMode(GameMode.SURVIVAL);
		p.setAllowFlight(false);
		Bukkit.broadcastMessage("§a+ §f" + p.getName()+  " s'est connecté. "+ (players.size() == teams.size()*4 ? "§a" : "§c") +"("+players.size()+"/"+(teams.size()*4)+")");
		kills.put(p, 0);
		bed.put(p, 0);
		
		for(Player specs : specs)
		{
			p.hidePlayer(specs);
		}
		
		for(Player pls : players)
		{
			ScoreboardUtils.setToLobbyScoreboard(pls);
			p.showPlayer(pls);
			pls.showPlayer(p);
		}
		
		p.getEnderChest().clear();
	}
	
	public void removePlayer(Player p)
	{
		players.remove(p);
		respawn.remove(p);
		
		if(state == GameState.WAITING)
		{
			Bukkit.broadcastMessage("§c- §f" + p.getName()+  "  s'est déconnecté. "+ (players.size() == teams.size()*4 ? "§a" : "§c") +"("+players.size()+"/"+(teams.size()*4)+")");
			
			for(Team t : Rush.getGameManager().getTeams())
			{
				if(t.getPlayers().contains(p))
				{
					t.removePlayer(p);
					break;
				}
			}
			p.setPlayerListName(p.getName());
		}
		else
		{
			if(getPlayerTeam(p) != null)
			{
				getPlayerTeam(p).removePlayer(p);
			}
		}
	}
	
	public boolean isInTeam(Player p)
	{
		for(Team team : teams)
		{
			if(team.getPlayers().contains(p))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public void startGame()
	{
		state = GameState.STARTED;
		Core.getCore().getServers().get(Core.getCore().currentServerId).setStatus(ServerStatus.PLAYING);
		Core.getCore().getServers().get(Core.getCore().currentServerId).updateToDatabase();
		
		List<Entity> list = Bukkit.getWorlds().get(0).getEntities();
    	Iterator<Entity> entities = list.iterator();
    	while (entities.hasNext()) {
    	    Entity entity = entities.next();
    	    if (entity instanceof Item) {
    	        entity.remove();
    	    }
    	}
		
		for(Player p : players)
		{
			if(!isInTeam(p))
			{
				Random ran = new Random();
				Integer i;
				
				do 
				{
					i = ran.nextInt(teams.size());
				}
				while(teams.get(i).isFull() || teams.get(i).getPlayers().contains(p));
				
				teams.get(i).addPlayer(p);
			}
		}
		
		for(Team team : teams)
		{
			for(Player pls : team.getPlayers())
			{
				InvUtils.giveGameItem(pls);
				
				pls.setGameMode(GameMode.SURVIVAL);
				pls.teleport(team.getSpawn());
				
				ScoreboardUtils.setoGameScoreboard(pls);
			}
		}
	}
	
	public void endGame()
	{
		state = GameState.ENDING;
		gameTask.timer = -1;
		
		Team winner = null;
		for(Team t : teams)
		{
			if(t.getPlayers().size() > 0)
			{
				winner = t;
				break;
			}
		}
		
		Integer point = 0;
		
		if(teams.size() == 8 && maxPlayer == 4)
		{
			point = 10;
		}
		else if(teams.size() == 8 && maxPlayer == 2)
		{
			point = 9;
		}
		else if(teams.size() == 4 && maxPlayer == 1)
		{
			point = 4;
		}
		else if(teams.size() == 4 && maxPlayer == 2)
		{
			point = 6;
		}
		else if(teams.size() == 4 && maxPlayer == 4)
		{
			point = 8;
		}
		
		for(Player pls : winner.getPlayers())
		{
			Core.getCore().getPlayerData(pls).addRushPoint(point);
			Core.getCore().getPlayerData(pls).addRushWins(1);
			pls.sendMessage("§6[§eRush§6]§r Statistiques et points sauvegardés. Vous pouvez vous déconnecter ! ");
		}
		
		Bukkit.broadcastMessage("§e§m----------------§6[§eRush§6]§e§m----------------");
		Bukkit.broadcastMessage("L'équipe " + winner.getTag() + " §ra gagné la partie ! ");
		Bukkit.broadcastMessage("§e§m--------------------------------------");
		
		for(Team t : teams)
		{
			t.setHasBed(true);
			t.getPlayers().clear();
		}
		
		Core.getCore().getServers().get(Core.getCore().currentServerId).setStatus(ServerStatus.RESTARTING);
		Core.getCore().getServers().get(Core.getCore().currentServerId).updateToDatabase();
		
		Core.getCore().restarting = true;
		
		Bukkit.reload();
	}
	
	public Team getPlayerTeam(Player p)
	{
		for(Team t : Rush.getGameManager().getTeams())
		{
			if(t.getPlayers().contains(p))
			{
				return t;
			}
		}
		
		return null;
	}
	
	public boolean isEnd()
	{
		int count = 0;
		for(Team team : teams)
		{
			if(team.getPlayers().size() > 0)
			{
				count++;
			}
		}
		
		return (count <= 1);
	}
}
