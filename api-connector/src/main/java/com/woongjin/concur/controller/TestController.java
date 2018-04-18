package com.woongjin.concur.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	protected static final Logger log = LoggerFactory.getLogger(TestController.class);

	/**
	 * testconnection
	 * @return
	 */
	@PostMapping(value="/system/v1.0/testconnection")
	public ResponseEntity<String> get() {
		return new ResponseEntity<String>("OK", HttpStatus.OK);
	}
}
