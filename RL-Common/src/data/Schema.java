package data;

import java.util.ArrayList;

/**
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public class Schema {

    private ArrayList<String> fieldNames;

    public Schema(ArrayList<String> fieldNames) {
        this.fieldNames = fieldNames;
    }

    public Schema() {
    }

    public ArrayList<String> getFieldNames() {
        return fieldNames;
    }

    public void setFieldNames(ArrayList<String> fieldNames) {
        this.fieldNames = fieldNames;
    }

    public int getIndex(String fieldName) {
        for (int i = 0; i < fieldNames.size(); i++) {
            if (fieldNames.get(i).equals(fieldName)) {
                return i;
            }
        }
        return -1;
    }

    public String getName(int index) {
        return fieldNames.get(index);
    }

    public int size() {
        return fieldNames.size();
    }
}
