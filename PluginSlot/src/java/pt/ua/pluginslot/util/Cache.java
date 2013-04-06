/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.ua.pluginslot.util;



import java.io.ByteArrayInputStream;
import java.io.File;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import pt.ua.pluginslot.rim.CacheData;

/**
 *
 * @author Luís S. Ribeiro
 */
public class Cache 
{
//    private File inCache = new File("Cache"+File.separator+"in");
//    private File outCache = new File("Cache"+File.separator+"out");
    
    private File rootWorkDir = new File("WorkDirs");
    private ArrayList<File> workDirs = new ArrayList<File>();

    public Cache() 
    {
        if(!rootWorkDir.exists())
            rootWorkDir.mkdirs();                
    }
    
    public File newWorkDir(String domain)
    {
        File dir = new File(rootWorkDir.getPath()+File.separator+domain);
        
        if(!dir.exists())
        {
            dir.mkdirs();
        }
        workDirs.add(dir);
        return dir;
    }
    
    public URL storeData(File dir, CacheData data) throws FileNotFoundException, FileAlreadyExistsException, IOException
    {
        if(workDirs.contains(dir))
        {
            return store(dir, data);
        }
        throw new FileNotFoundException(dir.getPath());
    }
    
    
    private URL store(File dir, CacheData data) throws FileNotFoundException, FileAlreadyExistsException, IOException
    {
        System.out.println(dir.getAbsolutePath());
        File f = null;
        try 
        {
            String fileName = data.getFileName();
            
            if(fileName == null || fileName.trim().equals(""))
            {
                fileName = hashContent(data.getData());
            }
            
            f = new File(dir.getPath()+File.separator+fileName);    
            if(data.isIsZipped())
            {
                f = unzip(f);
            }
            
            if(f.exists())
            {
                throw new FileAlreadyExistsException(fileName);
            }
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(data.getData());
            fos.close();                        
        }
        catch (NoSuchAlgorithmException ex) 
        {
            Logger.getLogger(Cache.class.getName()).log(Level.SEVERE, null, ex);
        }
        return f.toURI().toURL();
    }
    
    private static String hashContent(byte[] data) throws NoSuchAlgorithmException, IOException
    {
        MessageDigest md = MessageDigest.getInstance("SHA1");
        
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        
        byte[] dataBytes = new byte[1024];
 
        int nread = 0; 
 
        while ((nread = bis.read(dataBytes)) != -1) 
        {
            md.update(dataBytes, 0, nread);
        };
 
        byte[] mdbytes = md.digest();
 
        //convert the byte to hex format
        StringBuffer sb = new StringBuffer("");
        for (int i = 0; i < mdbytes.length; i++) {
            sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
        }
 
        return  sb.toString();
    }

    private File unzip(File f) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    

    
}
