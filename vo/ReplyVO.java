package com.spring.vo;

import java.sql.Date;

public class ReplyVO {
	private Integer rebid;
	private Integer bid;
	private String replyContent;
	private String writer;
	
	private Date regdate;
	private Date updatedate;
	
	
	
	public Integer getRebid() {
		return rebid;
	}
	public void setRebid(Integer rebid) {
		this.rebid = rebid;
	}
	public Integer getBid() {
		return bid;
	}
	public void setBid(Integer bid) {
		this.bid = bid;
	}
	public String getReplyContent() {
		return replyContent;
	}
	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}
	public String getwriter() {
		return writer;
	}
	public void setwriter(String writer) {
		this.writer = writer;
	}
	public Date getRegdate() {
		return regdate;
	}
	public void setRegdate(Date regdate) {
		this.regdate = regdate;
	}
	public Date getUpdatedate() {
		return updatedate;
	}
	public void setUpdatedate(Date updatedate) {
		this.updatedate = updatedate;
	}
	
	
	@Override
	public String toString() {
		return "replyVO [rebid=" + rebid + ", bid=" + bid + ", replyContent="
				+ replyContent + ", writer=" + writer + ", regdate="
				+ regdate + ", updatedate=" + updatedate + "]";
	}
	
	
	
}
