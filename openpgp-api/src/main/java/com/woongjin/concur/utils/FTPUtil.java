package com.woongjin.concur.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woongjin.framework.core.encrypt.AES256Cipher;

/**
 * 서버와 연결하여 파일을 업로드하고, 다운로드한다.
 *
 * @author haneulnoon
 * @since 2009-09-10
 *
 */
public class FTPUtil{

	protected static final Logger log = LoggerFactory.getLogger(FTPUtil.class);

    private FTPClient ftpClient = new FTPClient();

    /**
     * 서버와 연결에 필요한 값들을 가져와 초기화 시킴
     *
     * @param host
     *            서버 주소
     * @param userName
     *            접속에 사용될 아이디
     * @param password
     *            비밀번호
     * @param port
     *            포트번호
     */
    public void init(String host, String userName, String password, String port) {

        try {
			host = AES256Cipher.AES_Decode(host);
        	userName = AES256Cipher.AES_Decode(userName);
        	password = AES256Cipher.AES_Decode(password);

            ftpClient.connect(host, Integer.parseInt(port));
            ftpClient.login(userName, password);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            //ftpClient.setControlEncoding("UTF-8");
            //ftpClient.enterLocalPassiveMode();
            
		} catch (InvalidKeyException e) {
            log.error(e.getMessage());
		} catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage());
		} catch (NoSuchPaddingException e) {
            log.error(e.getMessage());
		} catch (InvalidAlgorithmParameterException e) {
            log.error(e.getMessage());
		} catch (IllegalBlockSizeException e) {
            log.error(e.getMessage());
		} catch (BadPaddingException e) {
            log.error(e.getMessage());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 하나의 파일을 업로드 한다.
     *
     * @param dir
     *            저장시킬 주소(서버)
     * @param file
     *            저장할 파일
     */
    public void upload(String dir, File file) {
        FileInputStream in = null;

        try {

            in = new FileInputStream(file);
        	ftpClient.changeWorkingDirectory(dir);
        	ftpClient.storeFile(file.getName(), in);

        } catch (FileNotFoundException e) {
            log.error(e.getMessage());
        } catch (IOException e) {
            log.error(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {

            try {
                in.close();
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }

    /**
     * 하나의 파일을 다운로드 한다.
     *
     * @param dir
     *            저장할 경로(서버)
     * @param fileName
     *            다운로드할 파일
     * @param downFile
     *            저장될 공간
     */
    public void download(String path, String fileName, File downloadFile) {

        BufferedInputStream bis = null;
        FileOutputStream out = null;
        try {

        	ftpClient.changeWorkingDirectory(path);
        	InputStream in = ftpClient.retrieveFileStream(fileName);
        	bis = new BufferedInputStream(in);
            out = new FileOutputStream(downloadFile);

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            int i;

            if(bis != null) {
	            while ((i = bis.read()) != -1) {
	                out.write(i);
	            }
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {

            try {
            	bis.close();
                out.close();
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }

	public ArrayList<String> getFileList(String path, String ext) {
    	
    	ArrayList<String> resList = new ArrayList<String>();
    	
    	try {
    		ftpClient.changeWorkingDirectory(path);
    		FTPFile[] filelist = ftpClient.listFiles();
            for(int i=0; i<filelist.length; i++){
            	FTPFile obj = filelist[i];
            	String fileName = obj.getName();
            	String fileExt = fileName.substring(fileName.lastIndexOf(".")+1);
            	if(fileExt.toLowerCase().equals(ext.toLowerCase())) {
            		resList.add(fileName);
            	}
            }
    	} catch (Exception e) {
            log.error(e.getMessage());
        }
    	return resList;
    }
    
    /**
     * 서버와의 연결을 끊는다.
     */
	public void disconnection() {
		if (ftpClient.isConnected()) {
			try {
				ftpClient.logout();
				ftpClient.disconnect();
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		}
	}
}