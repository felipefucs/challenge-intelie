package net.intelie.challenges;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

import net.intelie.challenges.implementations.EventIteratorImpl;
import net.intelie.challenges.interfaces.EventStore;
import net.intelie.challenges.models.Event;

public class EventIteratorImplTest {
    
    EventIteratorImpl eventIterator;
    EventStore eventStore;
    Event event1;
    Event event2;

    @Before
    public void init() {
        Random randomizer = new Random();
        event1 = new Event("1", 1l);
        event2 = new Event("2", randomizer.nextLong());
    };

    /**
     Asking for current Event without move next should raise an IllegalState Exception
     */
    @Test(expected = IllegalStateException.class)
    public void currentWithNomoveNextShouldThrowIllegalState() {
        List<Event> eventList = new ArrayList<>();
        eventList.add(event1);
        eventIterator =  new EventIteratorImpl(eventList);
        eventIterator.current();
    }

    /**
      Current Event Null Should Raise an Exeption
     */
    @Test(expected = IllegalStateException.class)
    public void currentNullEventShouldRaiseIllegalStateException() {
        List<Event> eventList = new ArrayList<>();
        eventList.add(event1);
        eventIterator = new EventIteratorImpl(eventList);
        eventIterator.moveNext();
        eventIterator.moveNext();
        eventIterator.current();
    }

    /**
      In a list with only one event move next shuld return false
     */
    @Test
    public void noSecondEventAfterMoveNextIsFalse() {
        List<Event> eventList = new ArrayList<>();
        eventList.add(event1);
        eventIterator = new EventIteratorImpl(eventList);
        eventIterator.moveNext();
        eventIterator.moveNext();
        assertEquals(false, eventIterator.moveNext());
    }

     /**
      Null list should not be able to move next
     */
    @Test
    public void NullListShouldNotHaveNext() {
        eventIterator = new EventIteratorImpl(null);
        assertEquals(false, eventIterator.moveNext());
    };  

     /**
      Correctly return curren event after it was added
     */
    @Test
    public void addingOneEventShouldReturnItAfterMoveNext() {
        List<Event> eventList = new ArrayList<>();
        eventList.add(event1);
        eventIterator = new EventIteratorImpl(eventList);
        eventIterator.moveNext();
        Event currentEvent = eventIterator.current();
        assertEquals(event1.timestamp(), currentEvent.timestamp());
        assertEquals(event1.type(), currentEvent.type());
    };


     /**
      Checking consistency for consecutive move next 
     */
    @Test
    public void CorrectlyCurrentEventAfterConsecutiveMoveNext() {
        List<Event> eventList = new ArrayList<>();
        eventList.add(event1);
        eventList.add(event2);
        eventIterator = new EventIteratorImpl(eventList);
        eventIterator.moveNext();
        Event currentEvent = eventIterator.current();
        assertEquals(event1.timestamp(), currentEvent.timestamp());
        assertEquals(event1.type(), currentEvent.type());
        eventIterator.moveNext();
        Event currentEvent2 = eventIterator.current();
        assertEquals(event2.timestamp(), currentEvent2.timestamp());
        assertEquals(event2.type(), currentEvent2.type());
    }

    /**
      No current Event after only existed was removed
     */
    @Test(expected = IllegalStateException.class)
    public void CurrentAfterOnlyEventRemovedShouldReturnIllegalStateException() {
        List<Event> eventList = new ArrayList<>();
        eventList.add(event1);
        eventIterator = new EventIteratorImpl(eventList);
        eventIterator.moveNext();
        eventIterator.remove();
        eventIterator.current();
    };

     /**
       Remove non Existent key should raise exception
     */
    @Test(expected = IllegalStateException.class)
    public void RemoveNonExistentKeyShouldThrowIllegalStateException() {
        eventIterator = new EventIteratorImpl(null);
        eventIterator.moveNext();
        eventIterator.remove();
    };

     /**
      Current Event after iterator was closed throws exception
     */
    @Test(expected = IllegalStateException.class)
    public void currentAfterClosedThrowIllegalStateException() throws Exception {
        eventIterator = new EventIteratorImpl(null);
        eventIterator.close();
        assertEquals(false, eventIterator.moveNext());
        eventIterator.current();
    }

    /**
      Remove after closed should raise exception
     */
    @Test(expected = IllegalStateException.class)
    public void removeAfterClosedShouldthrowException() throws Exception {
        eventIterator = new EventIteratorImpl(null);
        eventIterator.close();
        assertEquals(false, eventIterator.moveNext());
        eventIterator.remove();
    }



}
