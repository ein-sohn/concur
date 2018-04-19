package com.woongjin.concur.openpgp;

import java.io.File;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woongjin.concur.utils.FILEUtil;
import com.woongjin.concur.utils.PgpUtil;

public class PGPEncForExport {
	
	protected static final Logger log = LoggerFactory.getLogger(PGPEncForExport.class);

	private String active = System.getProperty("profiles.active");
	private String propertiesFile = active == null ||active.length() == 0 ? "openpgp" : "openpgp_"+active;
	private ResourceBundle props = ResourceBundle.getBundle(propertiesFile);

	private void put() {
		/**
		 * pre : 
		 * 1. erp에서 Vendor파일 batch로 보내도록
		 * 
		 *************************************************
		 * 1. in 폴더 vendor 파일  읽기
		 * 2. 파일 암호화 - concur
		 * 3. 원본파일 이전 - attach
		 * 4. concur ftp 전송 - 일단 테스트 시엔 / 폴더로
		 * 5. 암호화 파일 삭제
		 * 6. 로그생성
		 */
		try {
			
			/**
			 * 1. in 폴더 vendor 파일  읽기
			 */
			String	in_path = props.getString("local.path")+"/in";
					in_path = in_path.replaceAll("//", "/");
			String	out_path = props.getString("local.path")+"/out";
					out_path = out_path.replaceAll("//", "/");
			String	concur_in_dir = props.getString("ftp.root")+"/in";
					concur_in_dir = concur_in_dir.replaceAll("//", "/");
			String	concur_out_dir = props.getString("ftp.root")+"/out";
					concur_out_dir = concur_out_dir.replaceAll("//", "/");
			
			File dir = new File(in_path); 
			File[] fileList = dir.listFiles();

			com.woongjin.concur.utils.SFTPUtil sftp = new com.woongjin.concur.utils.SFTPUtil();

			try{
				sftp.init(
						props.getString("ftp.ip")
						, props.getString("ftp.id")
						, props.getString("ftp.pw")
						, props.getString("ftp.port")
				);

				for(int i = 0 ; i < fileList.length ; i++){
					File file = fileList[i];
					String fileExt = file.getName().substring(file.getName().lastIndexOf(".")+1);
					if(file.isFile() && fileExt.equals("txt")){
						try {
							/**
							 * 2. 파일 암호화
							 */
							File copyFile = new File(in_path+"/"+props.getString("local.backup.folder")+"/bom_"+file.getName());
							FILEUtil.copyFile(file, copyFile);
							File concurPubKeyFile = new File(props.getString("pgp.key.path")+"/"+props.getString("pgp.public.key"));
							File encFile = new File(file.getAbsoluteFile()+".pgp");
							log.debug("### file : " + file.getName());
							log.debug("### copyFile : " + copyFile.getName());
							log.debug("### encFile : " + encFile.getName());
							log.debug("### concurPubKeyFile : " + concurPubKeyFile.getName());
							PgpUtil.encode(file, encFile, concurPubKeyFile);
							
							/**
							 * 3. 원본파일 백업
							 */
							File movFile = new File(in_path+"/"+props.getString("local.backup.folder")+"/"+file.getName());
							log.debug("txt backup : " + file.renameTo(movFile));
							
							/**
							 * 4. concur ftp전송 - 일단 테스트 시엔 / 폴더로 
							 */
							if(active.equals("prd")) {
								sftp.upload(concur_in_dir, encFile);
							} else {
								sftp.upload(concur_out_dir, encFile);
							}
							
							/**
							 * 5. 암호화 파일 삭제
							 */
							if(encFile.length() > 0) {
								File encMovFile = new File(in_path+"/"+props.getString("local.backup.folder")+"/"+encFile.getName());
								log.debug("pgp backup : " + encFile.renameTo(encMovFile));
								//encFile.delete();
							}
							
							//com.woongjin.concur.utils.LOGUtil.log(file.getName(), "S");
							log.debug(">> file.getName() : " + file.getName());
							
						}catch(Exception e){
							e.printStackTrace();
							//com.woongjin.concur.utils.LOGUtil.log(file.getName(), "E", e.getMessage());
						}
					}
				}
				sftp.disconnection();
				
			}catch(Exception e){
				e.printStackTrace();
				//com.woongjin.concur.utils.LOGUtil.log("", "E", e.getMessage());
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		try{
			new PGPEncForExport().put(); // 벤더 파일 암호화
			System.exit(0);

		}catch(Exception e){
            e.printStackTrace();
        }
    }
}