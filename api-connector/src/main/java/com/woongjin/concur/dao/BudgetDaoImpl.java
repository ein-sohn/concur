package com.woongjin.concur.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.woongjin.concur.dao.BudgetDao;
import com.woongjin.framework.core.jco.JCODao;

@Repository
public class BudgetDaoImpl  extends JCODao implements BudgetDao {
	
	protected static final Logger log = LoggerFactory.getLogger(BudgetDaoImpl.class);

	@Override
	public String select(String url) {
		String propertiesFile = System.getProperty("spring.profiles.active");
		if(propertiesFile != null) {
			init("ZWHFI_CHECK_BUDGET_CONCUR");
			log.error("ZWHFI_CHECK_BUDGET_CONCUR");
		}
		return url;
	}
}
