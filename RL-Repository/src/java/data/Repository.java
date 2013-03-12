/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package data.persistence;

import data.Record;
import data.RecordSet;
import data.Schema;
import data.persistence.mongodb.Persistence;
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
     * This is a sample web service operation
     */
    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt) {
        return "Hello " + txt + " !";
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "storeRecordSet")
    public String storeRecordSet(@WebParam(name = "recordSet") RecordSet recordSet) {
        //TODO write your implementation code here:
        return null;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "test")
    public String test(@WebParam(name = "schema") Schema schema, @WebParam(name = "record") Record record) {
        return Persistence.instance().test(record, schema);
    }
}
