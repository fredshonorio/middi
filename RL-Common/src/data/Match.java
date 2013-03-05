package data;

import com.mongodb.BasicDBObject;
import java.util.Objects;

public class Match {

    public static final String FIELD_MATCHINGRECORD = "matchingRecord";
    public static final String FIELD_SCORE = "score";
    public final Record matchingRecord;
    public final double score;

    public Match(BasicDBObject obj, Schema schema) {
        assert obj != null;
        assert !obj.isEmpty();
        assert !obj.isPartialObject();
        assert schema != null;

        assert obj.containsField(FIELD_MATCHINGRECORD);
        this.matchingRecord = new Record(schema).fromDBObject((BasicDBObject) obj.get(FIELD_MATCHINGRECORD));

        assert obj.containsField(FIELD_SCORE);
        this.score = obj.getDouble(FIELD_SCORE);
    }

    public Match(Record referenceRecord, Record matchingRecord, double score) {
        assert referenceRecord != null;
        assert !referenceRecord.isPartial();
        assert matchingRecord != null;
        assert !matchingRecord.isPartial();
        assert score >= 0.0d && score <= 1.0f;

        this.matchingRecord = matchingRecord;
        this.score = score;
    }

    public BasicDBObject toDBOject() {
        BasicDBObject obj = new BasicDBObject(2);
        obj.append(FIELD_MATCHINGRECORD, matchingRecord.toDBObject());
        obj.append(FIELD_SCORE, score);

        return obj;
    }

    @Override
    public String toString() {
        return "{" + matchingRecord + ", score=" + score + "}";
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Match other = (Match) obj;
        if (!this.matchingRecord.equals(other.matchingRecord)) {
            return false;
        }
        if (Double.doubleToLongBits(this.score) != Double.doubleToLongBits(other.score)) {
            return false;
        }
        return true;
    }
}
