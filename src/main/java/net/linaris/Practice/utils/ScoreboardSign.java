package net.linaris.Practice.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.IScoreboardCriteria;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardDisplayObjective;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardObjective;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardScore;
import net.minecraft.server.v1_8_R3.PlayerConnection;


public class ScoreboardSign {
	

	static Map<String, ScoreboardSign>	assigned	= new HashMap<String, ScoreboardSign>();
	public static ScoreboardSign get(Player player) {
		return assigned.get(player.getName());
	}
	public void assign(Player player) {
		ScoreboardSign old = get(player);
		if (old != null) old.destroy();
		assigned.put(player.getName(), this);
		create();
	}
	
	
	
    private boolean created = false;
    private final String[] lines = new String[100];
    private final Player player;
    private String objectiveName;
    
    public ScoreboardSign(Player player, String objectiveName) {
        this.player = player;
        this.objectiveName = objectiveName;
        assign(player);
    }

    public void create() {
        if (created) return;

        PlayerConnection player = getPlayer();
        player.sendPacket(createObjectivePacket(0, objectiveName));
        player.sendPacket(setObjectiveSlot());
        int i = 0;
        while (i < lines.length)
            sendLine(i++);

        created = true;
    }

    public void destroy() {
        if (!created)
            return;

        getPlayer().sendPacket(createObjectivePacket(1, null));

        created = false;
        
		assigned.remove(player.getName());
    }

    private PlayerConnection getPlayer() {
        return ((CraftPlayer) player).getHandle().playerConnection;
    }

    private void sendLine(int line) {
        if (!created)
            return;

        int score = line;
        String val = lines[line];
        getPlayer().sendPacket(sendScore(val, score));
    }

    public void setObjectiveName(String name) {
        this.objectiveName = name;
        if (created)
            getPlayer().sendPacket(createObjectivePacket(2, name));
    }

    public void setLine(int line, String value) {
        String oldLine = getLine(line);
        if (oldLine != null && created)
            getPlayer().sendPacket(removeLine(oldLine));

        lines[line] = value;
        sendLine(line);
    }

    public void removeLine(int line) {
        String oldLine = getLine(line);
        if (oldLine != null && created)
            getPlayer().sendPacket(removeLine(oldLine));

        lines[line] = null;
    }

    public String getLine(int line) {
        return lines[line];
    }
    
    public int getLine(String line) {
        for (int i = 0 ; i < lines.length ; i++) 
        	if (lines[i] != null && lines[i].equals(line)) return i;
        return -1;
        
    }

    
    private PacketPlayOutScoreboardObjective createObjectivePacket(int mode, String displayName) {
        PacketPlayOutScoreboardObjective packet = new PacketPlayOutScoreboardObjective();
        try {
            // Nom de l'objectif
            Field name = packet.getClass().getDeclaredField("a");
            name.setAccessible(true);
            name.set(packet, player.getName());

            // Mode
            // 0 : cr�er
            // 1 : Supprimer
            // 2 : Mettre � jour
            Field modeField = packet.getClass().getDeclaredField("d");
            modeField.setAccessible(true);
            modeField.set(packet, mode);

            if (mode == 0 || mode == 2) {
                Field displayNameField = packet.getClass().getDeclaredField("b");
                displayNameField.setAccessible(true);
                displayNameField.set(packet, displayName);

                Field display = packet.getClass().getDeclaredField("c");
                display.setAccessible(true);
                display.set(packet, IScoreboardCriteria.EnumScoreboardHealthDisplay.INTEGER);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return packet;
    }

    private PacketPlayOutScoreboardDisplayObjective setObjectiveSlot() {
        PacketPlayOutScoreboardDisplayObjective packet = new PacketPlayOutScoreboardDisplayObjective();
        try {
            // Slot de l'objectif
            Field position = packet.getClass().getDeclaredField("a");
            position.setAccessible(true);
            position.set(packet, 1); // SideBar

            Field name = packet.getClass().getDeclaredField("b");
            name.setAccessible(true);
            name.set(packet, player.getName());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return packet;
    }

    private PacketPlayOutScoreboardScore sendScore(String line, int score) {
        PacketPlayOutScoreboardScore packet = new PacketPlayOutScoreboardScore(line);
        try {
            Field name = packet.getClass().getDeclaredField("b");
            name.setAccessible(true);
            name.set(packet, player.getName());

            Field scoreField = packet.getClass().getDeclaredField("c");
            scoreField.setAccessible(true);
            scoreField.set(packet, score); // SideBar

            Field action = packet.getClass().getDeclaredField("d");
            action.setAccessible(true);
            action.set(packet, PacketPlayOutScoreboardScore.EnumScoreboardAction.CHANGE);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return packet;
    }

    private PacketPlayOutScoreboardScore removeLine(String line) {
        return new PacketPlayOutScoreboardScore(line);
    }
}