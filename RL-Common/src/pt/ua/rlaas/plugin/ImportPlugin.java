package pt.ua.rlaas.plugin;

import java.util.Iterator;

import pt.ua.rlaas.data.Record;

public interface ImportPlugin {

    public void start();

    public Iterator<Record> iterator();

    public void setup(String opt);
}
