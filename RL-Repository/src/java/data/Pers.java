/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import data.mongodb.Persistence;

/**
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public class Pers {

    public static void main(String[] args) {
        Persistence r = Persistence.instance();
        System.out.println(r.checkDb());

//        if (r.checkDb()) {
//            r.getRecords("513625c2e433df575eece9ec");
//        }
    }
}
