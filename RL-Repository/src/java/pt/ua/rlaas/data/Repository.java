package pt.ua.rlaas.data;

import java.util.LinkedList;
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
public class Repository{

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

    @WebMethod(operationName = "storeRecords")
    public void storeRecords(List<Record> records, Schema schema, String recordSetId) {
        MongoPersistence p = MongoPersistence.instance();

        if (!p.checkDb()) {
            //problems!
        }
        p.storeRecords(records, schema, recordSetId);
    }

    @WebMethod(operationName = "getDirtyRecords")
    public List<Record> getDirtyRecords(String recordSetId) {
        MongoPersistence p = MongoPersistence.instance();

        if (!p.checkDb()) {
            //problems!
        }

        return p.getDirtyRecords(recordSetId);
    }

    @WebMethod(operationName = "getCleanRecords")
    public List<Record> getCleanRecords(String recordSetId) {
        MongoPersistence p = MongoPersistence.instance();

        if (!p.checkDb()) {
            //problems!
        }

        return p.getCleanRecords(recordSetId);
    }

    @WebMethod(operationName = "storeResultsId")
    public String storeResultsId(List<Result> results, Schema schema,
            String resultName) {
        MongoPersistence p = MongoPersistence.instance();

        if (!p.checkDb()) {
            //problems!
        }

        for (Result r : results) {
            if (r.getMatches() == null) {
                r.setMatches(new LinkedList<Match>());
            }
        }

        return p.storeResultsId(results, schema, resultName);
    }

    public String test(Record record, Schema schema) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @WebMethod(operationName = "getResultsByName")
    public List<Result> getResultsByName(@WebParam(name = "resultName") String resultName, @WebParam(name = "taxonomy") String taxonomy) {
        MongoPersistence p = MongoPersistence.instance();

        if (!p.checkDb()) {
            //problems!
        }

        return p.getResultsByName(resultName, taxonomy);
    }
}
