package pt.ua.rlaas.data.mongodb;

import java.util.ArrayList;
import java.util.List;

import pt.ua.rlaas.data.Result;
import pt.ua.rlaas.data.Schema;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import pt.ua.rlaas.data.Match;
import pt.ua.rlaas.data.Record;

public class DBResult {

    public static final String FIELD_REFERENCERECORD = "referenceRecord";
    public static final String FIELD_MATCHES = "matches";
    public static final String FIELD_TAXONOMYKEY = "taxonomyKey";

    public static Result fromDBObject(BasicDBObject obj) {
        return fromDBObject(obj, DBRecord.getImplicitSchema((BasicDBObject) obj.get(FIELD_REFERENCERECORD)));
    }

    public static Result fromDBObject(BasicDBObject obj, Schema schema) {
        assert obj != null;
        assert schema != null;

        assert obj.containsField(FIELD_REFERENCERECORD);

        Record referenceRecord = DBRecord.fromDBObject((BasicDBObject) obj.get(FIELD_REFERENCERECORD));

        assert obj.containsField(FIELD_MATCHES);
        BasicDBList matchList = (BasicDBList) obj.get(FIELD_MATCHES);

        List<Match> matches = new ArrayList<Match>();
        for (Object match : matchList) {
            matches.add(DBMatch.fromDBObject((BasicDBObject) match, schema));
        }
        assert matches.size() == matchList.size();

        assert obj.containsField(FIELD_TAXONOMYKEY);
        String taxonomyKey = obj.getString(FIELD_TAXONOMYKEY);

        return new Result(referenceRecord, matches, taxonomyKey);
    }

    public static BasicDBObject toDBObject(Result result, Schema schema) {
        
        BasicDBObject obj = new BasicDBObject();
        obj.append(FIELD_REFERENCERECORD, DBRecord.toDBObject(result.getReferenceRecord(), schema));

        ArrayList<BasicDBObject> matchList = new ArrayList<BasicDBObject>(result.getMatches().size());

        for (Match match : result.getMatches()) {
            matchList.add(DBMatch.toDBOject(match, schema));
        }
        obj.append(FIELD_MATCHES, matchList);

        obj.append(FIELD_TAXONOMYKEY, result.getTaxonomyKey());

        return obj;
    }
}
