/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.ua.rlaas;

import java.util.List;
import pt.ua.rlaas.data.Record;

/**
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public class RecordUpdate {
    private List<Record> records;
    private String pipelineName;

    public RecordUpdate() {
    }

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }

    public String getPipelineName() {
        return pipelineName;
    }

    public void setPipelineName(String pipelineName) {
        this.pipelineName = pipelineName;
    }
}
