package pt.ua.rlaas.data.mongodb;

import pt.ua.rlaas.data.IRepository;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

import pt.ua.rlaas.data.RecordSet;
import pt.ua.rlaas.data.Match;
import pt.ua.rlaas.data.Record;
import pt.ua.rlaas.data.Schema;
import pt.ua.rlaas.data.Result;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.WriteConcern;

import org.bson.types.ObjectId;

public class MongoPersistence implements IRepository {

    private final static String RESULTSET_META_COLLECTION = "resultsets";
    private final static String RESULTSET_COLLECTION_PREFIX = "resultSet_";
    private final static String RECORDSET_META_COLLECTION = "recordsets";
    private final static String RECORDSET_COLLECTION_PREFIX = "recordSet_";
    private DB db;
    private static MongoClient mongoClient = null;
    private static MongoPersistence instance;

    public static MongoPersistence instance() {
        if (instance == null) {
            instance = new MongoPersistence();
            System.out.println("new instance");
        }

        return instance;
    }

    private MongoPersistence() {

        if (mongoClient == null) {
            try {
                mongoClient = new MongoClient("localhost", 27017);
                mongoClient.setWriteConcern(WriteConcern.SAFE);
            } catch (UnknownHostException e) {
                System.exit(0);
            }
        }

        db = mongoClient.getDB("rl");
    }

    public boolean checkDb() {
        try {
            mongoClient.getDatabaseNames();
        } catch (MongoException e) {
            return false;
        } catch (Exception e) {
            return false;
        } catch (Error e) {
            return false;
        }
        return true;
    }

    public void dispose() {
        mongoClient.close();
    }

    @Override
    public String test(Record record, Schema schema) {
        DBCollection sets = db.getCollection("testing");

        DBObject obj = DBRecord.toDBObject(record, schema);
        sets.insert(obj);

        return ((ObjectId) obj.get("_id")).toString();
    }

    /**
     * Adds the recordset to storage.
     *
     * @param recordSet the recordset
     * @return The ID of the RecordSet
     */
    @Override
    public String storeRecordSet(RecordSet recordSet) {
        assert recordSet != null;

        DBCollection sets = db.getCollection(RECORDSET_META_COLLECTION);
        BasicDBObject rs = new BasicDBObject().append("name", recordSet.toString()); //TODO: name
        sets.insert(rs);
        String id = ((ObjectId) rs.get("_id")).toString();

        Schema schema = recordSet.getSchema();

        DBCollection records = db.getCollection(RECORDSET_COLLECTION_PREFIX + id);
        assert records.count() == 0 : "The recordset is already in database";
        for (Record r : recordSet) {
            records.insert(DBRecord.toDBObject(r, schema));
        }

        return id;
    }

    /**
     * Fetches all records from a RecordSet.
     *
     * @param recordSetId
     * @return The list of records
     */
    @Override
    public List<Record> getAllRecords(String recordSetId) {
        return getRecords(recordSetId, 0, 0);
    }

    /**
     * Fetches records from the database.
     *
     * @param recordSetId The id of the recordSet.
     * @param offset The number of the first record.
     * @param size The number of records after the first. If this number is 0 it
     * will fetch all the remaining records.
     * @return
     */
    @Override
    public List<Record> getRecords(String recordSetId, int offset, int size) {
        assert recordSetId != null;
        assert !recordSetId.isEmpty();
        assert offset >= 0;
        assert size >= 0;

        List<Record> records = new LinkedList<Record>();

        DBCollection sets = db.getCollection(RECORDSET_META_COLLECTION);
        DBObject query = new BasicDBObject("_id", recordSetId);

        assert sets.find(query).count() == 0 : "The recordset is not in database";
        assert db.getCollectionNames().contains(RECORDSET_COLLECTION_PREFIX + recordSetId) : "The recordset is not in database";

        DBCollection collection = db.getCollection(RECORDSET_COLLECTION_PREFIX + recordSetId);

        DBCursor cursor = collection.find().skip(offset);
        if (size > 0) {
            cursor = cursor.limit(size);
        }

        Schema tSchema = null;
        BasicDBObject tmp;

        while (cursor.hasNext()) {
            tmp = (BasicDBObject) cursor.next();
            if (tSchema == null) {
                tSchema = DBRecord.getImplicitSchema(tmp);
            }
            records.add(DBRecord.fromDBObject(tmp, tSchema));
        }
        return records;
    }

    @Override
    public String storeResults(List<Result> results, Schema schema) {
        assert results != null;
        assert !results.isEmpty();
        assert schema != null;

        DBCollection sets = db.getCollection(RESULTSET_META_COLLECTION);
        BasicDBObject rs = new BasicDBObject().append("name", results.toString()); //TODO: name
        sets.insert(rs);
        String id = ((ObjectId) rs.get("_id")).toString();

        DBCollection collection = db.getCollection(RESULTSET_COLLECTION_PREFIX + id);
        assert collection.count() == 0 : "The result set is already in database";

        collection.ensureIndex(DBResult.FIELD_TAXONOMYKEY);

        for (Result d : results) {
            collection.insert(DBResult.toDBObject(d, schema));
        }

        return id;
    }

    @Override
    public List<Result> getAllResults(String resultSetId) {
        assert resultSetId != null;
        assert !resultSetId.isEmpty();

        List<Result> results = new LinkedList<Result>();

        DBCollection sets = db.getCollection(RESULTSET_META_COLLECTION);
        DBObject query = new BasicDBObject("_id", resultSetId);

        assert sets.find(query).count() == 0 : "The recordset is not in database";
        assert db.getCollectionNames().contains(RESULTSET_COLLECTION_PREFIX + resultSetId) : "The recordset is not in database";

        DBCollection collection = db.getCollection(RESULTSET_COLLECTION_PREFIX + resultSetId);

        DBCursor cursor = collection.find();

        BasicDBObject tmp;

        while (cursor.hasNext()) {
            tmp = (BasicDBObject) cursor.next();
            results.add(DBResult.fromDBObject(tmp));
        }

        return results;
    }

    @Override
    public List<Result> getTaxonomyResults(String resultSetId, String taxonomy) {
        assert resultSetId != null;
        assert !resultSetId.isEmpty();

        List<Result> results = new LinkedList<Result>();

        DBCollection sets = db.getCollection(RESULTSET_META_COLLECTION);
        DBObject query = new BasicDBObject("_id", resultSetId);

        assert sets.find(query).count() == 0 : "The recordset is not in database";
        assert db.getCollectionNames().contains(RESULTSET_COLLECTION_PREFIX + resultSetId) : "The recordset is not in database";

        DBCollection collection = db.getCollection(RESULTSET_COLLECTION_PREFIX + resultSetId);
        DBObject queryTax = new BasicDBObject(DBResult.FIELD_TAXONOMYKEY, taxonomy);

        DBCursor cursor = collection.find(queryTax);

        BasicDBObject tmp;

        while (cursor.hasNext()) {
            tmp = (BasicDBObject) cursor.next();
            results.add(DBResult.fromDBObject(tmp));
        }

        return results;
    }

    /**
     * Deletes everything!
     */
    public void clear() {
        db.dropDatabase();
    }
}
