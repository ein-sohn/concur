package com.woongjin.concur.service;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.woongjin.concur.dao.BudgetDao;

@Service
public class BudgetServiceImpl implements BudgetService {
	
	protected static final Logger log = LoggerFactory.getLogger(BudgetServiceImpl.class);

	@Inject
	BudgetDao dao;

	@Override
	public String select(String url) {
		return dao.select(url);
	}
}
