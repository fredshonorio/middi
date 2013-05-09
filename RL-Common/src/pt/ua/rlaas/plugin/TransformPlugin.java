package pt.ua.rlaas.plugin;

import java.util.List;
import pt.ua.rlaas.data.Record;
import pt.ua.rlaas.data.Schema;

/**
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public interface TransformPlugin extends Plugin{
    /**
     * Interface for the transform plugin.
     * @param records The input records.
     * @param schema The input records' schema.
     * @param fields The fields the plugin should manipulate.
     * @return 
     */
    public List<Record> transform(List<Record> records, Schema schema, String[] fields);
}
