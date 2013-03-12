/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import data.Schema;
import java.util.ArrayList;

/**
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public class SchemaHelper {

    public static Schema newSchema(String... fieldValues) {

        Schema schema = new Schema();
        ArrayList<String> vals = new ArrayList<String>(fieldValues.length);
        for (String field : fieldValues) {
            vals.add(field);
        }

        schema.setFieldNames(vals);

        return schema;
    }
}
