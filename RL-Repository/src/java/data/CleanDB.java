/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package data;

import data.mongodb.MongoPersistence;

/**
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public class CleanDB {
    public static void main(String[] arg){
        MongoPersistence.instance().clear();;
    }
}
