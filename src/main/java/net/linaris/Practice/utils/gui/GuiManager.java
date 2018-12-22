package net.linaris.Practice.utils.gui;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import net.linaris.Practice.Practice;
import net.linaris.Practice.handlers.players.OmegaPlayer;

@SuppressWarnings("rawtypes")
public class GuiManager
{
    static HashMap<String, Class> openGuis;
    
    public static GuiScreen openGui(final GuiScreen gui) {
        openPlayer(gui.getPlayer(), gui.getClass());
        if (gui.isUpdate()) {
            new GuiTask(Practice.getInstance(), gui.getPlayer(), gui).runTaskTimer((Plugin)Practice.getInstance(), 0L, 20L);
        }
        else {
            gui.open();
        }
        return gui;
    }
    
    public static void openPlayer(final OmegaPlayer p, final Class<? extends GuiScreen> gui) {
        if (GuiManager.openGuis.containsKey(p.getName())) {
            GuiManager.openGuis.remove(p.getName());
            GuiManager.openGuis.put(p.getName(), gui);
        }
        else {
            GuiManager.openGuis.put(p.getName(), gui);
        }
    }
    
    public static void closePlayer(final Player p) {
        if (GuiManager.openGuis.containsKey(p.getName())) {
            GuiManager.openGuis.remove(p.getName());
        }
    }
    
    public static boolean isPlayer(final Player p) {
        return GuiManager.openGuis.containsKey(p.getName());
    }
    
    public static boolean isOpened(final Class<?> clas) {
        for (final Class<?> cla : GuiManager.openGuis.values()) {
            if (cla.equals(clas)) {
                return true;
            }
        }
        return false;
    }
    
    static {
        GuiManager.openGuis = new HashMap<String, Class>();
    }
}
