package pt.ua.rlaas.tasks;

/**
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public class TransformTask {

    private String recordSetInId;
    private String recordSetOutName;
    private long firstRecord;
    private long lastRecord;
    private String pluginName;
    private String domain;
    private String[] fields;

    public TransformTask(String recordSetInId, String recordSetOutName, long firstRecord, long lastRecord, String pluginName, String domain, String[] fields) {
        this.recordSetInId = recordSetInId;
        this.recordSetOutName = recordSetOutName;
        this.firstRecord = firstRecord;
        this.lastRecord = lastRecord;
        this.pluginName = pluginName;
        this.domain = domain;
        this.fields = fields;
    }

    public String getRecordSetInId() {
        return recordSetInId;
    }

    public void setRecordSetInId(String recordSetInId) {
        this.recordSetInId = recordSetInId;
    }

    public String getRecordSetOutName() {
        return recordSetOutName;
    }

    public void setRecordSetOutName(String recordSetOutName) {
        this.recordSetOutName = recordSetOutName;
    }

    public long getFirstRecord() {
        return firstRecord;
    }

    public void setFirstRecord(int firstRecord) {
        this.firstRecord = firstRecord;
    }

    public long  getLastRecord() {
        return lastRecord;
    }

    public void setLastRecord(int lastRecord) {
        this.lastRecord = lastRecord;
    }

    public String getPluginName() {
        return pluginName;
    }

    public void setPluginName(String pluginName) {
        this.pluginName = pluginName;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String[] getFields() {
        return fields;
    }

    public void setFields(String[] fields) {
        this.fields = fields;
    }
}
