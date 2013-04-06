/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.ua.pluginslot.rim;

import java.util.HashMap;

/**
 *
 * @author Luís S. Ribeiro
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public class PluginSlotTask {

    private String domain;
    private int command;
    private HashMap<String, String> settings;

    public PluginSlotTask() {
    }

    public int getCommand() {
        return command;
    }

    public void setCommand(int command) {
        this.command = command;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public HashMap<String, String> getSettings() {
        return settings;
    }

    public void setSettings(HashMap<String, String> settings) {
        this.settings = settings;
    }
}
