package plugin;

//import data.mongodb.DataSource;
import data.RecordSet;

public interface TransformPlugin {
    public RecordSet transform(RecordSet recordSet /*, DataSource source*/);
}
