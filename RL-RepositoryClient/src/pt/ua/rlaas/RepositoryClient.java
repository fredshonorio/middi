package pt.ua.rlaas;

import java.util.List;
import pt.ua.rlaas.data.IRepository;
import pt.ua.rlaas.data.Record;
import pt.ua.rlaas.data.RecordSet;
import pt.ua.rlaas.data.Repository;
import pt.ua.rlaas.data.Repository_Service;
import pt.ua.rlaas.data.Result;
import pt.ua.rlaas.data.Schema;

/**
 * Implements a repository web service client.
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

    public RepositoryClient(String addr) {
        //TODO: complete
        this();
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

    @Override
    public Schema getSchema(String recordSetId) {
        return repository.getSchema(recordSetId);
    }

    @Override
    public void storeRecords(List<Record> records, Schema schema, String recordSetId) {
        repository.storeRecords(records, schema, recordSetId);
    }

    @Override
    public List<Record> getDirtyRecords(String recordSetId) {
        return repository.getDirtyRecords(recordSetId);
    }

    @Override
    public List<Record> getCleanRecords(String recordSetId) {
        return repository.getCleanRecords(recordSetId);
    }

    @Override
    public String storeResultsId(List<Result> results, Schema schema, String resultName) {
        return repository.storeResultsId(results, schema, resultName);
    }

    @Override
    public List<Result> getResultsByName(String resultName, String taxonomy) {
        return repository.getResultsByName(resultName, taxonomy);
    }
}
