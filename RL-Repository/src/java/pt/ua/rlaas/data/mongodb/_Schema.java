package pt.ua.rlaas.data.mongodb;

import pt.ua.rlaas.data.Record;
import pt.ua.rlaas.data.Schema;
import java.util.Arrays;

public class _Schema {

    private final String[] fieldNames;

    public _Schema(String[] fieldNames) {
        assert fieldNames != null;
        assert fieldNames.length > 0;

        this.fieldNames = new String[fieldNames.length];

        for (int i = 0; i < fieldNames.length; i++) {
            this.fieldNames[i] = new String(fieldNames[i]);
        }
    }

    public int getIndex(String fieldName) {
        assert hasField(fieldName);

        for (int i = 0; i < fieldNames.length; i++) {
            if (fieldNames[i].equals(fieldName)) {
                return i;
            }
        }

        return -1;
    }

    public String getName(int index) {
        assert index >= 0 && index < fieldNames.length;
        return fieldNames[index];
    }

    public boolean hasField(String fieldName) {
        for (int i = 0; i < fieldNames.length; i++) {
            if (fieldNames[i].equals(fieldName)) {
                return true;
            }
        }

        return false;
    }

    public Record newRecord() {
//	return new DBRecord(this);
        return null;
    }

    public int length() {
        return fieldNames.length;
    }

    @Override
    public String toString() {
        return "Schema " + Arrays.toString(fieldNames) + "";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(fieldNames);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Schema other = (Schema) obj;
//        if (!Arrays.equals(fieldNames, other.fieldNames)) {
//            return false;
//        }
        return true;
    }
}
