package com.spring.dao;

import java.util.List;

import com.spring.vo.PageCriteria;
import com.spring.vo.ReplyVO;

public interface ReplyDAO {
	public List<ReplyVO> relist(Integer bid) throws Exception;
	
	public void write(ReplyVO rvo) throws Exception;
	
	public void modify(ReplyVO rvo) throws Exception;
	
	public void reDelete(Integer rebid) throws Exception;
	
	public List<ReplyVO> reListPage(Integer bid, PageCriteria pCri) throws Exception;
	
	public int reCount(Integer bid) throws Exception;
	
}
