package net.linaris.Practice.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.linaris.Practice.Practice;

public abstract class ScoreBoardModule implements Runnable {

	Practice game;
	
	public ScoreBoardModule(Practice game) {
		this.game = game;
		game.getServer().getScheduler().scheduleSyncRepeatingTask(game, this, 0, 2);
	}
	
	public void update() {
		onUpdate();
		for (Player p : Bukkit.getOnlinePlayers())
			onUpdate(p);
	}
	
	@Override
	public void run() {
		update();
	}
	
	

	public abstract void onUpdate();
	public abstract void onUpdate(Player p);

}
