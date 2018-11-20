package com.spring.vo;

public class FindCriteria extends PageCriteria{
	private String findType;
	private String keyword;
	
	public String getFindType(){
		return findType;
	}
	
	public void setFindType(String findType){
		this.findType = findType;
		
	}
	
	public String getKeyword(){
		return keyword;
		
	}
	
	public void setKeyword(String keyword){
		this.keyword = keyword;
	}

	@Override
	public String toString(){
		return super.toString() + " findCriteria " 
				+"[findType="+findType+", keyword="+keyword+"]";
			
	}
	
	
}
