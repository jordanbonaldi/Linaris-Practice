package net.linaris.Practice.utils;

public class TimeUtils
{
    public static String getTime(final long time) {
        final int seconds = (int)(time / 1000L) % 60;
        final int minutes = (int)(time / 60000L % 60L);
        final int hours = (int)(time / 3600000L % 24L);
        final String result = ((hours > 0) ? (" " + hours + " heure" + ((hours > 1) ? "s" : "")) : "") + ((minutes > 0) ? (" " + minutes + " minute" + ((minutes > 1) ? "s" : "")) : "") + " " + seconds + " seconde" + ((seconds > 1) ? "s" : "");
        return result.trim();
    }
}
