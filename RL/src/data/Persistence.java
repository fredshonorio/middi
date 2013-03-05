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

import core.Log;

public class Persistence {

    private DB db;
    private MongoClient mongoClient = null;
    private final Schema schema;

    private final static HashMap<Schema, Persistence> instances = new HashMap<Schema, Persistence>(3);

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
	try {
	    mongoClient = new MongoClient("localhost", 27017);
	} catch (UnknownHostException e) {
	    // e.printStackTrace();
	    System.exit(0);
	}

	db = mongoClient.getDB("rl");

	// DBCollection result = db.createCollection("result_1", null);

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

    public List<Record> fetchResult(String taxonomyKey) {
	DBCollection result = db.getCollection("result_1");

	Log.start("fetching results matching '" + taxonomyKey + "'");

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

	Log.stop("fetching results matching '" + taxonomyKey + "'");
	Log.log(rCount + " records fetched.");

	return matches;
    }

    public void clear() {
	DBCollection result = db.getCollection("result_1");
	result.drop();
    }

    public void storeResult(List<Result> data) {
	DBCollection result = db.getCollection("result_1");
	result.ensureIndex(Result.FIELD_TAXONOMYKEY);

	Log.start("storing result");

	for (Result d : data) {
	    result.insert(d.toDBObject());
	}

	Log.stop("storixng result");

    }
}
