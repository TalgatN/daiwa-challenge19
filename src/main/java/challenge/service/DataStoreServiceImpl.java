package challenge.service;

import challenge.entity.Data;
import challenge.repository.Store;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataStoreServiceImpl implements DataStoreService {
    private final Store store;

    public DataStoreServiceImpl(Store store) {
        this.store = store;
    }

    private boolean checkIdRange(int id) {
        return id >= 0 && id <= Math.pow(2, 61) - 1;
    }

    private boolean checkTimeStampRange(int timeStamp) {
        return timeStamp >= 0 && timeStamp <= Math.pow(2, 61) - 1;
    }

    //determine if a string contains a whitespace
    private boolean checkData(String observation) {
        if(observation == null)
            return true;
        Pattern pattern = Pattern.compile("\\s");
        Matcher matcher = pattern.matcher(observation);
        return matcher.find();
    }

    private void validateInput(Integer id, Integer timeStamp, String observation) throws Exception {
        if (!checkIdRange(id))
            throw new Exception("ERR id should be in an inclusive range [0..2^31 - 1]");
        if (!checkTimeStampRange(timeStamp))
            throw new Exception("ERR timestamp should be in an inclusive range  [0..2^63 - 1]");
        if (checkData(observation))
            throw new Exception("ERR observations should not contain whitespaces");
    }

    @Override
    public String create(Integer id, Integer timeStamp, String observation) throws Exception {
        validateInput(id, timeStamp, observation);

        store.create(id, timeStamp, observation);
        return "OK " + observation;
    }

    @Override
    public String update(Integer id, Integer timeStamp, String observation) throws Exception {
        validateInput(id, timeStamp, observation);

        String oldObservation = store.update(id, timeStamp, observation);

        return "OK " + oldObservation;
    }

    @Override
    public String get(Integer id, Integer timeStamp) throws Exception {
        if (!checkIdRange(id))
            throw new Exception("ERR id should be in an inclusive range [0..2^31 - 1]");
        if (!checkTimeStampRange(timeStamp))
            throw new Exception("ERR timestamp should be in an inclusive range  [0..2^63 - 1]");
        String observation = store.get(id, timeStamp);

        return "OK " + observation;
    }

    @Override
    public String delete(Integer id, Integer timeStamp) throws Exception {
        if (!checkIdRange(id))
            throw new Exception("Error, id should be in an inclusive range [0..2^31 - 1]");
        if (timeStamp != null && !checkTimeStampRange(timeStamp))
            throw new Exception("Error, timestamp should be in an inclusive range  [0..2^63 - 1]");
        String observation = store.delete(id, timeStamp);

        return "OK " + observation;
    }

    @Override
    public String latest(Integer id) throws Exception {
        if (!checkIdRange(id))
            throw new Exception("Error, id should be in an inclusive range [0..2^31 - 1]");

        String latest = store.latest(id);

        return "OK " + latest;
    }
}
