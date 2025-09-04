package com.bharath.springdata.transactionmanagement;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.bharath.springdata.transactionmanagement.services.BankAccountService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionmanagementApplicationTests {

	@Autowired
	BankAccountService service;
	
	@Test
	void testTransfer() {
		service.transfer(500);
	}
}
