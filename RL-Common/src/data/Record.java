package data;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

public class Record {

    public final static String FIELD_INTERNAL_ID = "_id";
    public final static String FIELD_SOURCEID = "sourceId";
    private final String[] fieldValues;
    public final Schema schema;
    private String sourceId = null;
    private String taxonomy = null;

    protected Record(Schema schema) {
        this.schema = schema;
        this.fieldValues = new String[schema.length()];
    }

    public Record fromDBObject(BasicDBObject obj) {
        assert !obj.isEmpty();
        assert !obj.isPartialObject();

        assert obj.containsField(FIELD_SOURCEID);
        sourceId = obj.getString(FIELD_SOURCEID);
        
        assert obj.size() == schema.length() + 2; //TODO : improve
        for (int i = 0; i < schema.length(); i++) {
            assert obj.containsField(schema.getName(i));
            fieldValues[i] = obj.getString(schema.getName(i));
        }

        assert !isPartial();
        return this;
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

    public String get(int index) {
        assert index >= 0 && index < fieldValues.length;
        return fieldValues[index];
    }

    public String get(String fieldName) {
        assert schema.hasField(fieldName);
        return fieldValues[schema.getIndex(fieldName)];
    }

    public String getSource() {
        assert hasSource();
        return sourceId;
    }

    public String getTaxonomy() {
        return taxonomy;
    }

    public boolean hasSource() {
        return true;
//        return sourceId != null;
    }

    public boolean hasTaxonomy() {
        return taxonomy != null;
    }

    public boolean isPartial() {
        for (String value : fieldValues) {
            if (value == null) {
                return true;
            }
        }
        return false;
    }

    public Record set(int index, String value) {
        assert index >= 0 && index < fieldValues.length;
        fieldValues[index] = value;
        return this;
    }

    public Record set(String fieldName, String value) {
        assert schema.hasField(fieldName);
        fieldValues[schema.getIndex(fieldName)] = value;
        return this;
    }

    public Record set(String[] fieldValues) {
        assert fieldValues != null;
        assert fieldValues.length == this.fieldValues.length;

        for (int i = 0; i < this.fieldValues.length; i++) {
            this.fieldValues[i] = new String(fieldValues[i]);
        }
        return this;
    }

    protected Record setSource(String sourceId) {
//        assert !hasSource();
        this.sourceId = sourceId;
        return this;
    }

    public void setTaxonomy(String taxonomy) {
        this.taxonomy = taxonomy;
    }

    public BasicDBObject toDBObject() {
        assert !isPartial();
        assert hasSource();

        BasicDBObject obj = new BasicDBObject(schema.length() + 1);

        obj.append(FIELD_SOURCEID, sourceId);

        for (int i = 0; i < schema.length(); i++) {
            String fiedlName = schema.getName(i);
            obj.append(fiedlName, fieldValues[i]);
        }

        return obj;
    }

    @Override
    public String toString() {
        StringBuffer b = new StringBuffer();

        b.append('{');
        for (int i = 0; i < fieldValues.length; i++) {
            b.append('\"');
            b.append(fieldValues[i]);
            b.append('\"');
            if (i != fieldValues.length - 1) {
                b.append(",");
            }
        }
        b.append('}');

        return b.toString();
    }

    @Override
    public int hashCode() {
        int hash = 3;
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
        final Record other = (Record) obj;
        if (!Arrays.deepEquals(this.fieldValues, other.fieldValues)) {
            return false;
        }
        if (!Objects.equals(this.schema, other.schema)) {
            return false;
        }
        return true;
    }
}
