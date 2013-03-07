package data;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public class Record implements Iterable<String> {

    private ArrayList<String> fieldValues;
    private String sourceId;
    private String taxonomy;

    public Record(ArrayList<String> fieldValues, String sourceId, String taxonomy) {
        this.fieldValues = fieldValues;
        this.sourceId = sourceId;
        this.taxonomy = taxonomy;
    }

    public Record(ArrayList<String> fieldValues) {
        this.fieldValues = fieldValues;
    }

    public ArrayList<String> getFieldValues() {
        return fieldValues;
    }

    public void setFieldValues(ArrayList<String> fieldValues) {
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
