package net.intelie.challenges.implementations;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import net.intelie.challenges.interfaces.EventIterator;
import net.intelie.challenges.interfaces.EventStore;
import net.intelie.challenges.models.Event;

public class EventStoreImpl  implements EventStore{

    /**
      CopyOnWriteArraylist should be the approach because 
      We can iterate over safely, even when simultaneous change is happening.
     */
    CopyOnWriteArrayList<Event> eventList = new CopyOnWriteArrayList<Event>();
    
    
    @Override
    public void insert(Event event) {
        this.eventList.add(event);
    }

    @Override
    public void removeAll(String type) {
        this.eventList.removeIf(x -> x.type() == type);
    }

    /**
      Converting the List to a stream makes code more safe because we do not need to 
      create other variables in memory to persist change 
     */
    @Override
    public EventIterator query(String type, long startTime, long endTime) {
        List<Event> list = eventList
             .stream()
             .filter(event -> event.type().equals(type))
             .filter(event -> event.timestamp() > startTime && event.timestamp() < endTime)
             .collect(Collectors.toList());
        if (list.isEmpty()) {
            return null;
        } else {
            return new EventIteratorImpl(list);
        }
        

    
    }
    
}
