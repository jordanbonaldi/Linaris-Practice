package net.linaris.Practice.handlers.matchs.events;

public abstract class Event
{
    private String name;
    private int time;
    
    public Event(final String name, final int time) {
        this.name = name;
        this.time = time + 1;
    }
    
    public void time() {
        --this.time;
    }
    
    public boolean isFinish() {
        return this.time <= 1;
    }
    
    public String getTimeFormat() {
        final int minutes = this.time / 60;
        final int seconds = this.time % 60;
        return (minutes == 0) ? ("\ufffdc" + seconds + "\ufffd7s") : ("\ufffdc" + minutes + "\ufffd7min\ufffdc" + String.format("%02d", seconds));
    }
    
    public abstract void onRun();
    
    public abstract void onUpdate();
    
    public String getName() {
        return this.name;
    }
    
    public int getTime() {
        return this.time;
    }
}
