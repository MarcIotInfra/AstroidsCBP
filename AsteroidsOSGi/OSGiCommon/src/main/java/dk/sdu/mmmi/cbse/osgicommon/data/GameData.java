package dk.sdu.mmmi.cbse.osgicommon.data;

import dk.sdu.mmmi.cbse.osgicommon.events.Event;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameData {

    private float delta;
    private int displayWidth;
    private int displayHeight;
    private int difficulty = 1;
    private final GameKeys keys = new GameKeys();
    private List<Event> events = new CopyOnWriteArrayList<>();

    public void addEvent(Event e) {
        events.add(e);
    }

    public void removeEvent(Event e) {
        events.remove(e);
    }

    public List<Event> getEvents() {
        return events;
    }

    public GameKeys getKeys() {
        return keys;
    }

    public void setDelta(float delta) {
        this.delta = delta;
    }

    public float getDelta() {
        return delta;
    }

    public void setDisplayWidth(int width) {
        this.displayWidth = width;
    }

    public int getDisplayWidth() {
        return displayWidth;
    }

    public void setDisplayHeight(int height) {
        this.displayHeight = height;
    }

    public int getDisplayHeight() {
        return displayHeight;
    }
    
    public int getDifficulty(){
        return difficulty;
    }
    
    public void increaseDifficulty(){
        this.difficulty ++;
    }
    
    public void reduceDifficultry(){
        this.difficulty --;
    }
    
    public void setDifficulty(int difficulty){
        this.difficulty = difficulty;
    }

    
    
    public <E extends Event> List<Event> getEvents(Class<E> type, Class<?> EntityClass) {
        List<Event> r = new ArrayList();
        for (Event event : events) {
            if (event.getClass().equals(type) && event.getSource().getClass().equals(EntityClass)) {
                r.add(event);
            }
        }

        return r;
    }
    
    public <E extends Event> List<Event> getEvents(Class<E> type) {
        List<Event> r = new ArrayList();
        for (Event event : events) {
            if (event.getClass().equals(type)) {
                r.add(event);
            }
        }

        return r;
    }
}
