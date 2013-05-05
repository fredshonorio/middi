package pt.ua.rlaas.data;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public class RecordSet implements Iterable<Record> {

    protected Schema schema;
    protected List<Record> records;
    protected String name;

    public RecordSet(Schema schema) {
        this.schema = schema;
        this.records = new LinkedList<Record>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RecordSet() {
    }

    public Schema getSchema() {
        return schema;
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
    }

    public List<Record> getRecords() {
        if (records == null) {
            records = new LinkedList<Record>();
        }
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }

    public void add(Record record) {
        if (records == null) {
            getRecords();
        }

        records.add(record);
    }

    public boolean matchSchema(Record record) {
        return record.getFieldValues().size() == schema.getFieldNames().size();
    }

    @Override
    public Iterator<Record> iterator() {
        return records.iterator();
    }
}
