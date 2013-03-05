package data;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import org.bson.types.ObjectId;

public class Persistence {

    private DB db;
    private static MongoClient mongoClient = null;
    private final Schema schema;
    private final static HashMap<Schema, Persistence> instances = new HashMap<Schema, Persistence>(3);
    private final static String RECORDSET_COLLECTION_PREFIX = "recordSet_";

    public static Persistence instance(Schema schema) {
        Persistence instance = instances.get(schema);

        if (instance == null) {
            instance = new Persistence(schema);
            instances.put(schema, instance);
        }

        return instance;

    }

    private Persistence(Schema schema) {
        this.schema = schema;
        if (mongoClient == null) {
            try {
                mongoClient = new MongoClient("localhost", 27017);
            } catch (UnknownHostException e) {
                // e.printStackTrace();
                System.exit(0);
            }
        }

        db = mongoClient.getDB("rl");
    }

    public boolean checkDb() {
        //
        // try {
        // mongoClient.getDatabaseNames();
        // // } catch (IOException e) {
        // // return false;
        // } catch (MongoException e) {
        // return false;
        // } catch (Exception e) {
        // System.out.println("wat");
        // }
        //
        // System.out.println("all good");

        return true;
    }

    public void dispose() {
        mongoClient.close();
    }

    /**
     * Adds the recordset to storage.
     *
     * @param recordSet the recordset
     * @return The ID of the RecordSet
     */
    public String storeRecordSet(RecordSet recordSet) {
        assert recordSet != null;

        DBCollection sets = db.getCollection("recordsets");
        BasicDBObject rs = new BasicDBObject().append("recordset", recordSet.toString()); //TODO: name
        sets.insert(rs);
        String id = ((ObjectId) rs.get("_id")).toString();

        DBCollection records = db.getCollection(RECORDSET_COLLECTION_PREFIX + id);
        assert records.count() == 0 : "The recordset is already in database";
        for (Record r : recordSet) {
            records.insert(r.toDBObject());
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

        DBCollection sets = db.getCollection("recordsets");
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
                tSchema = Record.getImplicitSchema(tmp);
            }
            records.add(tSchema.newRecord().fromDBObject(tmp));
        }
        return records;
    }

    public List<Record> fetchResult(String taxonomyKey) {
        DBCollection result = db.getCollection("result_1");

//        Log.start("fetching results matching '" + taxonomyKey + "'");

        BasicDBObject query = new BasicDBObject(Result.FIELD_TAXONOMYKEY, taxonomyKey);

        DBCursor c = result.find(query);

        DBObject tmp;
        int rCount = 0;

        List<Record> matches = new LinkedList<Record>();

        Result rFetched;
        while (c.hasNext()) {
            tmp = c.next();

            rFetched = new Result((BasicDBObject) tmp, schema);

            matches.add(rFetched.referenceRecord);
            rCount++;

            for (Match m : rFetched.matches) {
                matches.add(m.matchingRecord);
                rCount++;
            }
        }

//        Log.stop("fetching results matching '" + taxonomyKey + "'");
//        Log.log(rCount + " records fetched.");

        return matches;
    }

    public void clear() {
        DBCollection result = db.getCollection("result_1");
        result.drop();
    }

    public void storeResult(List<Result> data) {
        DBCollection result = db.getCollection("result_1");
        result.ensureIndex(Result.FIELD_TAXONOMYKEY);

//        Log.start("storing result");

        for (Result d : data) {
            result.insert(d.toDBObject());
        }

//        Log.stop("storixng result");

    }
}
