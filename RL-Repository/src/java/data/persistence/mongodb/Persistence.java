package data.persistence.mongodb;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import data.RecordSet;
import data.Match;
import data.Record;
import data.Schema;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import data.Result;

import org.bson.types.ObjectId;

public class Persistence {

    private final static String RESULTSET_META_COLLECTION = "resultsets";
    private final static String RESULTSET_COLLECTION_PREFIX = "resultSet_";
    private final static String RECORDSET_META_COLLECTION = "recordsets";
    private final static String RECORDSET_COLLECTION_PREFIX = "recordSet_";
    private DB db;
    private static MongoClient mongoClient = null;
    private static Persistence instance;

    public static Persistence instance() {
        if (instance == null) {
            instance = new Persistence();
        }

        return instance;

    }

    private Persistence() {
        if (mongoClient == null) {
            try {
                mongoClient = new MongoClient("localhost", 27017);
            } catch (UnknownHostException e) {
                System.exit(0);
            }
        }

        db = mongoClient.getDB("rl");
    }

    public boolean checkDb() {
        //TODO: try to implement once mongo updates
//        try {
//            mongoClient.getDatabaseNames();
//        } catch (MongoException e) {
//            return false;
//        } catch (Exception e) {
//            System.out.println("wat");
//            return false;
//        } catch (Error e) {
//            System.out.println("wat");
//            return false;
//        }
        return true;
    }

    public void dispose() {
        //TODO: what?
//        mongoClient.close();
    }

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

    public List<Record> getRecords(String recordSetId) {
        return getRecords(recordSetId, 0, Integer.MAX_VALUE); //TODO: fix
    }

    public List<Record> getRecords(String recordSetId, int offset, int size) {
        assert recordSetId != null;
        assert !recordSetId.isEmpty();
        assert offset >= 0;
        assert size > 0;

        List<Record> records = new LinkedList<Record>();

        DBCollection sets = db.getCollection(RECORDSET_META_COLLECTION);
        DBObject query = new BasicDBObject("_id", recordSetId);

        assert sets.find(query).count() == 0 : "The recordset is not in database";
        assert db.getCollectionNames().contains(RECORDSET_COLLECTION_PREFIX + recordSetId) : "The recordset is not in database";

        DBCollection collection = db.getCollection(RECORDSET_COLLECTION_PREFIX + recordSetId);

        DBCursor cursor = collection.find().skip(offset).limit(size);

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

    public List<Result> getResults(String resultSetId) {
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

    public List<Result> getResults(String resultSetId, String taxonomy) {
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

//    public List<Result> getResults(String id){
//        
//    }
    @Deprecated
    public List<Record> getResults(String taxonomyKey, Boolean old) {
        DBCollection result = db.getCollection("result_1");

//        Log.start("fetching results matching '" + taxonomyKey + "'");

        BasicDBObject query = new BasicDBObject(DBResult.FIELD_TAXONOMYKEY, taxonomyKey);

        DBCursor c = result.find(query);

        DBObject tmp;
        int rCount = 0;

        List<Record> matches = new LinkedList<Record>();

        DBResult rFetched;
        while (c.hasNext()) {
//            tmp = c.next();
//
//            rFetched = new Result((BasicDBObject) tmp, schema);
//
//            matches.add(rFetched.referenceRecord);
//            rCount++;
//
//            for (Match m : rFetched.matches) {
//                matches.add(m.matchingRecord);
//                rCount++;
//            }
        }

//        Log.stop("fetching results matching '" + taxonomyKey + "'");
//        Log.log(rCount + " records fetched.");

        return matches;
    }

    /**
     * Deletes everything!
     */
    public void clear() {
        db.dropDatabase();
    }

    @Deprecated
    public void storeResult(List<DBResult> data) {
        DBCollection result = db.getCollection("result_1");
        result.ensureIndex(DBResult.FIELD_TAXONOMYKEY);

//        Log.start("storing result");

        for (DBResult d : data) {
//            result.insert(d.toDBObject());
//            result.insert(DBResult.toDBObject(null, schema)toDBObject());
        }

//        Log.stop("storixng result");

    }
}
