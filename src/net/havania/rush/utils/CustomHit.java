package net.havania.rush.utils;

import org.bukkit.entity.Player;

public class CustomHit {
	
	public Player player;
	public Player attacker;
	public Long timestamp;
	
	public CustomHit(Player player, Player attacker) 
	{
		this.player = player;
		this.attacker = attacker;
		this.timestamp = System.currentTimeMillis();
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Player getAttacker() {
		return attacker;
	}

	public void setAttacker(Player attacker) {
		this.attacker = attacker;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

}
