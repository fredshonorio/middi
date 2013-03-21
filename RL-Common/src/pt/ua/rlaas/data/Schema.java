package pt.ua.rlaas.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public class Schema implements Iterable<String> {

    private List<String> fieldNames;

    public Schema() {
    }

    public Schema(String... fieldNames) {
        this.fieldNames = new ArrayList<String>(Arrays.asList(fieldNames));
    }

    public List<String> getFieldNames() {
        if (fieldNames == null) {
            fieldNames = new ArrayList<String>();
        }
        return this.fieldNames;
    }

    public void setFieldNames(List<String> fieldNames) {
        this.fieldNames = fieldNames;
    }

    @Override
    public Iterator<String> iterator() {
        return fieldNames.iterator();
    }

    @Override
    public String toString() {
        return fieldNames.toString();
    }
}
