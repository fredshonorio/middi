package data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import util.ExpandingArrayList;

/**
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public class Record implements Iterable<String> {

    private ExpandingArrayList<String> fieldValues;
    private String sourceId;
    private String taxonomy;

    public Record(List<String> fieldValues, String sourceId, String taxonomy) {
        this.fieldValues = new ExpandingArrayList<String>(fieldValues);
        this.sourceId = sourceId;
        this.taxonomy = taxonomy;
    }

    public Record() {
    }

    public Record(ExpandingArrayList<String> fieldValues) {
        this.fieldValues = fieldValues;
    }

    public ArrayList<String> getFieldValues() {
        return fieldValues;
    }

    public String getValue(int index) {
        return fieldValues.get(index);
    }

    public void setValue(int index, String value) {
        fieldValues.set(index, value);
    }

    public void setFieldValues(ExpandingArrayList<String> fieldValues) {
        this.fieldValues = fieldValues;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getTaxonomy() {
        return taxonomy;
    }

    public void setTaxonomy(String taxonomy) {
        this.taxonomy = taxonomy;
    }

    @Override
    public Iterator<String> iterator() {
        return fieldValues.iterator();
    }
}
