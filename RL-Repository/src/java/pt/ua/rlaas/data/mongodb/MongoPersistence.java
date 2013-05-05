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
import java.util.Iterator;

import org.bson.types.ObjectId;

public class MongoPersistence implements IRepository {

    private final static String RESULTSET_META_COLLECTION = "resultsets";
    private final static String RESULTSET_COLLECTION_PREFIX = "resultSet_";
    private final static String RECORDSET_META_COLLECTION = "recordsets";
    private final static String RECORDSET_COLLECTION_PREFIX = "recordSet_";
    private DB db;
    private static MongoClient mongoClient = null;
    private static MongoPersistence instance;

    private enum State {

        DIRTY, CLEAN, ALL
    }

    public static MongoPersistence instance() {
        if (instance == null) {
            instance = new MongoPersistence();
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

        if (!recordSet.getRecords().isEmpty()) {
            Schema schema = recordSet.getSchema();

            DBCollection records = db.getCollection(RECORDSET_COLLECTION_PREFIX + id);
            assert records.count() == 0 : "The recordset is already in database";
            for (Record r : recordSet) {
                records.insert(DBRecord.toDBObject(r, schema));
            }
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
        return getRecords(recordSetId, 0, 0, State.ALL);
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
//    @Override
    private List<Record> getRecords(String recordSetId, int offset, int size, State filter) {
        assert recordSetId != null;
        assert !recordSetId.isEmpty();
        assert offset >= 0;
        assert size >= 0;

        List<Record> records = new LinkedList<Record>();

        DBCollection sets = db.getCollection(RECORDSET_META_COLLECTION);
        DBObject query = new BasicDBObject("_id", recordSetId);

        assert sets.find(query).count() == 1 : "The recordset is not in database";
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

            if ((filter == State.DIRTY && tmp.getInt("dirty") == 1) || (filter == State.CLEAN && tmp.getInt("dirty") == 0) || filter == State.ALL) {
                records.add(DBRecord.fromDBObject(tmp, tSchema));
                BasicDBObject updateQuery = new BasicDBObject().append("_id", (ObjectId) tmp.get("_id"));
                BasicDBObject update = new BasicDBObject().append("$set", new BasicDBObject().append(DBRecord.FIELD_DIRTY, 0));

                collection.update(updateQuery, update);
            }
        }

        return records;
    }

    @Override
    public Schema getSchema(String recordSetId) {
        assert recordSetId != null;
        assert !recordSetId.isEmpty();

        DBCollection sets = db.getCollection(RECORDSET_META_COLLECTION);
        DBObject query = new BasicDBObject("_id", recordSetId);

        assert sets.find(query).count() == 0 : "The recordset is not in database";
        assert db.getCollectionNames().contains(RECORDSET_COLLECTION_PREFIX + recordSetId) : "The recordset is not in database";

        DBCollection collection = db.getCollection(RECORDSET_COLLECTION_PREFIX + recordSetId);

        DBCursor cursor = collection.find().limit(1);

        Schema tSchema = null;
        BasicDBObject tmp;
        tmp = (BasicDBObject) cursor.next();
        tSchema = DBRecord.getImplicitSchema(tmp);

        return tSchema;
    }

    @Override
    public String storeResultsId(List<Result> results, Schema schema,
            String resultName) {
        assert results != null;
        assert !results.isEmpty();
        assert schema != null;

        DBCollection sets = db.getCollection(RESULTSET_META_COLLECTION);

        DBObject query = new BasicDBObject("name", resultName);

        DBCursor curr = sets.find(query);
        String id;
        if (!curr.hasNext()) {
            BasicDBObject rs = new BasicDBObject().append("name", resultName);
            sets.insert(rs);
            id = ((ObjectId) rs.get("_id")).toString();
        } else {
            id = ((ObjectId) curr.next().get("_id")).toString();
        }

        DBCollection collection = db.getCollection(RESULTSET_COLLECTION_PREFIX + id);
//        assert collection.count() == 0 : "The result set is already in database";

        collection.ensureIndex(DBResult.FIELD_TAXONOMYKEY);

        BasicDBObject resultQuery = new BasicDBObject();
        DBCursor resultCurr;
//        DBObject insert;
        for (Result d : results) {
            DBObject o = DBRecord.toDBObjectSimple(d.getReferenceRecord(), schema);

            resultQuery.append(DBResult.FIELD_REFERENCERECORD, o);
            resultCurr = collection.find(resultQuery);

            if (resultCurr.count() == 0) { //add result
                collection.insert(DBResult.toDBObject(d, schema));
            } else {//increment matches of an existing result
                DBObject result = resultCurr.next();
                BasicDBObject command = new BasicDBObject();
                for (Match m : d.getMatches()) {
                    command.append("$push", new BasicDBObject().append(DBResult.FIELD_MATCHES, DBMatch.toDBOject(m, schema)));

                }
                collection.update(result, command);
            }
        }
        return id;
    }

//    private List<Match> getNewMatches(DBObject original, List<Match> newMatches) {
//        LinkedList<Match> n = new LinkedList<Match>();
//        
//        for (original)
//        
//        
//        return n;
//    }
    @Override
    public String storeResults(List<Result> results, Schema schema) {




        return null;
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

    @Override
    public void storeRecords(List<Record> records, Schema schema, String recordSetId) {
//        assert recordSet != null;

        DBCollection sets = db.getCollection(RECORDSET_META_COLLECTION);

        DBObject query = new BasicDBObject("_id", recordSetId);
        assert sets.find(query).count() == 1 : "The recordset is not in database";
        //FIX?

        DBCollection records_ = db.getCollection(RECORDSET_COLLECTION_PREFIX + recordSetId);
        for (Record r : records) {
            records_.insert(DBRecord.toDBObject(r, schema));
        }

//        return id;
    }

    @Override
    public List<Record> getRecords(String recordSetId, int offset, int size) {
        return getRecords(recordSetId, offset, size, State.ALL);
    }

    @Override
    public List<Record> getDirtyRecords(String recordSetId) {
        return getRecords(recordSetId, 0, 0, State.DIRTY);
    }

    @Override
    public List<Record> getCleanRecords(String recordSetId) {
        return getRecords(recordSetId, 0, 0, State.CLEAN);

    }

    @Override
    public List<Result> getResultsByName(String resultName, String taxonomy) {
        DBCollection sets = db.getCollection(RESULTSET_META_COLLECTION);

        DBObject query = new BasicDBObject("name", resultName);

        DBCursor curr = sets.find(query);
        String id;
        if (!curr.hasNext()) {
            return new LinkedList<Result>();
        } else {
            id = ((ObjectId) curr.next().get("_id")).toString();
        }

        if (taxonomy != null) {
            return getTaxonomyResults(id, taxonomy);
        } else {
            return getAllResults(id);
        }
    }
}