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

//        pipeline();
        //TODO: check pipeline on update calls
//        update();

        update2();
    }

    public static void pipeline() {
        Schema schema = new Schema();
        schema.getFieldNames().add("name");
        schema.getFieldNames().add("bi");
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

        UpdatePipeline up = new UpdatePipeline();

        TransformStep t = new TransformStep();
        t.setPluginName("rl.testplugins.ToUppercase");
        TransformStep t2 = new TransformStep();
        t2.setPluginName("rl.testplugins.FirstLetterTaxonomy");

        CompareConfig cfg = new CompareConfig();

        cfg.getPlugins().add("rl.testplugins.Levenshtein");

        cfg.setSchema(schema);

        cfg.getWeights().add(1d);
        cfg.getWeights().add(0d);

        cfg.setThresholdLow(0.3);
        cfg.setThresholdHigh(0.7);

        ExportStep ex = new ExportStep();
        ex.pluginName = "rl.testplugins.PrintExport";

        up.setPipelineName("pipeline");
        up.setDomain("rl");
        up.setStartingSchema(schema);
        up.getTransformSteps().add(t);
        up.getTransformSteps().add(t2);
        up.setCompareConfig(cfg);
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
