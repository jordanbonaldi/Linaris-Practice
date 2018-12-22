package net.linaris.Practice.handlers.players;

import org.bukkit.craftbukkit.v1_8_R3.entity.*;
import java.net.*;
import net.md_5.bungee.api.chat.*;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.map.*;
import org.bukkit.event.player.*;
import org.bukkit.conversations.*;
import org.bukkit.plugin.*;
import org.bukkit.craftbukkit.v1_8_R3.scoreboard.*;
import org.bukkit.scoreboard.*;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.permissions.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.block.*;
import org.bukkit.block.Block;
import org.bukkit.potion.*;
import org.bukkit.util.*;
import org.bukkit.util.Vector;
import org.bukkit.event.entity.*;
import java.util.*;
import org.bukkit.*;
import org.bukkit.Achievement;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.entity.Entity;

public class PlayerProvider
{
    protected CraftPlayer player;
    private int hash;
    
    public PlayerProvider() {
        this.hash = 0;
    }
    
    public boolean isOp() {
        return this.player.isOp();
    }
    
    public void setOp(final boolean value) {
        this.player.setOp(value);
    }
    
    public boolean isOnline() {
        return this.player.getUniqueId() != null && Bukkit.getPlayer(this.player.getUniqueId()) != null;
    }
    
    public InetSocketAddress getAddress() {
        return this.player.getAddress();
    }
    
    public double getEyeHeight() {
        return this.player.getEyeHeight();
    }
    
    public double getEyeHeight(final boolean ignoreSneaking) {
        return this.player.getEyeHeight(ignoreSneaking);
    }
    
    public void sendRawMessage(final String message) {
        this.player.sendRawMessage(message);
    }
    
    public void sendMessage(final String message) {
        this.player.sendMessage(message);
    }
    
    public void sendMessage(final TextComponent message) {
        this.player.spigot().sendMessage((BaseComponent)message);
    }
    
    public void sendMessage(final String[] messages) {
        this.player.sendMessage(messages);
    }
    
    public String getDisplayName() {
        return this.player.getDisplayName();
    }
    
    public void setDisplayName(final String name) {
        this.player.setDisplayName(name);
    }
    
    public String getPlayerListName() {
        return this.player.getPlayerListName();
    }
    
    public void setPlayerListName(final String name) {
        this.player.setPlayerListName(name);
    }
    
    public EntityPlayer getHandle() {
        return this.player.getHandle();
    }
    
    public void kickPlayer(final String message) {
        this.player.kickPlayer(message);
    }
    
    public void setCompassTarget(final Location loc) {
        this.player.setCompassTarget(loc);
    }
    
    public Location getCompassTarget() {
        return this.player.getCompassTarget();
    }
    
    public void chat(final String msg) {
        this.player.chat(msg);
    }
    
    public boolean performCommand(final String command) {
        return this.player.performCommand(command);
    }
    
    public void playNote(final Location loc, final byte instrument, final byte note) {
        this.player.playNote(loc, instrument, note);
    }
    
    public void playNote(final Location loc, final Instrument instrument, final Note note) {
        this.player.playNote(loc, instrument, note);
    }
    
    public void playSound(final Location loc, final Sound sound, final float volume, final float pitch) {
        this.player.playSound(loc, sound, volume, pitch);
    }
    
    public void playSound(final Location loc, final String sound, final float volume, final float pitch) {
        this.player.playSound(loc, sound, volume, pitch);
    }
    
    public void playEffect(final Location loc, final Effect effect, final int data) {
        this.player.playEffect(loc, effect, data);
    }
    
    public <T> void playEffect(final Location loc, final Effect effect, final T data) {
        this.player.playEffect(loc, effect, (Object)data);
    }
    
    public void sendBlockChange(final Location loc, final Material material, final byte data) {
        this.player.sendBlockChange(loc, material, data);
    }
    
    public void sendBlockChange(final Location loc, final int material, final byte data) {
        this.player.sendBlockChange(loc, material, data);
    }
    
    public void sendSignChange(final Location loc, final String[] lines) {
        this.player.sendSignChange(loc, lines);
    }
    
    public boolean sendChunkChange(final Location loc, final int sx, final int sy, final int sz, final byte[] data) {
        return this.player.sendChunkChange(loc, sx, sy, sz, data);
    }
    
