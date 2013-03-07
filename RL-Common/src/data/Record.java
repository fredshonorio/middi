package data;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public class Record implements Iterable<String> {

    private class RecordIterator implements Iterator<String> {

        private int i = 0;
        private ArrayList<String> fieldValues;

        public RecordIterator(ArrayList<String> fieldValues) {
            this.fieldValues = fieldValues;
        }

        @Override
        public boolean hasNext() {
            return i < fieldValues.size();
        }

        @Override
        public String next() {
            assert hasNext();
            return fieldValues.get(i++);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
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
        return new RecordIterator(this.fieldValues);
    }
}
