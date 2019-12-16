package challenge.service;

public interface DataStoreService {
    String create(Integer id, Integer timeStamp, String observation) throws Exception;
    String update(Integer id, Integer timeStamp, String observation) throws Exception;
    String delete(Integer id, Integer timeStamp) throws Exception;
    String get(Integer id, Integer timeStamp) throws Exception;
    String latest(Integer id) throws Exception;
}
