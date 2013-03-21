package data.mongodb;

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

        int size = schema.getFieldNames().size();

        ArrayList<String> values = new ArrayList<String>(size);

        assert obj.size() == size + 2;// : obj.toString()+ "!= " + Arrays.toString(fieldValues); //TODO : improve
        for (int i = 0; i < size; i++) {
            assert obj.containsField(schema.getFieldNames().get(i)) : "No field " + schema.getFieldNames().get(i);
            values.add(i, obj.getString(schema.getFieldNames().get(i)));
        }

        Record rec = new Record();
        rec.setFieldValues(values);
        rec.setSourceId(id);

        return rec;
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

        return new Schema(tmpSchema.toArray(new String[tmpSchema.size()]));
    }

    public static BasicDBObject toDBObject(Record record, Schema schema) {
        //TODO: validate sourceId
        int size = schema.getFieldNames().size();
        BasicDBObject obj = new BasicDBObject(size + 1);

        obj.append(FIELD_SOURCEID, record.getSourceId());

        for (int i = 0; i < size; i++) {
            String fiedlName = schema.getFieldNames().get(i);
            obj.append(fiedlName, record.getFieldValues().get(i)); //getValue(i)
        }

        return obj;
    }
}
