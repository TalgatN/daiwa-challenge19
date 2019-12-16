package challenge.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * Store.class is a data transfer object with fields
 */
public class Data {
    private Integer id;
    private Integer timeStamp;
    private String observation;
    private Map<Integer, String> history;
    private Integer latest; //the greatest timeStamp
    private Integer previous;

    public Data() {
        this.history = new HashMap<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Integer timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Map<Integer, String> getHistory() {
        return this.history;
    }

    public void addHistory(Integer timeStamp, String observation) {
        setLatest(timeStamp); //set the greatest timeStamp
        setPrevious(timeStamp); // set the observation to get the prior in the update
        history.put(timeStamp, observation); //put validation to only have an unique timestamp data
    }

    private void setLatest(Integer timeStamp) {
        this.latest = timeStamp;
    }

    private void setPrevious(Integer previous) {
        this.previous = previous;
    }

    public Integer getLatest() {
        return this.latest;
    }

    public Integer getPrevious() {
        return this.previous;
    }
}
