package net.linaris.Practice.utils;

import org.bukkit.plugin.*;
import org.bukkit.scheduler.*;

import net.linaris.Practice.*;

import java.util.*;
import org.bukkit.*;

public class TaskManager
{
    private static HashMap<String, Integer> taskList;
    public static BukkitScheduler scheduler;
    static Plugin plugin;
    
    public BukkitTask getTask(final String taskName) {
        final BukkitTask task = null;
        final int id = getTaskId(taskName);
        if (id > 0) {
            for (final BukkitTask pendingTask : TaskManager.scheduler.getPendingTasks()) {
                if (pendingTask.getTaskId() == id) {
                    return task;
                }
            }
        }
        return null;
    }
    
    public static String getTaskNameById(final int id) {
        for (final Map.Entry<String, Integer> entry : TaskManager.taskList.entrySet()) {
            if (entry.getValue() == id) {
                return entry.getKey();
            }
        }
        return null;
    }
    
    public static boolean taskExist(final String taskName) {
        return TaskManager.taskList.containsKey(taskName);
    }
    
    public static int getTaskId(final String taskName) {
        if (taskExist(taskName)) {
            return TaskManager.taskList.get(taskName);
        }
        return 0;
    }
    
    public static void cancelAllTask() {
        for (final int taskId : TaskManager.taskList.values()) {
            TaskManager.scheduler.cancelTask(taskId);
        }
    }
    
    public static boolean cancelTaskByName(final String taskName) {
        if (taskExist(taskName)) {
            final int taskId = getTaskId(taskName);
            TaskManager.taskList.remove(taskName);
            TaskManager.scheduler.cancelTask(taskId);
            return true;
        }
        return false;
    }
    
    public static void cancelTaskById(final int id) {
        TaskManager.scheduler.cancelTask(id);
    }
    
    public static void removeTaskByName(final String taskName) {
        TaskManager.taskList.remove(taskName);
    }
    
    public static void checkIfExist(final String taskName) {
        if (taskExist(taskName)) {
            cancelTaskByName(taskName);
        }
    }
    
    public static BukkitTask runTask(final Runnable runnable) {
        return TaskManager.scheduler.runTask(TaskManager.plugin, runnable);
    }
    
    public static BukkitTask runTaskLater(final Runnable runnable, final int tick) {
        return TaskManager.scheduler.runTaskLater(TaskManager.plugin, runnable, (long)tick);
    }
    
    public static BukkitTask runTaskLater(final String taskName, final Runnable task, final int duration) {
        final BukkitTask bukkitTask = TaskManager.scheduler.runTaskLater(TaskManager.plugin, task, (long)duration);
        final int id = bukkitTask.getTaskId();
        addTask(taskName, id);
        runTaskLater(new Runnable() {
            @Override
            public void run() {
                if (TaskManager.taskList.get(taskName) != null && TaskManager.taskList.get(taskName) == id) {
                    TaskManager.taskList.remove(taskName);
                }
            }
        }, duration);
        return bukkitTask;
    }
    
    public static void addTask(final String name, final int id) {
        TaskManager.taskList.put(name, id);
    }
    
    public static BukkitTask scheduleSyncRepeatingTask(final String taskName, final Runnable runnable, final int delay, final int refresh) {
        cancelTaskByName(taskName);
        final BukkitTask task = TaskManager.scheduler.runTaskTimer(TaskManager.plugin, runnable, (long)delay, (long)refresh);
        TaskManager.taskList.put(taskName, task.getTaskId());
        return task;
    }
    
    public static String getTaskName(final String string) {
        String taskName;
        for (taskName = string + "_" + new Random().nextInt(99999); taskExist(taskName); taskName = string + "_" + new Random().nextInt(99999)) {}
        return taskName;
    }
    
    static {
        TaskManager.taskList = new HashMap<String, Integer>();
        TaskManager.scheduler = Bukkit.getScheduler();
        TaskManager.plugin = (Plugin)Practice.getInstance();
    }
}
