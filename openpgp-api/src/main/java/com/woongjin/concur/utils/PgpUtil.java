package com.woongjin.concur.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

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
public class PgpUtil{

	protected static final Logger log = LoggerFactory.getLogger(PgpUtil.class);

	private static boolean isArmored = true;
	private static boolean integrityCheck = false;
	
	public static void encode(File decFile, File encFile, File publicKey) {
		FileInputStream pubKeyIs = null;
		FileOutputStream cipheredFileIs = null;
		try {
			pubKeyIs = new FileInputStream(publicKey);
			cipheredFileIs = new FileOutputStream(encFile);
			log.debug("decFile : " + decFile.getName());
			log.debug("encFile : " + encFile.getName());
			log.debug("publicKey : " + publicKey.getName());
			PgpHelper.getInstance().encryptFile(cipheredFileIs, decFile.getAbsolutePath().toString(), PgpHelper.getInstance().readPublicKey(pubKeyIs), isArmored, integrityCheck);
		} catch(IOException e) {
			log.error(e.getMessage());
		} catch(Exception e) {
			log.error(e.getMessage());
		} finally {
            try {
        		cipheredFileIs.close();
        		pubKeyIs.close();
            } catch (IOException e) {
                log.error(e.getMessage());
            }
		}
		
	}

	/**
	 * 파일 디코딩
	 * @param decFile
	 * @param encFile
	 * @param privateKeyFile
	 * @param passwd
	 */
	public static void decode(File decFile, File encFile, File privateKeyFile, String passwd) {
		FileInputStream cipheredFileIs = null;
		FileInputStream privKeyIn = null;
		FileOutputStream plainTextFileIs = null;
		try {
			
			passwd = AES256Cipher.AES_Decode(passwd);
			
			cipheredFileIs = new FileInputStream(encFile);
			privKeyIn = new FileInputStream(privateKeyFile);
			plainTextFileIs = new FileOutputStream(decFile);
			log.debug("decFile : " + decFile.getName());
			log.debug("encFile : " + encFile.getName());
			//log.debug("passwd : " + passwd);
			PgpHelper.getInstance().decryptFile(cipheredFileIs, plainTextFileIs, privKeyIn, passwd.toCharArray());
			
		} catch(IOException e) {
            log.error(e.getMessage());
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
		} catch (Exception e) {
            log.error(e.getMessage());
		} finally {
            try {
    			cipheredFileIs.close();
    			plainTextFileIs.close();
    			privKeyIn.close();
            } catch (IOException e) {
                log.error(e.getMessage());
            }
		}
	}
}