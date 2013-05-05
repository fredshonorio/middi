/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pt.ua.rlaas;

/**
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public class UserPlugin {

    protected String name;
    protected byte[] jarContents;

    public UserPlugin(String name, byte[] jarContents) {
        this.name = name;
        this.jarContents = jarContents;
    }

    public UserPlugin() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getJarContents() {
        return jarContents;
    }

    public void setJarContents(byte[] jarContents) {
        this.jarContents = jarContents;
    }
}
