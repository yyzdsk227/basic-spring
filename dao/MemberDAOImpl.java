package com.spring.dao;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import com.spring.vo.MemberVO;

@Repository
public class MemberDAOImpl implements MemberDAO {

	@Inject
	private SqlSession sqlSession;
	
	/*@Override
	public void register(MemberVO mvo) throws Exception{
		sqlSession.insert("register", mvo);
	}*/
	
	/*@Override
	public MemberVO check(int id) throws Exception{
		return sqlSession.selectOne("check", id);
	}*/
	
	@Override
	public MemberVO getMemById(String id) throws UsernameNotFoundException{
		MemberVO mem = sqlSession.selectOne("check", id);
		return mem;
	}
	
	@Override
	public String secCheck(String writer) throws Exception{
		return sqlSession.selectOne("s_check", writer);
		
	}
	
/*	@Override
	public void update(MemberVO mvo) throws Exception{
		sqlSession.update("update", mvo);
	}
	
	@Override
	public void delete(int uid) throws Exception{
		sqlSession.delete("delete", uid);
	}*/
	
}
