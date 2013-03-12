package data;

import java.util.List;

/**
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public interface IRepository {

    List<Record> getRecords(String recordSetId);

    List<Record> getRecords(String recordSetId, int offset, int size);

    List<Result> getResults(String resultSetId);

    List<Result> getResults(String resultSetId, String taxonomy);

    /**
     * Adds the recordset to storage.
     *
     * @param recordSet the recordset
     * @return The ID of the RecordSet
     */
    String storeRecordSet(RecordSet recordSet);

    @Deprecated
//    void storeResult(List<DBResult> data);

    String storeResults(List<Result> results, Schema schema);

    String test(Record record, Schema schema);
    
}
