package data.persistence.mongodb;

import data.Schema;
import com.mongodb.BasicDBObject;
import data.Record;
import java.util.ArrayList;
import java.util.Set;

public class DBRecord {

    public final static String FIELD_INTERNAL_ID = "_id";
    public final static String FIELD_SOURCEID = "sourceId";

    public static Record fromDBObject(BasicDBObject obj, Schema schema) {
        assert !obj.isEmpty();
        assert !obj.isPartialObject();

        assert obj.containsField(FIELD_SOURCEID);
        String id = obj.getString(FIELD_SOURCEID);

        ArrayList<String> values = new ArrayList<String>(schema.size());

        assert obj.size() == schema.size() + 2;// : obj.toString()+ "!= " + Arrays.toString(fieldValues); //TODO : improve
        for (int i = 0; i < schema.size(); i++) {
            assert obj.containsField(schema.getName(i)) : "No field " + schema.getName(i);
            values.set(i, obj.getString(schema.getName(i)));
        }

        return new Record(values, id, null);
    }

    public static Record fromDBObject(BasicDBObject obj) {
        return fromDBObject(obj, getImplicitSchema(obj));
    }

    public static Schema getImplicitSchema(BasicDBObject obj) {
        assert !obj.isEmpty();
        assert !obj.isPartialObject();


        ArrayList<String> tmpSchema = new ArrayList<String>();

        Set<String> fields = obj.keySet();

        for (String field : fields) {
            if (!(field.equals(FIELD_INTERNAL_ID) || field.equals(FIELD_SOURCEID))) {
                tmpSchema.add(field);
            }
        }

        return new Schema(tmpSchema);
    }

    public static BasicDBObject toDBObject(Record record, Schema schema) {
        //TODO: validade sourceId
        BasicDBObject obj = new BasicDBObject(schema.size() + 1);

        obj.append(FIELD_SOURCEID, record.getSourceId());

        for (int i = 0; i < schema.size(); i++) {
            String fiedlName = schema.getName(i);
            obj.append(fiedlName, record.getValue(i));
        }

        return obj;
    }
}
