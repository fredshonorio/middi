package data;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;

public class Result {
    public static final String FIELD_REFERENCERECORD = "referenceRecord";
    public static final String FIELD_MATCHES = "matches";
    public static final String FIELD_TAXONOMYKEY = "taxonomyKey";

    public final Record referenceRecord; // the reference record
    public final List<Match> matches; // the matching records
    public final String taxonomyKey; // the taxonomy key of the reference
				     // record

    public Result(BasicDBObject obj, Schema schema) {
	assert obj != null;
	assert schema != null;

	assert obj.containsField(FIELD_REFERENCERECORD);
	this.referenceRecord = schema.newRecord().fromDBObject((BasicDBObject) obj.get(FIELD_REFERENCERECORD));

	assert obj.containsField(FIELD_MATCHES);
	BasicDBList matchList = (BasicDBList) obj.get(FIELD_MATCHES);
	this.matches = new ArrayList<Match>();
	for (Object match : matchList) {
	    matches.add(new Match((BasicDBObject) match, schema));
	}
	assert matches.size() == matchList.size();

	assert obj.containsField(FIELD_TAXONOMYKEY);
	this.taxonomyKey = obj.getString(FIELD_TAXONOMYKEY);
    }

    public Result(Record referenceRecord, List<Match> matches, String taxonomyKey) {
	assert referenceRecord != null;
	assert !referenceRecord.isPartial();
	assert matches != null;
	assert taxonomyKey != null;

	this.referenceRecord = referenceRecord;
	this.matches = matches;
	this.taxonomyKey = taxonomyKey;
    }

    public BasicDBObject toDBObject() {
	BasicDBObject obj = new BasicDBObject();

	obj.append(FIELD_REFERENCERECORD, referenceRecord.toDBObject());

	ArrayList<BasicDBObject> matchList = new ArrayList<BasicDBObject>(matches.size());

	for (Match match : matches) {
	    assert !match.matchingRecord.isPartial();

	    matchList.add(match.toDBOject());
	}
	obj.append(FIELD_MATCHES, matchList);

	obj.append(FIELD_TAXONOMYKEY, taxonomyKey);

	return obj;
    }

    @Override
    public String toString() {

	String str = "{\n\t" + FIELD_REFERENCERECORD + " : " + referenceRecord.toDBObject().toString() + "\n";

	str += "\t" + FIELD_MATCHES + " : {\n";

	for (Match m : matches) {
	    str += "\t\t" + m.toDBOject().toString() + "\n";
	}
	str += "\t}\n";

	str += "\t" + FIELD_TAXONOMYKEY + " : \"" + taxonomyKey + "\"\n}";

	return str;
    }
}
