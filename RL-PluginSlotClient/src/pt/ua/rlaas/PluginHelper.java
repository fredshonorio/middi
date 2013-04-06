/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.ua.rlaas;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import pt.ua.pluginslot.Plugin;

/**
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public class PluginHelper {

    private static int lastId = 0;
    private static HashMap<String, String> map = new HashMap<String, String>();

    public Plugin newPlugin(String path, String name) {
        Plugin p = new Plugin();

        String id = Integer.toString(lastId);
        lastId++;

        if (!map.containsKey(path)) {
            map.put(name, id);
        }

        File f = new File(path);
        p.setId(id);
        try {
            p.setValue(read(f));
        } catch (IOException ex) {
            //TODO: asdafasd
        }

        return p;
    }

    private static byte[] read(File file) throws IOException /*, FileTooBigException*/ {
        byte[] buffer = new byte[(int) file.length()];
        InputStream ios = null;
        try {
            ios = new FileInputStream(file);
            if (ios.read(buffer) == -1) {
                throw new IOException("EOF reached while trying to read the whole file");
            }
        } finally {
            try {
                if (ios != null) {
                    ios.close();
                }
            } catch (IOException e) {
            }
        }

        return buffer;
    }
}