    public void sendMap(final MapView map) {
        this.player.sendMap(map);
    }
    
    public boolean teleport(final Location location, final PlayerTeleportEvent.TeleportCause cause) {
        return this.player.teleport(location, cause);
    }
    
    public void setSneaking(final boolean sneak) {
        this.player.setSneaking(sneak);
    }
    
    public boolean isSneaking() {
        return this.player.isSneaking();
    }
    
    public boolean isSprinting() {
        return this.player.isSprinting();
    }
    
    public void setSprinting(final boolean sprinting) {
        this.player.setSprinting(sprinting);
    }
    
    public void loadData() {
        this.player.loadData();
    }
    
    public void saveData() {
        this.player.saveData();
    }
    
    @Deprecated
    public void updateInventory() {
        this.player.updateInventory();
    }
    
    public void setSleepingIgnored(final boolean isSleeping) {
        this.player.setSleepingIgnored(isSleeping);
    }
    
    public boolean isSleepingIgnored() {
        return this.player.isSleepingIgnored();
    }
    
    public void awardAchievement(final Achievement achievement) {
        this.player.awardAchievement(achievement);
    }
    
    public void removeAchievement(final Achievement achievement) {
        this.player.removeAchievement(achievement);
    }
    
    public boolean hasAchievement(final Achievement achievement) {
        return this.player.hasAchievement(achievement);
    }
    
    public void incrementStatistic(final Statistic statistic) {
        this.player.incrementStatistic(statistic);
    }
    
    public void decrementStatistic(final Statistic statistic) {
        this.player.decrementStatistic(statistic);
    }
    
    public int getStatistic(final Statistic statistic) {
        return this.player.getStatistic(statistic);
    }
    
    public void incrementStatistic(final Statistic statistic, final int amount) {
        this.player.incrementStatistic(statistic, amount);
    }
    
    public void decrementStatistic(final Statistic statistic, final int amount) {
        this.player.decrementStatistic(statistic, amount);
    }
    
    public void setStatistic(final Statistic statistic, final int newValue) {
        this.player.setStatistic(statistic, newValue);
    }
    
    public void incrementStatistic(final Statistic statistic, final Material material) {
        this.player.incrementStatistic(statistic, material);
    }
    
    public void decrementStatistic(final Statistic statistic, final Material material) {
        this.player.decrementStatistic(statistic, material);
    }
    
    public int getStatistic(final Statistic statistic, final Material material) {
        return this.player.getStatistic(statistic, material);
    }
    
    public void incrementStatistic(final Statistic statistic, final Material material, final int amount) {
        this.player.incrementStatistic(statistic, material, amount);
    }
    
    public void decrementStatistic(final Statistic statistic, final Material material, final int amount) {
        this.player.decrementStatistic(statistic, material, amount);
    }
    
    public void setStatistic(final Statistic statistic, final Material material, final int newValue) {
        this.player.setStatistic(statistic, material, newValue);
    }
    
    public void incrementStatistic(final Statistic statistic, final EntityType entityType) {
        this.player.incrementStatistic(statistic, entityType);
    }
    
    public void decrementStatistic(final Statistic statistic, final EntityType entityType) {
        this.player.decrementStatistic(statistic, entityType);
    }
    
    public int getStatistic(final Statistic statistic, final EntityType entityType) {
        return this.player.getStatistic(statistic, entityType);
    }
    
    public void incrementStatistic(final Statistic statistic, final EntityType entityType, final int amount) {
        this.player.incrementStatistic(statistic, entityType, amount);
    }
    
    public void decrementStatistic(final Statistic statistic, final EntityType entityType, final int amount) {
        this.player.decrementStatistic(statistic, entityType, amount);
    }
    
    public void setStatistic(final Statistic statistic, final EntityType entityType, final int newValue) {
        this.player.setStatistic(statistic, entityType, newValue);
    }
    
    public void setPlayerTime(final long time, final boolean relative) {
        this.player.setPlayerTime(time, relative);
    }
    
    public long getPlayerTimeOffset() {
        return this.player.getPlayerTimeOffset();
    }
    
    public long getPlayerTime() {
        return this.player.getPlayerTime();
    }
    
    public boolean isPlayerTimeRelative() {
        return this.player.isPlayerTimeRelative();
    }
    
    public void resetPlayerTime() {
        this.player.resetPlayerTime();
    }
    
