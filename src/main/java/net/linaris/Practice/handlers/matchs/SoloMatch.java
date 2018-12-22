package net.linaris.Practice.handlers.matchs;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import net.linaris.Practice.database.DatabaseConnector;
import net.linaris.Practice.database.daos.BuildsDao;
import net.linaris.Practice.database.daos.MatchsDao;
import net.linaris.Practice.database.daos.StatsDao;
import net.linaris.Practice.database.models.BuildModel;
import net.linaris.Practice.database.models.StatsModel;
import net.linaris.Practice.database.models.UserModel;
import net.linaris.Practice.handlers.games.GameType;
import net.linaris.Practice.handlers.games.WrappedLocation;
import net.linaris.Practice.handlers.maps.Map;
import net.linaris.Practice.handlers.matchs.events.Event;
import net.linaris.Practice.handlers.matchs.events.EventsManager;
import net.linaris.Practice.handlers.matchs.items.UseDefaultKitItem;
import net.linaris.Practice.handlers.matchs.items.UseKitItem;
import net.linaris.Practice.handlers.players.OmegaPlayer;
import net.linaris.Practice.handlers.states.StatesManager;
import net.linaris.Practice.handlers.states.states.InGameState;
import net.linaris.Practice.listeners.ScoreBoard;
import net.linaris.Practice.utils.ScoreboardSign;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class SoloMatch
  extends Match
{
  private OmegaPlayer playerA;
  private OmegaPlayer playerB;
  private EventsManager eventsManager;
  private boolean finished;
  private List<Block> blocksPlaced;
  
  public OmegaPlayer getPlayerA()
  {
    return this.playerA;
  }
  
  public OmegaPlayer getPlayerB()
  {
    return this.playerB;
  }
  
  public EventsManager getEventsManager()
  {
    return this.eventsManager;
  }
  
  public SoloMatch(World world, GameType type, Map map, boolean ranked, OmegaPlayer playerA, OmegaPlayer playerB)
    throws Exception
  {
    super(world, type, map, ranked);
    this.blocksPlaced = new ArrayList();
    this.playerA = playerA;
    if (isRanked())
    	this.playerA.getData().removeRanked();
    addPlayer(playerA);
    this.playerB = playerB;
    if (isRanked())
    	this.playerB.getData().removeRanked();
    addPlayer(playerB);
    this.eventsManager = new EventsManager();
    this.eventsManager.addEvent(new PvPStart());
    this.eventsManager.setNextEvent();
  }
  
  public void onStart()
  {
	ScoreBoard.game += 2;
    teleportTo(this.playerA.getPlayer(), (WrappedLocation)getMap().getSpawns().get(0));
    teleportTo(this.playerB.getPlayer(), (WrappedLocation)getMap().getSpawns().get(1));
    setupKits(this.playerA);
    setupKits(this.playerB);
    
    ScoreBoard.playing.put(this.playerA, true);
    ScoreBoard.playing.put(this.playerB, true);
	ScoreboardSign bar = ScoreboardSign.get(this.playerA.getPlayer());
	if (bar != null) {
		bar.destroy();
	}
	ScoreboardSign bar2 = ScoreboardSign.get(this.playerB.getPlayer());
	if (bar2 != null) {
		bar2.destroy();
	}
    this.playerA.sendMessage("§aVous êtes en combat avec §e" + this.playerB.getName());
    this.playerB.sendMessage("§aVous êtes en combat avec §e" + this.playerA.getName());
  }
  
  public void onUpdate()
  {
    this.eventsManager.update();
  }
  
  public void onFinish()
  {
		ScoreBoard.game -= 2;
	    ScoreBoard.playing.put(this.playerA, false);
	    ScoreBoard.playing.put(this.playerB, false);
    for (OmegaPlayer target : getIngames().values()) {
      if (target.isOnline()) {
        StatesManager.defaultState(target);
      }
    }
    for (OmegaPlayer target : getSpectators().values()) {
      if (target.isOnline()) {
        StatesManager.defaultState(target);
      }
    }
  }
  
  public void wantDie(Player player)
  {
    if (this.finished) {
      return;
    }
    if (isSpectator(player.getName()))
    {
      removePlayer(player.getName());
      return;
    }
    if (!isInGame(player.getName())) {
      return;
    }
    removePlayer(player.getName());
    addSpectator(this.playerA.isSimilar(player) ? this.playerA : this.playerB);
    win(this.playerA.isSimilar(player) ? this.playerB : this.playerA);
  }
  
  @SuppressWarnings("deprecation")
public void win(OmegaPlayer player)
  {
    this.finished = true;
    OmegaPlayer loser = player.equals(this.playerA) ? this.playerB : this.playerA;
    
    this.playerA.updateTimePlayedBy(getDuration());
    this.playerB.updateTimePlayedBy(getDuration());
    
    this.model = DatabaseConnector.getMatchsDao().addSoloMatch(this.getMap().getId(), this.getType().getId(), true, player, loser);
    
    StatsModel winnerStats = DatabaseConnector.getStatsDao().getStats(player.getData().getId(), getType().getId());
    StatsModel loserStats = DatabaseConnector.getStatsDao().getStats(loser.getData().getId(), getType().getId());
    
    sendMessage("§8§l§m<----------§a§l Fin du match §8§l§m---------->");
    sendMessage("");
    
    TextComponent winnerComp = new TextComponent("   §a§lGagnant: §r" + player.getName() + " (§c" + (int)player.getHealth() + "❤§r)");
    winnerComp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/inventorybackup " + getId() + " " + player.getData().getId()));
    winnerComp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[] { new TextComponent("§cVoir l'inventaire du joueur") }));
    sendMessage(winnerComp);
    sendMessage("§8 ");
    TextComponent loserComp = new TextComponent("   §a§lPerdant: §r" + loser.getName() + " §c§lMort");
    loserComp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/inventorybackup " + getId() + " " + loser.getData().getId()));
    loserComp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[] { new TextComponent("§cVoir l'inventaire du joueur") }));
    sendMessage(loserComp);
    
    sendMessage("");
    sendMessage("§7§oClique sur le pseudo du joueur pour voir son inventaire");
    sendMessage("§8§l§m<-------------------------------->");
	for (int i = 0; i < 36; ++i)
		this.playerA.getInventory().setItem(i, new ItemStack(Material.AIR));
	for (int i = 0; i < 36; ++i)
		this.playerB.getInventory().setItem(i, new ItemStack(Material.AIR));
    if (isRanked())
    {
      winnerStats.addWinRanked();
      winnerStats.addPlayedRanked();
      winnerStats.setElo(winnerStats.getElo() + 5 + (player.isDonator() ? 2 : 0));
      player.sendMessage("§a+" + (5 + (player.isDonator() ? 2 : 0)) + " élo ( " + winnerStats.getElo() + " )");
      
      loserStats.addLoseRanked();
      loserStats.addPlayedRanked();
      loserStats.setElo(loserStats.getElo() - (5 - (loser.isDonator() ? 2 : 0)));
      
      loser.sendMessage("§c-" + (5 - (loser.isDonator() ? 2 : 0)) +" élo ( " + loserStats.getElo() + " )");
    }
    else
    {
      winnerStats.addWin();
      winnerStats.addPlayed();
      
      loserStats.addLose();
      loserStats.addPlayed();
    }
    DatabaseConnector.getStatsDao().save(winnerStats);
    DatabaseConnector.getStatsDao().save(loserStats);
    
    player.save();
    loser.save();
    
    finish();
  }
  
  public void setupKits(final OmegaPlayer player) {
      player.reset();
      final UseDefaultKitItem defaultKitItem = new UseDefaultKitItem(this.getType().getDefaultBuild());
      player.getInventory().setItem(1, defaultKitItem.getStaticItem());
      player.getInventory().setArmorContents(this.getType().getArmor());
      final List<BuildModel> builds = DatabaseConnector.getBuildsDao().getBuilds(player.getData().getId(), this.getType().getId());
      if (builds.isEmpty()) {
          return;
      }
      for (int i = 0; i < 5 && builds.size() >= i + 1; ++i) {
          final BuildModel build = builds.get(i);
          player.getInventory().setItem(3 + i, new UseKitItem(build).getStaticItem());
      }
  }
  
  @EventHandler
  public void onPlayerDead(PlayerDeathEvent e)
  {
    wantDie(e.getEntity());
  }
  
  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent e)
  {
    wantDie(e.getPlayer());
  }
  
  @EventHandler
  public void onPlayerKick(PlayerKickEvent e)
  {
    wantDie(e.getPlayer());
  }
  
  @EventHandler(priority=EventPriority.HIGHEST)
  public void onPlayerDamage(EntityDamageEvent e)
  {
    if (!(e.getEntity() instanceof Player)) {
      return;
    }
    if ((isInGame(e.getEntity().getName())) && 
      (!this.finished) && (
      (this.eventsManager.getEvent() == null) || (!(this.eventsManager.getEvent() instanceof PvPStart)))) {
      e.setCancelled(false);
    }
  }
 
  @EventHandler(priority=EventPriority.HIGHEST)
  public void onFoodLevelChange(FoodLevelChangeEvent e)
  {
    if (!(e.getEntity() instanceof Player)) {
      return;
    }
    if ((isInGame(e.getEntity().getName())) && 
      (!this.finished)) {
      e.setCancelled(false);
    }
  }
  
  @EventHandler
  public void onPlayerMove(PlayerMoveEvent e)
  {
    if (!(e.getPlayer() instanceof Player)) {
      return;
    }
    if ((isInGame(e.getPlayer().getName())) && 
      (this.eventsManager.getEvent() != null) && ((this.eventsManager.getEvent() instanceof PvPStart))) {
      e.getPlayer().teleport(e.getTo());
    }
    if (e.getTo().getY() <= 15.0)
    	wantDie(e.getPlayer());
  }
  
  @EventHandler(priority=EventPriority.HIGHEST)
  public void onPlayerPlace(BlockPlaceEvent e)
  {
    if (!(e.getPlayer() instanceof Player)) {
      return;
    }
    if (isInGame(e.getPlayer().getName()))
    {
      if (!getType().isBuildable())
      {
        e.setCancelled(true);
        return;
      }
      e.setCancelled(false);
      this.blocksPlaced.add(e.getBlock());
    }
  }
  
  @EventHandler(priority=EventPriority.HIGHEST)
  public void onPlayerBreak(BlockBreakEvent e)
  {
    if (!(e.getPlayer() instanceof Player)) {
      return;
    }
    if (isInGame(e.getPlayer().getName()))
    {
      if (!getType().isBuildable())
      {
        e.setCancelled(true);
        return;
      }
      if (!this.blocksPlaced.contains(e.getBlock()))
      {
        e.setCancelled(true);
        return;
      }
      e.setCancelled(false);
      this.blocksPlaced.remove(e.getBlock());
    }
  }
  
  public void onPlayerSpectator(OmegaPlayer player) {}
  
  class PvPStart extends Event
  {
      public PvPStart() {
          super("D\u00e9but", 5);
      }
    
    public void onRun()
    {
      SoloMatch.this.sendMessage("§aLe match commence ! Bonne chance");
      SoloMatch.this.playerA.setHealth(20);
      SoloMatch.this.playerA.setFallDistance(0.0F);
      SoloMatch.this.playerB.setHealth(20);
      SoloMatch.this.playerB.setFallDistance(0.0F);
    }
    
    public void onUpdate()
    {
      SoloMatch.this.sendMessage("§aLe match commence dans " + getTime() + " secondes !");
    }
  }
}
