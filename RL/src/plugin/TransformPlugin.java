package plugin;

import data.DataSource;
import data.RecordSet;

public interface TransformPlugin {
    public RecordSet transform(RecordSet recordSet, DataSource source);
}