    public void setPlayerWeather(final WeatherType type) {
        this.player.setPlayerWeather(type);
    }
    
    public WeatherType getPlayerWeather() {
        return this.player.getPlayerWeather();
    }
    
    public void resetPlayerWeather() {
        this.player.resetPlayerWeather();
    }
    
    public boolean isBanned() {
        return this.player.isBanned();
    }
    
    public void setBanned(final boolean value) {
        this.player.setBanned(value);
    }
    
    public boolean isWhitelisted() {
        return this.player.isWhitelisted();
    }
    
    public void setWhitelisted(final boolean value) {
        this.player.setWhitelisted(value);
    }
    
    public void setGameMode(final GameMode mode) {
        this.player.setGameMode(mode);
    }
    
    public GameMode getGameMode() {
        return this.player.getGameMode();
    }
    
    public void giveExp(final int exp) {
        this.player.giveExp(exp);
    }
    
    public void giveExpLevels(final int levels) {
        this.player.giveExpLevels(levels);
    }
    
    public float getExp() {
        return this.player.getExp();
    }
    
    public void setExp(final float exp) {
        this.player.setExp(exp);
    }
    
    public int getLevel() {
        return this.player.getLevel();
    }
    
    public void setLevel(final int level) {
        this.player.setLevel(level);
    }
    
    public int getTotalExperience() {
        return this.player.getTotalExperience();
    }
    
    public void setTotalExperience(final int exp) {
        this.player.setTotalExperience(exp);
    }
    
    public float getExhaustion() {
        return this.player.getExhaustion();
    }
    
    public void setExhaustion(final float value) {
        this.player.setExhaustion(value);
    }
    
    public float getSaturation() {
        return this.player.getSaturation();
    }
    
    public void setSaturation(final float value) {
        this.player.setSaturation(value);
    }
    
    public int getFoodLevel() {
        return this.player.getFoodLevel();
    }
    
    public void setFoodLevel(final int value) {
        this.player.setFoodLevel(value);
    }
    
    public Location getBedSpawnLocation() {
        return this.player.getBedSpawnLocation();
    }
    
    public void setBedSpawnLocation(final Location location) {
        this.player.setBedSpawnLocation(location);
    }
    
    public void setBedSpawnLocation(final Location location, final boolean override) {
        this.player.setBedSpawnLocation(location, override);
    }
    
    public void hidePlayer(final Player player) {
        this.player.hidePlayer(player);
    }
    
    public void showPlayer(final Player player) {
        this.player.showPlayer(player);
    }
    
    public boolean canSee(final Player player) {
        return this.player.canSee(player);
    }
    
    public Map<String, Object> serialize() {
        return (Map<String, Object>)this.player.serialize();
    }
    
    public Player getPlayer() {
        return this.player.getPlayer();
    }
    
    @Override
    public String toString() {
        return this.player.toString();
    }
    
    @Override
    public int hashCode() {
        if (this.hash == 0 || this.hash == 485) {
            this.hash = 485 + ((this.getUniqueId() != null) ? this.getUniqueId().hashCode() : 0);
        }
        return this.hash;
    }
    
    public long getFirstPlayed() {
        return this.player.getFirstPlayed();
    }
    
    public long getLastPlayed() {
        return this.player.getLastPlayed();
    }
    
    public boolean hasPlayedBefore() {
        return this.player.hasPlayedBefore();
    }
    
    public boolean beginConversation(final Conversation conversation) {
        return this.player.beginConversation(conversation);
    }
    
    public void abandonConversation(final Conversation conversation) {
        this.player.abandonConversation(conversation);
    }
    
    public void abandonConversation(final Conversation conversation, final ConversationAbandonedEvent details) {
        this.player.abandonConversation(conversation, details);
    }
    
    public void acceptConversationInput(final String input) {
        this.player.acceptConversationInput(input);
    }
    
    public boolean isConversing() {
        return this.player.isConversing();
    }
    
    public void sendPluginMessage(final Plugin source, final String channel, final byte[] message) {
        this.player.sendPluginMessage(source, channel, message);
    }
    
    public void setTexturePack(final String url) {
        this.player.setTexturePack(url);
    }
    
    public void setResourcePack(final String url) {
        this.player.setResourcePack(url);
    }
    
