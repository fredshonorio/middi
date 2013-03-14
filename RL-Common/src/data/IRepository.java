package data;

import java.util.List;

/**
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public interface IRepository {

    /**
     * Adds the recordset to storage.
     *
     * @param recordSet the recordset
     * @return The ID of the RecordSet
     */
    String storeRecordSet(RecordSet recordSet);

    List<Record> getAllRecords(String recordSetId);

    List<Record> getRecords(String recordSetId, int offset, int size);

    List<Result> getAllResults(String resultSetId);

    List<Result> getTaxonomyResults(String resultSetId, String taxonomy);

    @Deprecated
    String storeResults(List<Result> results, Schema schema);

    String test(Record record, Schema schema);
    
}
