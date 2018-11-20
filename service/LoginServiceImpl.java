package com.spring.service;

import javax.inject.Inject;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.spring.dao.MemberDAO;
import com.spring.vo.MemberVO;

@Service
public class LoginServiceImpl implements UserDetailsService {

	@Inject
	private MemberDAO mdao;
	
	//파라미터로 인한 확장
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
			MemberVO mem = mdao.getMemById(username);
			
			if(null == mem){
				throw new UsernameNotFoundException("User Not Found");
			}
		
			
			
			
		return mem;
	}

	
	

	
	
	
}
