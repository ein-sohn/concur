package com.woongjin.concur.openpgp;

import java.io.File;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woongjin.concur.utils.PgpUtil;

public class PGPDecForImport {
	
	protected static final Logger log = LoggerFactory.getLogger(PGPDecForImport.class);

	private String active = System.getProperty("profiles.active");
	private String propertiesFile = active == null ||active.length() == 0 ? "openpgp" : "openpgp_"+active;
	private ResourceBundle props = ResourceBundle.getBundle(propertiesFile);

	private void get() {
		/**
		 * pre : 
		 * 1. concur에 public key 등록
		 * 
		 *************************************************
		 * 1. concur ftp - 폴더내 암호 파일 읽기
		 * 2. 연계서버로 request payment 파일다운
		 * 3. sercet key로 post 파일 복호화
		 * 4. 다운받은 encording 파일 백업폴더로 이동
		 * 
		 *************************************************
		 * end : erp에서 ftp의 post파일 가져가도록
		 */
		try {
			
			/**
			 * 1. concur ftp - 폴더내 암호 파일 읽기
			 */
			String	concur_out_dir = props.getString("ftp.root")+"/out";
					concur_out_dir = concur_out_dir.replaceAll("//", "/");
			String	out_path = props.getString("local.path")+"/out";
					out_path = out_path.replaceAll("//", "/");

			com.woongjin.concur.utils.SFTPUtil sftp = new com.woongjin.concur.utils.SFTPUtil();
			//com.woongjin.concur.utils.FTPUtil sftp = new com.woongjin.concur.utils.FTPUtil();

			sftp.init(
					props.getString("ftp.ip")
					, props.getString("ftp.id")
					, props.getString("ftp.pw")
					, props.getString("ftp.port")
			);
			
			ArrayList<String> fileList = sftp.getFileList(concur_out_dir,"pgp");
			for(String filename: fileList) {
				
				try {
				
					File outEncFile = new File(out_path+"/"+filename);
					String outDecFileName = filename.replace(".pgp","");
					
					File bakEncFile = new File(out_path+"/"+props.getString("local.backup.folder")+"/"+filename);
					if(bakEncFile.length() == 0) {
						
						/**
						 * 2. 연계서버로 request payment 파일다운
						 */
						if(active.equals("prd")) {
							// TODO - archive 폴더의 파일명리스트 가져와서 같은 이름이 없는 파일만 다운로드..
							// 개발의 경우, EncVendorExport에서 암호화 파일을 만들어둔다.
							sftp.download(concur_out_dir, filename, outEncFile);
						}
						/**
						 * 3. sercet key로 post 파일 복호화
						 * - 백업파일에 기존파일 있는지 보고, 해당 파일 다운 및 작업 여부 결정
						 */
						String passwd = props.getString("pgp.private.passwd");
						File privKeyFile = new File(props.getString("pgp.key.path")+"/"+props.getString("pgp.private.key"));
						File outDecFile = new File(out_path+"/"+outDecFileName);
		
						PgpUtil.decode(outDecFile, outEncFile, privKeyFile, passwd);
						
						// 디코딩 파일의 크기가 0이면, decoding 실패
						if(outDecFile.length() == 0) outDecFile.delete();
						
						/**
						 * 4. 다운받은 파일 이전 - in/back폴더로
						 */
						log.debug("pgp backup : " + outEncFile.renameTo(bakEncFile));
						
						//com.woongjin.concur.utils.LOGUtil.log(filename, "S");
						log.debug(">> decode filename : " + filename);
					} else {
						log.debug(">> skip filename : " + filename);
					}
				} catch (Exception e) {
					e.printStackTrace();
					//com.woongjin.concur.utils.LOGUtil.log("", "E", e.getMessage());
				}
			}
			sftp.disconnection();

		} catch (Exception e) {
			e.printStackTrace();
			//com.woongjin.concur.utils.LOGUtil.log("", "E", e.getMessage());
		}
	}
	

	public static void main(String[] args) {

		try{
			new PGPDecForImport().get(); // 벤더 파일 암호화
			System.exit(0);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}