package pt.ua.rlaas.data;

import pt.ua.rlaas.data.mongodb.MongoPersistence;

/**
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public class CleanDB {
    public static void main(String[] arg){
        MongoPersistence.instance().clear();
        MongoPersistence.instance().dispose();
    }
}
