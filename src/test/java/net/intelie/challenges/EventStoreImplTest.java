package net.intelie.challenges;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import net.intelie.challenges.implementations.EventStoreImpl;
import net.intelie.challenges.interfaces.EventIterator;
import net.intelie.challenges.interfaces.EventStore;
import net.intelie.challenges.models.Event;

public class EventStoreImplTest {

    EventStore eventStore;
    EventIterator eventIterator;


    @Before
    public void initialize() {
        eventStore = new EventStoreImpl();
    }

    /**
      A query applied to an empty event store should return null
     */
    @Test
    public void emptyEventStoreShouldReturnNull() {
    assertEquals(null, eventStore.query("type", 1l, 3l));
    };

    /**
      Filtering queries test
     */
    @Test()
    public void filteringQueriesForValuesBetweenZeroAndTwo() {
        String type = "a";
        Event event1 = new Event(type, 1l);
        Event event2 = new Event(type, 2l);
        Event event3 = new Event(type, 3l);
        eventStore.insert(event1);
        eventStore.insert(event2);
        eventStore.insert(event3);
        eventIterator = eventStore.query(type, 1l, 3l);
        eventIterator.moveNext();
        assertEquals(event2, eventIterator.current());
    }

    /**
      Removing and maintaining certain type of events
     */
    @Test
    public void removeEventOfTypeShouldMaintainOthers() {
      String removeType = "a";
      String maintainType = "b";
      Event event1 = new Event(removeType, 1l);
      Event event2 = new Event(removeType, 2l);
      Event event3 = new Event(maintainType, 2l);
      eventStore.insert(event1);
      eventStore.insert(event2);
      eventStore.insert(event3);
      eventStore.removeAll(removeType);
      eventIterator = eventStore.query(removeType, 0l, 2l);
      assertEquals(null, eventIterator);
      eventIterator = eventStore.query(maintainType, 1l, 3l);
      eventIterator.moveNext();
      assertEquals(event3, eventIterator.current());
    }

    @Test
    public void OnelistShouldExistAfterEventsAdded() {
    String type = "a";
    Event event1 = new Event(type, 1l);
    Event event2 = new Event(type, 2l);
    eventStore.insert(event1);    
    eventStore.insert(event2);
    EventIterator outputIterator = eventStore.query(type, 0l, 10l);    
    outputIterator.moveNext();
    assertEquals(event1, outputIterator.current());
    outputIterator.moveNext();
    assertEquals(event2, outputIterator.current());
  };









}