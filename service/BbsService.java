package com.spring.service;

import java.util.List;

import com.spring.vo.BbsVO;
import com.spring.vo.FindCriteria;
import com.spring.vo.PageCriteria;

//비즈니스 계층
public interface BbsService {
	public void write(BbsVO bvo) throws Exception;
	
	public BbsVO read(Integer bid) throws Exception;
	
	public void modify(BbsVO bvo) throws Exception;
	
	public void remove(Integer bid) throws Exception;
	
	public List<BbsVO> list() throws Exception;
	
	public List<BbsVO> listCriteria(PageCriteria pCria) throws Exception;
	
	public int CountData(PageCriteria pCria) throws Exception;
	
	public List<BbsVO> listFind(FindCriteria findCria) throws Exception;
	
	public int findCountData(FindCriteria findCria) throws Exception;
	
}
