/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.ua.rlaas.plugin;

import java.io.File;
import java.util.HashMap;

/**
 *
 * @author Luís S. Ribeiro
 */
public interface Plugin 
{
    void init(HashMap<String,String> settings);
    
    void destroy();
    
}
