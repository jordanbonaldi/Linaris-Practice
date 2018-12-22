package net.linaris.Practice.utils;

import org.bukkit.Bukkit;

import net.linaris.Practice.database.*;

public class PlayerUtils
{
    public static String getLastPlayerName(final Long id) {
        return DatabaseConnector.getUsersDao().get(id, new String[0]).getLastName();
    }
}
