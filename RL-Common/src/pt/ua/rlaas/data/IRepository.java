package pt.ua.rlaas.data;

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

    /**
     * Fetches all records from a RecordSet.
     *
     * @param recordSetId
     * @return The list of records
     */
    List<Record> getAllRecords(String recordSetId);

    /**
     * Fetches records from the database.
     *
     * @param recordSetId The id of the recordSet.
     * @param offset The number of the first record.
     * @param size The number of records after the first. If this number is 0 it
     * will fetch all the remaining records.
     * @return
     */
    List<Record> getRecords(String recordSetId, int offset, int size);

    /**
     * Adds a set of results to the database.
     *
     * @param results
     * @param schema
     * @return
     */
    String storeResults(List<Result> results, Schema schema);

    /**
     * Fetches all results from a result set.
     *
     * @param resultSetId The id of the result set.
     * @return The list of results.
     */
    List<Result> getAllResults(String resultSetId);

    /**
     * Fetches the results that match a given taxonomy.
     *
     * @param resultSetId The id of the result set.
     * @param taxonomy The taxonomy value.
     * @return The list of results.
     */
    List<Result> getTaxonomyResults(String resultSetId, String taxonomy);

    String test(Record record, Schema schema);

    Schema getSchema(String recordSetId);

    void storeRecords(List<Record> records, Schema schema, String recordSetId);

    List<Record> getDirtyRecords(String recordSetId);

    List<Record> getCleanRecords(String recordSetId);

    String storeResultsId(List<Result> results, Schema schema,
            String resultName);

    List<Result> getResultsByName(String resultName, String taxonomy);
}
