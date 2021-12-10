package com.philips.project.gateway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.philips.project.gateway.bean.CustomUserDetails;
import com.philips.project.gateway.bean.User;
import com.philips.project.gateway.repositories.UserRepository;
 
@Service
public class CustomUserDetailsService implements UserDetailsService {
 
    @Autowired
    private UserRepository userRepo;
    
     
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new CustomUserDetails(user);
    }
    
    public boolean checkIfExist(String email) {
    	User userDB =  userRepo.findByEmail(email);
    	if(userDB == null)	
    		return false;
    	
    	return true;
    }
 
}