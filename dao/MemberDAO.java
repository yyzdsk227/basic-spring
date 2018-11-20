package com.spring.dao;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.spring.vo.MemberVO;

public interface MemberDAO {

/*	public void register(MemberVO mvo) throws Exception;
	
	public MemberVO check(int uid) throws Exception;*/

	public MemberVO getMemById(String id) throws UsernameNotFoundException;
	
	public String secCheck(String writer) throws Exception;
	
/*	public void update(MemberVO mvo) throws Exception;
	
	public void delete(int uid) throws Exception;*/
}