    public Set<String> getListeningPluginChannels() {
        return (Set<String>)this.player.getListeningPluginChannels();
    }
    
    public EntityType getType() {
        return this.player.getType();
    }
    
    public boolean isFlying() {
        return this.player.isFlying();
    }
    
    public void setFlying(final boolean value) {
        this.player.setFlying(value);
    }
    
    public boolean getAllowFlight() {
        return this.player.getAllowFlight();
    }
    
    public void setAllowFlight(final boolean value) {
        this.player.setAllowFlight(value);
    }
    
    public int getNoDamageTicks() {
        return this.player.getNoDamageTicks();
    }
    
    public void setFlySpeed(final float value) {
        this.player.setFlySpeed(value);
    }
    
    public void setWalkSpeed(final float value) {
        this.player.setWalkSpeed(value);
    }
    
    public float getFlySpeed() {
        return this.player.getFlySpeed();
    }
    
    public float getWalkSpeed() {
        return this.player.getWalkSpeed();
    }
    
    public void setMaxHealth(final double amount) {
        this.player.setMaxHealth(amount);
    }
    
    public void resetMaxHealth() {
        this.player.resetMaxHealth();
    }
    
    public CraftScoreboard getScoreboard() {
        return this.player.getScoreboard();
    }
    
    public void setScoreboard(final Scoreboard scoreboard) {
        this.player.setScoreboard(scoreboard);
    }
    
    public void setHealthScale(final double value) {
        this.player.setHealthScale(value);
    }
    
    public double getHealthScale() {
        return this.player.getHealthScale();
    }
    
    public void setHealthScaled(final boolean scale) {
        this.player.setHealthScaled(scale);
    }
    
    public boolean isHealthScaled() {
        return this.player.isHealthScaled();
    }
    
    public String getName() {
        return this.player.getName();
    }
    
    public PlayerInventory getInventory() {
        return this.player.getInventory();
    }
    
    public EntityEquipment getEquipment() {
        return this.player.getEquipment();
    }
    
    public Inventory getEnderChest() {
        return this.player.getEnderChest();
    }
    
    public ItemStack getItemInHand() {
        return this.player.getItemInHand();
    }
    
    public void setItemInHand(final ItemStack item) {
        this.player.setItemInHand(item);
    }
    
    public ItemStack getItemOnCursor() {
        return this.player.getItemOnCursor();
    }
    
    public void setItemOnCursor(final ItemStack item) {
        this.player.setItemOnCursor(item);
    }
    
    public boolean isSleeping() {
        return this.player.isSleeping();
    }
    
    public int getSleepTicks() {
        return this.player.getSleepTicks();
    }
    
    public boolean isPermissionSet(final String name) {
        return this.player.isPermissionSet(name);
    }
    
    public boolean isPermissionSet(final Permission perm) {
        return this.player.isPermissionSet(perm);
    }
    
    public boolean hasPermission(final String name) {
        return this.player.hasPermission(name);
    }
    
    public boolean hasPermission(final Permission perm) {
        return this.player.hasPermission(perm);
    }
    
    public PermissionAttachment addAttachment(final Plugin plugin, final String name, final boolean value) {
        return this.player.addAttachment(plugin, name, value);
    }
    
    public PermissionAttachment addAttachment(final Plugin plugin) {
        return this.player.addAttachment(plugin);
    }
    
    public PermissionAttachment addAttachment(final Plugin plugin, final String name, final boolean value, final int ticks) {
        return this.player.addAttachment(plugin, name, value, ticks);
    }
    
    public PermissionAttachment addAttachment(final Plugin plugin, final int ticks) {
        return this.player.addAttachment(plugin, ticks);
    }
    
    public void removeAttachment(final PermissionAttachment attachment) {
        this.player.removeAttachment(attachment);
    }
    
