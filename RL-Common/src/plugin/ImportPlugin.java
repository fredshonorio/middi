package plugin;

import java.util.Iterator;

import data.Record;

public interface ImportPlugin {

    public void start();

    public Iterator<Record> iterator();

    public void setup(String opt);
}
