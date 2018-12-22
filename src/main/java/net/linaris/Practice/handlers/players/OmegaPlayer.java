



package net.linaris.Practice.handlers.players;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import org.apache.commons.lang.time.DateUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;

import net.linaris.Practice.database.DatabaseConnector;
import net.linaris.Practice.database.models.StatsModel;
import net.linaris.Practice.database.models.UserModel;
import net.linaris.Practice.handlers.players.utils.AttributeKey;
import net.linaris.Practice.handlers.players.utils.AttributeMap;
import net.linaris.Practice.handlers.states.State;
import net.linaris.Practice.handlers.states.states.LobbyState;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PlayerConnection;

public class OmegaPlayer
  extends PlayerProvider
{
  private UUID uuid;
  private String name;
  private AttributeMap attributes;
  private PlayerConnection connection;
  private State state;
  private UserModel data;
  private HashMap<Integer, StatsModel> stats;
  private Team team;
  private boolean vanish;
  private boolean freeze;
  
  public UUID getUuid()
  {
    return this.uuid;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public AttributeMap getAttributes()
  {
    return this.attributes;
  }
  
  public PlayerConnection getConnection()
  {
    return this.connection;
  }
  
  public State getState()
  {
    return this.state;
  }
  
  public UserModel getData()
  {
    return this.data;
  }
  
  public HashMap<Integer, StatsModel> getStats()
  {
    return this.stats;
  }
  
  public Team getTeam()
  {
    return this.team;
  }
  
  public boolean isVanish()
  {
    return this.vanish;
  }
  
  public boolean isFreeze()
  {
    return this.freeze;
  }
  
  public OmegaPlayer()
  {
    this.attributes = new AttributeMap();
    this.stats = new HashMap();
  }
  
  public <T> T getAttr(AttributeKey<T> key)
  {
    return (T)this.attributes.get(key);
  }
  
  public <T> void setAttr(AttributeKey<T> key, T attribute)
  {
    this.attributes.set(key, attribute);
  }
  
  public int getPing()
  {
    return getHandle().ping;
  }
  
  OmegaPlayer setBacked(Player player)
  {
    String lastName;
    if (player != null)
    {
      this.player = ((CraftPlayer)player);
      this.connection = getHandle().playerConnection;
      this.name = player.getName();
      this.uuid = player.getUniqueId();
      this.data = DatabaseConnector.getUsersDao().getUser(this.uuid);
      lastName = this.data.getLastName();
      if (!this.name.equals(lastName))
      {
        this.data.setLastName(this.name);
        DatabaseConnector.getUsersDao().save(this.data);
      }
      Date today = new Date();
      if ((this.data.getLastConnection().longValue() == 0L) || (!DateUtils.isSameDay(today, new Date(this.data.getLastConnection().longValue()))))
      {
        this.data.setRankedRemaining(25);
        sendMessage("§aVous avez reçu §c25 Match classé§a pour votre connection du jour !");
      }
      this.data.setLastConnection(Long.valueOf(System.currentTimeMillis()));
      
      this.team = (Bukkit.getScoreboardManager().getMainScoreboard().getTeam(player.getName()) == null ? Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam(player.getName()) : Bukkit.getScoreboardManager().getMainScoreboard().getTeam(player.getName()));
      this.team.addPlayer(player);
      this.team.setPrefix(this.data.getRank().getPrefix());
    }
    else
    {
      this.player = null;
      this.connection = null;
      this.attributes.clear();
      setState(null);
      save();
      if (this.stats.isEmpty()) {
        for (StatsModel model : this.stats.values()) {
          DatabaseConnector.getStatsDao().save(model);
        }
      }
      if (this.team != null) {
        this.team.unregister();
      }
    }
    return this;
  }
  
  public void heal()
  {
    setHealth(getMaxHealth());
    setFoodLevel(20);
    setSaturation(5.0F);
    setFireTicks(1);
    removePotionEffect(PotionEffectType.BLINDNESS);
    removePotionEffect(PotionEffectType.CONFUSION);
    removePotionEffect(PotionEffectType.HUNGER);
    removePotionEffect(PotionEffectType.POISON);
    removePotionEffect(PotionEffectType.SATURATION);
    removePotionEffect(PotionEffectType.SLOW);
    removePotionEffect(PotionEffectType.SLOW_DIGGING);
    removePotionEffect(PotionEffectType.WEAKNESS);
    removePotionEffect(PotionEffectType.WITHER);
    removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
    removePotionEffect(PotionEffectType.SPEED);
    removePotionEffect(PotionEffectType.ABSORPTION);
  }
  
  public void save()
  {
    if (this.data != null) {
      DatabaseConnector.getUsersDao().save(this.data);
    }
  }
  
  public void reset()
  {
    heal();
    setAllowFlight(false);
    setFlying(false);
    closeInventory();
    setGameMode(GameMode.SURVIVAL);
    getInventory().clear();
    getInventory().setHelmet(null);
    getInventory().setChestplate(null);
    getInventory().setLeggings(null);
    getInventory().setBoots(null);
  }
  
  public boolean isOnline()
  {
    return this.player != null;
  }
  
  public boolean isAvailable()
  {
    return (isOnline()) && ((this.state instanceof LobbyState));
  }
  
  public boolean isSimilar(Player player)
  {
    return player.getName().equals(this.name);
  }
  
  public void updateTimePlayedBy(long time)
  {
    getData().setTimePlayed(Long.valueOf(getData().getTimePlayed().longValue() + time));
  }
  
  public void setRank(Rank rank)
  {
    this.data.setRank(rank);
    this.team.setPrefix(rank.getPrefix());
  }
  
  public void setVanish(boolean vanish)
  {
    this.vanish = vanish;
    if (this.team != null) {
      this.team.setPrefix(vanish ? "§8§oHidden " : this.data.getRank().getPrefix());
    }
    for (OmegaPlayer player : OmegaPlayerManager.get()) {
      if (player.isOnline()) {
        if (player.getData().getRank().getModerationLevel() < this.data.getRank().getModerationLevel())
        {
          if (vanish) {
            player.hidePlayer(this.player);
          } else {
            player.showPlayer(this.player);
          }
        }
        else if (vanish) {
          player.sendMessage("§b" + this.name + " §epasse en mode §8§oHidden");
        } else {
          player.sendMessage("§b" + this.name + " §esort du mode §8§oHidden");
        }
      }
    }
  }
  
  public void setFreeze(boolean freeze)
  {
    this.freeze = freeze;
    if (this.team != null) {
      this.team.setPrefix(freeze ? "§b§oFreeze " : this.data.getRank().getPrefix());
    }
    if (freeze)
    {
      sendMessage("§cVous avez été freeze ! Si vous vous déconnectez vous serez ban !");
      sendMessage("§cMerci de bien vouloir patientez pendant que le modérateur s'occupe de vous");
    }
    else
    {
      sendMessage("§cVous avez été unfreeze ! Bon jeu ;)");
    }
    for (OmegaPlayer player : OmegaPlayerManager.get()) {
      if (player.isOnline()) {
        if (player.getData().getRank().getModerationLevel() >= 2) {
          if (freeze) {
            player.sendMessage("§b" + this.name + " §ca été freeze");
          } else {
            player.sendMessage("§b" + this.name + " §cn'est plus freeze");
          }
        }
      }
    }
  }
  
  public void sendActionBar(String message)
  {
    if (!isOnline()) {
      return;
    }
    CraftPlayer p = this.player;
    IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
    PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte)2);
    p.getHandle().playerConnection.sendPacket(ppoc);
  }
  
  public void setState(State newState)
  {
    if (this.state != null) {
      this.state.onExit();
    }
    this.state = newState;
    if (this.state != null) {
      this.state.onEnter();
    }
  }
  
  public StatsModel getStats(int gameType)
  {
    if (this.stats.containsKey(Integer.valueOf(gameType))) {
      return (StatsModel)this.stats.get(Integer.valueOf(gameType));
    }
    StatsModel model = DatabaseConnector.getStatsDao().getStats(this.data.getId(), gameType);
    this.stats.put(Integer.valueOf(gameType), model);
    return model;
  }
  
  public boolean isDonator()
  {
    return this.data.getRank().getVipLevel() >= 1;
  }
  
  public boolean equals(Object obj)
  {
    if (!(obj instanceof OmegaPlayer)) {
      return false;
    }
    OmegaPlayer target = (OmegaPlayer)obj;
    return this.name.equals(target.getName());
  }
}
