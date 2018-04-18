package com.woongjin.concur.controller;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.woongjin.concur.service.BudgetService;

@RestController
public class BudgetController {

	protected static final Logger log = LoggerFactory.getLogger(BudgetController.class);
	
	@Inject
	private BudgetService budgetService;

	@PostMapping(value="/event/v1.0/notify")
	public String doPost(@RequestBody String params) {
		return params;
		//return budgetService.select(params);
	}
}
