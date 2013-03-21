package pt.ua.rlaas.plugin;



import java.io.File;
import java.util.HashMap;


public interface TransformPlugin extends Plugin
{
    
    void transform(File workDir, HashMap<String,String> settings);
    
}
