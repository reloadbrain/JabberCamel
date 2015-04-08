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
	@Basic(optional=false) private long count;
	@Basic(optional=false) private String op;
	@Basic(optional=false) private String lastPostedBy;
	
	public Link(String url, long count, String op, String lastPostedBy) {
		this.url = url;
		this.count = count;
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
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
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
