package challenge.repository;

import challenge.entity.Data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 This Class stores input data by the user. There is no requirement to restrict the store to a given size
 */

public class InMemoryHistoryStore implements Store {

    private final Map<Integer, Data> storeList;

    public InMemoryHistoryStore() {
        this.storeList = new HashMap<>();
    }

    private Data createDataObj(int id, int timeStamp, String observation) {
        Data data = new Data();
        data.setId(id);
        data.setTimeStamp(timeStamp);
        data.setObservation(observation);
        data.addHistory(timeStamp, observation);
        return data;
    }

    @Override
    public void create(Integer id, Integer timeStamp, String observation) throws Exception {
        if(storeList.get(id) != null)
            throw new Exception("ERR A history already exists for identifier '" + id + "'");

        Data data = createDataObj(id, timeStamp, observation);
        storeList.put(id, data);
    }

    @Override
    public String update(Integer id, Integer timeStamp, String observation) throws Exception {
        Data data = storeList.get(id);
        if( data == null)
            throw new Exception("ERR No history exists for identifier '" + id + "'");

        //Returns the data from the prior observation as-of that timestamp

        String previousObservation = null;

        if (data.getHistory().containsKey(timeStamp))
            previousObservation = data.getHistory().get(timeStamp);
        else
            previousObservation = data.getHistory().get(data.getPrevious());

        //update observation
        data.addHistory(timeStamp, observation);

        return previousObservation;
    }

    @Override
    public String get(Integer id, Integer timeStamp) throws Exception {
        Data data = storeList.get(id);

        if(data == null)
            throw new Exception("ERR No history exists for identifier '" + id + "' with a timestamp " + timeStamp);
        if (data.getHistory().size() == 0)
            throw new Exception("ERR No history exists for identifier '" + id + "' with a timestamp " + timeStamp);

        //        final int maxTimestamp = Collections.max(data.getHistory().entrySet(), Map.Entry.comparingByValue()).getKey();
        if (timeStamp >= data.getLatest())
            return data.getHistory().get(data.getLatest());

        if (data.getHistory().get(timeStamp) == null)
            throw new Exception("ERR No history exists for identifier '" + id + "' with a timestamp " + timeStamp);

        return data.getHistory().get(timeStamp);
    }

    @Override
    public String delete(Integer id, Integer timeStamp) throws Exception {
        Data data = storeList.get(id);
        if(data == null)
            throw new Exception("ERR No history exists for identifier '" + id + "'");

        if(timeStamp != null) {
            String currentObservation = get(id, timeStamp); //current observation as-of the given timestamp
            if(data.getHistory().size() != 0) {
                data.getHistory().entrySet()
                        .removeIf(entry -> entry.getKey() >= timeStamp); //remove all entries equal or greater than a timestamp
                return currentObservation;
            }
            else {
                throw new Exception("ERR No observation exists for timestamp " + timeStamp);
            }
        }

        if (data.getLatest() == null)
            throw new Exception("ERR No history exists for identifier '" + id + "'");

        final int maxTimestamp = Collections.max(data.getHistory().entrySet(), Map.Entry.comparingByKey()).getKey();
        String greatestTimestampObservation = data.getHistory().get(maxTimestamp);

        //removes a whole history for an identifier and returns observation with the greatest timestamp
        storeList.remove(id);

        return greatestTimestampObservation;
    }

    @Override
    public String latest(Integer id) throws Exception {
        Data data = storeList.get(id);
        if( data == null || data.getHistory().size() == 0)
            throw new Exception("ERR No history exists for identifier '" + id + "'");

        final int maxTimestamp = Collections.max(data.getHistory().entrySet(), Map.Entry.comparingByKey()).getKey();
        String observation = data.getHistory().get(maxTimestamp);

        return maxTimestamp + " " + observation;
    }

}
