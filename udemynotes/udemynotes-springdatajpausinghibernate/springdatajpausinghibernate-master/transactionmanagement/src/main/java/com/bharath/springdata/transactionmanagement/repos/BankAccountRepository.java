package com.bharath.springdata.transactionmanagement.repos;

import org.springframework.data.repository.CrudRepository;

import com.bharath.springdata.transactionmanagement.entities.BankAccount;

public interface BankAccountRepository extends CrudRepository<BankAccount, Integer> {

}