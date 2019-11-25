package com.userfront.service;

import java.security.Principal;

import com.userfront.domain.PrimaryAccount;
import com.userfront.domain.SavingsAccount;

public interface AccountService {
	PrimaryAccount createPrimaryAccount();
	SavingsAccount createSavingsAccount();
	void deposit(String accountType, double parseDouble, Principal principal);
	void withdraw(String accountType, double parseDouble, Principal principal);
}
