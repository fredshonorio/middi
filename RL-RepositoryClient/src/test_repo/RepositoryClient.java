package test_repo;

import data.IRepository;
import data.Record;
import data.RecordSet;
import data.Repository;
import data.Repository_Service;
import data.Result;
import data.Schema;
import java.util.List;

/**
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public class RepositoryClient implements IRepository {

    private Repository_Service svc;
    private Repository repository;

    public RepositoryClient() {
        svc = new Repository_Service();
        repository = svc.getRepositoryPort();
    }

    @Override
    public List<Record> getRecords(String recordSetId) {
        return repository.getRecords(recordSetId);
    }

    @Override
    public List<Record> getRecords(String recordSetId, int offset, int size) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Result> getResults(String resultSetId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Result> getResults(String resultSetId, String taxonomy) {
        return repository.getResults(resultSetId, taxonomy);
    }

    @Override
    public String storeRecordSet(RecordSet recordSet) {
        return repository.storeRecordSet(recordSet);
    }

    @Override
    public String storeResults(List<Result> results, Schema schema) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String test(Record record, Schema schema) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
