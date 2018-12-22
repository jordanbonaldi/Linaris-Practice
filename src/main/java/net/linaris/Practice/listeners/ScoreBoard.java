package net.linaris.Practice.listeners;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.linaris.Practice.Practice;
import net.linaris.Practice.database.DatabaseConnector;
import net.linaris.Practice.database.models.StatsModel;
import net.linaris.Practice.handlers.players.OmegaPlayer;
import net.linaris.Practice.handlers.players.OmegaPlayerManager;
import net.linaris.Practice.utils.RainbowEffect;
import net.linaris.Practice.utils.ScoreBoardModule;
import net.linaris.Practice.utils.ScoreboardSign;

public class ScoreBoard extends ScoreBoardModule{

	RainbowEffect NAME;
	String title;

	public static HashMap<OmegaPlayer, Boolean> playing = new HashMap<OmegaPlayer, Boolean>();
	public static int game = 0;

	public ScoreBoard(Practice game) {
		super(game);

		NAME = new RainbowEffect("LINARIS", "§f§l", "§6§l", 40);

	}

	@Override
	public void onUpdate(Player p) {
        OmegaPlayer pl = OmegaPlayerManager.get().getPlayer(p.getName());
        StatsModel uhc = DatabaseConnector.getStatsDao().getStats(pl.getData().getId(), 0);
        StatsModel pot = DatabaseConnector.getStatsDao().getStats(pl.getData().getId(), 1);
        StatsModel archer = DatabaseConnector.getStatsDao().getStats(pl.getData().getId(), 2);

		if(playing.get(pl) == false){

		
		ScoreboardSign bar = ScoreboardSign.get(p);
		if (bar == null) {
			bar = new ScoreboardSign(p, title);
			bar.create();
		}

		HashMap<Integer, String> lines = new HashMap<Integer, String>();
		bar.setObjectiveName(title);
		
		lines.put(13, "§1");
		lines.put(12, "§fGrade: §c" + pl.getData().getRank().getName());
		lines.put(11, "§2");
		lines.put(10, "§fClassé rest.: §b" + (pl.getData().getRank().getVipLevel() >= 1 ? "§cInf" : pl.getData().getRankedRemaining()));
		lines.put(9, "§fBuildUHC: §b" + uhc.getElo());
		lines.put(8, "§fPotion: §b" + pot.getElo());
		lines.put(7, "§fArcher: §b" + archer.getElo());
		lines.put(6, "§4");
		lines.put(5, "§fJoueurs: §b" + Bukkit.getOnlinePlayers().size());
		lines.put(4, "§5");
		lines.put(3, "§fEn match: §b" + game);
		lines.put(2, "§3");
		lines.put(1, "§ewww.linaris.fr");
		
		
		if (lines.isEmpty())
			return;
		for (int i = 1; i < 16; i++) {
			if (!lines.containsKey(i)) {
				if (bar.getLine(i) != null)
					bar.removeLine(i);
			} else {
				if (bar.getLine(i) == null)
					bar.setLine(i, lines.get(i));
				else if (!bar.getLine(i).equals(lines.get(i)))
					bar.setLine(i, lines.get(i));
			}
		}
		}

	}

	@Override
	public void onUpdate() {
		title = NAME.next();
	}

}
