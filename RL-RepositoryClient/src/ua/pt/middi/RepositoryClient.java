package ua.pt.middi;

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
    public List<Record> getAllRecords(String recordSetId) {
        return repository.getAllRecords(recordSetId);
    }

    @Override
    public List<Record> getRecords(String recordSetId, int offset, int size) {
        return repository.getRecords(recordSetId, offset, size);
    }

    @Override
    public List<Result> getAllResults(String resultSetId) {
        return repository.getAllResults(resultSetId);
    }

    @Override
    public List<Result> getTaxonomyResults(String resultSetId, String taxonomy) {
        return repository.getTaxonomyResults(resultSetId, taxonomy);
    }

    @Override
    public String storeRecordSet(RecordSet recordSet) {
        return repository.storeRecordSet(recordSet);
    }

    @Override
    public String storeResults(List<Result> results, Schema schema) {
        return repository.storeResults(results, schema);
    }

    @Override
    public String test(Record record, Schema schema) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
