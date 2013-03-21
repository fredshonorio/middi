package pt.ua.rlaas.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public class Record implements Iterable<String> {

    protected List<String> fieldValues;
    protected String sourceId;
    protected String taxonomy;

    public Record() {
    }

    public Record(String sourceId, String... fieldValues) {
        this.fieldValues = new ArrayList<String>(fieldValues.length);

        for (String value : fieldValues) {
            this.fieldValues.add(value);
        }
        this.sourceId = sourceId;
    }

    public List<String> getFieldValues() {
        if (fieldValues == null) {
            fieldValues = new ArrayList<String>();
        }
        return fieldValues;
    }

    public void setFieldValues(List<String> fieldValues) {
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

    @Override
    public String toString() {
        return "Record{" + "fieldValues=" + fieldValues.toString() + ", sourceId=" + sourceId + ", taxonomy=" + taxonomy + '}';
    }
}
