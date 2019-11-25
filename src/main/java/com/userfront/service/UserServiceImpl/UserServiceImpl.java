package com.userfront.service.UserServiceImpl;

import java.util.Set;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.userfront.dao.RoleDao;
import com.userfront.dao.UserDao;
import com.userfront.domain.User;
import com.userfront.domain.security.UserRole;
import com.userfront.service.AccountService;
import com.userfront.service.UserService;

@Service
public class UserServiceImpl implements UserService{
	
	private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder; 
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private RoleDao roleDao;
	
	public void save(User user) {
		userDao.save(user);
	}
	
	@Autowired
	private AccountService accountService;
	
	public User findByUsername(String username) {
		return userDao.findByUsername(username);
	}
	public User findByEmail(String email) {
		return userDao.findByEmail(email);
	}
	
	@Transactional
	public User createUser(User user, Set<UserRole> userRoles) {
		User localUser = userDao.findByUsername(user.getUsername());
		
		if( localUser != null) {
			LOG.info("User with username {} already exists. Nothing will be done.", user.getUsername());
		} else {
			String ecryptedPassword = passwordEncoder.encode(user.getPassword());
			user.setPassword(ecryptedPassword);
			
			for(UserRole ur : userRoles) {
				roleDao.save(ur.getRole());
			}
			
			user.getUserRoles().addAll(userRoles);
			
			user.setPrimaryAccount(accountService.createPrimaryAccount());
			user.setSavingsAccount(accountService.createSavingsAccount());
			
			localUser = userDao.save(user);
		}
		
		return localUser;
	}
	
	public boolean checkUserExists(String username, String email) {
		if(checkUsernameExists(username) || checkEmailExists(email)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean checkEmailExists(String email) {
		if(findByEmail(email) != null) {
			return true;
		}
		return false;
	}

	public boolean checkUsernameExists(String username) {
		if(findByUsername(username) != null) {
			return true;
		}
		return false;
	}
	
}
