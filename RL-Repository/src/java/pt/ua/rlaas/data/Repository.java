package pt.ua.rlaas.data;

import pt.ua.rlaas.data.mongodb.MongoPersistence;
import java.util.List;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 * Exposes persistance functionality through a web-service.
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
        MongoPersistence p = MongoPersistence.instance();
        if (!p.checkDb()) {
            //problems!
        }
        return p.storeRecordSet(recordSet);
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getAllRecords")
    public List<Record> getAllRecords(@WebParam(name = "recordSetId") String recordSetId) {
        MongoPersistence p = MongoPersistence.instance();
        if (!p.checkDb()) {
            //problems!
        }
        return p.getAllRecords(recordSetId);
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getTaxonomyResults")
    public java.util.List<Result> getResults(@WebParam(name = "resultSetId") String resultSetId, @WebParam(name = "taxonomy") String taxonomy) {
        MongoPersistence p = MongoPersistence.instance();
        if (!p.checkDb()) {
            //problems!
        }
        return p.getTaxonomyResults(resultSetId, taxonomy);
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getRecords")
//    @RequestWrapper(className = "data.getRecords_1")
//    @ResponseWrapper(className = "data.getRecords_1Response")
    public List<Record> getRecords(@WebParam(name = "recordSetId") String recordSetId, @WebParam(name = "offset") int offset, @WebParam(name = "size") int size) {
        MongoPersistence p = MongoPersistence.instance();
        if (!p.checkDb()) {
            //problems!
        }
        return p.getRecords(recordSetId, offset, size);
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "storeResults")
    public String storeResults(@WebParam(name = "results") List<Result> results, @WebParam(name = "schema") Schema schema) {
        MongoPersistence p = MongoPersistence.instance();
        if (!p.checkDb()) {
            //problems!
        }
        return p.storeResults(results, schema);
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getAllResults")
    public List<Result> getAllResults(@WebParam(name = "resultSetId") String resultSetId) {
        MongoPersistence p = MongoPersistence.instance();
        if (!p.checkDb()) {
            //problems!
        }
        return p.getAllResults(resultSetId);
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "operation")
    public String operation() throws PersistenceException {
        throw new PersistenceException();
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getSchema")
    public Schema getSchema(@WebParam(name = "recordSetId") String recordSetId) {
        MongoPersistence p = MongoPersistence.instance();
        if (!p.checkDb()) {
            //problems!
        }
        return p.getSchema(recordSetId);
    }
}
