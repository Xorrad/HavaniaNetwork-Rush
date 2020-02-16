package net.havania.rush;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

import net.havania.rush.command.CenterVillagerCommand;
import net.havania.rush.command.SecureZoneCommand;
import net.havania.rush.command.SpecCommand;
import net.havania.rush.command.StartCommand;
import net.havania.rush.command.TeamCommand;
import net.havania.rush.game.GameManager;
import net.havania.rush.game.GameState;
import net.havania.rush.game.Team;
import net.havania.rush.listener.entity.EntityDamageByEntity;
import net.havania.rush.listener.entity.EntitySpawn;
import net.havania.rush.listener.entity.TNTExplode;
import net.havania.rush.listener.entity.VillagerMultipleTrade;
import net.havania.rush.listener.player.PlayerBlockBreak;
import net.havania.rush.listener.player.PlayerCanPlace;
import net.havania.rush.listener.player.PlayerChat;
import net.havania.rush.listener.player.PlayerDamage;
import net.havania.rush.listener.player.PlayerDeath;
import net.havania.rush.listener.player.PlayerDrop;
import net.havania.rush.listener.player.PlayerFoodLeft;
import net.havania.rush.listener.player.PlayerInteract;
import net.havania.rush.listener.player.PlayerInventoryClick;
import net.havania.rush.listener.player.PlayerJoin;
import net.havania.rush.listener.player.PlayerKill;
import net.havania.rush.listener.player.PlayerMove;
import net.havania.rush.listener.player.PlayerPlace;
import net.havania.rush.listener.player.PlayerQuit;
import net.havania.rush.listener.player.PlayerTake;

public class Rush extends JavaPlugin implements Listener {
	
	static Rush instance;
	static GameManager gameManager;
		
	@Override
	public void onEnable() 
	{
		instance = this;
		registerVariables();
		registerListeners();
		registerCommands();
		loadConfig();
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onDisable() 
	{
		for(Player pls : Bukkit.getOnlinePlayers())
		{
			pls.kickPlayer("§cLe serveur redémarre !");
		}
		
		gameManager.setGameState(GameState.RELOADING);
		gameManager.gameTask.reloadWorld();
	}
	
	private void registerListeners()
	{
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(this, this);
		pm.registerEvents(new PlayerJoin(), this);
		pm.registerEvents(new PlayerQuit(), this);
		pm.registerEvents(new PlayerInteract(), this);
		pm.registerEvents(new PlayerInventoryClick(), this);
		pm.registerEvents(new PlayerBlockBreak(), this);
		pm.registerEvents(new PlayerFoodLeft(), this);
		pm.registerEvents(new PlayerDamage(), this);
		pm.registerEvents(new PlayerTake(), this);
		pm.registerEvents(new PlayerPlace(), this);
		pm.registerEvents(new PlayerDeath(), this);
		pm.registerEvents(new EntitySpawn(), this);
		pm.registerEvents(new PlayerChat(), this);
		pm.registerEvents(new PlayerKill(), this);
		pm.registerEvents(new EntityDamageByEntity(), this);
		pm.registerEvents(new TNTExplode(), this);
		pm.registerEvents(new PlayerDrop(), this);
		pm.registerEvents(new PlayerMove(), this);
		pm.registerEvents(new PlayerCanPlace(), this);
		pm.registerEvents(new VillagerMultipleTrade(), this);
	}
	
	private void registerCommands()
	{
		getCommand("start").setExecutor(new StartCommand());
		getCommand("spec").setExecutor(new SpecCommand());
		getCommand("team").setExecutor(new TeamCommand());
		getCommand("center_villager").setExecutor(new CenterVillagerCommand());
		getCommand("secure_zone").setExecutor(new SecureZoneCommand());
	}
	
	private void registerVariables()
	{
		gameManager = new GameManager();
	}
	
	private void loadConfig()
	{
		saveDefaultConfig();
		
		gameManager.maxPlayer = getConfig().getInt("max-player");
		gameManager.maxDistanceToSpawn = getConfig().getInt("max-distance-to-spawn");
		ConfigurationSection teamsSection = getConfig().getConfigurationSection("teams");
		
		for(String team : teamsSection.getKeys(false))
		{
			String id = team;
			String name = teamsSection.getString(team + ".name");
			String color = teamsSection.getString(team + ".color").replace('&', '§');
			byte woolData = (byte) teamsSection.getInt(team + ".woolId");
			String spawnS = teamsSection.getString(team + ".spawn");
			Location spawn = new Location(Bukkit.getWorld(spawnS.split(",")[0]), Double.parseDouble(spawnS.split(",")[1]), Double.parseDouble(spawnS.split(",")[2]), Double.parseDouble(spawnS.split(",")[3]), Float.parseFloat(spawnS.split(",")[4]), Float.parseFloat(spawnS.split(",")[5]));
			String bedS = teamsSection.getString(team + ".bed");
			Location bed = new Location(Bukkit.getWorld(bedS.split(",")[0]), Double.parseDouble(bedS.split(",")[1]), Double.parseDouble(bedS.split(",")[2]), Double.parseDouble(bedS.split(",")[3]));
			boolean enable = teamsSection.getBoolean(team + ".enable");
			
			HashMap<Integer, Location> resourcesSpawn = new HashMap<>();
			ConfigurationSection resourceSection = getConfig().getConfigurationSection("teams." + team + ".resources_spawn");
			
			for(String number : resourceSection.getKeys(false))
			{
				String resourcesS = resourceSection.getString(number);
				Location resourcesSpawnL = new Location(Bukkit.getWorld(resourcesS.split(",")[0]), Double.parseDouble(resourcesS.split(",")[1]) + 0.5, Double.parseDouble(resourcesS.split(",")[2]) + 0.5, Double.parseDouble(resourcesS.split(",")[3]) + 0.5);
				resourcesSpawn.put(Integer.parseInt(number), resourcesSpawnL);
			}
			
			if(enable)
			{
				gameManager.getTeams().add(new Team(id, name, color, woolData, spawn, bed, resourcesSpawn, gameManager.maxPlayer));
			}
		}
		
		Bukkit.getConsoleSender().sendMessage("§6[HavaniaRush] §ateams were loaded succefully !");
	}
	
	public void clearPotionEffects(Player p)
	{
		for(PotionEffect effect : p.getActivePotionEffects())
		{
		    p.removePotionEffect(effect.getType());
		}
	}
	
	public static Rush getInstance() { return instance; }
	public static GameManager getGameManager() { return gameManager; }
	
	@EventHandler
	public void onWeather(WeatherChangeEvent e)
	{
		e.setCancelled(true);
	}

}
