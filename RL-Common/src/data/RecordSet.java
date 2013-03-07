package data;

import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public class RecordSet implements Iterable<Record>{

    private Schema schema;
    private LinkedList<Record> records;

    public RecordSet(Schema schema) {
        this.schema = schema;
        this.records = new LinkedList<Record>();
    }

    public RecordSet() {
    }

    public void add(Record record) {
        assert matchSchema(record);
        records.add(record);
    }

    public boolean matchSchema(Record record) {
        return record.getFieldValues().size() == schema.getFieldNames().size();
    }

    public Schema getSchema() {
        return schema;
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
    }

    public LinkedList<Record> getRecords() {
        return records;
    }

    public void setRecords(LinkedList<Record> records) {
        this.records = records;
    }

    @Override
    public Iterator<Record> iterator() {
        return records.iterator();
    }
    
    
}
