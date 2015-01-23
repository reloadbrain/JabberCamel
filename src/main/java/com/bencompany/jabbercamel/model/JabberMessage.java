package com.bencompany.jabbercamel.model;

import javax.persistence.*;

/*
 * Model for a Jabber Message
 */
@Entity(name="Message")
public class JabberMessage {
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO) private long id;
	@Basic(optional=false) private String username;
	@Basic(optional=false) private String timestamp;
	@Basic(optional=false) private String message;
	
	public JabberMessage() { }

	public JabberMessage(String username, String timestamp, String message) { 
		setUsername(username);
		setTimestamp(timestamp);
		setMessage(message);
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	@Override
	public String toString() {
		return getTimestamp() + " | " + getUsername() + ": " + getMessage();
	}
}
