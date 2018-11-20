package com.spring.vo;

import java.io.UnsupportedEncodingException;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

public class PagingMaker {

	private int totalData;	//전체 데아타갯수
	private int startPage;	//페이지 목록의 시작번호
	private int endPage;	//페이지 목록의 끝번호
	private boolean prev;	// 이전 버튼을 나타내는 부울 값
	private boolean next;	// 다음 버튼을 나타내는 부울 값
	
	private int displayPageNum = 10;	//페이지목록에 나타낼 페이지 번호의 수
	
	private PageCriteria pCria;
	
	public void setCri(PageCriteria pCria){
		this.pCria = pCria;
	}
	
	

	public PageCriteria getCri() {
		return pCria;
	}

	public void setTotalData(int totalData){
		this.totalData = totalData;
		
		getPagingData();
	}	
	/*
	i = 3/100
	System.out.println(i); ==> 0
	
	i = (int) 3.0/100
	System.out.println(i); ========> 33
	
	i = 3.0/100.0;
	System.out.println(i); ========> 0.03
	
	*/
	
	private void getPagingData(){
	 endPage =	(int)(Math.ceil(pCria.getPage()/(double)displayPageNum) * displayPageNum);
		
	 startPage = (endPage - displayPageNum) + 1;
	 
	 int finalEndPage = (int)(Math.ceil(totalData/(double)pCria.getNumPerPage()));
	
	 if(endPage> finalEndPage){
		 endPage = finalEndPage;
	 }
	 
	 prev= startPage == 1? false : true;
	 
	 next= endPage * pCria.getNumPerPage() > totalData ? false : true;
 	}//setPagingData()
	
	public String makeURI(int page){
		UriComponents uriComponents = UriComponentsBuilder.newInstance()
				.queryParam("page", page)
				.queryParam("numPerPage", pCria.getNumPerPage())
				.build();
		
		return uriComponents.toUriString();
	}
	
	
	public String makeFind(int page) throws UnsupportedEncodingException{
		UriComponents uriComponents = UriComponentsBuilder.newInstance()
				.queryParam("page", page)
				.queryParam("numPerPage", pCria.getNumPerPage())
				.queryParam("findType", ((FindCriteria)pCria).getFindType())
				.queryParam("keyword", ((FindCriteria)pCria).getKeyword())
				.build()
				.encode("UTF-8");
		
		return uriComponents.toUriString();
	}
	
	
	//getter,setter
	public int getStartPage() {
		return startPage;
	}

	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}

	public int getEndPage() {
		return endPage;
	}

	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}

	public boolean isPrev() {
		return prev;
	}

	public void setPrev(boolean prev) {
		this.prev = prev;
	}

	public boolean isNext() {
		return next;
	}

	public void setNext(boolean next) {
		this.next = next;
	}

	public int getDisplayPageNum() {
		return displayPageNum;
	}

	public void setDisplayPageNum(int displayPageNum) {
		this.displayPageNum = displayPageNum;
	}

	public int getTotalData() {
		return totalData;
	}
	
	
}
