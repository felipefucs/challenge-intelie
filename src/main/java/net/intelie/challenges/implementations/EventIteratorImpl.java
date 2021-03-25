package net.intelie.challenges.implementations;

import net.intelie.challenges.models.Event;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import net.intelie.challenges.interfaces.EventIterator;

public class EventIteratorImpl implements EventIterator {

    private List<Event> eventList;
    private Event selectedEvent;

    /**
      The best solution for incrementing in a "thread safe way" as we dont need to use synchronization
      as it compromises performance because threads are blocked
     */
    private final AtomicInteger index = new AtomicInteger(-1);

    public EventIteratorImpl(List<Event> eventList) {
        this.eventList = eventList;
    }

    @Override
    public void close() throws Exception {
        eventList = null;

    }

    @Override
    public boolean moveNext() {
        selectedEvent = null;
        if (eventList == null || eventList.isEmpty()) {
            return false;
        } else {
            final int nextIndex = index.incrementAndGet();
            if (nextIndex < eventList.size()) {
                selectedEvent = eventList.get(nextIndex);
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public Event current() {
        if (selectedEvent == null) {
            throw new IllegalStateException();
        }
        return this.selectedEvent;
    }

    @Override
    public void remove() {
        if (eventList != null ){
            if(!eventList.isEmpty()) {
                eventList.remove(index.get());
                this.selectedEvent = null;
            }
        }else {
            throw new IllegalStateException();
        }

    }

}
