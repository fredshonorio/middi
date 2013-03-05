package data;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class RecordSet implements Iterable<Record> {

    public final Schema schema;

    public final LinkedList<Record> records = new LinkedList<Record>();

    public RecordSet(Schema schema) {
	assert schema != null;
	this.schema = schema;
    }

    public RecordSet add(Record record) {
	assert record != null;
	assert !record.isPartial();
	assert schema.equals(record.schema);
	records.add(record);
	return this;
    }

    public RecordSet add(List<Record> records) {
	assert records != null;

	for (Record r : records) {
	    add(r);
	}

	return this;
    }

    public Iterator<Record> iterator() {
	return records.iterator();
    }

    public int size() {
	return records.size();
    }

}
