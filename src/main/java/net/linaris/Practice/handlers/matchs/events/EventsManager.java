package net.linaris.Practice.handlers.matchs.events;

import java.util.*;

public class EventsManager
{
    private List<Event> events;
    private Event event;
    
    public EventsManager() {
        this.events = new ArrayList<Event>();
    }
    
    public void addEvent(final Event event) {
        this.events.add(event);
    }
    
    public void update() {
        if (this.event == null) {
            return;
        }
        if (this.event.isFinish()) {
            this.event.onRun();
            this.setNextEvent();
            if (this.event != null) {
                this.event.time();
            }
            return;
        }
        this.event.time();
        this.event.onUpdate();
    }
    
    public Event setNextEvent() {
        if (!this.hasNextEvent()) {
            return this.event = null;
        }
        this.event = this.events.iterator().next();
        this.events.remove(this.event);
        return this.event;
    }
    
    public boolean hasNextEvent() {
        return this.events.iterator().hasNext();
    }
    
    public List<Event> getEvents() {
        return this.events;
    }
    
    public Event getEvent() {
        return this.event;
    }
}
