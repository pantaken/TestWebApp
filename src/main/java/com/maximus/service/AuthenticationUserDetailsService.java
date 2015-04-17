package com.maximus.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class AuthenticationUserDetailsService implements UserDetailsService {

	public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
		List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
		GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
		authList.add(authority);
		if (!"wangxianlei".equals(s)) {
			throw new UsernameNotFoundException(s);
		}
			
		UserDetails user = new User("wangxianlei", "e10adc3949ba59abbe56e057f20f883e", true,true,true,true,authList);
		return user;
	}
	
}