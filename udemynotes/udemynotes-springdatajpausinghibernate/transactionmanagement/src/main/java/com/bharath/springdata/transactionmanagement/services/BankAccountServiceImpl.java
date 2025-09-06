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

/*

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true) // default for the whole class = read-only
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ✅ Read-only transaction
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // ✅ Override with a write transaction
    @Transactional   // same as @Transactional(readOnly = false)
    public User createUser(User user) {
        return userRepository.save(user);
    }

    // ✅ Another write transaction
    @Transactional
    public void updateEmail(Long userId, String newEmail) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setEmail(newEmail); // Hibernate will detect this change
        // No need to call save() explicitly, flush happens at commit
    }
}

 */

 /*
Step by step:

@Transactional
public void updateEmail(Long userId, String newEmail) {
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
    user.setEmail(newEmail);
    // No explicit save() call here
}


1. Transaction starts
	-	When the method is called, Spring opens a new transaction (because of @Transactional).
	-	A persistence context (Hibernate’s 1st-level cache) is created for this transaction.

2. Entity loaded
-	findById fetches the User entity.
-	That entity is now in the persistence context and is considered managed.

3. Property changed
	-	You call user.setEmail(newEmail).
	-	Hibernate sees this change because it tracks managed entities.

4. Dirty checking
	-	At the end of the transaction (just before commit), Hibernate runs a process called dirty checking:
		--	It compares the current state of managed entities with the snapshot it took when they were loaded.
		--	It notices that email has changed.

5. SQL generated
	-	Hibernate prepares an UPDATE statement only for the changed column(s), like:
	-	update user set email = ? where id = ?

6. Transaction commit
	-	The SQL is executed against the database.
	-	Transaction is committed.
	-	The persistence context is cleared.
👉 That’s why you don’t need to call save() after modifying an entity inside a transaction. As long as the entity is managed, Hibernate will flush changes automatically at commit.

The interesting part: if you marked this method as @Transactional(readOnly = true), Hibernate wouldn’t bother tracking changes — so that setEmail would silently do nothing in the DB.
  */