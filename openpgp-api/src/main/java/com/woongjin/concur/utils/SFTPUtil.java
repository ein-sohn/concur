package com.woongjin.concur.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Vector;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.woongjin.framework.core.encrypt.AES256Cipher;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * 서버와 연결하여 파일을 업로드하고, 다운로드한다.
 *
 * @author haneulnoon
 * @since 2009-09-10
 *
 */
public class SFTPUtil{

	protected static final Logger log = LoggerFactory.getLogger(SFTPUtil.class);

	private Session session = null;

    private Channel channel = null;

    private ChannelSftp channelSftp = null;

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

        JSch jsch = new JSch();
        try {
			host = AES256Cipher.AES_Decode(host);
        	userName = AES256Cipher.AES_Decode(userName);
        	password = AES256Cipher.AES_Decode(password);
        	log.debug("ftp -> " + host +":"+port+"/"+userName+"@"+password);

            session = jsch.getSession(userName, host, Integer.parseInt(port));
            session.setPassword(password);

            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");

            session.setConfig(config);
            session.connect();

            channel = session.openChannel("sftp");
            channel.connect();

		} catch (InvalidKeyException e) {
            log.error(e.getMessage());
		} catch (UnsupportedEncodingException e) {
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
        } catch (JSchException e) {
            log.error(e.getMessage());
        }

        channelSftp = (ChannelSftp) channel;
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
            channelSftp.cd(dir);
            channelSftp.put(in, file.getName());

        } catch (SftpException e) {
            log.error(e.getMessage());
        } catch (FileNotFoundException e) {
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
     * @param downloadFileName
     *            다운로드할 파일
     * @param fullpath
     *            저장될 공간
     */
    public void download(String dir, String downloadFileName, File downloadFile) {

        InputStream in = null;
        FileOutputStream out = null;
        try {

            channelSftp.cd(dir);
            in = channelSftp.get(downloadFileName);

        } catch (SftpException e) {
            e.printStackTrace();
        }

        try {

            out = new FileOutputStream(downloadFile);

            int i;

            if(in != null) {
	            while ((i = in.read()) != -1) {
	                out.write(i);
	            }
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            try {
                out.close();
                in.close();
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }

    @SuppressWarnings("unchecked")
	public ArrayList<String> getFileList(String path, String ext) {
    	
    	ArrayList<String> resList = new ArrayList<String>();
    	
    	try {
    		Vector<LsEntry> filelist = (Vector<LsEntry>) channelSftp.ls(path);
            for(LsEntry obj : filelist){
            	String fileName = obj.getFilename();
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

        channelSftp.quit();
    }
}