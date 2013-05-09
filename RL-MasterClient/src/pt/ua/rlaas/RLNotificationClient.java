package pt.ua.rlaas;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import pt.ua.rlaas.data.Record;
import pt.ua.rlaas.data.Schema;

/**
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public class RLNotificationClient {

    /**
     * @param args the command line arguments
     */
    static Master n = new Master_Service().getMasterPort();

    public static void main(String[] args) {
        //TODO: plugin settings
        pipeline();
        //TODO: check pipeline on update calls
        update();
        
        stop();
        
//        update2();
    }
    
    
    public static void stop(){
        n.stopPipeline("pipeline");
    }

    public static void pipeline() {

        //load plugins
        try {
            byte[] pl = read("C:\\Users\\PC\\code\\dissertation\\new\\RL-TestPlugins\\dist\\RL-TestPlugins.jar");
            n.uploadPlugin("rl.testplugins.ToUppercase", pl);
            n.uploadPlugin("rl.testplugins.Levenshtein", pl);
            n.uploadPlugin("rl.testplugins.PrintExport", pl);
            n.uploadPlugin("rl.testplugins.FirstLetterTaxonomy", pl);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(RLNotificationClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RLNotificationClient.class.getName()).log(Level.SEVERE, null, ex);
        }

        // define pipeline for record updates
        UpdatePipeline up = new UpdatePipeline();
        up.setDomain("rl");
        up.setPipelineName("pipeline");

        //define the schema
        Schema schema = new Schema();
        schema.getFieldNames().add("name");
        schema.getFieldNames().add("bi");
        up.setStartingSchema(schema);

        //transforms
        TransformStep t1 = new TransformStep();
        t1.setPluginName("rl.testplugins.ToUppercase");
        t1.getFields().add("name");

        //transform 1 settings (optional)
        TransformStep.Settings s1 = new TransformStep.Settings();
        TransformStep.Settings.Entry setting1 = new TransformStep.Settings.Entry();
        setting1.setKey("test");
        setting1.setValue("me");
        s1.getEntry().add(setting1);
        t1.setSettings(s1);

        TransformStep t2 = new TransformStep();
        t2.setPluginName("rl.testplugins.FirstLetterTaxonomy");
        up.getTransformSteps().add(t1);
        up.getTransformSteps().add(t2);

        //compare
        CompareConfig cfg = new CompareConfig();
        //set schema (in this case it's the same, but it may not be)
        cfg.setSchema(schema);

        //add plugins and weights (in the same order)
        cfg.getPlugins().add("rl.testplugins.Levenshtein");
        cfg.getWeights().add(1d);
        cfg.getWeights().add(0d);

        //define thresholds
        cfg.setThresholdLow(0.3);
        cfg.setThresholdHigh(0.7);

        up.setCompareConfig(cfg);

        //export
        ExportStep ex = new ExportStep();
        ex.setPluginName("rl.testplugins.PrintExport");
        //export settings (optional)
        ExportStep.Settings s2 = new ExportStep.Settings();
        ExportStep.Settings.Entry setting2 = new ExportStep.Settings.Entry();
        setting2.setKey("what's");
        setting2.setValue("up");
        s2.getEntry().add(setting2);
        ex.setSettings(s2);
        up.setExportStep(ex);

        n.definePipeline(up);
    }

    public static void update() {
        RecordUpdate update = new RecordUpdate();
        update.setPipelineName("pipeline");

        Record r1 = new Record();
        r1.getFieldValues().add("Zezinho");
        r1.getFieldValues().add("1234");

        Record r2 = new Record();
        r2.getFieldValues().add("Zezinho1");
        r2.getFieldValues().add("1234");

        update.getRecords().add(r1);
        update.getRecords().add(r2);

        n.updateRecords(update);
    }

    public static void update2() {
        RecordUpdate update = new RecordUpdate();
        update.setPipelineName("pipeline");

        Record r1 = new Record();
        r1.getFieldValues().add("Zezinho2");
        r1.getFieldValues().add("1234");

        update.getRecords().add(r1);

        n.updateRecords(update);
    }

    public static byte[] read(String filename) throws FileNotFoundException, IOException {
        File file = new File(filename);
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
