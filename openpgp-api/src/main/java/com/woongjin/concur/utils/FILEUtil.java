package com.woongjin.concur.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 서버와 연결하여 파일을 업로드하고, 다운로드한다.
 *
 * @author haneulnoon
 * @since 2009-09-10
 *
 */
public class FILEUtil{

	protected static final Logger log = LoggerFactory.getLogger(FILEUtil.class);

	private void copyWithStreams(File aSourceFile, File aTargetFile) {
	    //log.debug("Copying files with streams.");
	    BufferedInputStream inStream = null;
	    BufferedOutputStream outStream = null;
	    
	    try{
	      try {
	        inStream = new BufferedInputStream(new FileInputStream(aSourceFile));
	        outStream = new BufferedOutputStream(new FileOutputStream(aTargetFile));

	        int bytesRead = 0;
	        outStream.write(0xEF);
	        outStream.write(0xBB);
	        outStream.write(0xBF);
	        while ( ( bytesRead = inStream.read() ) != -1 ) {
	        	if(bytesRead > 0){ // 239  187 *191 236 132 157 
	        		outStream.write(bytesRead);
		        }
	        }
	      }
	      finally {
	        if (inStream != null) inStream.close();
	        if (outStream != null) outStream.close();
	      }
	    } catch (FileNotFoundException ex){
	      log.debug("File not found: " + ex);
	    } catch (IOException ex){
	      log.debug(ex.getMessage());
	    }
	  }
	  
	 public static void copyFile(File aSourceFile, File aTargetFile) {
		 new FILEUtil().copyWithStreams(aSourceFile, aTargetFile);
	 }
}
