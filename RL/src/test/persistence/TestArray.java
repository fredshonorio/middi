/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.persistence;

import util.ExpandingArrayList;

/**
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public class TestArray {

    public static void main(String[] args) {
        ExpandingArrayList<String> st = new ExpandingArrayList<>();


        st.add("a");
        st.add("a");
        st.add("a");
        st.add("a");
        st.add("a");
        System.out.println(st.toString());
        st.add(2, "b");
        st.add(8, "b");

        System.out.println(st.toString());
        st.set(3, "c");
        System.out.println(st.toString());
        st.set(10, "d");
        System.out.println(st.toString());







    }
}
