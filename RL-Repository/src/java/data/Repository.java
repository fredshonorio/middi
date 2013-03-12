/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import data.Record;
import data.RecordSet;
import data.Schema;
import data.mongodb.Persistence;
import java.util.List;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
@WebService(serviceName = "Repository")
public class Repository {

    /**
     * Web service operation
     */
    @WebMethod(operationName = "storeRecordSet")
    public String storeRecordSet(@WebParam(name = "recordSet") RecordSet recordSet) {
        Persistence p = Persistence.instance();
        if (!p.checkDb()) {
            //problems!
        }
        return p.storeRecordSet(recordSet);
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getRecords")
    public List<Record> getRecords(@WebParam(name = "recordSetId") String recordSetId) {
        Persistence p = Persistence.instance();
        if (!p.checkDb()) {
            //problems!
        }
        return p.getRecords(recordSetId);
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getResults")
    public java.util.List<Result> getResults(@WebParam(name = "resultSetId") String resultSetId, @WebParam(name = "taxonomy") String taxonomy) {
        Persistence p = Persistence.instance();
        if (!p.checkDb()) {
            //problems!
        }
        return p.getResults(resultSetId, taxonomy);
    }
}
