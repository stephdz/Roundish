package fr.dz.roundish.util;

import java.net.URLConnection;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;

import com.google.common.collect.Maps;


/** 
 * Custom file name map 
 */
public class FileNameMap implements java.net.FileNameMap { 
      
    // Constants 
    private static final Map<String,String> customFileNameMap = initCustomFileNameMap();
    private static final String DEFAULT_MIME_TYPE = "application/octet-stream";
      
    // Used base file name map 
    private java.net.FileNameMap baseFileNameMap; 
      
    /** 
     * Default constructor 
     */
    public FileNameMap() { 
        this.baseFileNameMap = URLConnection.getFileNameMap(); 
    } 
  
    @Override
    public String getContentTypeFor(String fileName) { 
        String mimeType = baseFileNameMap.getContentTypeFor(fileName); 
        if ( mimeType == null ) { 
            mimeType = customFileNameMap.get(FilenameUtils.getExtension(fileName)); 
        }
        if ( mimeType == null ) { 
            mimeType = DEFAULT_MIME_TYPE; 
        } 
        return mimeType; 
    } 
  
    /** 
     * Constructs the custom file name map 
     * TODO Complete that list 
     * @return 
     */
    private static Map<String,String> initCustomFileNameMap() { 
        Map<String,String> customFileNameMap = Maps.newHashMap(); 
        customFileNameMap.put("js", "text/javascript");
        customFileNameMap.put("map", "text/javascript"); 
        customFileNameMap.put("css", "text/css"); 
        customFileNameMap.put("png", "image/png"); 
        customFileNameMap.put("gif", "image/gif"); 
        return customFileNameMap; 
    } 
} 
