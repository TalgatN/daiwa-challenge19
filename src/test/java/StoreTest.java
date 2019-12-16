import challenge.repository.InMemoryHistoryStore;
import challenge.repository.Store;
import challenge.service.DataStoreService;
import challenge.service.DataStoreServiceImpl;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class StoreTest {

    @Test
    public void validateInput() throws Exception {
        Store store = new InMemoryHistoryStore();
        DataStoreService storeService = new DataStoreServiceImpl(store);

        assertEquals("OK 1.5", storeService.create(0, 100, "1.5"));
    }

    @Test(expected = Exception.class)
    public void validateInputWrongIDRangeException() throws Exception {
        Store store = new InMemoryHistoryStore();
        DataStoreService storeService = new DataStoreServiceImpl(store);

        assertEquals("OK 1.5", storeService.create(-1, 100, "1.5"));
    }

    @Test(expected = Exception.class)
    public void validateInputWrongTimeStampRangeException() throws Exception {
        Store store = new InMemoryHistoryStore();
        DataStoreService storeService = new DataStoreServiceImpl(store);

        assertEquals("OK 1.5", storeService.create(1, -100, "1.5"));
    }

    @Test(expected = Exception.class)
    public void validateInputWrongObservationException() throws Exception {
        Store store = new InMemoryHistoryStore();
        DataStoreService storeService = new DataStoreServiceImpl(store);

        assertEquals("OK 1.5", storeService.create(-1, 100, "1  .5"));
    }

    @Test
    public void interactionExample() throws Exception {
        Store store = new InMemoryHistoryStore();
        DataStoreService storeService = new DataStoreServiceImpl(store);

        assertEquals("OK 1.5", storeService.create(0, 100, "1.5"));
        assertEquals("OK 1.5", storeService.update(0, 105, "1.6"));
        assertEquals("OK 1.5", storeService.get(0, 100));
        assertEquals("OK 1.6", storeService.get(0, 110));
        assertEquals("OK 105 1.6", storeService.latest(0));

        assertEquals("OK 2.5", storeService.create(1, 110, "2.5"));
        assertEquals("OK 2.5", storeService.update(1, 115, "2.4"));
        assertEquals("OK 2.4", storeService.update(1, 120, "2.3"));
        assertEquals("OK 2.3", storeService.update(1, 125, "2.2"));
        assertEquals("OK 125 2.2", storeService.latest(1));
        assertEquals("OK 2.3", storeService.get(1, 120));
        assertEquals("OK 2.3", storeService.update(1, 120, "2.35"));
        assertEquals("OK 2.35", storeService.get(1, 122));
        assertEquals("OK 2.35", storeService.delete(1, 122));
        assertEquals("OK 2.35", storeService.get(1, 125));
        assertEquals("OK 2.35", storeService.delete(1, null));

    }

    @Test
    public void createSuccessful() throws Exception {
        Store store = new InMemoryHistoryStore();
        DataStoreService storeService = new DataStoreServiceImpl(store);
        assertEquals("OK 21", storeService.create(1, 101, "21"));
    }

    @Test(expected = Exception.class)
    public void createFailsOnDuplicate() throws Exception {
        Store store = new InMemoryHistoryStore();
        DataStoreService storeService = new DataStoreServiceImpl(store);
        storeService.create(1, 101, "21");
        storeService.create(1, 105, "32");
    }

    @Test(expected = Exception.class)
    public void createIDOutOfRange() throws Exception {
        Store store = new InMemoryHistoryStore();
        DataStoreService storeService = new DataStoreServiceImpl(store);
        storeService.create(-21, 110, "33");
    }

    @Test(expected = Exception.class)
    public void createTimeStampOutOfRange() throws Exception {
        Store store = new InMemoryHistoryStore();
        DataStoreService storeService = new DataStoreServiceImpl(store);
        storeService.create(1, -110, "35");
    }

    @Test
    public void update() throws Exception {
        Store store = new InMemoryHistoryStore();
        DataStoreService storeService = new DataStoreServiceImpl(store);
        storeService.create(1, 120, "45");

        assertEquals("OK 45", storeService.update(1, 121, "47"));
    }

    @Test(expected = Exception.class)
    public void updateFails() throws Exception {
        Store store = new InMemoryHistoryStore();
        DataStoreService storeService = new DataStoreServiceImpl(store);

        storeService.update(1, 125, "51");
    }

    @Test
    public void deleteID() throws Exception {
        Store store = new InMemoryHistoryStore();
        DataStoreService storeService = new DataStoreServiceImpl(store);
        storeService.create(1, 120, "45");
        storeService.update(1, 125, "26");

        assertEquals("OK 26", storeService.delete(1, null));
    }

    @Test(expected = Exception.class)
    public void deleteIDFails() throws Exception {
        Store store = new InMemoryHistoryStore();
        DataStoreService storeService = new DataStoreServiceImpl(store);
        storeService.create(1, 120, "45");
        storeService.update(1, 125, "26");

        storeService.delete(2, null);
    }

    @Test
    public void deleteIdAndTimeStamp() throws Exception {
        Store store = new InMemoryHistoryStore();
        DataStoreService storeService = new DataStoreServiceImpl(store);
        storeService.create(1, 120, "45");
        storeService.update(1, 125, "26");

        assertEquals("OK 26", storeService.delete(1, 125));
    }

    @Test
    public void deleteIdAndTimeStampWithAllEntriesForward() throws Exception {
        Store store = new InMemoryHistoryStore();
        DataStoreService storeService = new DataStoreServiceImpl(store);
        storeService.create(1, 120, "45");
        storeService.update(1, 123, "50");
        storeService.update(1, 125, "26");
        storeService.update(1, 130, "30");
        storeService.update(1, 140, "50");

        assertEquals("OK 26", storeService.delete(1, 125));
//        assertThat(store.getStoreList().get(1).getHistory().size(), is(2));
    }

    @Test()
    public void deleteIdTimeStampMOreThanLatest() throws Exception {
        Store store = new InMemoryHistoryStore();
        DataStoreService storeService = new DataStoreServiceImpl(store);
        storeService.create(1, 120, "45");
        storeService.update(1, 125, "26");

        assertEquals("OK 26", storeService.delete(1, 130));
    }

    @Test
    public void get() throws Exception {
        Store store = new InMemoryHistoryStore();
        DataStoreService storeService = new DataStoreServiceImpl(store);
        storeService.create(1, 120, "45");
        storeService.update(1, 125, "26");

        assertEquals("OK 26", storeService.get(1, 125));
    }

    @Test
    public void getWithHighestTimeStamp() throws Exception {
        Store store = new InMemoryHistoryStore();
        DataStoreService storeService = new DataStoreServiceImpl(store);
        storeService.create(1, 120, "45");
        storeService.update(1, 125, "26");

        assertEquals("OK 26", storeService.get(1, 130));
    }

    @Test(expected = Exception.class)
    public void getWithLowerTimeStamp() throws Exception {
        Store store = new InMemoryHistoryStore();
        DataStoreService storeService = new DataStoreServiceImpl(store);
        storeService.create(1, 120, "45");
        storeService.update(1, 125, "26");

        assertNotEquals("OK 26", storeService.get(1, 115));
    }

    @Test
    public void latest() throws Exception {
        Store store = new InMemoryHistoryStore();
        DataStoreService storeService = new DataStoreServiceImpl(store);
        storeService.create(1, 125, "45");
        storeService.update(1, 135, "26");

        assertEquals("OK 135 26", storeService.latest(1));
    }

    @Test(expected = Exception.class)
    public void latestFail() throws Exception {
        Store store = new InMemoryHistoryStore();
        DataStoreService storeService = new DataStoreServiceImpl(store);
        storeService.create(5, 125, "45");
        storeService.update(5, 135, "26");
        storeService.delete(5, null);

        storeService.latest(1);
    }
}