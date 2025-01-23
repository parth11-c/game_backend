package com.data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.data.entity.User;
import com.data.repository.URepo;


@Service
public class CumtomUserDetailService implements UserDetailsService{

	
	@Autowired
	private URepo urepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
 
		
	User user=	urepo.findByName(username).orElseThrow(()-> new RuntimeException("User Not Found"));
		
		return user; 
	}

}
