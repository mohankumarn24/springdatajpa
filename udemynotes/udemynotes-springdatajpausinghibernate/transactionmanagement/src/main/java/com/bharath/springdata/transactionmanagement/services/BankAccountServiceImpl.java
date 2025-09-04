package com.bharath.springdata.transactionmanagement.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bharath.springdata.transactionmanagement.entities.BankAccount;
import com.bharath.springdata.transactionmanagement.repos.BankAccountRepository;

@Service
@Transactional   // added to enable commit and rollback
public class BankAccountServiceImpl implements BankAccountService {

	@Autowired
	BankAccountRepository repository;

	@Override
	public void transfer(int amount) {

		BankAccount obamasAccount = repository.findById(1).get();
		obamasAccount.setBal(obamasAccount.getBal() - amount);
		repository.save(obamasAccount);

		if (true) {
          throw new RuntimeException();
		}
		// transaction will be rolled back

		BankAccount trumpsAccount = repository.findById(1).get();
		trumpsAccount.setBal(trumpsAccount.getBal() + amount);
        repository.save(trumpsAccount);
	}
}
