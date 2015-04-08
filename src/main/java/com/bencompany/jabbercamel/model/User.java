package com.bencompany.jabbercamel.model;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO) private long id;
	@Basic(optional=false) private String userName;
	@Basic(optional=false) private long messageCount;
	
	public User(long id, String userName, long messageCount) {
		this.id = id;
		this.userName = userName;
		this.messageCount = messageCount;
	}
	public User() {
		
	}
	
	public long getMessageCount() {
		return messageCount;
	}
	public void setMessageCount(long messageCount) {
		this.messageCount = messageCount;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName; 
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
}
