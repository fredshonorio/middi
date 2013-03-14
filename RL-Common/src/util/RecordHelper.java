package util;

import data.Record;
import data.Schema;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public final class RecordHelper {

    public final Schema schema;
    private int size;

    public RecordHelper(Schema schema) {
        this.schema = schema;
        this.size = schema.getFieldNames().size();
    }

    public Record newRecord(String sourceId) {
        Record r = new Record();

        r.setSourceId(sourceId);

        fill(r);

        return r;
    }

    public void set(Record record, String field, String value) {
        int idx = findIndex(field);

        if (idx == -1) {
            return;
        }
        if (idx >= record.getFieldValues().size()) {
            fill(record);
        }
        record.getFieldValues().set(idx, value);
    }

    public String get(Record record, String field) {
        int idx = findIndex(field);

        if (idx == -1) {
            return null;
        }
        return record.getFieldValues().get(idx);
    }

    private int findIndex(String field) {
        List<String> fieldNames = schema.getFieldNames();
        for (int i = 0; i < size; i++) {
            if (fieldNames.get(i).equals(field)) {
                return i;
            }
        }
        return -1;
    }

//    @Deprecated
    public Record newRecord(String sourceId, String... fieldValues) {
        assert fieldValues.length == size;

        Record r = new Record();
        r.setSourceId(sourceId);

        ArrayList<String> tmp = new ArrayList<String>(size);

        for (String value : fieldValues) {
            tmp.add(value);
        }

        r.setFieldValues(tmp);

        return r;
    }

    private boolean schemaMatches(Record record) {
        return record.getFieldValues().size() == size;
    }

    private void fill(Record record) {
        List<String> tmp = record.getFieldValues();

        int i = tmp.size();

        for (; i < size; i++) {
            tmp.add("");
        }

        record.setFieldValues(tmp);
    }

    public CharSequence toString(Record record) {
        StringBuffer buff = new StringBuffer();
        buff.append('[');

        for (int i = 0; i < size; i++) {
            buff.append(schema.getFieldNames().get(i));
            buff.append(": \"");
            if (record.getFieldValues().size() > i) {
                buff.append(record.getFieldValues().get(i));
            } else {
                buff.append("NULL");
            }
            buff.append('\"');

            if (i != size - 1) {
                buff.append(' ');
            }
        }

        buff.append(']');
        return buff;
    }
}
