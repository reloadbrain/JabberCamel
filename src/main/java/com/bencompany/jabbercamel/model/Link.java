package com.bencompany.jabbercamel.model;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="Link")
public class Link {

	@Id @GeneratedValue(strategy=GenerationType.AUTO) private long id;
	@Basic(optional=false) private String url;
	@Basic(optional=false) private long count_;
	@Basic(optional=false) private String op;
	@Basic(optional=false) private String lastPostedBy;
	
	public Link(String url, long count_, String op, String lastPostedBy) {
		this.url = url;
		this.count_ = count_;
		this.op = op;
		this.lastPostedBy = lastPostedBy;
	}
	
	public Link() {
		
	}
	
	public String getLastPostedBy() {
		return lastPostedBy;
	}
	public void setLastPostedBy(String lastPostedBy) {
		this.lastPostedBy = lastPostedBy;
	}
	public long getCount_() {
		return count_;
	}
	public void setCount_(long count) {
		this.count_ = count;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getOp() {
		return op;
	}
	public void setOp(String op) {
		this.op = op;
	}
}
