package data.persistence.mongodb;

import com.mongodb.BasicDBObject;

import data.Match;
import data.Record;
import data.Schema;

public class DBMatch {

    public static final String FIELD_MATCHINGRECORD = "matchingRecord";
    public static final String FIELD_SCORE = "score";

    public static Match fromDBObject(BasicDBObject obj) {
        assert obj != null;
        assert !obj.isEmpty();
        assert !obj.isPartialObject();

        assert obj.containsField(FIELD_MATCHINGRECORD);
        Record matchingRecord = DBRecord.fromDBObject((BasicDBObject) obj.get(FIELD_MATCHINGRECORD));


        assert obj.containsField(FIELD_SCORE);
        Double score = obj.getDouble(FIELD_SCORE);

        return new Match(matchingRecord, score);
    }

    /**
     * Less performant
     * @param obj
     * @param schema
     * @return 
     */
    public static Match fromDBObject(BasicDBObject obj, Schema schema) {
        assert obj != null;
        assert !obj.isEmpty();
        assert !obj.isPartialObject();
        assert schema != null;

        assert obj.containsField(FIELD_MATCHINGRECORD);
        Record matchingRecord = DBRecord.fromDBObject((BasicDBObject) obj.get(FIELD_MATCHINGRECORD), schema);

        assert obj.containsField(FIELD_SCORE);
        Double score = obj.getDouble(FIELD_SCORE);

        return new Match(matchingRecord, score);
    }

    public static BasicDBObject toDBOject(Match match, Schema schema) {
        BasicDBObject obj = new BasicDBObject(2);
        obj.append(FIELD_MATCHINGRECORD, DBRecord.toDBObject(match.getMatchingRecord(), schema));
        obj.append(FIELD_SCORE, match.getScore());

        return obj;
    }
}
