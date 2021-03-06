package com.spring.dao;


import java.util.List;


import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.spring.vo.BbsVO;
import com.spring.vo.FindCriteria;
import com.spring.vo.PageCriteria;

@Repository
public class BbsDAOImpl implements BbsDAO {

	@Inject
	private SqlSession sqlSession;
	
		
	@Override
	public void insert(BbsVO bvo) throws Exception{
		sqlSession.insert("insert" , bvo);
	}
	
	@Override
	public BbsVO read(Integer bid) throws Exception{
		return sqlSession.selectOne("read", bid);
//		BbsDAOImpl bbsDAOImpl = sqlSession.getMapper(BbsDAOImpl.class);
//		
//		return bbsDAOImpl.read(bid);
	}
	
	@Override
	public void update(BbsVO bvo) throws Exception{
		sqlSession.update("update", bvo);
	}

	@Override
	public void delete(Integer bid) throws Exception{
		sqlSession.delete("delete", bid);
	}
	
	@Override
	public List<BbsVO> list() throws Exception{
		return sqlSession.selectList("list");
	}
	
	@Override
	public List<BbsVO> listPage(int page) throws Exception{
		
		if(page <=0){
			page = 1;
		}
		
		page = (page -1) *15;
		
		return sqlSession.selectList("listPage", page);

	}
	
	@Override
	public List<BbsVO> listCriteria(PageCriteria cria) throws Exception{
		return sqlSession.selectList("listCriteria", cria);
	}
	
	
	@Override
	public int countData(PageCriteria pCria) throws Exception{
		return sqlSession.selectOne("countData", pCria);
	}
	
	@Override
	public List<BbsVO> listFind(FindCriteria findCria) throws Exception{
		
		return sqlSession.selectList("listFind", findCria);
	}
	
	@Override
	public int findCountData(FindCriteria findCria) throws Exception{
		return sqlSession.selectOne("findCountData", findCria);
	}

	@Override
	public void updateHit(Integer bid) throws Exception {
		sqlSession.update("updateHit", bid);
		
	}

}

