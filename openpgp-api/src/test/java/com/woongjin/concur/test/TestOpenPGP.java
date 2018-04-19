package com.woongjin.concur.test;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woongjin.concur.utils.PgpUtil;

public class TestOpenPGP{
	
	protected static final Logger log = LoggerFactory.getLogger(TestOpenPGP.class);

	public static void main(String [] args) {
		File originFile = new File("/server/pgp/key/vendor_p0045746proy_linux_20180412104234.txt");
		File outEncFile = new File("/server/pgp/key/vendor_p0045746proy_linux_20180412104234.txt.pgp");
		File publicKeyFile = new File("/server/pgp/key/concursolutions.asc");
		PgpUtil.encode(originFile, outEncFile, publicKeyFile);
		log.debug("encoding");
//		File outDecFile = new File("/server/pgp/key/dec_vendor_p0045746proy_linux_20180411201126.txt");
//		File privateKeyFile = new File("/server/pgp/key/drb_private.asc");
//		PGPUtil.decode(outDecFile, outEncFile, privateKeyFile,"LmuR9PkOUkfgNtKLi3FN9w==");
//		log.debug("decoding");
	}
}