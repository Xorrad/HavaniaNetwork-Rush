package net.havania.rush.game;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

import net.havania.rush.Rush;

public class Team {
	
	public String id;
	public boolean hasBed;
	public boolean elemited;
	public ArrayList<Player> players;
	public String name;
	public String color;
	public Location bed;
	public Location spawn;
	public HashMap<Integer, Location> resourcesSpawn;
	public byte woolData;
	public int maxPlayer;
	public org.bukkit.scoreboard.Team team;
	
	public Team(String id, String name, String color, byte woolData, Location spawn, Location bed, HashMap<Integer, Location> resourcesSpawn) {
		this.id = id;
		this.hasBed = true;
		this.elemited = false;
		this.players = new ArrayList<>();
		this.name = name;
		this.color = color;
		this.spawn = spawn;
		this.bed = bed;
		this.resourcesSpawn = resourcesSpawn;
		this.woolData = woolData;
		this.maxPlayer = 4;
		this.team = Rush.getGameManager().board.registerNewTeam(name);
		this.team.setPrefix(color);
	}
	
	public Team(String id, String name, String color, byte woolData, Location spawn, Location bed, HashMap<Integer, Location> resourcesSpawn, int maxPlayer) {
		this.id = id;
		this.hasBed = true;
		this.elemited = false;
		this.players = new ArrayList<>();
		this.name = name;
		this.color = color;
		this.spawn = spawn;
		this.bed = bed;
		this.resourcesSpawn = resourcesSpawn;
		this.woolData = woolData;
		this.maxPlayer = maxPlayer;
		this.team = Rush.getGameManager().board.registerNewTeam(name);
		this.team.setPrefix(color);
		
		Objective objective = Rush.getGameManager().board.registerNewObjective("prefix", "dummy");
		objective.setDisplaySlot(DisplaySlot.PLAYER_LIST);
		objective.setDisplayName("prefix");
		 
		for(Player online : Bukkit.getOnlinePlayers()){
		  Score score = objective.getScore(online);
		  score.setScore(5);
		}
	}
	
	public Location getBed() {
		return bed;
	}

	public void setBed(Location bed) {
		this.bed = bed;
	}

	public Location getSpawn() {
		return spawn;
	}

	public void setSpawn(Location spawn) {
		this.spawn = spawn;
	}

	public boolean hasBed() {
		return hasBed;
	}

	public void setHasBed(boolean hasBed) {
		this.hasBed = hasBed;
	}

	public boolean isElemited() {
		return elemited;
	}

	public void setElemited(boolean elemited) {
		this.elemited = elemited;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}
	
	public void addPlayer(Player p)
	{
		for(Team t : Rush.getGameManager().getTeams())
		{
			if(t.getPlayers().contains(p))
			{
				t.removePlayer(p);
			}
		}
		
		this.team.addPlayer(p);
		this.players.add(p);
		p.sendMessage("Vous avez rejoint l'équipe " + this.color + this.name + " §f!");
		//p.setPlayerListName(this.color + p.getName());
		p.setBedSpawnLocation(this.spawn, true);
	}
	
	public void removePlayer(Player p)
	{
		this.team.removePlayer(p);
		this.players.remove(p);
	}
	
	public boolean isFull()
	{
		if(this.players.size() == this.maxPlayer)
		{
			return true;
		}
		return false;
	}

	public int getMaxPlayer() {
		return maxPlayer;
	}

	public void setMaxPlayer(int maxPlayer) {
		this.maxPlayer = maxPlayer;
	}

	public byte getWoolData() {
		return woolData;
	}

	public void setWoolData(byte woolData) {
		this.woolData = woolData;
	}
	
	public String getTag()
	{
		return this.color + this.name;
	}

	public Location getResourcesSpawn(Integer number) {
		return resourcesSpawn.get(number);
	}

	public HashMap<Integer, Location> getResourcesSpawn() {
		return resourcesSpawn;
	}

	public void setResourcesSpawn(Location resourcesSpawn, Integer number) {
		this.resourcesSpawn.put(number, resourcesSpawn);
	}
}