    public void recalculatePermissions() {
        this.player.recalculatePermissions();
    }
    
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return (Set<PermissionAttachmentInfo>)this.player.getEffectivePermissions();
    }
    
    public InventoryView getOpenInventory() {
        return this.player.getOpenInventory();
    }
    
    public InventoryView openInventory(final Inventory inventory) {
        return this.player.openInventory(inventory);
    }
    
    public InventoryView openWorkbench(final Location location, final boolean force) {
        return this.player.openWorkbench(location, force);
    }
    
    public InventoryView openEnchanting(final Location location, final boolean force) {
        return this.player.openEnchanting(location, force);
    }
    
    public void openInventory(final InventoryView inventory) {
        this.player.openInventory(inventory);
    }
    
    public void closeInventory() {
        this.player.closeInventory();
    }
    
    public boolean isBlocking() {
        return this.player.isBlocking();
    }
    
    public int getExpToLevel() {
        return this.player.getExpToLevel();
    }
    
    public void setHealth(final double health) {
        this.player.setHealth(health);
    }
    
    public List<Block> getLineOfSight(final HashSet<Byte> transparent, final int maxDistance) {
        return (List<Block>)this.player.getLineOfSight(transparent, maxDistance);
    }
    
    public Block getTargetBlock(final HashSet<Byte> transparent, final int maxDistance) {
        return this.player.getTargetBlock(transparent, maxDistance);
    }
    
    public List<Block> getLastTwoTargetBlocks(final HashSet<Byte> transparent, final int maxDistance) {
        return (List<Block>)this.player.getLastTwoTargetBlocks(transparent, maxDistance);
    }
    
    public int getRemainingAir() {
        return this.player.getRemainingAir();
    }
    
    public void setRemainingAir(final int ticks) {
        this.player.setRemainingAir(ticks);
    }
    
    public int getMaximumAir() {
        return this.player.getMaximumAir();
    }
    
    public void setMaximumAir(final int ticks) {
        this.player.setMaximumAir(ticks);
    }
    
    public void damage(final double amount) {
        this.player.damage(amount);
    }
    
    public void damage(final double amount, final Entity source) {
        this.player.damage(amount, source);
    }
    
    public Location getEyeLocation() {
        return this.player.getEyeLocation();
    }
    
    public int getMaximumNoDamageTicks() {
        return this.player.getMaximumNoDamageTicks();
    }
    
    public void setMaximumNoDamageTicks(final int ticks) {
        this.player.setMaximumNoDamageTicks(ticks);
    }
    
    public void setLastDamage(final double damage) {
        this.player.setLastDamage(damage);
    }
    
    public void setNoDamageTicks(final int ticks) {
        this.player.setNoDamageTicks(ticks);
    }
    
    public Player getKiller() {
        return this.player.getKiller();
    }
    
    public boolean addPotionEffect(final PotionEffect effect) {
        return this.player.addPotionEffect(effect);
    }
    
    public boolean addPotionEffect(final PotionEffect effect, final boolean force) {
        return this.player.addPotionEffect(effect, force);
    }
    
    public boolean addPotionEffects(final Collection<PotionEffect> effects) {
        return this.player.addPotionEffects((Collection)effects);
    }
    
    public boolean hasPotionEffect(final PotionEffectType type) {
        return this.player.hasPotionEffect(type);
    }
    
    public void removePotionEffect(final PotionEffectType type) {
        this.player.removePotionEffect(type);
    }
    
    public Collection<PotionEffect> getActivePotionEffects() {
        return (Collection<PotionEffect>)this.player.getActivePotionEffects();
    }
    
    public <T extends Projectile> T launchProjectile(final Class<? extends T> projectile) {
        return (T)this.player.launchProjectile((Class)projectile);
    }
    
    public <T extends Projectile> T launchProjectile(final Class<? extends T> projectile, final Vector velocity) {
        return (T)this.player.launchProjectile((Class)projectile, velocity);
    }
    
    public boolean hasLineOfSight(final Entity other) {
        return this.player.hasLineOfSight(other);
    }
    
    public boolean getRemoveWhenFarAway() {
        return this.player.getRemoveWhenFarAway();
    }
    
    public void setRemoveWhenFarAway(final boolean remove) {
        this.player.setRemoveWhenFarAway(remove);
    }
    
    public void setCanPickupItems(final boolean pickup) {
        this.player.setCanPickupItems(pickup);
    }
    
    public boolean getCanPickupItems() {
        return this.player.getCanPickupItems();
    }
    
    public boolean isLeashed() {
        return this.player.isLeashed();
    }
    
    public Entity getLeashHolder() throws IllegalStateException {
        return this.player.getLeashHolder();
    }
    
    public boolean setLeashHolder(final Entity holder) {
        return this.player.setLeashHolder(holder);
    }
    
    public Location getLocation() {
        return this.player.getLocation();
    }
    
    public Location getLocation(final Location loc) {
        return this.player.getLocation(loc);
    }
    
    public Vector getVelocity() {
        return this.player.getVelocity();
    }
    
    public void setVelocity(final Vector vel) {
        this.player.setVelocity(vel);
    }
    
    public boolean isOnGround() {
        return this.player.isOnGround();
    }
    
    public World getWorld() {
        return this.player.getWorld();
    }
    
    public boolean teleport(final Location location) {
        return this.player.teleport(location);
    }
    
    public boolean teleport(final Entity destination) {
        return this.player.teleport(destination);
    }
    
    public boolean teleport(final Entity destination, final PlayerTeleportEvent.TeleportCause cause) {
        return this.player.teleport(destination, cause);
    }
    
    public List<Entity> getNearbyEntities(final double x, final double y, final double z) {
        return (List<Entity>)this.player.getNearbyEntities(x, y, z);
    }
    
    public int getEntityId() {
        return this.player.getEntityId();
    }
    
    public int getFireTicks() {
        return this.player.getFireTicks();
    }
    
    public int getMaxFireTicks() {
        return this.player.getMaxFireTicks();
    }
    
    public void setFireTicks(final int ticks) {
        this.player.setFireTicks(ticks);
    }
    
    public void remove() {
        this.player.remove();
    }
    
    public boolean isDead() {
        return this.player.isDead();
    }
    
    public boolean isValid() {
        return this.player != null && this.player.isValid();
    }
    
    public Server getServer() {
        return this.player.getServer();
    }
    
    public Entity getPassenger() {
        return this.player.getPassenger();
    }
    
    public boolean setPassenger(final Entity passenger) {
        return this.player.setPassenger(passenger);
    }
    
    public boolean isEmpty() {
        return this.player.isEmpty();
    }
    
    public boolean eject() {
        return this.player.eject();
    }
    
    public float getFallDistance() {
        return this.player.getFallDistance();
    }
    
    public void setFallDistance(final float distance) {
        this.player.setFallDistance(distance);
    }
    
    public void setLastDamageCause(final EntityDamageEvent event) {
        this.player.setLastDamageCause(event);
    }
    
    public EntityDamageEvent getLastDamageCause() {
        return this.player.getLastDamageCause();
    }
    
    public UUID getUniqueId() {
        return this.player.getUniqueId();
    }
    
    public int getTicksLived() {
        return this.player.getTicksLived();
    }
    
    public void setTicksLived(final int value) {
        this.player.setTicksLived(value);
    }
    
    public void playEffect(final EntityEffect type) {
        this.player.playEffect(type);
    }
    
    public boolean isInsideVehicle() {
        return this.player.isInsideVehicle();
    }
    
    public boolean leaveVehicle() {
        return this.player.leaveVehicle();
    }
    
    public Entity getVehicle() {
        return this.player.getVehicle();
    }
    
    public void setCustomName(final String name) {
        this.player.setCustomName(name);
    }
    
    public String getCustomName() {
        return this.player.getCustomName();
    }
    
    public void setCustomNameVisible(final boolean flag) {
        this.player.setCustomNameVisible(flag);
    }
    
    public boolean isCustomNameVisible() {
        return this.player.isCustomNameVisible();
    }
    
    @Deprecated
    public Arrow shootArrow() {
        return this.player.shootArrow();
    }
    
    @Deprecated
    public Egg throwEgg() {
        return this.player.throwEgg();
    }
    
    @Deprecated
    public Snowball throwSnowball() {
        return this.player.throwSnowball();
    }
    
    @Deprecated
    public Player.Spigot spigot() {
        return this.player.spigot();
    }
    
    public void setLastDamage(final int arg0) {
        this.setLastDamage(arg0);
    }
    
    public void damage(final int arg0) {
        this.player.damage((double)arg0);
    }
    
    public void damage(final int arg0, final Entity arg1) {
        this.player.damage((double)arg0, arg1);
    }
    
    public void setHealth(final int arg0) {
        this.player.setHealth((double)arg0);
    }
    
    public void setMaxHealth(final int arg0) {
        this.player.setMaxHealth((double)arg0);
    }
    
    public double getLastDamage() {
        return this.player.getHealth();
    }
    
    public double getHealth() {
        return this.player.getHealth();
    }
    
    public double getMaxHealth() {
        return this.player.getMaxHealth();
    }
}
